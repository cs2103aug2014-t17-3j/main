/**
 * This file is part of TheTODO, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2014 TheTODO
 * Copyright (c) Poh Wei Cheng <calvinpohwc@gmail.com>
 *				 Chen Kai Hsiang <kaihsiang95@gmail.com>
 *				 Khin Wathan Aye <y.caiyun@gmail.com>
 *				 Neo Eng Tai <neoengtai@gamil.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.the.todo.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import com.the.todo.command.CommandStatus.Status;
import com.the.todo.model.ToDo;
import com.the.todo.model.ToDo.Priority;
import com.the.todo.parser.CategoryPriorityParser;
import com.the.todo.storage.ToDoStore;

//@author A0119764W

public class ToDoSearch extends ToDoCommand {
	private static final String MEDIUM = "MEDIUM";
	private static final String LOW = "LOW";
	private static final String HIGH = "HIGH";

	private static final String EXECUTE_ILLEGAL_ARGUMENT = "Please enter a valid input.";

	private ToDoStore todoStorage;
	private String query;
	private List<ToDo> updateList;

	public ToDoSearch(ToDoStore todoStorage, List<ToDo> updateList, String query) {
		super();
		this.todoStorage = todoStorage;
		this.updateList = updateList;
		this.query = query;

		this.undoable = false;
	}

	/* (non-Javadoc)
	 * @see com.the.todo.command.ToDoCommand#performExecute()
	 */
	@Override
	protected CommandStatus performExecute() {
		if (query == null || query.equals("")) {
			return new CommandStatus(Status.ERROR, EXECUTE_ILLEGAL_ARGUMENT);
		}

		ArrayList<ToDo> allToDos = (ArrayList<ToDo>) todoStorage.getAll();
		List<ToDo> interimResults, finalResults;
		List<String> foundList = CategoryPriorityParser.parseAll(query);
		String keywords = null;
		String categoryFound = null;
		String priorityFound = null;
		String originalPriorityInString = null;
		Priority priority = null;

		for (int i = 0; i < foundList.size(); i++) {
			if (foundList.get(i).toUpperCase().equals(HIGH)
					|| foundList.get(i).toUpperCase().equals(LOW)
					|| foundList.get(i).toUpperCase().equals(MEDIUM)) {
				originalPriorityInString = foundList.get(i);
				priorityFound = originalPriorityInString.toUpperCase();
				priority = ToDo.Priority.valueOf(priorityFound);
				originalPriorityInString = "+" + originalPriorityInString;
			} else {
				categoryFound = "+" + foundList.get(i);
			}
		}

		keywords = CategoryPriorityParser.removeStringFromTitle(query,
				categoryFound);
		keywords = CategoryPriorityParser.removeStringFromTitle(keywords,
				originalPriorityInString);
		keywords = keywords.trim();

		interimResults = searchByCategory(allToDos, categoryFound);
		interimResults = searchByPriority(interimResults, priority);
		finalResults = searchByKeywords(interimResults, keywords);

		updateList.clear();
		for (ToDo todo : finalResults) {
			updateList.add(todo);
		}

		return new CommandStatus(Status.SUCCESS);
	}

	/**
	 * 
	 * 
	 * 
	 * @param todos - List of task that is taken from the storage
	 * @param category - the group that the user is trying to find.
	 * @return - result of the search
	 */
	private List<ToDo> searchByCategory(List<ToDo> todos, String category) {
		List<ToDo> results = new ArrayList<ToDo>(todos);
		ListIterator<ToDo> i;

		if (category != null) {
			i = results.listIterator();
			while (i.hasNext()) {
				ToDo next = i.next();
				String nextCategory = next.getCategory();
				if (nextCategory == null
						|| !nextCategory.equalsIgnoreCase(category)) {
					i.remove();
				}
			}
		}
		
		return results;
	}

	/**
	 * 
	 * 
	 * 
	 * @param todos - List of task that is taken from the storage
	 * @param query - keyword that is entered by the user to conduct a search.
	 * @return - result of the search
	 */
	private List<ToDo> searchByKeywords(List<ToDo> todos, String query) {
		List<ToDo> results = new ArrayList<ToDo>(todos);
		ListIterator<ToDo> i;

		if (query == null || query.equals("")) {
			return results;
		}

		i = results.listIterator();
		while (i.hasNext()) {
			ToDo nextToDo = i.next();
			if (!containsAll(nextToDo.getTitle(), query)) {
				i.remove();
			}
		}

		return results;
	}

	/**
	 * 
	 * 
	 * 
	 * @param todos - List of task that is taken from the storage
	 * @param priority - the importance level of the task that the user wanted to search
	 * @return - result of the search
	 */
	private List<ToDo> searchByPriority(List<ToDo> todos, Priority priority) {
		List<ToDo> results = new ArrayList<ToDo>(todos);
		ListIterator<ToDo> i = results.listIterator();

		if (priority != null) {
			while (i.hasNext()) {
				ToDo next = i.next();
				Priority nextPriority = next.getPriority();
				if (nextPriority == null || !nextPriority.equals(priority)) {
					i.remove();
				}
			}
		}

		return results;
	}

	/**
	 * @param data
	 * @param query
	 * @return
	 */
	private boolean containsAll(String data, String query) {
		data = data.toLowerCase();
		query = query.toLowerCase();
		
		String[] tokenizedData = data.split("\\s+");
		String[] tokenizedQuery = query.split("\\s+");
		List<String> tokenizedQueryList = new ArrayList<String>(
				Arrays.asList(tokenizedQuery));

		tokenizedQueryList.removeAll(Arrays.asList(tokenizedData));
		if (tokenizedQueryList.size() == 0) {
			return true;
		}
		return false;
	}

	@Override
	protected CommandStatus performUndo() {
		return null;
	}
}

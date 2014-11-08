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

	@Override
	protected CommandStatus performExecute() {
		if (query == null || query.equals("")) {
			return new CommandStatus(Status.ERROR, EXECUTE_ILLEGAL_ARGUMENT);
		}

		ArrayList<ToDo> allToDos = (ArrayList<ToDo>) todoStorage.getAll();
		List<ToDo> interimResults, finalResults;
		List<String> foundList = CategoryPriorityParser.parseAll(query);
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

		String keywords = CategoryPriorityParser.removeStringFromTitle(query, categoryFound);
		keywords = CategoryPriorityParser.removeStringFromTitle(keywords, originalPriorityInString);
		
		interimResults = searchByCategory(allToDos, categoryFound);
		interimResults = searchByPriority(interimResults, priority);
		finalResults = searchByKeywords(interimResults, keywords);

		updateList.clear();
		for (ToDo todo : finalResults) {
			updateList.add(todo);
		}

		return new CommandStatus(Status.SUCCESS);
	}

	private List<ToDo> searchByCategory(List<ToDo> todos, String category) {
		// assert category to have this format "+"....
		List<ToDo> results = new ArrayList<ToDo>(todos);
		ListIterator<ToDo> i = results.listIterator();

		if (category != null) {
			while (i.hasNext()) {
				ToDo next = i.next();
				String nextCategory = next.getCategory();
				if (nextCategory == null || !nextCategory.equalsIgnoreCase(category)) {
					i.remove();
				}
			}
		}
		return results;
	}

	private List<ToDo> searchByKeywords(List<ToDo> todos, String query) {
		List<ToDo> results = new ArrayList<ToDo>(todos);

		if (query == null || query.equals("")) {
			return results;
		}

		ListIterator<ToDo> i = results.listIterator();
		while (i.hasNext()) {
			ToDo nextToDo = i.next();
			if (!containsAll(nextToDo.getTitle(), query)) {
				i.remove();
			}
		}

		return results;
	}

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

	private boolean containsAll(String data, String query) {
		String[] tokenizedData = data.split(" ");
		String[] tokenizedQuery = query.split(" ");
		List<String> tokenizedQueryList = new ArrayList<String>(
				Arrays.asList(tokenizedQuery));

		for (int i = 0; i < tokenizedData.length
				&& tokenizedQueryList.size() != 0; i++) {
			ListIterator<String> j = tokenizedQueryList.listIterator();
			while (j.hasNext()) {
				if (j.next().equalsIgnoreCase(tokenizedData[i])) {
					j.remove();
					break;
				}
			}
		}

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

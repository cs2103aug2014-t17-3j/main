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

import java.util.List;

import org.joda.time.LocalDateTime;

import com.joestelmach.natty.DateGroup;
import com.the.todo.command.CommandStatus.Status;
import com.the.todo.model.ToDo;
import com.the.todo.model.ToDo.Priority;
import com.the.todo.model.ToDo.Type;
import com.the.todo.parser.CategoryPriorityParser;
import com.the.todo.parser.DateAndTimeParser;
import com.the.todo.parser.exception.InvalidDateException;
import com.the.todo.storage.ToDoStore;

public class ToDoAdd extends ToDoCommand {

	private static final String DELIM_SPACE = " ";
	private static final String MEDIUM = "MEDIUM";
	private static final String LOW = "LOW";
	private static final String HIGH = "HIGH";
	private static final String EXECUTE_ILLEGAL_ARGUMENT = "Seems like you are missing somethings.";
	private static final String EXECUTE_ERROR = "An error occured while updating ToDo.";
	private static final String EXECUTE_SUCCESS = "Successful";

	private ToDoStore todoStorage;
	private ToDo todo;
	private String input;

	public ToDoAdd(ToDoStore todoStorage, String input) {
		super();
		this.todoStorage = todoStorage;
		this.input = input;
		this.undoable = true;
	}

	@Override
	protected CommandStatus performExecute() {

		String todoTitle = input;
		String[] todoStrings = input.split(DELIM_SPACE, 2);

		if (todoStrings.length != 2) {
			return new CommandStatus(Status.ERROR, EXECUTE_ILLEGAL_ARGUMENT);
		}

		try {
			todo = createToDo(input);
		} catch (InvalidDateException e) {
			return new CommandStatus(Status.ERROR, "Invalid date!");
		}

		if (todo == null) {
			return new CommandStatus(Status.ERROR, EXECUTE_ERROR);
		}

		todoStorage.save(todo);
		return new CommandStatus(Status.SUCCESS, String.format(EXECUTE_SUCCESS,
				todo.getTitle()));
	}

	@Override
	protected CommandStatus performUndo() {
		todoStorage.delete(todo);
		return new CommandStatus(Status.SUCCESS, String.format(
				"Task successfully removed: %s", todo.getTitle()));
	}

	private ToDo createToDo(String input) throws InvalidDateException {
		ToDo.Type type;
		List<String> foundList = CategoryPriorityParser.parseAll(input);
		String categoryFound = null;
		String priorityFound = null;
		String originalPriorityInString = null;
		String title = null;
		ToDo.Priority priority = null;

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
		title = CategoryPriorityParser.removeStringFromTitle(input,
				categoryFound).trim();
		title = CategoryPriorityParser.removeStringFromTitle(title,
				originalPriorityInString).trim();
		List<DateGroup> dateGroup = DateAndTimeParser.parse(title);

		type = getToDoType(dateGroup);
		todo = createToDoType(type, title, dateGroup, categoryFound, priority);

		return todo;
	}

	private ToDo createToDoType(Type type, String title,
			List<DateGroup> dateGroup, String category, Priority priority) {
		ToDo todo = null;

		switch (type) {
		case FLOATING:
			todo = new ToDo(title);
			break;
		case DEADLINE:
			LocalDateTime dueDateTime = new LocalDateTime(dateGroup.get(0)
					.getDates().get(0));
			todo = new ToDo(title, dueDateTime);
			break;
		case TIMED:
			LocalDateTime startDateTime = new LocalDateTime(dateGroup.get(0)
					.getDates().get(0));
			LocalDateTime endDateTime = new LocalDateTime(dateGroup.get(0)
					.getDates().get(1));
			todo = new ToDo(title, startDateTime, endDateTime);
			break;
		}

		if (category != null) {
			todo.setCategory(category);
		}
		if (priority != null) {
			todo.setPriority(priority);
		}

		return todo;
	}

	private Type getToDoType(List<DateGroup> dateGroup) {
		if (dateGroup.size() == 0) {
			return Type.FLOATING;
		} else if (dateGroup.get(0).getDates().size() == 1) {
			return Type.DEADLINE;
		} else if (dateGroup.get(0).getDates().size() == 2) {
			return Type.TIMED;
		}

		return Type.FLOATING;
	}

}

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

package com.the.todo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.LocalDate;

import com.the.todo.command.CommandStatus;
import com.the.todo.command.CommandStatus.Status;
import com.the.todo.command.ToDoAdd;
import com.the.todo.command.ToDoCommand;
import com.the.todo.command.ToDoComplete;
import com.the.todo.command.ToDoDelete;
import com.the.todo.command.ToDoEdit;
import com.the.todo.command.ToDoIncomplete;
import com.the.todo.command.ToDoSearch;
import com.the.todo.command.ToDoUndo;
import com.the.todo.command.ToDoView;
import com.the.todo.io.FileHandler;
import com.the.todo.model.ToDo;
import com.the.todo.model.ToDo.Type;
import com.the.todo.storage.JsonFileStore;
import com.the.todo.storage.ToDoStore;
import com.the.todo.util.CommandUtil;

public class Logic {

	private ToDoStore todoStorage;

	private List<ToDo> todoDisplay;
	private Map<DateCategory, List<ToDo>> todoMapDisplay;
	private List<UUID> todoIdStorage;
	private Stack<ToDoCommand> undoStack;
	private CommandType lastCommand;
	private DisplayType displayType;
	private ToDo lastChangedTodo;

	private static Logic logic = null;
	private static final String FILENAME = "thetodo.json";
	private static final Logger LOG = LogManager.getLogger(Logic.class);

	public static enum CommandType {
		ADD, VIEW, EDIT, DELETE, COMPLETE, INCOMPLETE, SEARCH, UNDO, INVALID
	};

	public static enum DisplayType {
		ALL, SEARCH
	};

	public static enum DateCategory {
		OVERDUE, TODAY, TOMORROW, UPCOMING, SOMEDAY
	}

	public Logic() {
		todoStorage = new JsonFileStore(FILENAME);
		todoDisplay = new ArrayList<ToDo>();
		todoMapDisplay = new LinkedHashMap<DateCategory, List<ToDo>>();
		todoIdStorage = new ArrayList<UUID>();
		undoStack = new Stack<ToDoCommand>();
		lastCommand = null;
		displayType = DisplayType.ALL;
		lastChangedTodo = null;

		initializeDisplayList();
		updateDisplayItems(todoStorage.getAll());
	}

	public static Logic getInstance() {
		if (logic == null) {
			logic = new Logic();
		}

		return logic;
	}

	public ToDoStore getTodoStorage() {
		return todoStorage;
	}

	public Map<DateCategory, List<ToDo>> getToDoMapDisplay() {
		return todoMapDisplay;
	}

	public CommandStatus processCommand(String input) {
		String originlCommand = CommandUtil.getFirstPhrase(input);
		String command = originlCommand.toLowerCase();
		String params = CommandUtil.getParams(originlCommand, input);
		CommandType commandType = getCommandType(command);
		boolean isSingleChange = false;

		LOG.info("Inputs: [" + input + "].");
		
		ToDoCommand todoCommand = null;
		displayType = DisplayType.ALL;

		CommandStatus commandStatus;

		switch (commandType) {
		case ADD:
			todoCommand = new ToDoAdd(todoStorage, params);
			isSingleChange = true;
			break;
		case VIEW:
			todoCommand = new ToDoView(todoStorage, todoDisplay, params);
			displayType = DisplayType.SEARCH;
			break;
		case EDIT:
		case DELETE:
		case COMPLETE:
		case INCOMPLETE:
			try {
				todoCommand = parseToDoCommand(commandType, params);
			} catch (Exception e) {
				commandType = CommandType.INVALID;
				commandStatus = new CommandStatus(Status.INVALID,
						"Invalid command.");
			}
			isSingleChange = true;
			break;
		case SEARCH:
			todoCommand = new ToDoSearch(todoStorage, todoDisplay, params);
			displayType = DisplayType.SEARCH;
			break;
		case UNDO:
			todoCommand = new ToDoUndo(todoStorage, undoStack);
			isSingleChange = true;
			break;
		case INVALID:
			break;
		default:
			break;
		}

		if (commandType != CommandType.INVALID) {
			commandStatus = todoCommand.execute();
			todoStorage.saveToFile();

			if (displayType == DisplayType.ALL)
				updateDisplayItems(todoStorage.getAll());
			else
				updateDisplayItems(todoDisplay);

			if (todoCommand.isUndoable()
					&& commandStatus.getStatus() == Status.SUCCESS) {
				undoStack.push(todoCommand);
			}

			lastCommand = commandType;
		} else {
			commandStatus = new CommandStatus(Status.INVALID,
					"Invalid command.");
		}
		
		if (isSingleChange){
			lastChangedTodo = todoCommand.getTodo();
		} else {
			lastChangedTodo = null;
		}

		System.out.println("-----------------------------");
		for (ToDo todo : todoStorage.getAll()) {
			System.out.println("ID: " + todo.getId());
			System.out.println("Type: " + todo.getType());
			System.out.println("Title: " + todo.getTitle());
			System.out.println("Start Date: " + todo.getStartDate());
			System.out.println("End Date: " + todo.getEndDate());
			System.out.println("Category: " + todo.getCategory());
			System.out.println("Completed: " + todo.isCompleted());
			System.out.println("Delete: " + todo.isDeleted());
			System.out.println("Priority: " + todo.getPriority());
		}
		System.out.println("--------Completed---------");
		for (ToDo todo : todoStorage.getAllCompleted()) {
			System.out.println("ID: " + todo.getId());
			System.out.println("Type: " + todo.getType());
			System.out.println("Title: " + todo.getTitle());
			System.out.println("Start Date: " + todo.getStartDate());
			System.out.println("End Date: " + todo.getEndDate());
			System.out.println("Category: " + todo.getCategory());
			System.out.println("Completed: " + todo.isCompleted());
			System.out.println("Delete: " + todo.isDeleted());
			System.out.println("Priority: " + todo.getPriority());
		}
		System.out.println("--------UnCompleted---------");
		for (ToDo todo : todoStorage.getAllUncompleted()) {
			System.out.println("ID: " + todo.getId());
			System.out.println("Type: " + todo.getType());
			System.out.println("Title: " + todo.getTitle());
			System.out.println("Start Date: " + todo.getStartDate());
			System.out.println("End Date: " + todo.getEndDate());
			System.out.println("Category: " + todo.getCategory());
			System.out.println("Completed: " + todo.isCompleted());
			System.out.println("Delete: " + todo.isDeleted());
			System.out.println("Priority: " + todo.getPriority());
		}
		
		LOG.info("System Message: [" + commandStatus.getMessage() + "].");

		return commandStatus;
	}

	CommandType getCommandType(String input) {

		if (input.trim().isEmpty()) {
			return CommandType.INVALID;
		}

		String command = CommandUtil.getFirstPhrase(input);

		switch (command) {
		case "a":
		case "add":
			return CommandType.ADD;
		case "v":
		case "view":
			return CommandType.VIEW;
		case "e":
		case "edit":
			return CommandType.EDIT;
		case "d":
		case "rm":
		case "del":
		case "delete":
			return CommandType.DELETE;
		case "c":
		case "done":
		case "complete":
			return CommandType.COMPLETE;
		case "incomplete":
			return CommandType.INCOMPLETE;
		case "s":
		case "search":
			return CommandType.SEARCH;
		case "u":
		case "undo":
			return CommandType.UNDO;
		default:
			return CommandType.INVALID;
		}
	}

	private void sortByDate(Map<DateCategory, List<ToDo>> todoMap,
			List<ToDo> todoList) {
		todoMap.clear();

		Type todoType;
		LocalDate startDate;
		LocalDate endDate;
		LocalDate today = new LocalDate();
		LocalDate tomorrow = new LocalDate().plusDays(1);

		List<ToDo> todoOverdue = new ArrayList<ToDo>();
		List<ToDo> todoToday = new ArrayList<ToDo>();
		List<ToDo> todoTomorrow = new ArrayList<ToDo>();
		List<ToDo> todoUpcoming = new ArrayList<ToDo>();
		List<ToDo> todoSomeday = new ArrayList<ToDo>();

		for (ToDo todo : todoList) {
			todoType = todo.getType();
			startDate = todo.getStartDate().toLocalDate();
			endDate = todo.getEndDate().toLocalDate();

			if (todoType == Type.FLOATING) {
				todoSomeday.add(todo);
			}

			if (todoType == Type.DEADLINE) {
				if (endDate.isBefore(today)) {
					todoOverdue.add(todo);
				} else if (endDate.equals(today)) {
					todoToday.add(todo);
				} else if (endDate.equals(tomorrow)) {
					todoTomorrow.add(todo);
				}
			}

			if (todoType == Type.TIMED) {
				if (endDate.isBefore(today)) {
					todoOverdue.add(todo);
				}
				if (!today.isBefore(startDate) && !today.isAfter(endDate)) {
					todoToday.add(todo);
				}
				if (!tomorrow.isBefore(startDate) && !tomorrow.isAfter(endDate)) {
					todoTomorrow.add(todo);
				}
			}

			if (displayType == DisplayType.SEARCH) {
				if (todoType == Type.DEADLINE) {
					if (endDate.isAfter(tomorrow)) {
						todoUpcoming.add(todo);
					}
				} else if (todoType == Type.TIMED) {
					if (endDate.isAfter(tomorrow)) {
						todoUpcoming.add(todo);
					}
				}
			}
		}

		if (todoOverdue.size() > 0) {
			todoMap.put(DateCategory.OVERDUE, todoOverdue);
		}
		if (todoToday.size() > 0) {
			todoMap.put(DateCategory.TODAY, todoToday);
		}
		if (todoTomorrow.size() > 0) {
			todoMap.put(DateCategory.TOMORROW, todoTomorrow);
		}
		if (todoUpcoming.size() > 0) {
			todoMap.put(DateCategory.UPCOMING, todoUpcoming);
		}
		if (todoSomeday.size() > 0) {
			todoMap.put(DateCategory.SOMEDAY, todoSomeday);
		}

		for (Entry<DateCategory, List<ToDo>> entry : todoMap.entrySet()) {
			Collections.sort(entry.getValue());
		}
	}

	private void updateIdStorage(List<UUID> todoIdStorage,
			Map<DateCategory, List<ToDo>> todoMap) {
		if (todoMap == null || todoMap.isEmpty()) {
			return;
		}

		todoIdStorage.clear();

		for (Entry<DateCategory, List<ToDo>> entry : todoMap.entrySet()) {
			for (ToDo todo : entry.getValue()) {
				todoIdStorage.add(todo.getId());
			}
		}
	}

	private void initializeDisplayList() {
		todoDisplay.clear();

		for (ToDo item : todoStorage.getAll()) {
			todoDisplay.add(new ToDo(item));
		}
	}

	private void updateDisplayItems(List<ToDo> list) {
		sortByDate(todoMapDisplay, list);
		updateIdStorage(todoIdStorage, todoMapDisplay);
	}

	private ToDoCommand parseToDoCommand(CommandType commandType, String input)
			throws Exception {

		String index = CommandUtil.getFirstPhrase(input);
		String params = CommandUtil.getParams(index, input);

		try {
			UUID id = getId(Integer.valueOf(index));
			ToDo todo = getToDo(id);
			
			if (todo == null) {
				throw new Exception("Invalid ID.");
			}

			switch (commandType) {
			case EDIT:
				return new ToDoEdit(todoStorage, todo, params);
			case DELETE:
				return new ToDoDelete(todoStorage, todo);
			case COMPLETE:
				return new ToDoComplete(todoStorage, todo);
			case INCOMPLETE:
				return new ToDoIncomplete(todoStorage, todo);
			default:
				return null;
			}
		} catch (NumberFormatException ex) {
			throw new Exception("Invalid Command.");
		}
	}

	private ToDo getToDo(UUID id) {
		ToDo todo = null;

		if (id != null) {
			for (ToDo item : todoStorage.getAll()) {
				if (item.getId().equals(id)) {
					todo = item;
					break;
				}
			}
		}

		return todo;
	}

	private UUID getId(int index) {
		UUID id;

		try {
			id = todoIdStorage.get(index - 1);
		} catch (IndexOutOfBoundsException ex) {
			id = null;
		}

		return id;
	}

	public CommandType getLastCommand() {
		return lastCommand;
	}

	public DisplayType getDisplayType() {
		return displayType;
	}
	public ToDo getLastChangedToDo () {
		return lastChangedTodo;
	}

}

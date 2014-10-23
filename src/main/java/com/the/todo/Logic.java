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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.TreeMap;
import java.util.UUID;

import org.joda.time.LocalDate;

import com.the.todo.command.CommandStatus;
import com.the.todo.command.CommandStatus.Status;
import com.the.todo.command.ToDoAdd;
import com.the.todo.command.ToDoCommand;
import com.the.todo.command.ToDoComplete;
import com.the.todo.command.ToDoDelete;
import com.the.todo.command.ToDoEdit;
import com.the.todo.command.ToDoIncomplete;
import com.the.todo.command.ToDoRead;
import com.the.todo.command.ToDoUndo;
import com.the.todo.model.ToDo;
import com.the.todo.storage.JsonFileStore;
import com.the.todo.storage.ToDoStore;
import com.the.todo.util.CommandUtil;

public class Logic {

	private ToDoStore todoStorage;

	private List<ToDo> todoDisplay;
	private Map<LocalDate, List<ToDo>> todoMapDisplay;
	private List<UUID> todoIdStorage;
	private Stack<ToDoCommand> undoStack;

	private static Logic logic = null;
	private static final String FILENAME = "thetodo.json";

	private static enum CommandType {
		ADD, READ, EDIT, DELETE, COMPLETE, INCOMPLETE, SEARCH, UNDO, INVALID
	};

	private static enum DisplayType {
		ALL, SEARCH
	};

	public Logic() {
		todoStorage = new JsonFileStore(FILENAME);
		todoDisplay = new ArrayList<ToDo>();
		todoMapDisplay = new TreeMap<LocalDate, List<ToDo>>();
		todoIdStorage = new ArrayList<UUID>();
		undoStack = new Stack<ToDoCommand>();

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

	public Map<LocalDate, List<ToDo>> getToDoMapDisplay() {
		return todoMapDisplay;
	}

	public CommandStatus processCommand(String input) {
		String command = CommandUtil.getFirstPhrase(input);
		String params = CommandUtil.getParams(command, input);
		CommandType commandType = getCommandType(command);

		ToDoCommand todoCommand = null;
		DisplayType displayType = DisplayType.ALL;

		CommandStatus commandStatus;

		switch (commandType) {
		case ADD:
			todoCommand = new ToDoAdd(todoStorage, params);
			break;
		case READ:
			todoCommand = new ToDoRead(todoStorage, todoDisplay);
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
				commandStatus = new CommandStatus(Status.INVALID, "Invalid command.");
			}
			break;
		case SEARCH:
			displayType = DisplayType.SEARCH;
			break;
		case UNDO:
			todoCommand = new ToDoUndo(todoStorage, undoStack);
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
		} else {
			commandStatus = new CommandStatus(Status.INVALID,
					"Invalid command.");
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
		}

		return commandStatus;
	}

	private CommandType getCommandType(String input) {

		if (input.trim().isEmpty()) {
			return CommandType.INVALID;
		}

		String command = CommandUtil.getFirstPhrase(input);

		switch (command) {
		case "add":
			return CommandType.ADD;
		case "read":
			return CommandType.READ;
		case "edit":
			return CommandType.EDIT;
		case "delete":
			return CommandType.DELETE;
		case "complete":
			return CommandType.COMPLETE;
		case "incomplete":
			return CommandType.INCOMPLETE;
		case "search":
			return CommandType.SEARCH;
		case "undo":
			return CommandType.UNDO;
		default:
			return CommandType.INVALID;
		}
	}

	private void sortByDate(Map<LocalDate, List<ToDo>> todoMap,
			List<ToDo> todoList) {
		todoMap.clear();

		List<ToDo> todoByDate;
		LocalDate date;

		for (ToDo todo : todoList) {
			date = todo.getEndDate().toLocalDate();

			if (todoMap.containsKey(date)) {
				todoByDate = todoMap.get(date);
				todoByDate.add(todo);
			} else {
				todoByDate = new ArrayList<ToDo>();
				todoByDate.add(todo);
				todoMap.put(date, todoByDate);
			}
		}

		for (Entry<LocalDate, List<ToDo>> entry : todoMap.entrySet()) {
			Collections.sort(entry.getValue());
		}
	}

	private void updateIdStorage(List<UUID> todoIdStorage,
			Map<LocalDate, List<ToDo>> todoMap) {
		if (todoMap == null || todoMap.isEmpty()) {
			return;
		}

		todoIdStorage.clear();

		for (Entry<LocalDate, List<ToDo>> entry : todoMap.entrySet()) {
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

	private ToDoCommand parseToDoCommand(CommandType commandType, String input) throws Exception {

		String index = CommandUtil.getFirstPhrase(input);
		String params = CommandUtil.getParams(index, input);

		try {
			UUID id = getId(Integer.valueOf(index));
			ToDo todo = getToDo(id);
			
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

}

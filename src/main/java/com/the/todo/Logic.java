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

import java.util.List;

import com.the.todo.command.CommandStatus;
import com.the.todo.command.CommandStatus.Status;
import com.the.todo.command.ToDoAdd;
import com.the.todo.command.ToDoCommand;
import com.the.todo.command.ToDoComplete;
import com.the.todo.command.ToDoDelete;
import com.the.todo.command.ToDoEdit;
import com.the.todo.command.ToDoRead;
import com.the.todo.model.ToDo;
import com.the.todo.storage.JsonFileStore;
import com.the.todo.storage.ToDoStore;
import com.the.todo.util.StringUtil;

public class Logic {

	private ToDoStore todoStorage;
	private List<ToDo> todoList;

	private static Logic logic = null;

	private static final String FILENAME = "thetodo.json";

	private static enum CommandType {
		ADD, READ, EDIT, DELETE, COMPLETE, INCOMPLETE, SEARCH, UNDO, INVALID
	};

	public Logic() {
		todoStorage = new JsonFileStore(FILENAME);
		todoList = todoStorage.getAll();
	}

	public static Logic getInstance() {
		if (logic == null) {
			logic = new Logic();
		}

		return logic;
	}

	public List<ToDo> getTodoList() {
		return todoList;
	}

	public ToDoStore getTodoStorage() {
		return todoStorage;
	}

	public CommandStatus processCommand(String userInput) {
		CommandType command = getCommandType(userInput);
		String todoTitleOrId = getTitleOrId(userInput);
		ToDoCommand todoCommand = null;
		CommandStatus commandStatus;

		switch (command) {
		case ADD:
			todoCommand = new ToDoAdd(todoStorage, todoTitleOrId);
			break;
		case READ:
			todoCommand = new ToDoRead(todoStorage, todoList);
			break;
		case EDIT:
			int id = Integer.valueOf(userInput.split(" ", 3)[1]);
			ToDo taskToEdit = getToDo(id);
			todoCommand = new ToDoEdit(todoStorage, taskToEdit, todoTitleOrId);
			break;
		case DELETE:
			ToDo taskToDelete = getToDo(Integer.valueOf(todoTitleOrId));
			todoCommand = new ToDoDelete(todoStorage, taskToDelete);
			break;
		case COMPLETE:
			ToDo taskToComplete = getToDo(Integer.valueOf(todoTitleOrId));
			todoCommand = new ToDoComplete(todoStorage, taskToComplete);
			break;
		case SEARCH:
			break;
		case UNDO:
			break;
		case INVALID:
			break;
		default:
			break;
		}

		if (command != CommandType.INVALID) {
			commandStatus = todoCommand.execute();
			todoStorage.saveToFile();
			todoList = todoStorage.getAll();
		} else {
			commandStatus = new CommandStatus(Status.INVALID,
					"Invalid command.");
		}

		System.out.println("-----------------------------");
		for (ToDo todo : todoStorage.getAll()) {
			System.out.println("Title: " + todo.getTitle());
			System.out.println("Start Date: " + todo.getStartDate());
			System.out.println("End Date: " + todo.getEndDate());
			System.out.println("Category: " + todo.getCategory());
			System.out.println("Completed: " + todo.isCompleted());
			System.out.println("Delete: " + todo.isDeleted());
		}

		return commandStatus;
	}

	private CommandType getCommandType(String userInput) {

		if (userInput.trim().isEmpty()) {
			return CommandType.INVALID;
		}

		String[] splitInput = StringUtil.splitString(userInput, " ", 2);
		String command = splitInput[0].toLowerCase();

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

	private static String getTitleOrId(String userInput) {
		String[] splitInput = StringUtil.splitString(userInput, " ", 2);

		if (splitInput.length == 1) {
			return "";
		}

		return splitInput[1];
	}

	private ToDo getToDo(int id) {
		ToDo todo;
		try {
			todo = todoList.get(id - 1);
		} catch (IndexOutOfBoundsException ex) {
			todo = null;
		}
		return todo;
	}

}

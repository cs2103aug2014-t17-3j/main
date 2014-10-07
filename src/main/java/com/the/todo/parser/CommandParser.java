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

package com.the.todo.parser;

import com.the.todo.command.CommandStatus;
import com.the.todo.command.ToDoAdd;
import com.the.todo.command.ToDoCommand;
import com.the.todo.command.ToDoComplete;
import com.the.todo.command.ToDoDelete;
import com.the.todo.command.ToDoEdit;
import com.the.todo.model.ToDo;
import com.the.todo.storage.ToDoStore;
import com.the.todo.util.StringUtil;

public class CommandParser {

	private static ToDoCommand todoCommand;

	private static enum CommandType {
		ADD, READ, EDIT, DELETE, COMPLETE, INCOMPLETE, SEARCH, UNDO, INVALID
	};

	public static CommandStatus processCommand(ToDoStore todoStorage,
			String userInput) {
		CommandType command = getCommandType(userInput);
		String todoTitle = getTitle(userInput);
		ToDoCommand todoCommand = null;
		CommandStatus commandStatus;

		switch (command) {
		case ADD:
			todoCommand = new ToDoAdd(todoStorage, todoTitle);
			break;
		case READ:
			break;
		case EDIT:
			todoCommand = new ToDoEdit(todoStorage, todoTitle);
			break;
		case DELETE:
			todoCommand = new ToDoDelete(todoStorage, todoTitle);
			break;
		case COMPLETE:
			todoCommand = new ToDoComplete(todoStorage, todoTitle);
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

		commandStatus = todoCommand.execute();
		
		System.out.println("-----------------------------");
		for (ToDo todo : todoStorage.getAll()) {
			System.out.println("ID: " + todo.getId());
			System.out.println("Title: " + todo.getTitle());
			System.out.println("Date: " + todo.getEndDate());
			System.out.println("Category: " + todo.getCategory());
			System.out.println("Completed: " + todo.isCompleted());
			System.out.println("Delete: " + todo.isDeleted());
		}
		
		return commandStatus;
	}

//	public static void commandProcess(ToDoStore todoStorage, String command)
//			throws Exception {
//		String[] inputs = command.split(" ", 2);
//
//		switch (inputs[0].trim().toLowerCase()) {
//		case "add":
//			todoCommand = new ToDoAdd(todoStorage, inputs[1]);
//			todoCommand.execute();
//			// ToDo todo = processAdd(inputs[1]);
//			// todoStorage.save(todo);
//			break;
//		case "read":
//			todoStorage.getAll();
//			break;
//		case "delete":
//			todoCommand = new ToDoDelete(todoStorage, inputs[1]);
//			todoCommand.execute();
//			break;
//		case "edit":
//			todoCommand = new ToDoEdit(todoStorage, inputs[1]);
//			todoCommand.execute();
//			break;
//		case "complete":
//			String inputComplete = inputs[1];
//			String[] completeInputs = inputComplete.trim().split(" ", 2);
//			ToDo todoComplete = todoStorage.get(completeInputs[0]);
//			todoComplete.setCompleted(true);
//			todoStorage.update(todoComplete.getId(), todoComplete);
//		}
//
//		System.out.println("-----------------------------");
//		for (ToDo todo : todoStorage.getAll()) {
//			System.out.println("ID: " + todo.getId());
//			System.out.println("Title: " + todo.getTitle());
//			System.out.println("Date: " + todo.getEndDate());
//			System.out.println("Category: " + todo.getCategory());
//			System.out.println("Completed: " + todo.isCompleted());
//			System.out.println("Delete: " + todo.isDeleted());
//		}
//	}

	private static CommandType getCommandType(String userInput) {

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

	private static String getTitle(String userInput) {
		String[] splitInput = StringUtil.splitString(userInput, " ", 2);
		return splitInput[1];
	}

}
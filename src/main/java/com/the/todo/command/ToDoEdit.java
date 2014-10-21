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
import java.util.List;
import java.util.StringTokenizer;

import org.joda.time.LocalDateTime;
import org.ocpsoft.prettytime.nlp.parse.DateGroup;

import com.the.todo.command.CommandStatus.Status;
import com.the.todo.model.ToDo;
import com.the.todo.model.ToDo.Type;
import com.the.todo.parser.DateParser;
import com.the.todo.parser.exception.InvalidDateException;
import com.the.todo.storage.ToDoStore;
import com.the.todo.util.StringUtil;

public class ToDoEdit extends ToDoCommand {

	private static final String DELIM = "-";
	private static final String EXECUTE_DOES_NOT_EXIST = "It seems like ToDo %s does not exist.";
	private static final String EXECUTE_ILLEGAL_ARGUMENT = "Mmm ... Seems like you are missing some argument.";
	private static final String EXECUTE_ERROR = "An error occured while updating ToDo.";
	private static final String EXECUTE_SUCCESS = "A great success updating ToDo: %s";

	private static enum FieldType {
		TITLE, CATEGORY, STARTDATE, ENDDATE, REMOVE_START, REMOVE_END, PRIORITY, INVALID
	};
	
	private static enum inputStringType {
		T, TITLE, C, CATEGORY, S, STARTDATE, E, ENDDATE, RS, REMOVESTART, RE, REMOVEEND, P, PRIORITY
	};


	private ToDoStore todoStorage;
	private ToDo todo;
	private String input;

	public ToDoEdit(ToDoStore todoStorage, ToDo todo, String input) {
		super();
		this.todoStorage = todoStorage;
		this.todo = todo;
		this.input = input;
		this.undoable = true;
	}

	@Override
	protected CommandStatus performExecute() {

		String todoTitle;
		String[] todoStrings = StringUtil.splitString(input, " ", 2);

		if (todoStrings.length != 2) {
			return new CommandStatus(Status.ERROR, EXECUTE_ILLEGAL_ARGUMENT);
		}

		todoTitle = todoStrings[1];

		if (this.todo == null) {
			return new CommandStatus(Status.ERROR, String.format(
					EXECUTE_DOES_NOT_EXIST, ""));
		}

		try {
			this.todo = editToDo(this.todo, todoTitle);
		} catch (InvalidDateException e) {
			return new CommandStatus(Status.ERROR, "Invalid date!");
		}

		if (this.todo == null) {
			return new CommandStatus(Status.ERROR, EXECUTE_ERROR);
		}

		return new CommandStatus(Status.SUCCESS, String.format(EXECUTE_SUCCESS,
				""));
	}

	@Override
	protected CommandStatus performUndo() {
		return new CommandStatus(Status.INVALID);
	}

	private ToDo editToDo(ToDo todo, String input) throws InvalidDateException {
		/* String Tokenizer */
		List<String> tokenString = new ArrayList<String>();
		StringTokenizer tokens = new StringTokenizer(input, DELIM);

		/* String */
		String[] splitSubInputArr;

		while (tokens.hasMoreTokens()) {
			tokenString.add(tokens.nextToken());
		}
		for (int i = 0; i < tokenString.size(); i++) {
			splitSubInputArr = stringSplit(tokenString.get(i), 2);
			if (splitSubInputArr.length == 2) {
				todo = proccessEditData(todo, splitSubInputArr);
			} else if (splitSubInputArr.length == 1) {
					removeStartAndEndDate(todo, splitSubInputArr);
			} else {
				break;
			}
		}
		editTaskType(todo);

		return todo;
	}

	private ToDo proccessEditData(ToDo todo, String[] splitSubInputArr)
			throws InvalidDateException {
		String fieldType;
		String remainingString;
		fieldType = splitSubInputArr[0];
		remainingString = splitSubInputArr[1];
		todo = processFieldType(fieldType, remainingString, todo);
		return todo;
	}

	private void editTaskType(ToDo todo) {
		LocalDateTime startDate;
		LocalDateTime endDate;
		Type typeOfTaskBefore = todo.getType();
		startDate = todo.getStartDate();
		endDate = todo.getEndDate();
		Type typeOfTaskAfter = checkChangeTaskType(startDate, endDate);
		if (typeOfTaskBefore != typeOfTaskAfter) {
			if (typeOfTaskAfter == Type.FLOATING) {
				todo.setFloatingToDo();
			} else if (typeOfTaskAfter == Type.TIMED) {
				todo.setTimedToDo();
			} else {
				todo.setDeadlineToDo();
			}
		}
	}

	private void removeStartAndEndDate(ToDo todo, String[] splitSubInputArr) {
		FieldType removeType = getFieldType(splitSubInputArr[0]);
		if (removeType.equals(FieldType.REMOVE_START)) {
			todo.removeStartDate();
		} else {
			todo.removeEndDate();
		}
	}

	private String[] stringSplit(String subStringInput, int numberOfParts) {
		String[] splitSubInputArr;
		splitSubInputArr = StringUtil.splitString(subStringInput, " ",
				numberOfParts);
		return splitSubInputArr;
	}

	private ToDo processFieldType(String fieldType, String remainingString,
			ToDo todo) throws InvalidDateException {
		FieldType typeOfField = getFieldType(fieldType);
		List<DateGroup> groups;
		LocalDateTime date;

		switch (typeOfField) {
		case TITLE:
			todo.setTitle(remainingString);
			break;
		case CATEGORY:
			todo.setCategory(remainingString);
			break;
		case STARTDATE:
			groups = DateParser.parse(remainingString);
			date = new LocalDateTime(groups.get(0).getDates().get(0));
			todo.setStartDate(date);
			break;
		case ENDDATE:
			groups = DateParser.parse(remainingString);
			date = new LocalDateTime(groups.get(0).getDates().get(0));
			todo.setEndDate(date);
			break;
		case PRIORITY:
			break;
		case INVALID:
			break;
		default:
			break;
		}
		return todo;
	}

	private FieldType getFieldType(String userInput) {

		userInput = userInput.toUpperCase();
		inputStringType usersInput = inputStringType.valueOf(userInput);
		switch (usersInput) {
		case T:
		case TITLE:
			return FieldType.TITLE;
		case C:
		case CATEGORY:
			return FieldType.CATEGORY;
		case S:
		case STARTDATE:
			return FieldType.STARTDATE;
		case E:
		case ENDDATE:
			return FieldType.ENDDATE;
		case RS:
		case REMOVESTART:
			return FieldType.REMOVE_START;
		case RE:
		case REMOVEEND:
			return FieldType.REMOVE_END;
		case P:
		case PRIORITY:
			return FieldType.PRIORITY;
		default:
			return FieldType.INVALID;
		}
	}

	private Type checkChangeTaskType(LocalDateTime startDate,
			LocalDateTime endDate) {

		if ((startDate == null) && (endDate == null)) {
			return Type.FLOATING;
		} else if ((startDate.equals(ToDo.INVALID_DATE)) && (endDate != null)) {
			return Type.DEADLINE;
		} else {
			return Type.TIMED;
		}

	}

}

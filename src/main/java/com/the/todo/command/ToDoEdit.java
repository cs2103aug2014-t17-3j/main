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

import com.joestelmach.natty.DateGroup;
import com.the.todo.command.CommandStatus.Status;
import com.the.todo.model.ToDo;
import com.the.todo.model.ToDo.Type;
import com.the.todo.parser.DateAndTimeParser;
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
		T, TITLE, C, CATEGORY, S, STARTDATE, E, ENDDATE, RS, REMOVESTART, RE, REMOVEEND, P, PRIORITY, INVALID
	};

	private ToDoStore todoStorage;
	private ToDo todo;
	private ToDo newtodo;
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

		String todoTitle = input;
		String[] todoStrings = input.split(" ", 2);
		newtodo = new ToDo(todo);
		todoStorage.update(newtodo.getId(), newtodo);

		// if (todoStrings.length != 2) {
		// return new CommandStatus(Status.ERROR, EXECUTE_ILLEGAL_ARGUMENT);
		// }

		// todoTitle = todoStrings[1];

		if (this.newtodo == null) {
			return new CommandStatus(Status.ERROR, String.format(
					EXECUTE_DOES_NOT_EXIST, ""));
		}

		try {
			if (todoTitle.isEmpty()) {
				return new CommandStatus(Status.ERROR, EXECUTE_ILLEGAL_ARGUMENT);
			}
			if (todo.getType() == Type.FLOATING) {
				if ((input.contains("-s")) && !input.contains("-e")) {
					return new CommandStatus(Status.ERROR,
							EXECUTE_ILLEGAL_ARGUMENT);
				}
			}
			this.newtodo = editToDo(this.newtodo, todoTitle);
		} catch (InvalidDateException e) {
			return new CommandStatus(Status.ERROR, "Invalid date!");
		} catch (Exception e) {
			return new CommandStatus(Status.ERROR, EXECUTE_ILLEGAL_ARGUMENT);
		}

		if (this.newtodo == null) {
			return new CommandStatus(Status.ERROR, EXECUTE_ERROR);
		}

		return new CommandStatus(Status.SUCCESS, String.format(EXECUTE_SUCCESS,
				""));
	}

	@Override
	protected CommandStatus performUndo() {
		todoStorage.update(todo.getId(), todo);
		if (this.newtodo == null) {
			return new CommandStatus(Status.ERROR, EXECUTE_ERROR);
		}

		return new CommandStatus(Status.SUCCESS, String.format(EXECUTE_SUCCESS,
				""));
	}

	private ToDo editToDo(ToDo todo, String input) throws Exception {
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
				String commands = splitSubInputArr[0].toUpperCase();
				FieldType typeOfField = FieldType.valueOf(commands);
				if (typeOfField.equals(FieldType.RS)
						|| typeOfField.equals(FieldType.RE)
						|| typeOfField.equals(FieldType.REMOVESTART)
						|| typeOfField.equals(FieldType.REMOVEEND)) {
					todo = removeStartAndEndDate(todo, splitSubInputArr[0]);
				} else {
					throw new Exception("Missing Args");
				}
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

	private Type checkChangeTaskType(LocalDateTime startDate,
			LocalDateTime endDate) {

		if ((startDate.equals(ToDo.INVALID_DATE))
				&& (endDate.equals(ToDo.INVALID_DATE))) {
			return Type.FLOATING;
		} else if ((startDate.equals(ToDo.INVALID_DATE)) && (endDate != null)) {
			return Type.DEADLINE;
		} else {
			return Type.TIMED;
		}

	}

	private ToDo removeStartAndEndDate(ToDo todo, String stringSplit) {
		stringSplit = stringSplit.toUpperCase();
		FieldType fieldType = FieldType.valueOf(stringSplit);
		if (fieldType.equals(FieldType.REMOVESTART)
				|| fieldType.equals(FieldType.RS)) {
			todo.removeStartDate();
		} else if (fieldType.equals(FieldType.REMOVEEND)
				|| fieldType.equals(FieldType.RE)) {
			todo.removeEndDate();
		}
		return todo;

	}

	private String[] stringSplit(String subStringInput, int numberOfParts) {
		String[] splitSubInputArr;
		splitSubInputArr = StringUtil.splitString(subStringInput, " ",
				numberOfParts);
		return splitSubInputArr;
	}

	private ToDo processFieldType(String fieldType, String remainingString,
			ToDo todo) throws InvalidDateException {
		fieldType = fieldType.toUpperCase();
		FieldType typeOfField = FieldType.valueOf(fieldType);
		List<DateGroup> groups;
		LocalDateTime date;

		switch (typeOfField) {
		case T:
		case TITLE:
			todo.setTitle(remainingString);
			break;
		case C:
		case CATEGORY:
			todo.setCategory(remainingString);
			break;
		case S:
		case STARTDATE:
			groups = DateAndTimeParser.parse(remainingString);
			date = new LocalDateTime(groups.get(0).getDates().get(0));
			todo.setStartDate(date);
			break;
		case E:
		case ENDDATE:
			groups = DateAndTimeParser.parse(remainingString);
			date = new LocalDateTime(groups.get(0).getDates().get(0));
			todo.setEndDate(date);
			break;
		case P:
		case PRIORITY:
			break;
		case INVALID:
			break;
		default:
			break;
		}
		return todo;
	}
}

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.LocalDateTime;
import org.ocpsoft.prettytime.nlp.parse.DateGroup;

import com.the.todo.command.CommandStatus.Status;
import com.the.todo.model.ToDo;
import com.the.todo.parser.DateParser;
import com.the.todo.parser.exception.InvalidDateException;
import com.the.todo.storage.ToDoStore;
import com.the.todo.util.StringUtil;

public class ToDoEdit extends ToDoCommand {

	private static final String EXECUTE_DOES_NOT_EXIST = "It seems like ToDo %s does not exist.";
	private static final String EXECUTE_ILLEGAL_ARGUMENT = "Mmm ... Seems like you are missing some argument.";
	private static final String EXECUTE_ERROR = "An error occured while updating ToDo.";
	private static final String EXECUTE_SUCCESS = "A great success updating ToDo: %s";

	private static enum FieldType {
		TITLE, CATEGORY, STARTDATE, ENDDATE, STARTTIME, ENDTIME, PRIORITY, INVALID
	};

	private static enum TaskType {
		FLOATTASK, TIMETASK, DATETASK
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
			// TODO Auto-generated catch block
			return new CommandStatus(Status.ERROR, "Invalid date!");
		}

		if (this.todo == null) {
			return new CommandStatus(Status.ERROR, EXECUTE_ERROR);
		}

		// todoStorage.update(this.todo.getId(), this.todo);
		return new CommandStatus(Status.SUCCESS, String.format(EXECUTE_SUCCESS,
				""));
	}

	@Override
	protected CommandStatus performUndo() {
		return new CommandStatus(Status.INVALID);
	}

	// private ToDo editToDo(ToDo todo, String input) {
	// String category = CategoryParser.parse(input);
	// String title = CategoryParser.removeCategory(input, category);
	// LocalDate date = DateParser.parseDate(title);
	//
	// if (DateParser.parseDate(todo.getTitle()) == null) {
	// if (date != null && !checkSpaces(title)) {
	// todo.setTitle(todo.getTitle() + " " + title);
	// todo.setEndDate(date);
	// } else {
	// todo.setTitle(title);
	// todo.setEndDate(date);
	// }
	// } else {
	// if (!DateParser.checkDigits(todo.getTitle())) {
	// String oldRelativeDate = DateParser.getRelativeDate(todo.getTitle());
	// String newRelativeDate = DateParser.getRelativeDate(title);
	// todo.setTitle(input.replace(oldRelativeDate, newRelativeDate));
	// }
	// }
	//
	// // if (date != null) {
	// // todo.setEndDate(date);
	// // }
	//
	// if (category != null) {
	// todo.setCategory(category);
	// }
	//
	// if (!title.isEmpty() && date == null) {
	// todo.setTitle(title);
	// }
	//
	// return todo;
	// }

	private ToDo editToDo(ToDo todo, String input) throws InvalidDateException {
		int inputStartIndex;
		int inputEndIndex;
		String subStringInput;
		String[] splitSubInputArr;
		String remainingString;
		String fieldType;
		LocalDateTime startDate = todo.getStartDate();
		LocalDateTime endDate = todo.getEndDate();
		TaskType typeOfTaskBefore;
		TaskType typeOfTaskAfter;

		typeOfTaskBefore = checkChangeTaskType(startDate, endDate);

		while (!input.isEmpty()) {
			inputStartIndex = input.indexOf("-");
			inputEndIndex = input.indexOf("-", inputStartIndex + 1);

			if (inputStartIndex == -1) {
				subStringInput = null;
			} else {
				if (inputStartIndex != -1 && inputEndIndex != -1) {
					subStringInput = input.substring(inputStartIndex,
							inputEndIndex).trim();
				} else {
					subStringInput = input.substring(inputStartIndex).trim();
				}

			}
			splitSubInputArr = stringSplit(subStringInput, 2);
			fieldType = splitSubInputArr[0];
			remainingString = splitSubInputArr[1];
			todo = processFieldType(fieldType, remainingString, todo);
			input = input.replace(subStringInput, " ").trim();
		}
		startDate = todo.getStartDate();
		endDate = todo.getEndDate();
		typeOfTaskAfter = checkChangeTaskType(startDate, endDate);
		if (typeOfTaskBefore != typeOfTaskAfter) {
			// change Task Type
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
		case STARTTIME:
			break;
		case ENDTIME:
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

		if (userInput.trim().isEmpty()) {
			return FieldType.INVALID;
		}

		switch (userInput) {
		case "-title":
			return FieldType.TITLE;
		case "-category":
			return FieldType.CATEGORY;
		case "-startdate":
			return FieldType.STARTDATE;
		case "-enddate":
			return FieldType.ENDDATE;
		case "-starttime":
			return FieldType.STARTTIME;
		case "-endtime":
			return FieldType.ENDTIME;
		case "-priority":
			return FieldType.PRIORITY;
		default:
			return FieldType.INVALID;
		}
	}

	private TaskType checkChangeTaskType(LocalDateTime startDate,
			LocalDateTime endDate) {

		if ((startDate == null) && (endDate == null)) {
			return TaskType.FLOATTASK;
		} else if ((startDate == null) && (endDate != null)) {
			return TaskType.TIMETASK;
		} else {
			return TaskType.DATETASK;
		}

	}

}

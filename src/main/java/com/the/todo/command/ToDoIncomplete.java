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

import com.the.todo.command.CommandStatus.Status;
import com.the.todo.model.ToDo;
import com.the.todo.storage.ToDoStore;

//@author A0112969W

public class ToDoIncomplete extends ToDoCommand {

	private static final String EXECUTE_DOES_NOT_EXIST = "ToDo %s does not exist.";
	private static final String EXECUTE_ILLEGAL_ARGUMENT = "Seems like you are missing somethings.";
	private static final String EXECUTE_ERROR = "An error occured while updating ToDo.";
	private static final String EXECUTE_SUCCESS = "Successful";
	private static final String EXECUTE_NOTDONE = "Task had been uncompleted";

	private ToDoStore todoStorage;
	private ToDo newtodo;

	public ToDoIncomplete(ToDoStore todoStorage, ToDo todo) {
		super();
		this.todoStorage = todoStorage;
		this.todo = todo;
		this.undoable = true;
	}

	/* (non-Javadoc)
	 * @see com.the.todo.command.ToDoCommand#performExecute()
	 * This method will check whether the task exist. If the task does not 
	 * exist it will send return a error message. It will also check for duplicated commands,
	 * if the task had been uncompleted and the user entered incomplete command again the method 
	 * will feedback an error message.
	 */
	@Override
	protected CommandStatus performExecute() {
		
		newtodo = new ToDo(todo);
		todoStorage.update(newtodo.getId(), newtodo);

		if (this.newtodo == null) {
			return new CommandStatus(Status.ERROR, String.format(
					EXECUTE_DOES_NOT_EXIST, ""));
		}

		if(!newtodo.isCompleted()) {
			return new CommandStatus(Status.ERROR, EXECUTE_NOTDONE);
		} else {
			this.newtodo = isIncompleteToDo(this.newtodo);
		}
	
		if (this.newtodo == null) {
			return new CommandStatus(Status.ERROR, EXECUTE_ERROR);
		}

		return new CommandStatus(Status.SUCCESS, String.format(EXECUTE_SUCCESS,
				""));
	}

	@Override
	protected CommandStatus performUndo() {
		todoStorage.update(newtodo.getId(), newtodo);
		this.newtodo = isCompleteToDo(this.newtodo);

		if (this.newtodo == null) {
			return new CommandStatus(Status.ERROR, EXECUTE_ERROR);
		}

		return new CommandStatus(Status.SUCCESS, String.format(EXECUTE_SUCCESS,
				""));
	}

	private ToDo isIncompleteToDo(ToDo todo) {
		todo.setCompleted(false);
		return todo;
	}

	private ToDo isCompleteToDo(ToDo todo) {
		todo.setCompleted(true);
		return todo;
	}
	
	@Override
	public ToDo getTodo (){
		return newtodo;
	}
}

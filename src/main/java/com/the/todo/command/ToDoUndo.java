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

import java.util.EmptyStackException;
import java.util.Stack;

import com.the.todo.command.CommandStatus.Status;
import com.the.todo.model.ToDo;
import com.the.todo.storage.ToDoStore;

//@author A0111815R

public class ToDoUndo extends ToDoCommand {

	private ToDoStore todoStorage;
	private Stack<ToDoCommand> undoStack;

	private static final String EXECUTE_ERROR_EMPTY = "There is nothing to undo.";

	public ToDoUndo(ToDoStore todoStorage, Stack<ToDoCommand> undoStack) {
		this.todoStorage = todoStorage;
		this.undoStack = undoStack;
	}

	@Override
	protected CommandStatus performExecute() {
		try {
			ToDoCommand lastCommand = undoStack.peek();
			todo = lastCommand.getTodo();
			CommandStatus status = lastCommand.undo();
			undoStack.pop();
			return status;
		} catch (EmptyStackException ex) {
			return new CommandStatus(Status.ERROR, EXECUTE_ERROR_EMPTY);
		}
	}

	@Override
	protected CommandStatus performUndo() {
		return null;
	}

	@Override
	public ToDo getTodo (){
		return todo;
	}
}

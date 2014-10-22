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

import com.the.todo.command.CommandStatus.Status;
import com.the.todo.model.ToDo;
import com.the.todo.storage.ToDoStore;

public class ToDoRead extends ToDoCommand {

	ToDoStore todoStorage;
	List<ToDo> todoList;

	public ToDoRead(ToDoStore todoStorage, List<ToDo> todoList) {
		this.todoStorage = todoStorage;
		this.todoList = todoList;
		this.undoable = false;
	}

	@Override
	protected CommandStatus performExecute() {
		todoList.clear();
		
		for (ToDo todo : todoStorage.getAll()) {
			todoList.add(new ToDo(todo));
		}
		
		return new CommandStatus(Status.SUCCESS, "");
	}

	@Override
	protected CommandStatus performUndo() {
		return null;
	}

}

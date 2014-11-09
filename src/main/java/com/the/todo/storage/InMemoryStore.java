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

package com.the.todo.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.the.todo.model.ToDo;

//@author A0111815R

public class InMemoryStore implements ToDoStore {

	private List<ToDo> store;

	public InMemoryStore() {
		this.store = new ArrayList<ToDo>();
	}

	@Override
	public List<ToDo> getAll() {
		return store;
	}

	@Override
	public List<ToDo> getAllCompleted() {
		List<ToDo> completeToDo = new ArrayList<ToDo>();

		for (int i = 0; i < store.size(); i++) {
			if (store.get(i).isCompleted()) {
				completeToDo.add(store.get(i));
			}
		}
		return completeToDo;
	}

	@Override
	public List<ToDo> getAllUncompleted() {
		List<ToDo> inCompleteToDo = new ArrayList<ToDo>();

		for (int i = 0; i < store.size(); i++) {
			if (!store.get(i).isCompleted()) {
				inCompleteToDo.add(store.get(i));
			}
		}
		return inCompleteToDo;
	}

	@Override
	public ToDo get(ToDo todo) {
		int index = store.indexOf(todo);
		return store.get(index);
	}

	@Override
	public ToDo save(ToDo todo) {
		store.add(todo);
		return todo;
	}

	@Override
	public ToDo update(UUID id, ToDo todo) {
		int index = Integer.MAX_VALUE;

		for (int i = 0; i < store.size(); i++) {
			if (store.get(i).getId().equals(id)) {
				index = i;
				break;
			}
		}

		if (index != Integer.MAX_VALUE) {
			store.remove(index);
			store.add(todo);
			return todo;
		} else {
			return null;
		}
	}

	@Override
	public void delete(ToDo todo) {
		store.remove(todo);
	}

	@Override
	public int count() {
		return store.size();
	}

	@Override
	public void saveToFile() {
	}

}

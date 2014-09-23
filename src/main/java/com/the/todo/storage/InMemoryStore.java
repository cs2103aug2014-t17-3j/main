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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.the.todo.model.ToDo;

public class InMemoryStore implements ToDoStore {

	private Map<String, ToDo> store;

	public InMemoryStore() {
		this.store = new HashMap<String, ToDo>();
	}

	@Override
	public Collection<ToDo> getAll() {
		return store.values();
	}

	@Override
	public ToDo get(String id) {
		return store.get(id);
	}

	@Override
	public ToDo save(ToDo todo) {
		store.put(todo.getId(), todo);
		return todo;
	}

	@Override
	public ToDo update(String id, ToDo todo) {
		ToDo old = store.get(id);
		if (old == null) {
			return null;
		}
		store.put(id, todo);
		return todo;
	}

	@Override
	public void delete(String id) {
		store.remove(id);
	}

	@Override
	public int count() {
		return store.size();
	}

}

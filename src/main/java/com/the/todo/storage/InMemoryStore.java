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

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.the.todo.io.FileHandler;
import com.the.todo.model.ToDo;

public class InMemoryStore implements ToDoStore {

	private Gson gson;
	private List<ToDo> store;

	public InMemoryStore() {
		this.gson = Converters.registerLocalDate(new GsonBuilder()).serializeNulls().create();
		this.store = readFromFile();
	}

	@Override
	public List<ToDo> getAll() {		
		return store;
	}

	@Override
	public List<ToDo> getAllCompleted() {
		return null;
	}

	@Override
	public List<ToDo> getAllUncompleted() {
		return null;
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
	public void delete(ToDo todo) {
		store.remove(todo);
	}

	@Override
	public int count() {
		return store.size();
	}
	
	private List<ToDo> readFromFile() {
		String contents = null;
		try {
			contents = FileHandler.readFile("");
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
		
		if (contents.isEmpty()) {
			return new ArrayList<ToDo>();
		}
		
		Type collectionType = new TypeToken<List<ToDo>>() {}.getType();
		List<ToDo> todos = gson.fromJson(contents, collectionType);
		
		return todos;
	}
	
	@Override
	public void saveToFile() {
		try {
			FileHandler.writeFile("", gson.toJson(this.store));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

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

import org.joda.time.LocalDate;

import com.the.todo.model.ToDo;
import com.the.todo.storage.InMemoryStore;

public class CommandParser {

	private static InMemoryStore memoryStore;

	public CommandParser() {
		memoryStore = new InMemoryStore();
	}

	public InMemoryStore getMemoryStore() {
		return memoryStore;
	}

	public void commandProcess(String command) {
		String[] inputs = command.split(" ", 2);

		switch (inputs[0].trim().toLowerCase()) {
		case "add":
			ToDo todo = processAdd(inputs[1]);
			memoryStore.save(todo);
		case "read":
			memoryStore.getAll();
			break;
		case "delete":
			memoryStore.delete(inputs[1]);
			break;
		case "edit":
			String input = inputs[1];
			String[] editInputs = input.trim().split(" ", 2);
			ToDo todoUpdate = memoryStore.get(editInputs[0]);
			if (todoUpdate == null) {
				System.out.println("No Such object");
			} else {
				todoUpdate = processEdit(editInputs[1], todoUpdate);
				memoryStore.update(editInputs[0], todoUpdate);
			}
			break;
		}

		System.out.println("-----------------------------");
		for (ToDo todo : memoryStore.getAll()) {
			System.out.println("ID: " + todo.getId());
			System.out.println("Title: " + todo.getTitle());
			System.out.println("Date: " + todo.getEndDate());
			System.out.println("Category: " + todo.getCategory());
			System.out.println("Completed: " + todo.isCompleted());
			System.out.println("Delete: " + todo.isDeleted());
		}
	}

	private ToDo processEdit(String input, ToDo todoUpdate) {
		int subIndex = input.indexOf('+');
		int endIndex;
		String subString;
		LocalDate date = DateParser.parseDate(input);
		if (date == null) {
			todoUpdate.setTitle(input);
		} else {
			todoUpdate.setTitle(input);
			todoUpdate.setEndDate(date);
		}
		if (subIndex != -1) {
			String beforeSubString = input.substring(subIndex).trim();
			endIndex = beforeSubString.indexOf(" ");
			if(endIndex != -1) {
				subString = beforeSubString.substring(0, endIndex);
			}else{
				subString = input.substring(subIndex).trim();
			}
			todoUpdate.setCategory(subString);
		}
		return todoUpdate;
	}

	private ToDo processAdd(String input) {
		ToDo todo = new ToDo(input);
		int subIndex = input.indexOf('+');
		int endIndex;
		String subString;
		if (subIndex != -1) {
			String beforeSubString = input.substring(subIndex).trim();
			endIndex = beforeSubString.indexOf(" ");
			if(endIndex != -1) {
				subString = beforeSubString.substring(0, endIndex);
			}else{
				subString = input.substring(subIndex).trim();
			}
			todo.setCategory(subString);
		}
		LocalDate date = DateParser.parseDate(input);
		if (date != null) {
			todo.setEndDate(date);
		}

		return todo;
	}

}
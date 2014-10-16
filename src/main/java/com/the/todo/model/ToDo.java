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

package com.the.todo.model;

import org.joda.time.LocalDateTime;

public class ToDo implements Comparable<ToDo> {

	public static enum Type {
		FLOATING, DEADLINE, TIMED
	};

	public static final LocalDateTime INVALID_DATE = new LocalDateTime(Integer.MAX_VALUE);
	private static int nextId = 0; 

	private Type type;
	private int id;
	private String title;
	private String description;
	private String category;
	private LocalDateTime startDate = INVALID_DATE;
	private LocalDateTime endDate = INVALID_DATE;
	private boolean completed = false;
	private boolean deleted = false;

	public ToDo() {
		this("");
	}

	public ToDo(String title) {
		this.type = Type.FLOATING;
		this.id = nextId;
		this.title = title;
		
		nextId++;
	}
	
	public ToDo(String title, LocalDateTime endDate) {
		this(title);
		this.type = Type.DEADLINE;
		this.endDate = endDate;
	}
	
	public ToDo(String title, LocalDateTime startDate, LocalDateTime endDate) {
		this(title);
		this.type = Type.TIMED;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isFloatingTask() {
		if (this.type.equals(Type.FLOATING)) {
			return true;
		}

		return false;
	}

	public boolean isTimedTask() {
		if (this.type.equals(Type.TIMED)) {
			return true;
		}

		return false;
	}

	public boolean isDeadlineTask() {
		if (this.type.equals(Type.DEADLINE)) {
			return true;
		}

		return false;
	}

	@Override
	public String toString() {
		return "ToDo [title=" + title + ", category=" + category
				+ ", startDate=" + startDate + ", endDate=" + endDate
				+ ", completed=" + completed + ", deleted=" + deleted + "]";
	}

	@Override
	public int compareTo(ToDo todo) {

		if (todo == null) {
			throw new IllegalArgumentException();
		}

		if (this.isFloatingTask() && todo.isFloatingTask()) {
			return this.getTitle().compareToIgnoreCase(todo.getTitle());
		}

		if (this.isFloatingTask()) {
			return 1;
		}

		if (todo.isFloatingTask()) {
			return -1;
		}

		LocalDateTime currentTaskDate = getDateToCompare(this);
		LocalDateTime inputTaskDate = getDateToCompare(todo);

		return currentTaskDate.compareTo(inputTaskDate);

	}

	private LocalDateTime getDateToCompare(ToDo todo) {
		if (todo.isFloatingTask()) {
			return ToDo.INVALID_DATE;
		}

		if (todo.isDeadlineTask()) {
			return todo.getEndDate();
		} else {
			return todo.getStartDate();
		}
	}
}

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

import java.util.UUID;

import org.joda.time.LocalDateTime;

public class ToDo implements Comparable<ToDo> {

	public static enum Type {
		FLOATING, DEADLINE, TIMED
	};

	public static enum Priority {
		HIGH, MEDIUM, LOW
	};

	public static final LocalDateTime INVALID_DATE = new LocalDateTime(
			"292278993-12-31T23:59:59.999");

	private UUID id;
	private Type type;
	private String title;
	private String description;
	private String category;
	private LocalDateTime startDate = INVALID_DATE;
	private LocalDateTime endDate = INVALID_DATE;
	private Priority priority;
	private boolean completed = false;
	private boolean deleted = false;

	public ToDo() {
		this("");
	}

	public ToDo(String title) {
		this.type = Type.FLOATING;
		this.id = UUID.randomUUID();
		this.title = title;
		this.priority = Priority.LOW;
	}

	public ToDo(String title, LocalDateTime endDate) {
		this(title);
		this.type = Type.DEADLINE;
		this.endDate = endDate;
		this.priority = Priority.LOW;
	}

	public ToDo(String title, LocalDateTime startDate, LocalDateTime endDate) {
		this(title);
		this.type = Type.TIMED;
		this.startDate = startDate;
		this.endDate = endDate;
		this.priority = Priority.LOW;
	}

	public ToDo(ToDo oldToDo) {
		this.id = oldToDo.getId();
		this.type = oldToDo.getType();
		this.title = oldToDo.getTitle();
		this.category = oldToDo.getCategory();
		this.startDate = oldToDo.getStartDate();
		this.endDate = oldToDo.getEndDate();
		this.priority = oldToDo.getPriority();
		this.completed = oldToDo.isCompleted();
		this.deleted = oldToDo.isDeleted();
	}

	public UUID getId() {
		return id;
	}

	public Type getType() {
		return type;
	}

	public void setFloatingToDo() {
		this.type = Type.FLOATING;
	}

	public void setDeadlineToDo() {
		this.type = Type.DEADLINE;
	}

	public void setTimedToDo() {
		this.type = Type.TIMED;
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

	public void removeStartDate() {
		this.startDate = INVALID_DATE;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public void removeEndDate() {
		this.endDate = INVALID_DATE;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority prioritySetByUser) {
		priority = prioritySetByUser;
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

	public boolean isFloatingToDo() {
		if (this.type.equals(Type.FLOATING)) {
			return true;
		}

		return false;
	}

	public boolean isTimedToDo() {
		if (this.type.equals(Type.TIMED)) {
			return true;
		}

		return false;
	}

	public boolean isDeadlineToDo() {
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

		LocalDateTime currentTaskDate = getDateToCompare(this);
		LocalDateTime inputTaskDate = getDateToCompare(todo);

		boolean isSameDateAndTime = currentTaskDate.compareTo(inputTaskDate) == 0;
		boolean isSameType = this.getType().compareTo(todo.getType()) == 0;
		boolean isSamePriority = this.getPriority().compareTo(todo.getPriority()) == 0;
		
		if (isSamePriority) {
			if (isSameDateAndTime) {
				if (isSameType) {
					return this.getTitle().compareToIgnoreCase(todo.getTitle());
				} else {
					if (this.getType() == Type.FLOATING) {
						return -1;
					} else if (todo.getType() == Type.FLOATING) {
						return 1;
					} else {
						return this.getType().compareTo(todo.getType());
					}
				}
			} else {
				return currentTaskDate.compareTo(inputTaskDate);
			}
		} else {
			return this.getPriority().compareTo(todo.getPriority());
		}

	}

	private LocalDateTime getDateToCompare(ToDo todo) {
		if (todo.isFloatingToDo()) {
			return ToDo.INVALID_DATE;
		}

		if (todo.isDeadlineToDo()) {
			return todo.getEndDate();
		} else {
			return todo.getStartDate();
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ToDo other = (ToDo) obj;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (completed != other.completed)
			return false;
		if (deleted != other.deleted)
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (priority != other.priority)
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

}
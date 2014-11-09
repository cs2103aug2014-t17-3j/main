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

package com.the.todo.task;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;

import org.controlsfx.control.Notifications;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import com.the.todo.model.ToDo;

public class ReminderTask {

	Timer timer;

	public ReminderTask(ToDo todo, LocalDateTime currentDateTime) {
		Period p = new Period(currentDateTime, todo.getEndDate(),
				PeriodType.seconds());

		timer = new Timer();
		timer.schedule(new ToDoReminder(todo), p.getValue(0) * 1000);
	}

	class ToDoReminder extends TimerTask {

		private final ToDo todo;

		public ToDoReminder(ToDo todo) {
			this.todo = todo;
		}

		@Override
		public void run() {
			Platform.runLater(new Runnable() {
				public void run() {
					Notifications.create().title("TheTODO Reminder")
							.text(todo.getTitle()).showWarning();
					timer.cancel();
				}
			});
		}

	}

}

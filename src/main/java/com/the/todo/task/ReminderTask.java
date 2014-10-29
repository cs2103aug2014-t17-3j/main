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

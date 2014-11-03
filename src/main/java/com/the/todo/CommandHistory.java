package com.the.todo;

import java.util.ArrayList;

public class CommandHistory {
	private ArrayList<String> commandHistory;
	private int i;

	public CommandHistory() {
		commandHistory = new ArrayList<String>();
		i = -1;
	}

	public void add(String cmd) {
		commandHistory.add(0, cmd);
		i = -1;
	}

	public String previous() {
		String prev = null;

		if (i < commandHistory.size() - 1) {
			i++;
			prev = commandHistory.get(i);
		}

		return prev;
	}

	public String next() {
		String next = null;

		if (i > 0) {
			i--;
			next = commandHistory.get(i);
		} else {
			i = -1;
		}

		return next;
	}
}

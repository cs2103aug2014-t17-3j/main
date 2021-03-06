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

package com.the.todo;

import java.util.ArrayList;

//@author A0119764W

public class CommandHistory {
	private ArrayList<String> commandHistory;
	private int i;

	public CommandHistory() {
		commandHistory = new ArrayList<String>();
		i = -1;
	}

	/**
	 * Adds command to the history and resets the position to the most recent command. 
	 * A call to previous() returns the last command added and a call to next() returns null.
	 * 
	 * @param cmd
	 */
	public void add(String cmd) {
		commandHistory.add(0, cmd);
		i = -1;
	}

	/**
	 * @return the next command in the history. Returns null if there are no next history.
	 */
	public String previous() {
		String prev = null;

		if (i < commandHistory.size() - 1) {
			i++;
			prev = commandHistory.get(i);
		}

		return prev;
	}

	/**
	 * @return the previous command in the history. Returns null if there are no previous history.
	 */
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

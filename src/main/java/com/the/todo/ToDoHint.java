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

import com.the.todo.util.CommandUtil;

public class ToDoHint {

	final String ADD_HINT = "add [Task] from [Start Date] to/on/by [End Date] at [Time] +[Category] +[Priority]";
	final String SEARCH_HINT = "search [Keyword]/[Category]";
	final String DELETE_HINT = "delete [ID]";
	final String EDIT_HINT = "edit [ID] -title [title] -category [Category] -startdate [Start Date] -enddate [End Date] -priority [Priority] ";
	final String VIEW_HINT = "view all/completed/incomplete";
	final String COMPLETE_HINT = "complete [ID]";
	final String INCOMPLETE_HINT = "incomplete [ID]";
	private String string;

	public ToDoHint(String recentlyTypedStr) {
		this.string = recentlyTypedStr;

	}

	public String getHints() {

		switch (determineCommand(string)) {
		case "add":
			return ADD_HINT;
		case "search":
			return SEARCH_HINT;
		case "view":
			return VIEW_HINT;
		case "delete":
			return DELETE_HINT;
		case "edit":
			return EDIT_HINT;
		case "complete":
			return COMPLETE_HINT;
		case "incomplete":
			return INCOMPLETE_HINT;
		default:
			return "";
		}

	}

	private String determineCommand(String s) {
		String[] str = { "add", "search", "delete", "edit", "view", "complete",
				"incomplete" };
		if (s.isEmpty()) {
			return "invalid";
		}
		if (s.contains(" ")) {// full command expected
			s = CommandUtil.getFirstPhrase(s);
			for (int i = 0; i < str.length; i++) {
				if (str[i].equals(s)) {
					return str[i];
				}
			}
			return "invalid";

		}
		for (int i = 0; i < str.length; i++) {
			if (str[i].startsWith(s)) {
				return str[i];
			}
		}

		return "invalid";
	}

}

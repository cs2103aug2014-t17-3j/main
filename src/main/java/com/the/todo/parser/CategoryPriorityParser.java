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

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.the.todo.model.ToDo;

public class CategoryPriorityParser {

	private static final String SPACE_DELIM = " ";
	private static final String PLUS_DELIM = "+";
	private static final String MEDIUM = "MEDIUM";
	private static final String LOW = "LOW";
	private static final String HIGH = "HIGH";

	public static List<String> parseAll(String input) {
		List<String> foundList = new ArrayList<String>();
		List<String> wantedList = new ArrayList<String>();
		StringTokenizer tokens = new StringTokenizer(input, SPACE_DELIM);

		while (tokens.hasMoreTokens()) {
			foundList.add(tokens.nextToken());
		}
		for (int i = 0; i < foundList.size(); i++) {
			if (foundList.get(i).contains("+")) {
				StringTokenizer tokenCheck = new StringTokenizer(
						foundList.get(i), PLUS_DELIM);
				while (tokenCheck.hasMoreTokens()) {
					wantedList.add(tokenCheck.nextToken());
				}
			}
		}

		return wantedList;
	}

	public static String parseCategory(List<String> searchList) {
		String category = null;
		for(int i = 0; i < searchList.size(); i++) {
			if (!searchList.get(i).toUpperCase().equals(HIGH)
					|| !searchList.get(i).toUpperCase().equals(LOW)
					|| !searchList.get(i).toUpperCase().equals(MEDIUM)) {
				category = searchList.get(i);
			}
		}
		return category;
	}
	
	public static String parsePriority(List<String> searchList) {
		String priority = null;
		for(int i = 0; i < searchList.size(); i++) {
			if (searchList.get(i).toUpperCase().equals(HIGH)
					|| searchList.get(i).toUpperCase().equals(LOW)
					|| searchList.get(i).toUpperCase().equals(MEDIUM)) {
				priority = searchList.get(i);
			}
		}
		return priority;
	}

	public static String removeStringFromTitle(String input, String removeString) {
		if (removeString != null) {
			return input.replace(removeString, "").replaceAll("( )+", " ").trim();
		} else {
			return input;
		}
	}

}

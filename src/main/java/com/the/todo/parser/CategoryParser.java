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

public class CategoryParser {

	private static final String DELIM = " ";

	public static String parse(String input) {
		String category;
		int categoryStartIndex = input.indexOf("+");
		int categoryEndIndex = input.indexOf(" ", categoryStartIndex + 1);

		if (categoryStartIndex == -1) {
			category = null;
		} else {
			if (categoryEndIndex == -1) {
				category = input.substring(categoryStartIndex).trim();
			} else {
				category = input
						.substring(categoryStartIndex, categoryEndIndex).trim();
			}
		}

		return category;
	}

	public static List<String> parseAll(String input) {
		List<String> foundList = new ArrayList<String>();
		List<String> wantedList = new ArrayList<String>();
		StringTokenizer tokens = new StringTokenizer(input, DELIM);

		while (tokens.hasMoreTokens()) {
			foundList.add(tokens.nextToken());
		}
		for(int i = 0; i < foundList.size(); i++) {
			if(foundList.get(i).contains("+")) {
				wantedList.add(foundList.get(i).replace("+", " ").trim());
			}
		}

		return wantedList;
	}

	public static String removeStringFromTitle(String input, String category) {
		if (category != null) {
			return input.replace(category, "").replaceAll("( )+", " ").trim();
		} else {
			return input;
		}
	}

}

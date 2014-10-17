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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.ocpsoft.prettytime.nlp.PrettyTimeParser;
import org.ocpsoft.prettytime.nlp.parse.DateGroup;

public class DateParser {

	public static List<DateGroup> parseDate(String userInput) {
		List<DateGroup> groups = null;
		LocalDateTime date; 
		if (userInput.isEmpty()) {
			return null;
		}
		if (checkDigits(userInput)) {
			date = formatParse(userInput);
			groups = prettyTimeParse(date.toString());
		} else {
			groups = prettyTimeParse(userInput);
		}
		return groups;
	}

	private static LocalDateTime formatParse(String userInput) {
		LocalDateTime date = null;
		String editedDate = "";
		StringTokenizer st;
		int dashIndex = userInput.indexOf('-');
		List<String> dateSplit = new ArrayList<String>();

		if (dashIndex== -1) {
			st = new StringTokenizer(userInput, "/");
			while (st.hasMoreTokens()) {
				dateSplit.add(st.nextToken());
			}
		} else {
			st = new StringTokenizer(userInput, "-");
			while (st.hasMoreTokens()) {
				dateSplit.add(st.nextToken());
			}
		}
		editedDate = dateSplit.get(2) + "/" + dateSplit.get(1) + "/"
				+ dateSplit.get(0);
		try {
			date = LocalDateTime.parse(editedDate,
					DateTimeFormat.forPattern("yyyy/MM/dd"));
		} catch (IllegalFieldValueException e) {
			throw new IllegalFieldValueException(DateTimeFieldType.era(),
					e.getMessage());
		}
		return date;
	}

	public static String getRelativeDate(String userInput) {
		List<DateGroup> groups = new PrettyTimeParser().parseSyntax(userInput);

		if (groups.size() == 0) {
			return null;
		}

		return groups.get(0).getText();
	}

	private static List<DateGroup> prettyTimeParse(String userInput) {
		List<DateGroup> groups = new PrettyTimeParser().parseSyntax(userInput);

		if (groups.size() == 0) {
			return null;
		}

//		LocalDateTime date = new LocalDateTime(groups.get(0).getDates().get(0));
		return groups;
	}

	public static boolean checkDigits(String s) {
		Pattern pattern = Pattern
				.compile("(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})|(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))|(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})");// ("\\b\\d+");
		Matcher matcher = pattern.matcher(s);
		if (matcher.find()) {
			return true;
		}
		return false;
	}

}
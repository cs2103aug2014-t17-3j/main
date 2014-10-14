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

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.ocpsoft.prettytime.nlp.PrettyTimeParser;
import org.ocpsoft.prettytime.nlp.parse.DateGroup;

public class DateParser {

	private static String[] date_formats = { "yyyy-MM-dd", "yyyy/MM/dd",
			"dd/MM/yyyy", "dd-MM-yyyy", "yyyy MMMMM d", "yyyy d MMMMM",
			"MMMMM d yyyy", "d MMMMM yyyy", "MMMMM d", "d MMMMM", "MM/dd/yyyy" };
	private static boolean isValid = false;

	public static LocalDateTime parseDate(String userInput) {
		LocalDateTime date = null;
		if (userInput.isEmpty()) {
			return date;
		}
		if (checkDigits(userInput)) {
			date = formatParse(userInput);
		} else {
			date = prettyTimeParse(userInput);
		}
		return date;
	}

	private static LocalDateTime formatParse(String userInput) {
		LocalDateTime date = null;
		for (String input : userInput.split(" ")) {
			for (String format : DateParser.date_formats) {
				try {
					DateTimeFormatter dtf = DateTimeFormat.forPattern(format);
					date = dtf.parseLocalDateTime(input);
					isValid = true;
				} catch (Exception ex) {
					isValid = false;
				}
			}
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

	private static LocalDateTime prettyTimeParse(String userInput) {
		List<DateGroup> groups = new PrettyTimeParser().parseSyntax(userInput);

		if (groups.size() == 0) {
			return null;
		}

		LocalDateTime date = new LocalDateTime(groups.get(0).getDates().get(0));
		return date;
	}

	public static boolean checkDigits(String s) {
		Pattern pattern = Pattern.compile("\\b\\d+");
		Matcher matcher = pattern.matcher(s);
		if (matcher.find()) {
			return true;
		}
		return false;
	}
	
	public static boolean getIsValid(){
		return isValid;
	}

}
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;

import com.joestelmach.natty.DateGroup;
import com.the.todo.parser.exception.InvalidDateException;
import com.the.todo.util.StringUtil;

public class DateAndTimeParser {

	private static final DateTimeFormatter YEAR_MONTH_DAY_SLASH = DateTimeFormat
			.forPattern("yyyy/MM/dd");
	private static final DateTimeFormatter DAY_MONTH_YEAR_SLASH = DateTimeFormat
			.forPattern("dd/MM/yyyy");
	private static final DateTimeFormatter DAY_MONTH_YEAR_DASH = DateTimeFormat
			.forPattern("dd-MM-yyyy");

	private static final DateTimeParser[] DATE_TIME_PARSERS = {
			DAY_MONTH_YEAR_SLASH.getParser(), DAY_MONTH_YEAR_DASH.getParser() };
	private static final DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
			.append(null, DATE_TIME_PARSERS).toFormatter();

	public static List<DateGroup> parse(String input)
			throws InvalidDateException {
		if (!checkValidDates(input)) {
			throw new InvalidDateException();
		}

		String formattedInput = changeDateStringsFormat(input);
		formattedInput = removeAllIntegers(formattedInput);
		formattedInput = formattedInput.replaceAll("([A-Za-z]+\\d+)", "");

		NattyParserWrapper prettyTime = NattyParserWrapper.getInstance();

		List<DateGroup> groups = prettyTime.parseDateOnly(formattedInput);
		List<DateGroup> newGroups = new ArrayList<DateGroup>();
		for (DateGroup group : groups) {
			String matchingValue = group.getText();
			String regex = ".*\\b" + matchingValue + "\\b.*";

			if (formattedInput.matches(regex)) {
				newGroups.add(group);
			}
		}

		return newGroups;
	}

	private static String removeAllIntegers(String input) {
		String output = input;
		String[] tokens = StringUtil.splitString(input, " ");

		for (String token : tokens) {
			try {
				Integer.parseInt(token);
				output = output.replaceFirst(token, "");
			} catch (NumberFormatException ex) {
			}
		}

		return output;
	}

	public static boolean checkValidDates(String input) {
		boolean isValid = true;
		Pattern pattern = Pattern
				.compile("(?:\\d*)?\\d+[/-](?:\\d*)?\\d+[/-](?:\\d*)?\\d+");
		Matcher matcher = pattern.matcher(input);

		while (matcher.find()) {
			String matchedDate = matcher.group();
			try {
				LocalDate.parse(matchedDate, DATE_TIME_FORMATTER);
			} catch (Exception e) {
				isValid = false;
				break;
			}
		}

		return isValid;
	}

	public static String changeDateStringsFormat(String input) {
		String formattedInput = input;
		Pattern pattern = Pattern
				.compile("(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})|\\b(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))|\\b(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})");
		Matcher matcher = pattern.matcher(input);

		while (matcher.find()) {
			String matchedDate = matcher.group();
			LocalDate date = LocalDate.parse(matchedDate, DAY_MONTH_YEAR_SLASH);
			formattedInput = formattedInput.replace(matchedDate,
					date.toString(YEAR_MONTH_DAY_SLASH));
			System.out.println(formattedInput);
		}

		return formattedInput;
	}

}
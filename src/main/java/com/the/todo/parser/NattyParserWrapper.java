package com.the.todo.parser;

import java.util.List;
import java.util.TimeZone;

import org.joda.time.LocalDateTime;

import com.joestelmach.natty.CalendarSource;
import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

public class NattyParserWrapper {

	private static final String INITIALIZE_MESSAGE = "Initialize PrettyTime Parser";

	private static NattyParserWrapper nattyParserWrapper = null;
	private static Parser nattyParser = null;

	private NattyParserWrapper() {
		nattyParser = new Parser(TimeZone.getDefault());
	}

	public static NattyParserWrapper getInstance() {
		if (nattyParserWrapper == null) {
			nattyParserWrapper = new NattyParserWrapper();
			nattyParser.parse(INITIALIZE_MESSAGE);
		}

		return nattyParserWrapper;
	}

	public List<DateGroup> parseDateOnly(String date) {
		CalendarSource.setBaseDate(new LocalDateTime().withTime(23, 59, 00, 00)
				.toDate());
		return nattyParser.parse(date);
	}
}

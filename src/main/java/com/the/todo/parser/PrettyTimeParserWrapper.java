package com.the.todo.parser;

import java.util.List;
import java.util.TimeZone;

import org.joda.time.LocalDateTime;

import com.joestelmach.natty.CalendarSource;
import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

public class PrettyTimeParserWrapper {

	private static final String INITIALIZE_MESSAGE = "Initialize PrettyTime Parser";

	private static PrettyTimeParserWrapper prettyTimeParserWrapper = null;
	private static Parser prettyTimeParser = null;

	private PrettyTimeParserWrapper() {
		prettyTimeParser = new Parser(TimeZone.getDefault());
	}

	public static PrettyTimeParserWrapper getInstance() {
		if (prettyTimeParserWrapper == null) {
			prettyTimeParserWrapper = new PrettyTimeParserWrapper();
			prettyTimeParser.parse(INITIALIZE_MESSAGE);
		}

		return prettyTimeParserWrapper;
	}

	public List<DateGroup> parseDateOnly(String date) {
		CalendarSource.setBaseDate(new LocalDateTime().withTime(23, 59, 00, 00)
				.toDate());
		return prettyTimeParser.parse(date);
	}
}

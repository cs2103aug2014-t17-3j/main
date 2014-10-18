package com.the.todo.parser;

import java.util.List;
import java.util.TimeZone;

import org.joda.time.LocalDateTime;
import org.ocpsoft.prettytime.nlp.PrettyTimeParser;
import org.ocpsoft.prettytime.nlp.parse.DateGroup;
import org.ocpsoft.prettytime.shade.com.joestelmach.natty.CalendarSource;

public class PrettyTimeParserWrapper {

	private static final String INITIALIZE_MESSAGE = "Initialize PrettyTime Parser";

	private static PrettyTimeParserWrapper prettyTimeParserWrapper = null;
	private static PrettyTimeParser prettyTimeParser = null;

	private PrettyTimeParserWrapper() {
		prettyTimeParser = new PrettyTimeParser(TimeZone.getDefault());
	}

	public static PrettyTimeParserWrapper getInstance() {
		if (prettyTimeParserWrapper == null) {
			prettyTimeParserWrapper = new PrettyTimeParserWrapper();
			prettyTimeParser.parseSyntax(INITIALIZE_MESSAGE);
		}

		return prettyTimeParserWrapper;
	}

	public List<DateGroup> parseDateOnly(String date) {
		CalendarSource.setBaseDate(new LocalDateTime().withTime(23, 59, 00, 00)
				.toDate());
		return prettyTimeParser.parseSyntax(date);
	}
}

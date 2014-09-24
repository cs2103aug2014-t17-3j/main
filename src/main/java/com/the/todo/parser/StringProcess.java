package com.the.todo.parser;

import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

public class StringProcess {

	private static String[] date_formats = { "yyyy-MM-dd", "yyyy/MM/dd",
			"dd/MM/yyyy", "dd-MM-yyyy", "yyyy MMMMM d", "yyyy d MMMMM",
			"MMMMM d yyyy", "d MMMMM yyyy", "MMMMM d", "d MMMMM" };

	public LocalDate stringProcess(String userInput) {
		LocalDate date = null;
		date = dateProcessing(userInput);
		if (date == null)
			date = nattyProcess(userInput);
		return date;
	}

	private LocalDate dateProcessing(String userInput) {
		LocalDate date = null;
		for (String input : userInput.split(" ")) {
			for (String format : StringProcess.date_formats) {
				try {
					DateTimeFormatter dtf = DateTimeFormat.forPattern(format);
					date = dtf.parseLocalDate(input);
				} catch (Exception ex) {
	
				}
			}
		}
		return date;
	}

	private LocalDate nattyProcess(String userInput) {
		Parser parser = new Parser();

		List<DateGroup> groups = parser.parse(userInput);
		LocalDate date = new LocalDate(groups.get(0).getDates().get(0));

		return date;
	}

}

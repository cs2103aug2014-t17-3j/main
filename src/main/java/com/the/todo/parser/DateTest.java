package com.the.todo.parser;

import static org.junit.Assert.*;

import org.joda.time.LocalDate;
import org.junit.Test;

public class DateTest {

	private LocalDate date = null;
	private LocalDate expectedDate = null;
	private StringProcess processingString = new StringProcess();

	@Test
	public void dateTest() {
		date = processingString.stringProcess("");
		assertNull(date);

		date = processingString.stringProcess("study on 29/9/2014");
		expectedDate = new LocalDate(2014, 9, 29);
		assertEquals(expectedDate, date);

		date = processingString.stringProcess("study on 2016/2/15");
		expectedDate = new LocalDate(2016, 2, 15);
		assertEquals(expectedDate, date);

		date = processingString.stringProcess("study on 11/28/2014");
		expectedDate = new LocalDate(2014, 11, 28);
		assertEquals(expectedDate, date);

		date = processingString.stringProcess("study on Christmas");
		expectedDate = new LocalDate(2014, 12, 25);
		assertEquals(expectedDate, date);
		
		date = processingString.stringProcess("easter study");
		expectedDate = new LocalDate(2015, 4, 5);
		assertEquals(expectedDate, date);

//		date = processingString.stringProcess("study on labor day");
//		expectedDate = new LocalDate(2015, 5, 1);
//		assertEquals(expectedDate, date);

		// date = processingString.stringProcess("study on 29/2/2015");
		// expectedDate = new LocalDate(2015, 2, 29);
		// assertEquals(expectedDate, date);
	}

}

package com.the.todo.parser;

import static org.junit.Assert.*;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.the.todo.Logic;
import com.the.todo.io.FileHandler;
import com.the.todo.storage.ToDoStore;

public class EditFunctionTest {

	private Logic appLogic;
	private ToDoStore todoStorage;

	@Before
	public void setUp() throws Exception {
		FileHandler.writeFile("thetodo.json", "");
		appLogic = new Logic();
		appLogic.processCommand("add CS2013 IVLE quiz");
		todoStorage = appLogic.getTodoStorage();
	}

	@After
	public void tearDown() throws Exception {
		appLogic = null;
		todoStorage = null;
		FileHandler.writeFile("thetodo.json", "");
	}

	/************************************************* Edit 1 Item ******************************************/
	@Test
	public void testUpdateTitle() {
		appLogic.processCommand("edit 1 -title CS2101 developer guide");

		assertEquals(1, todoStorage.count());
		assertEquals("CS2101 developer guide", todoStorage.getAll().get(0)
				.getTitle());
	}

	@Test
	public void testUpdateCategory() {
		appLogic.processCommand("edit 1 -category +test");

		assertEquals(1, todoStorage.count());
		assertEquals("+test", todoStorage.getAll().get(0).getCategory());
	}

	@Test
	public void testUpdateStartDateNumberFormat() {
		LocalDate expectedDate = DateParser.parseDate("29/09/2014");
		appLogic.processCommand("edit 1 -startdate 29/09/2014");

		assertEquals(1, todoStorage.count());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getStartDate());
	}
	
	@Test
	public void testUpdateStartDateRelativeDateFormat() {
		LocalDate expectedDate = calcNextFriday(new LocalDate());
		appLogic.processCommand("edit 1 -startdate friday");

		assertEquals(1, todoStorage.count());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getStartDate());
	}

/***********************************************edit 2 items*****************************************/
	@Test
	public void testUpdateTitleWithCategory() {
		appLogic.processCommand("edit 1 -title CS2101 developer guide -category +test");

		assertEquals(1, todoStorage.count());
		assertEquals("CS2101 developer guide", todoStorage.getAll().get(0)
				.getTitle());
		assertEquals("+test", todoStorage.getAll().get(0).getCategory());
	}
	
	@Test
	public void testUpdateTitleWithStartDateNumberFormat() {
		LocalDate expectedDate = DateParser.parseDate("30/03/2015");
		appLogic.processCommand("edit 1 -title Start Date Test -startdate 30/03/2015");

		assertEquals(1, todoStorage.count());
		assertEquals("Start Date Test", todoStorage.getAll().get(0)
				.getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getStartDate());
	}
	
	@Test
	public void testUpdateTitleWithStartDateRelativeFormat() {
		LocalDate expectedDate = DateParser.parseDate("friday");
		appLogic.processCommand("edit 1 -title Start Date Test(Relative Date) -startdate friday");

		assertEquals(1, todoStorage.count());
		assertEquals("Start Date Test(Relative Date)", todoStorage.getAll().get(0)
				.getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getStartDate());
	}
	
	@Test
	public void testUpdateTitleWithEndDateNumberFormat() {
		LocalDate expectedDate = DateParser.parseDate("25/05/2014");
		appLogic.processCommand("edit 1 -title End Date Test -enddate 25/05/2014");

		assertEquals(1, todoStorage.count());
		assertEquals("End Date Test", todoStorage.getAll().get(0)
				.getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getEndDate());
	}
	
	@Test
	public void testUpdateTitleWithEndDateRelativeFormat() {
		LocalDate expectedDate = DateParser.parseDate("friday");
		appLogic.processCommand("edit 1 -title End Date Test(Relative Date) -enddate friday");

		assertEquals(1, todoStorage.count());
		assertEquals("End Date Test(Relative Date)", todoStorage.getAll().get(0)
				.getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getEndDate());
	}
	
	@Test
	public void testUpdateTitleWithStartAndEndDateRelativeFormat() {
		LocalDate expectedDate1 = DateParser.parseDate("friday");
		LocalDate expectedDate2 = DateParser.parseDate("tuesday");
		appLogic.processCommand("edit 1 -startdate tuesday -enddate friday");

		assertEquals(1, todoStorage.count());
		assertEquals(expectedDate2, todoStorage.getAll().get(0).getStartDate());
		assertEquals(expectedDate1, todoStorage.getAll().get(0).getEndDate());
	}
	
	@Test
	public void testUpdateTitleWithStartAndEndDateNumberFormat() {
		LocalDate expectedDate1 = DateParser.parseDate("25/05/2014");
		LocalDate expectedDate2 = DateParser.parseDate("1/04/2014");
		appLogic.processCommand("edit 1 -startdate 1/04/2014 -enddate 25/05/2014");

		assertEquals(1, todoStorage.count());
		assertEquals(expectedDate2, todoStorage.getAll().get(0).getStartDate());
		assertEquals(expectedDate1, todoStorage.getAll().get(0).getEndDate());
	}
/*************************************************edit 3 items*******************************************/
	@Test
	public void testUpdateTitleWithCategoryAndStartDateNumberFormat() {
		LocalDate expectedDate = DateParser.parseDate("25/05/2014");
		appLogic.processCommand("edit 1 -title test 3 items -category +test -startdate 25/05/2014");

		assertEquals(1, todoStorage.count());
		assertEquals("test 3 items", todoStorage.getAll().get(0)
				.getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getStartDate());
		assertEquals("+test", todoStorage.getAll().get(0).getCategory());
	}
	
	@Test
	public void testUpdateTitleWithCategoryAndStartDateRelativeFormat() {
		LocalDate expectedDate = DateParser.parseDate("friday");
		appLogic.processCommand("edit 1 -title test 3 items -category +test -startdate friday");

		assertEquals(1, todoStorage.count());
		assertEquals("test 3 items", todoStorage.getAll().get(0)
				.getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getStartDate());
		assertEquals("+test", todoStorage.getAll().get(0).getCategory());
	}
/*********************************************** edit 4 items*****************************************/
	@Test
	public void testUpdateTitleWithCategoryAndStartAndEndDateRelativeFormat() {
		LocalDate expectedDate1 = DateParser.parseDate("25/05/2014");
		LocalDate expectedDate2 = DateParser.parseDate("1/06/2014");
		appLogic.processCommand("edit 1 -title test 3 items -category +test -startdate 25/05/2014 -enddate 1/06/2014");

		assertEquals(1, todoStorage.count());
		assertEquals("test 3 items", todoStorage.getAll().get(0)
				.getTitle());
		assertEquals(expectedDate1, todoStorage.getAll().get(0).getStartDate());
		assertEquals(expectedDate2, todoStorage.getAll().get(0).getEndDate());
		assertEquals("+test", todoStorage.getAll().get(0).getCategory());
	}
	
	
	private LocalDate calcNextFriday(LocalDate d) {
		if (d.getDayOfWeek() >= DateTimeConstants.FRIDAY) {
			d = d.plusWeeks(1);
		}
		return d.withDayOfWeek(DateTimeConstants.FRIDAY);
	}

}

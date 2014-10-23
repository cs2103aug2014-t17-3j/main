package com.the.todo.command;

import static org.junit.Assert.*;

import org.joda.time.LocalDateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.the.todo.Logic;
import com.the.todo.io.FileHandler;
import com.the.todo.model.ToDo;
import com.the.todo.parser.DateParser;
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

	/************************************************* Edit 0 Item ******************************************/
	@Test
	public void testUpdateTitleNull() {
		appLogic.processCommand("edit 1 -title ");

		assertEquals(1, todoStorage.count());
		assertEquals("CS2013 IVLE quiz", todoStorage.getAll().get(0).getTitle());
	}

	@Test
	public void testUpdateCategoryNull() {
		appLogic.processCommand("edit 1 -category ");

		assertEquals(1, todoStorage.count());
		assertNull(todoStorage.getAll().get(0).getCategory());
	}

	@Test
	public void testUpdateStartDateNumberFormatNull() throws Exception {
		LocalDateTime expectedDate = ToDo.INVALID_DATE;
		appLogic.processCommand("edit 1 -startdate ");

		assertEquals(1, todoStorage.count());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getStartDate());
	}

	@Test
	public void testUpdateEndDateRelativeDateFormatNull() throws Exception {
		LocalDateTime expectedDate = ToDo.INVALID_DATE;
		appLogic.processCommand("edit 1 -enddate ");

		assertEquals(1, todoStorage.count());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getStartDate());
	}

	/********************************************** with 1 parameters ******************************************/
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
	public void testUpdateStartDateNumberFormat() throws Exception {
		LocalDateTime expectedDate = new LocalDateTime(DateParser
				.parse("29/09/2014").get(0).getDates().get(0));
		appLogic.processCommand("edit 1 -startdate 29/09/2014");

		assertEquals(1, todoStorage.count());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getStartDate());
	}

	@Test
	public void testUpdateStartDateRelativeDateFormat() throws Exception {
		LocalDateTime expectedDate = new LocalDateTime(DateParser
				.parse("friday").get(0).getDates().get(0));
		appLogic.processCommand("edit 1 -startdate friday");

		assertEquals(1, todoStorage.count());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getStartDate());
	}
	
	@Test
	public void testRemoveStartDateNumberFormat() throws Exception {
		LocalDateTime expectedDate = ToDo.INVALID_DATE;
		appLogic.processCommand("edit 1 -startdate 29/09/2014");
		appLogic.processCommand("edit 1 -removestart");

		assertEquals(1, todoStorage.count());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getStartDate());
	}

	@Test
	public void testRemoveStartDateRelativeDateFormat() throws Exception {
		LocalDateTime expectedDate = ToDo.INVALID_DATE;
		appLogic.processCommand("edit 1 -startdate friday");
		appLogic.processCommand("edit 1 -removestart");

		assertEquals(1, todoStorage.count());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getStartDate());
	}
	
	@Test
	public void testRemoveEndDateNumberFormat() throws Exception {
		LocalDateTime expectedDate = ToDo.INVALID_DATE;
		appLogic.processCommand("edit 1 -enddate 29/09/2014");
		appLogic.processCommand("edit 1 -removeend");

		assertEquals(1, todoStorage.count());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getEndDate());
	}

	@Test
	public void testRemoveEndDateRelativeDateFormat() throws Exception {
		LocalDateTime expectedDate = ToDo.INVALID_DATE;
		appLogic.processCommand("edit 1 -enddate friday");
		appLogic.processCommand("edit 1 -removeend");

		assertEquals(1, todoStorage.count());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getEndDate());
	}

	/**************************************************** Edit 1 Item with short form keywords *******************/
	@Test
	public void testUpdateT() {
		appLogic.processCommand("edit 1 -t Test short form");
	
		assertEquals(1, todoStorage.count());
		assertEquals("Test short form", todoStorage.getAll().get(0).getTitle());
	}

	@Test
	public void testUpdateC() {
		appLogic.processCommand("edit 1 -c +ShortForm");

		assertEquals(1, todoStorage.count());
		assertEquals("+ShortForm", todoStorage.getAll().get(0).getCategory());
	}

	@Test
	public void testUpdateSDateNumberFormat() throws Exception {
		LocalDateTime expectedDate = new LocalDateTime(DateParser
				.parse("29/09/2014").get(0).getDates().get(0));
		appLogic.processCommand("edit 1 -s 29/09/2014");

		assertEquals(1, todoStorage.count());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getStartDate());
	}

	@Test
	public void testUpdateSDateRelativeDateFormat() throws Exception {
		LocalDateTime expectedDate = new LocalDateTime(DateParser
				.parse("friday").get(0).getDates().get(0));
		appLogic.processCommand("edit 1 -s friday");

		assertEquals(1, todoStorage.count());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getStartDate());
	}
	
	@Test
	public void testRemoveSDateNumberFormat() throws Exception {
		LocalDateTime expectedDate = ToDo.INVALID_DATE;
		appLogic.processCommand("edit 1 -s 29/09/2014");
		appLogic.processCommand("edit 1 -rs");

		assertEquals(1, todoStorage.count());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getStartDate());
	}

	@Test
	public void testRemoveSDateRelativeDateFormat() throws Exception {
		LocalDateTime expectedDate = ToDo.INVALID_DATE;
		appLogic.processCommand("edit 1 -s friday");
		appLogic.processCommand("edit 1 -rs");

		assertEquals(1, todoStorage.count());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getStartDate());
	}
	
	@Test
	public void testRemoveEDateNumberFormat() throws Exception {
		LocalDateTime expectedDate = ToDo.INVALID_DATE;
		appLogic.processCommand("edit 1 -e 29/09/2014");
		appLogic.processCommand("edit 1 -re");

		assertEquals(1, todoStorage.count());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getEndDate());
	}

	@Test
	public void testRemoveEDateRelativeDateFormat() throws Exception {
		LocalDateTime expectedDate = ToDo.INVALID_DATE;
		appLogic.processCommand("edit 1 -e friday");
		appLogic.processCommand("edit 1 -re");

		assertEquals(1, todoStorage.count());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getEndDate());
	}
	/*********************************************** edit 2 items *****************************************/
	@Test
	public void testUpdateTitleWithCategory() {
		appLogic.processCommand("edit 1 -title CS2101 developer guide -category +test");

		assertEquals(1, todoStorage.count());
		assertEquals("CS2101 developer guide", todoStorage.getAll().get(0)
				.getTitle());
		assertEquals("+test", todoStorage.getAll().get(0).getCategory());
	}

	@Test
	public void testUpdateTitleWithStartDateNumberFormat() throws Exception {
		LocalDateTime expectedDate = new LocalDateTime(DateParser
				.parse("30/03/2015").get(0).getDates().get(0));
		appLogic.processCommand("edit 1 -title Start Date Test -startdate 30/03/2015");

		assertEquals(1, todoStorage.count());
		assertEquals("Start Date Test", todoStorage.getAll().get(0).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getStartDate());
	}

	@Test
	public void testUpdateTitleWithStartDateRelativeFormat() throws Exception {
		LocalDateTime expectedDate = new LocalDateTime(DateParser
				.parse("friday").get(0).getDates().get(0));
		appLogic.processCommand("edit 1 -title Start Date Test(Relative Date) -startdate friday");

		assertEquals(1, todoStorage.count());
		assertEquals("Start Date Test(Relative Date)", todoStorage.getAll()
				.get(0).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getStartDate());
	}

	@Test
	public void testUpdateTitleWithEndDateNumberFormat() throws Exception {
		LocalDateTime expectedDate = new LocalDateTime(DateParser
				.parse("25/05/2014").get(0).getDates().get(0));
		appLogic.processCommand("edit 1 -title End Date Test -enddate 25/05/2014");

		assertEquals(1, todoStorage.count());
		assertEquals("End Date Test", todoStorage.getAll().get(0).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getEndDate());
	}

	@Test
	public void testUpdateTitleWithEndDateRelativeFormat() throws Exception {
		LocalDateTime expectedDate = new LocalDateTime(DateParser
				.parse("friday").get(0).getDates().get(0));
		appLogic.processCommand("edit 1 -title End Date Test(Relative Date) -enddate friday");

		assertEquals(1, todoStorage.count());
		assertEquals("End Date Test(Relative Date)", todoStorage.getAll()
				.get(0).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getEndDate());
	}

	@Test
	public void testUpdateTitleWithStartAndEndDateRelativeFormat()
			throws Exception {
		LocalDateTime expectedDate1 = new LocalDateTime(DateParser
				.parse("friday").get(0).getDates().get(0));
		LocalDateTime expectedDate2 = new LocalDateTime(DateParser
				.parse("tuesday").get(0).getDates().get(0));

		assertEquals(1, todoStorage.count());

		appLogic.processCommand("edit 1 -startdate tuesday -enddate friday");

		assertEquals(1, todoStorage.count());
		assertEquals(expectedDate2, todoStorage.getAll().get(0).getStartDate());
		assertEquals(expectedDate1, todoStorage.getAll().get(0).getEndDate());
	}

	@Test
	public void testUpdateTitleWithStartAndEndDateNumberFormat()
			throws Exception {
		LocalDateTime expectedDate1 = new LocalDateTime(DateParser
				.parse("25/05/2014").get(0).getDates().get(0));
		LocalDateTime expectedDate2 = new LocalDateTime(DateParser
				.parse("1/04/2014").get(0).getDates().get(0));
		appLogic.processCommand("edit 1 -startdate 1/04/2014 -enddate 25/05/2014");

		assertEquals(1, todoStorage.count());
		assertEquals(expectedDate2, todoStorage.getAll().get(0).getStartDate());
		assertEquals(expectedDate1, todoStorage.getAll().get(0).getEndDate());
	}

	/*********************************************** edit 2 items with short form keywords *****************************************/
	@Test
	public void testUpdateTWithC() {
		appLogic.processCommand("edit 1 -t CS2101 developer guide -c +test");

		assertEquals(1, todoStorage.count());
		assertEquals("CS2101 developer guide", todoStorage.getAll().get(0)
				.getTitle());
		assertEquals("+test", todoStorage.getAll().get(0).getCategory());
	}

	@Test
	public void testUpdateTWithSDateNumberFormat() throws Exception {
		LocalDateTime expectedDate = new LocalDateTime(DateParser
				.parse("30/03/2015").get(0).getDates().get(0));
		appLogic.processCommand("edit 1 -t Start Date Test -s 30/03/2015");

		assertEquals(1, todoStorage.count());
		assertEquals("Start Date Test", todoStorage.getAll().get(0).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getStartDate());
	}

	@Test
	public void testUpdateTWithSDateRelativeFormat() throws Exception {
		LocalDateTime expectedDate = new LocalDateTime(DateParser
				.parse("friday").get(0).getDates().get(0));
		appLogic.processCommand("edit 1 -t Start Date Test(Relative Date) -s friday");

		assertEquals(1, todoStorage.count());
		assertEquals("Start Date Test(Relative Date)", todoStorage.getAll()
				.get(0).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getStartDate());
	}

	@Test
	public void testUpdateTWithEDateNumberFormat() throws Exception {
		LocalDateTime expectedDate = new LocalDateTime(DateParser
				.parse("25/05/2014").get(0).getDates().get(0));
		appLogic.processCommand("edit 1 -t End Date Test -e 25/05/2014");

		assertEquals(1, todoStorage.count());
		assertEquals("End Date Test", todoStorage.getAll().get(0).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getEndDate());
	}

	@Test
	public void testUpdateTWithEDateRelativeFormat() throws Exception {
		LocalDateTime expectedDate = new LocalDateTime(DateParser
				.parse("friday").get(0).getDates().get(0));
		appLogic.processCommand("edit 1 -t End Date Test(Relative Date) -e friday");

		assertEquals(1, todoStorage.count());
		assertEquals("End Date Test(Relative Date)", todoStorage.getAll()
				.get(0).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getEndDate());
	}

	@Test
	public void testUpdateTitleWithSAndEDateRelativeFormat() throws Exception {
		LocalDateTime expectedDate1 = new LocalDateTime(DateParser
				.parse("friday").get(0).getDates().get(0));
		LocalDateTime expectedDate2 = new LocalDateTime(DateParser
				.parse("tuesday").get(0).getDates().get(0));

		assertEquals(1, todoStorage.count());

		appLogic.processCommand("edit 1 -s tuesday -e friday");

		assertEquals(1, todoStorage.count());
		assertEquals(expectedDate2, todoStorage.getAll().get(0).getStartDate());
		assertEquals(expectedDate1, todoStorage.getAll().get(0).getEndDate());
	}

	@Test
	public void testUpdateTitleWithSAndEDateNumberFormat() throws Exception {
		LocalDateTime expectedDate1 = new LocalDateTime(DateParser
				.parse("25/05/2014").get(0).getDates().get(0));
		LocalDateTime expectedDate2 = new LocalDateTime(DateParser
				.parse("1/04/2014").get(0).getDates().get(0));
		appLogic.processCommand("edit 1 -s 1/04/2014 -e 25/05/2014");

		assertEquals(1, todoStorage.count());
		assertEquals(expectedDate2, todoStorage.getAll().get(0).getStartDate());
		assertEquals(expectedDate1, todoStorage.getAll().get(0).getEndDate());
	}

	/************************************************* edit 3 items *******************************************/
	@Test
	public void testUpdateTitleWithCategoryAndStartDateNumberFormat()
			throws Exception {
		LocalDateTime expectedDate = new LocalDateTime(DateParser
				.parse("25/05/2014").get(0).getDates().get(0));
		appLogic.processCommand("edit 1 -title test 3 items -category +test -startdate 25/05/2014");

		assertEquals(1, todoStorage.count());
		assertEquals("test 3 items", todoStorage.getAll().get(0).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getStartDate());
		assertEquals("+test", todoStorage.getAll().get(0).getCategory());
	}

	@Test
	public void testUpdateTitleWithCategoryAndStartDateRelativeFormat()
			throws Exception {
		LocalDateTime expectedDate = new LocalDateTime(DateParser
				.parse("friday").get(0).getDates().get(0));
		appLogic.processCommand("edit 1 -title test 3 items -category +test -startdate friday");

		assertEquals(1, todoStorage.count());
		assertEquals("test 3 items", todoStorage.getAll().get(0).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getStartDate());
		assertEquals("+test", todoStorage.getAll().get(0).getCategory());
	}

	/*********************************************** edit 4 items *****************************************/
	@Test
	public void testUpdateTitleWithCategoryAndStartAndEndDateRelativeFormat()
			throws Exception {
		LocalDateTime expectedDate1 = new LocalDateTime(DateParser
				.parse("25/05/2014").get(0).getDates().get(0));
		LocalDateTime expectedDate2 = new LocalDateTime(DateParser
				.parse("1/06/2014").get(0).getDates().get(0));
		appLogic.processCommand("edit 1 -title test 3 items -category +test -startdate 25/05/2014 -enddate 1/06/2014");

		assertEquals(1, todoStorage.count());
		assertEquals("test 3 items", todoStorage.getAll().get(0).getTitle());
		assertEquals(expectedDate1, todoStorage.getAll().get(0).getStartDate());
		assertEquals(expectedDate2, todoStorage.getAll().get(0).getEndDate());
		assertEquals("+test", todoStorage.getAll().get(0).getCategory());
	}
	
	/**************************************************** Undo Edit Function ******************************/
	@Test
	public void testUndo() {
		appLogic.processCommand("edit 1 -t Test short form");
		appLogic.processCommand("undo");

		assertEquals(1, todoStorage.count());
		assertEquals("CS2013 IVLE quiz", todoStorage.getAll().get(0).getTitle());
	}
	
	@Test
	public void testUndoEndDate() {
		LocalDateTime expectedDate = ToDo.INVALID_DATE;
		appLogic.processCommand("edit 1 -e 29/09/2014");
		appLogic.processCommand("undo");

		assertEquals(1, todoStorage.count());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getEndDate());
	}

}

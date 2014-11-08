package com.the.todo.command;

import static org.junit.Assert.*;

import org.joda.time.LocalDateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.the.todo.Logic;
import com.the.todo.io.FileHandler;
import com.the.todo.model.ToDo;
import com.the.todo.parser.DateAndTimeParser;
import com.the.todo.storage.ToDoStore;

/**
 * This test cases is created to test all different combinations that the user might want to do with edit function
 * 
 * +---------+-----------------------------------+ 
 * | Section | Remarks                           |
 * +---------+-----------------------------------+
 * |    1    | Edit 1 field                      |
 * +---------+-----------------------------------+
 * |    2    | Edit 1 fields with short keyword  | 
 * +---------+-----------------------------------+
 * |    3    | Edit 2 fields                     |
 * +---------+-----------------------------------+
 * |    4    | Edit 2 fields with short keywords |
 * +---------+-----------------------------------+
 * |    5    | Edit 3 fields                     |
 * +---------+-----------------------------------+
 * |    6    | Edit 4 fields                     |
 * +---------+-----------------------------------+
 * |    7    | Edit 5 fields                     |
 * +---------+-----------------------------------+
 * |    8    | Undo Edited portion               |
 * +---------+-----------------------------------+
 * 
 * There are error messages that cater for the below cases.
 * +-----+-----------------------------------------+
 * | S/N | Not Tested                              |
 * +-----+-----------------------------------------+
 * |  1  | Edit without ID                         |
 * +-----+-----------------------------------------+
 * |  2  | Edit without keywords                   |
 * +-----+-----------------------------------------+
 * |  3  | Edit floating task with only start date |
 * +-----+-----------------------------------------+
 *
 */
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

	/***************************************** Section 1 ***********************************************/
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
	public void testUpdatePriority() {
		appLogic.processCommand("edit 1 -priority +high");

		assertEquals(1, todoStorage.count());
		assertEquals(ToDo.Priority.HIGH, todoStorage.getAll().get(0).getPriority());
	}

	@Test
	public void testRemoveStartDateNumberFormat() throws Exception {
		LocalDateTime expectedDate = ToDo.INVALID_DATE;
		LocalDateTime expectedEndDate = new LocalDateTime(DateAndTimeParser
				.parse("30/10/2014").get(0).getDates().get(0));
		appLogic.processCommand("edit 1 -startdate 29/09/2014 -enddate 30/10/2014");
		appLogic.processCommand("edit 1 -removestart");

		assertEquals(1, todoStorage.count());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getStartDate());
		assertEquals(expectedEndDate, todoStorage.getAll().get(0).getEndDate());
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
		appLogic.processCommand("edit 1 -enddate monday");//today can, friday cannot
		appLogic.processCommand("edit 1 -removeend");

		assertEquals(1, todoStorage.count());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getEndDate());
	}

	/***************************************** Section 2 ***********************************************/
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
	public void testUpdateP() {
		appLogic.processCommand("edit 1 -p +Medium");

		assertEquals(1, todoStorage.count());
		assertEquals(ToDo.Priority.MEDIUM, todoStorage.getAll().get(0).getPriority());
	}

	@Test
	public void testRemoveSDateNumberFormat() throws Exception {
		LocalDateTime expectedDate = ToDo.INVALID_DATE;
		LocalDateTime expectedEndDate = new LocalDateTime(DateAndTimeParser
				.parse("30/10/2014").get(0).getDates().get(0));
		appLogic.processCommand("edit 1 -s 29/09/2014 -e 30/10/2014");
		appLogic.processCommand("edit 1 -rs");

		assertEquals(1, todoStorage.count());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getStartDate());
		assertEquals(expectedEndDate, todoStorage.getAll().get(0).getEndDate());
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

	/***************************************** Section 3 ***********************************************/
	@Test
	public void testUpdateTitleWithCategory() {
		appLogic.processCommand("edit 1 -title CS2101 developer guide -category +test");

		assertEquals(1, todoStorage.count());
		assertEquals("CS2101 developer guide", todoStorage.getAll().get(0)
				.getTitle());
		assertEquals("+test", todoStorage.getAll().get(0).getCategory());
	}
	
	@Test
	public void testUpdateTitleWithPriority() {
		appLogic.processCommand("edit 1 -title CS2101 developer guide -priority +High");

		assertEquals(1, todoStorage.count());
		assertEquals("CS2101 developer guide", todoStorage.getAll().get(0)
				.getTitle());
		assertEquals(ToDo.Priority.HIGH, todoStorage.getAll().get(0).getPriority());
	}

	@Test
	public void testUpdateTitleWithEndDateNumberFormat() throws Exception {
		LocalDateTime expectedDate = new LocalDateTime(DateAndTimeParser
				.parse("25/05/2014").get(0).getDates().get(0));
		appLogic.processCommand("edit 1 -title End Date Test -enddate 25/05/2014");

		assertEquals(1, todoStorage.count());
		assertEquals("End Date Test", todoStorage.getAll().get(0).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getEndDate());
	}

	@Test
	public void testUpdateTitleWithEndDateRelativeFormat() throws Exception {
		LocalDateTime expectedDate = new LocalDateTime(DateAndTimeParser
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
		LocalDateTime expectedDate1 = new LocalDateTime(DateAndTimeParser
				.parse("friday").get(0).getDates().get(0));
		LocalDateTime expectedDate2 = new LocalDateTime(DateAndTimeParser
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
		LocalDateTime expectedDate1 = new LocalDateTime(DateAndTimeParser
				.parse("25/05/2014").get(0).getDates().get(0));
		LocalDateTime expectedDate2 = new LocalDateTime(DateAndTimeParser
				.parse("1/04/2014").get(0).getDates().get(0));
		appLogic.processCommand("edit 1 -startdate 1/04/2014 -enddate 25/05/2014");

		assertEquals(1, todoStorage.count());
		assertEquals(expectedDate2, todoStorage.getAll().get(0).getStartDate());
		assertEquals(expectedDate1, todoStorage.getAll().get(0).getEndDate());
	}

	/***************************************** Section 4 ***********************************************/
	@Test
	public void testUpdateTWithC() {
		appLogic.processCommand("edit 1 -t CS2101 developer guide -c +test");

		assertEquals(1, todoStorage.count());
		assertEquals("CS2101 developer guide", todoStorage.getAll().get(0)
				.getTitle());
		assertEquals("+test", todoStorage.getAll().get(0).getCategory());
	}
	
	@Test
	public void testUpdateTWithP() {
		appLogic.processCommand("edit 1 -t CS2101 developer guide -p +Low");

		assertEquals(1, todoStorage.count());
		assertEquals("CS2101 developer guide", todoStorage.getAll().get(0)
				.getTitle());
		assertEquals(ToDo.Priority.LOW, todoStorage.getAll().get(0).getPriority());
	}

//	@Test
//	public void testUpdateTWithSDateNumberFormat() throws Exception {
//		LocalDateTime expectedDate = new LocalDateTime(DateAndTimeParser
//				.parse("30/03/2015").get(0).getDates().get(0));
//		appLogic.processCommand("edit 1 -s 01/01/2015 -e 05/05/2015");
//		appLogic.processCommand("edit 1 -t Start Date Test -s 30/03/2015");
//
//		assertEquals(1, todoStorage.count());
//		assertEquals("Start Date Test", todoStorage.getAll().get(0).getTitle());
//		assertEquals(expectedDate, todoStorage.getAll().get(0).getStartDate());
//	}

	@Test
	public void testUpdateTWithEDateNumberFormat() throws Exception {
		LocalDateTime expectedDate = new LocalDateTime(DateAndTimeParser
				.parse("25/05/2014").get(0).getDates().get(0));
		appLogic.processCommand("edit 1 -t End Date Test -e 25/05/2014");

		assertEquals(1, todoStorage.count());
		assertEquals("End Date Test", todoStorage.getAll().get(0).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getEndDate());
	}

	@Test
	public void testUpdateTWithEDateRelativeFormat() throws Exception {
		LocalDateTime expectedDate = new LocalDateTime(DateAndTimeParser
				.parse("friday").get(0).getDates().get(0));
		appLogic.processCommand("edit 1 -t End Date Test(Relative Date) -e friday");

		assertEquals(1, todoStorage.count());
		assertEquals("End Date Test(Relative Date)", todoStorage.getAll()
				.get(0).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getEndDate());
	}

	@Test
	public void testUpdateTitleWithSAndEDateRelativeFormat() throws Exception {
		LocalDateTime expectedDate1 = new LocalDateTime(DateAndTimeParser
				.parse("friday").get(0).getDates().get(0));
		LocalDateTime expectedDate2 = new LocalDateTime(DateAndTimeParser
				.parse("tuesday").get(0).getDates().get(0));

		assertEquals(1, todoStorage.count());

		appLogic.processCommand("edit 1 -s tuesday -e friday");

		assertEquals(1, todoStorage.count());
		assertEquals(expectedDate2, todoStorage.getAll().get(0).getStartDate());
		assertEquals(expectedDate1, todoStorage.getAll().get(0).getEndDate());
	}

	@Test
	public void testUpdateTitleWithSAndEDateNumberFormat() throws Exception {
		LocalDateTime expectedDate1 = new LocalDateTime(DateAndTimeParser
				.parse("25/05/2014").get(0).getDates().get(0));
		LocalDateTime expectedDate2 = new LocalDateTime(DateAndTimeParser
				.parse("1/04/2014").get(0).getDates().get(0));
		appLogic.processCommand("edit 1 -s 1/04/2014 -e 25/05/2014");

		assertEquals(1, todoStorage.count());
		assertEquals(expectedDate2, todoStorage.getAll().get(0).getStartDate());
		assertEquals(expectedDate1, todoStorage.getAll().get(0).getEndDate());
	}

	/***************************************** Section 5 ***********************************************/
	@Test
	public void testUpdateTitleWithPriorityAndCategory()
			throws Exception {
		appLogic.processCommand("edit 1 -title test 3 items -priority +High -category +test");

		assertEquals(1, todoStorage.count());
		assertEquals("test 3 items", todoStorage.getAll().get(0).getTitle());
		assertEquals("+test", todoStorage.getAll().get(0).getCategory());
		assertEquals(ToDo.Priority.HIGH, todoStorage.getAll().get(0).getPriority());
	}
	
	@Test
	public void testUpdateTitleWithCategoryAndStartDateNumberFormat()
			throws Exception {
		LocalDateTime expectedDate = new LocalDateTime(DateAndTimeParser
				.parse("25/05/2014").get(0).getDates().get(0));
		appLogic.processCommand("edit 1 -title test 3 items -category +test -enddate 25/05/2014");

		assertEquals(1, todoStorage.count());
		assertEquals("test 3 items", todoStorage.getAll().get(0).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getEndDate());
		assertEquals("+test", todoStorage.getAll().get(0).getCategory());
	}

	@Test
	public void testUpdateTitleWithCategoryAndStartDateRelativeFormat()
			throws Exception {
		LocalDateTime expectedDate = new LocalDateTime(DateAndTimeParser
				.parse("friday").get(0).getDates().get(0));
		appLogic.processCommand("edit 1 -title test 3 items -category +test -enddate friday");

		assertEquals(1, todoStorage.count());
		assertEquals("test 3 items", todoStorage.getAll().get(0).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getEndDate());
		assertEquals("+test", todoStorage.getAll().get(0).getCategory());
	}
	
	@Test
	public void testUpdateTitleWithPriorityAndStartDateNumberFormat()
			throws Exception {
		LocalDateTime expectedDate = new LocalDateTime(DateAndTimeParser
				.parse("25/05/2014").get(0).getDates().get(0));
		appLogic.processCommand("edit 1 -title test 3 items -priority +High -enddate 25/05/2014");

		assertEquals(1, todoStorage.count());
		assertEquals("test 3 items", todoStorage.getAll().get(0).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getEndDate());
		assertEquals(ToDo.Priority.HIGH, todoStorage.getAll().get(0).getPriority());
	}

	@Test
	public void testUpdateTitleWithPriorityAndStartDateRelativeFormat()
			throws Exception {
		LocalDateTime expectedDate = new LocalDateTime(DateAndTimeParser
				.parse("friday").get(0).getDates().get(0));
		appLogic.processCommand("edit 1 -title test 3 items -priority +High -enddate friday");

		assertEquals(1, todoStorage.count());
		assertEquals("test 3 items", todoStorage.getAll().get(0).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getEndDate());
		assertEquals(ToDo.Priority.HIGH, todoStorage.getAll().get(0).getPriority());
	}

	/***************************************** Section 6 ***********************************************/
	@Test
	public void testUpdateTitleWithCategoryAndStartAndEndDateRelativeFormat()
			throws Exception {
		LocalDateTime expectedDate1 = new LocalDateTime(DateAndTimeParser
				.parse("25/05/2014").get(0).getDates().get(0));
		LocalDateTime expectedDate2 = new LocalDateTime(DateAndTimeParser
				.parse("1/06/2014").get(0).getDates().get(0));
		appLogic.processCommand("edit 1 -title test 3 items -category +test -startdate 25/05/2014 -enddate 1/06/2014");

		assertEquals(1, todoStorage.count());
		assertEquals("test 3 items", todoStorage.getAll().get(0).getTitle());
		assertEquals(expectedDate1, todoStorage.getAll().get(0).getStartDate());
		assertEquals(expectedDate2, todoStorage.getAll().get(0).getEndDate());
		assertEquals("+test", todoStorage.getAll().get(0).getCategory());
	}
	
	@Test
	public void testUpdateTitleWithPriorityAndStartAndEndDateRelativeFormat()
			throws Exception {
		LocalDateTime expectedDate1 = new LocalDateTime(DateAndTimeParser
				.parse("25/05/2014").get(0).getDates().get(0));
		LocalDateTime expectedDate2 = new LocalDateTime(DateAndTimeParser
				.parse("1/06/2014").get(0).getDates().get(0));
		appLogic.processCommand("edit 1 -title test 3 items -priority +High -startdate 25/05/2014 -enddate 1/06/2014");

		assertEquals(1, todoStorage.count());
		assertEquals("test 3 items", todoStorage.getAll().get(0).getTitle());
		assertEquals(expectedDate1, todoStorage.getAll().get(0).getStartDate());
		assertEquals(expectedDate2, todoStorage.getAll().get(0).getEndDate());
		assertEquals(ToDo.Priority.HIGH, todoStorage.getAll().get(0).getPriority());
	}
	
	@Test
	public void testUpdateTitleWithCategoryAndPriorityAndEndDateRelativeFormat()
			throws Exception {
		LocalDateTime expectedDate2 = new LocalDateTime(DateAndTimeParser
				.parse("1/06/2014").get(0).getDates().get(0));
		appLogic.processCommand("edit 1 -title test 3 items -category +test -priority +High -enddate 1/06/2014");

		assertEquals(1, todoStorage.count());
		assertEquals("test 3 items", todoStorage.getAll().get(0).getTitle());
		assertEquals(expectedDate2, todoStorage.getAll().get(0).getEndDate());
		assertEquals("+test", todoStorage.getAll().get(0).getCategory());
		assertEquals(ToDo.Priority.HIGH, todoStorage.getAll().get(0).getPriority());
	}
	
	/***************************************** Section 7 ***********************************************/
	@Test
	public void testUpdateTitleWithCategoryAndPriorityAndStartAndEndDateRelativeFormat()
			throws Exception {
		LocalDateTime expectedDate1 = new LocalDateTime(DateAndTimeParser
				.parse("25/05/2014").get(0).getDates().get(0));
		LocalDateTime expectedDate2 = new LocalDateTime(DateAndTimeParser
				.parse("1/06/2014").get(0).getDates().get(0));
		appLogic.processCommand("edit 1 -title test 3 items -category +test -priority +High -startdate 25/05/2014 -enddate 1/06/2014");

		assertEquals(1, todoStorage.count());
		assertEquals("test 3 items", todoStorage.getAll().get(0).getTitle());
		assertEquals(expectedDate1, todoStorage.getAll().get(0).getStartDate());
		assertEquals(expectedDate2, todoStorage.getAll().get(0).getEndDate());
		assertEquals("+test", todoStorage.getAll().get(0).getCategory());
		assertEquals(ToDo.Priority.HIGH, todoStorage.getAll().get(0).getPriority());
	}
	

	/***************************************** Section 8 ***********************************************/
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

package com.the.todo.command;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.the.todo.Logic;
import com.the.todo.io.FileHandler;
import com.the.todo.model.ToDo;
import com.the.todo.storage.ToDoStore;

/**
 * This test cases is created to test all different combinations that the user might put in the input String
 * 
 * +---------+-----------------------------------------------------------------+
 * | Section |                             Remarks                             |
 * +---------+-----------------------------------------------------------------+
 * |    1    | Title Only                                                      |
 * +---------+-----------------------------------------------------------------+
 * |    2    | Title + Date[Number & Relative]                                 |
 * +---------+-----------------------------------------------------------------+
 * |    3    | Title + Category                                                |
 * +---------+-----------------------------------------------------------------+
 * |    4    | Title+ Category + Date                                          |
 * +---------+-----------------------------------------------------------------+
 * |    5    | Title + Priority                                                |
 * +---------+-----------------------------------------------------------------+
 * |    6    | Title + Priority + Category[P & C near each other]              |
 * +---------+-----------------------------------------------------------------+
 * |    7    | Title + Priority + Category[P & C far from each other]          |
 * +---------+-----------------------------------------------------------------+
 * |    8    | Title + Priority + Category[P & C Stick Together Without Space] |
 * +---------+-----------------------------------------------------------------+
 * 
 *
 */

//@author A0111780N

public class AddFunctionTest {

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
	// Add 1 task without start and end date.
	// Type will be floating task
	@Test
	public void testAddOneItem() {

		appLogic.processCommand("add remember to get milk");
		todoStorage = appLogic.getTodoStorage();

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get milk", todoStorage.getAll().get(1)
				.getTitle());
	}

	/***************************************** Section 2 ***********************************************/
	// Add 1 task with end date.
	// Type will be deadline task
	@Test
	public void testAddNumberFormatDate() {
		LocalDateTime expectedDate = new LocalDateTime(2014, 11, 11, 23, 59);
		appLogic.processCommand("add remember to get milk on 11/11/2014");
		todoStorage = appLogic.getTodoStorage();

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get milk on 11/11/2014", todoStorage.getAll()
				.get(1).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(1).getEndDate());
	}

	@Test
	public void testAddRelativeFormatDate() {
		LocalDateTime expectedDate = calcNextFriday(new LocalDateTime());
		appLogic.processCommand("add remember to get milk on Friday");
		todoStorage = appLogic.getTodoStorage();

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get milk on Friday", todoStorage.getAll()
				.get(1).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(1).getEndDate());
	}
	
	// Add 1 task with start date and end date.
	// Type will be Timed task
	@Test
	public void testAddNumberFormatDateStartAndEnd() {
		LocalDateTime expectedDate = new LocalDateTime(2014, 11, 11, 23, 59);
		LocalDateTime expectedEndDate = new LocalDateTime(2014, 11, 24, 23, 59);
		appLogic.processCommand("add remember to get milk from 11/11/2014 to 24/11/2014");
		todoStorage = appLogic.getTodoStorage();

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get milk from 11/11/2014 to 24/11/2014", todoStorage.getAll()
				.get(1).getTitle());
		assertEquals(expectedEndDate, todoStorage.getAll().get(1).getEndDate());
		assertEquals(expectedDate, todoStorage.getAll().get(1).getStartDate());
	}

	/***************************************** Section 3 ***********************************************/
	@Test
	public void testAddCategoryRandomPlace1WithTitle() {
		appLogic.processCommand("add remember to get present +Birthday");

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get present", todoStorage.getAll().get(1)
				.getTitle());
		assertEquals("+Birthday", todoStorage.getAll().get(1).getCategory());
	}

	@Test
	public void testAddCategoryRandomPlace2WithTitle() {
		appLogic.processCommand("add +Birthday remember to get present ");

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get present", todoStorage.getAll().get(1)
				.getTitle());
		assertEquals("+Birthday", todoStorage.getAll().get(1).getCategory());
	}

	/***************************************** Section 4 ***********************************************/
	@Test
	public void testAddCategoryRandomPlaceWithDate() {
		LocalDateTime expectedDate = calcNextFriday(new LocalDateTime());
		appLogic.processCommand("add +Birthday remember to get present on Friday");

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get present on Friday", todoStorage.getAll()
				.get(1).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(1).getEndDate());
		assertEquals("+Birthday", todoStorage.getAll().get(1).getCategory());
	}

	@Test
	public void testAddCategoryRandomPlace1() {
		LocalDateTime expectedDate = calcNextFriday(new LocalDateTime());
		appLogic.processCommand("add remember to get present on Friday +Birthday");

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get present on Friday", todoStorage.getAll()
				.get(1).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(1).getEndDate());
		assertEquals("+Birthday", todoStorage.getAll().get(1).getCategory());
	}

	@Test
	public void testAddCategoryRandomPlace2() {
		LocalDateTime expectedDate = calcNextFriday(new LocalDateTime());
		appLogic.processCommand("add remember to get present +Birthday on Friday");

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get present on Friday", todoStorage.getAll()
				.get(1).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(1).getEndDate());
		assertEquals("+Birthday", todoStorage.getAll().get(1).getCategory());
	}

	@Test
	public void testAddCategoryRandomPlace3() {
		LocalDateTime expectedDate = calcNextFriday(new LocalDateTime());
		appLogic.processCommand("add +Birthday remember to get present on Friday");

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get present on Friday", todoStorage.getAll()
				.get(1).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(1).getEndDate());
		assertEquals("+Birthday", todoStorage.getAll().get(1).getCategory());
	}

	/***************************************** Section 5 ***********************************************/
	@Test
	public void testAddPriorityRandomPlace1() {
		LocalDateTime expectedDate = calcNextFriday(new LocalDateTime());
		appLogic.processCommand("add remember to get present on Friday +high");

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get present on Friday", todoStorage.getAll()
				.get(1).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(1).getEndDate());
		assertEquals(ToDo.Priority.HIGH, todoStorage.getAll().get(1).getPriority());
	}

	@Test
	public void testAddPriorityRandomPlace2() {
		LocalDateTime expectedDate = calcNextFriday(new LocalDateTime());
		appLogic.processCommand("add remember to get present +medium on Friday");

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get present on Friday", todoStorage.getAll()
				.get(1).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(1).getEndDate());
		assertEquals(ToDo.Priority.MEDIUM, todoStorage.getAll().get(1).getPriority());
	}

	@Test
	public void testAddPriorityRandomPlace3() {
		LocalDateTime expectedDate = calcNextFriday(new LocalDateTime());
		appLogic.processCommand("add +high remember to get present on Friday");

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get present on Friday", todoStorage.getAll()
				.get(1).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(1).getEndDate());
		assertEquals(ToDo.Priority.HIGH, todoStorage.getAll().get(1).getPriority());
	}
	
	@Test
	public void testAddPriorityRandomPlace4() {
		LocalDateTime expectedDate = calcNextFriday(new LocalDateTime());
		appLogic.processCommand("add remember to get present on Friday +HigH");

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get present on Friday", todoStorage.getAll()
				.get(1).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(1).getEndDate());
		assertEquals(ToDo.Priority.HIGH, todoStorage.getAll().get(1).getPriority());
	}

	@Test
	public void testAddPriorityRandomPlace5() {
		LocalDateTime expectedDate = calcNextFriday(new LocalDateTime());
		appLogic.processCommand("add remember to get present +meDiUm on Friday");

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get present on Friday", todoStorage.getAll()
				.get(1).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(1).getEndDate());
		assertEquals(ToDo.Priority.MEDIUM, todoStorage.getAll().get(1).getPriority());
	}

	@Test
	public void testAddPriorityRandomPlace6() {
		LocalDateTime expectedDate = calcNextFriday(new LocalDateTime());
		appLogic.processCommand("add +hIGh remember to get present on Friday");

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get present on Friday", todoStorage.getAll()
				.get(1).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(1).getEndDate());
		assertEquals(ToDo.Priority.HIGH, todoStorage.getAll().get(1).getPriority());
	}

	/***************************************** Section 6 ***********************************************/
	@Test
	public void testAddPriorityCategoryRandomPlace1() {
		LocalDateTime expectedDate = calcNextFriday(new LocalDateTime());
		appLogic.processCommand("add remember to get present on Friday +Gift +high");

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get present on Friday", todoStorage.getAll()
				.get(1).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(1).getEndDate());
		assertEquals("+Gift", todoStorage.getAll().get(1).getCategory());
		assertEquals(ToDo.Priority.HIGH, todoStorage.getAll().get(1).getPriority());
	}

	@Test
	public void testAddPriorityCategoryRandomPlace2() {
		LocalDateTime expectedDate = calcNextFriday(new LocalDateTime());
		appLogic.processCommand("add remember to get present +Gift +medium on Friday");

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get present on Friday", todoStorage.getAll()
				.get(1).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(1).getEndDate());
		assertEquals("+Gift", todoStorage.getAll().get(1).getCategory());
		assertEquals(ToDo.Priority.MEDIUM, todoStorage.getAll().get(1).getPriority());
	}

	@Test
	public void testAddPriorityCategoryRandomPlace3() {
		LocalDateTime expectedDate = calcNextFriday(new LocalDateTime());
		appLogic.processCommand("add +Gift +high remember to get present on Friday");

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get present on Friday", todoStorage.getAll()
				.get(1).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(1).getEndDate());
		assertEquals("+Gift", todoStorage.getAll().get(1).getCategory());
		assertEquals(ToDo.Priority.HIGH, todoStorage.getAll().get(1).getPriority());
	}

	/***************************************** Section 7 ***********************************************/
	@Test
	public void testAddPriorityCategoryRandomPlace4() {
		LocalDateTime expectedDate = calcNextFriday(new LocalDateTime());
		appLogic.processCommand("add remember to get present +Gift on Friday +high");

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get present on Friday", todoStorage.getAll()
				.get(1).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(1).getEndDate());
		assertEquals("+Gift", todoStorage.getAll().get(1).getCategory());
		assertEquals(ToDo.Priority.HIGH, todoStorage.getAll().get(1).getPriority());
	}

	@Test
	public void testAddPriorityCategoryRandomPlace5() {
		LocalDateTime expectedDate = calcNextFriday(new LocalDateTime());
		appLogic.processCommand("add remember to get present +medium on Friday +Gift");

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get present on Friday", todoStorage.getAll()
				.get(1).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(1).getEndDate());
		assertEquals("+Gift", todoStorage.getAll().get(1).getCategory());
		assertEquals(ToDo.Priority.MEDIUM, todoStorage.getAll().get(1).getPriority());
	}

	@Test
	public void testAddPriorityCategoryRandomPlace6() {
		LocalDateTime expectedDate = calcNextFriday(new LocalDateTime());
		appLogic.processCommand("add +Gift remember to get present on Friday +high");

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get present on Friday", todoStorage.getAll()
				.get(1).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(1).getEndDate());
		assertEquals("+Gift", todoStorage.getAll().get(1).getCategory());
		assertEquals(ToDo.Priority.HIGH, todoStorage.getAll().get(1).getPriority());
	}
	
	/***************************************** Section 8 ***********************************************/
	@Test
	public void testAddPriorityCategoryRandomPlace7() {
		LocalDateTime expectedDate = calcNextFriday(new LocalDateTime());
		appLogic.processCommand("add remember to get present on Friday +Gift+high");

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get present on Friday", todoStorage.getAll()
				.get(1).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(1).getEndDate());
		assertEquals("+Gift", todoStorage.getAll().get(1).getCategory());
		assertEquals(ToDo.Priority.HIGH, todoStorage.getAll().get(1).getPriority());
	}

	@Test
	public void testAddPriorityCategoryRandomPlace8() {
		LocalDateTime expectedDate = calcNextFriday(new LocalDateTime());
		appLogic.processCommand("add remember to get present +medium+Gift on Friday");

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get present on Friday", todoStorage.getAll()
				.get(1).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(1).getEndDate());
		assertEquals("+Gift", todoStorage.getAll().get(1).getCategory());
		assertEquals(ToDo.Priority.MEDIUM, todoStorage.getAll().get(1).getPriority());
	}

	@Test
	public void testAddPriorityCategoryRandomPlace9() {
		LocalDateTime expectedDate = calcNextFriday(new LocalDateTime());
		appLogic.processCommand("add +Gift+high remember to get present on Friday");

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get present on Friday", todoStorage.getAll()
				.get(1).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(1).getEndDate());
		assertEquals("+Gift", todoStorage.getAll().get(1).getCategory());
		assertEquals(ToDo.Priority.HIGH, todoStorage.getAll().get(1).getPriority());
	}
	/****************************************************** End *******************************************************/
	private LocalDateTime calcNextFriday(LocalDateTime d) {
		if (d.getDayOfWeek() >= DateTimeConstants.FRIDAY) {
			d = d.plusWeeks(1);
		}
		return d.withDayOfWeek(DateTimeConstants.FRIDAY).withHourOfDay(23)
				.withMinuteOfHour(59).withSecondOfMinute(0)
				.withMillisOfSecond(0);
	}

}

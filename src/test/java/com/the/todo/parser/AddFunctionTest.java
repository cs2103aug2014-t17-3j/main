package com.the.todo.parser;

import static org.junit.Assert.*;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.the.todo.Logic;
import com.the.todo.io.FileHandler;
import com.the.todo.storage.ToDoStore;

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

	@Test
	public void testAdd() {
		LocalDateTime expectedDate = new LocalDateTime(2014, 11, 11, 23, 59);
		appLogic.processCommand("add remember to get milk on 11/11/2014");
		todoStorage = appLogic.getTodoStorage();

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get milk on 11/11/2014", todoStorage.getAll().get(1).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(1).getEndDate());
	}

	@Test
	public void testAddCategory() {
		LocalDateTime expectedDate = calcNextFriday(new LocalDateTime());
		appLogic.processCommand("add remember to get present on Friday +Birthday");

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get present on Friday", todoStorage.getAll().get(1)
				.getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(1).getEndDate());
		assertEquals("+Birthday", todoStorage.getAll().get(1).getCategory());
	}

	@Test
	public void testAddCategoryRandomPlace1() {
		LocalDateTime expectedDate = calcNextFriday(new LocalDateTime());
		appLogic.processCommand("add remember to get present on Friday +Birthday");

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get present on Friday", todoStorage.getAll().get(1)
				.getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(1).getEndDate());
		assertEquals("+Birthday", todoStorage.getAll().get(1).getCategory());
	}

	@Test
	public void testAddCategoryRandomPlace2() {
		LocalDateTime expectedDate = calcNextFriday(new LocalDateTime());
		appLogic.processCommand("add remember to get present +Birthday on Friday");

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get present on Friday", todoStorage.getAll().get(1)
				.getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(1).getEndDate());
		assertEquals("+Birthday", todoStorage.getAll().get(1).getCategory());
	}

	@Test
	public void testAddCategoryRandomPlace3() {
		LocalDateTime expectedDate = calcNextFriday(new LocalDateTime());
		appLogic.processCommand("add +Birthday remember to get present on Friday");

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get present on Friday", todoStorage.getAll().get(1)
				.getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(1).getEndDate());
		assertEquals("+Birthday", todoStorage.getAll().get(1).getCategory());
	}
	
	private LocalDateTime calcNextFriday(LocalDateTime d) {
		if (d.getDayOfWeek() >= DateTimeConstants.FRIDAY) {
			d = d.plusWeeks(1);
		}
		return d.withDayOfWeek(DateTimeConstants.FRIDAY).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(0).withMillisOfSecond(0);
	}

}
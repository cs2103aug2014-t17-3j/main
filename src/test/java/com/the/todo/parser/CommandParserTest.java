package com.the.todo.parser;

import static org.junit.Assert.*;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.the.todo.storage.InMemoryStore;

public class CommandParserTest {
	
	private CommandParser cp;
	private InMemoryStore ms;
	
	@Before
	public void setUp() throws Exception {
		cp = new CommandParser();
		cp.commandProcess("add CS2013 IVLE quiz");
	}
	
	@After
	public void tearDown() throws Exception {
		cp = null;
		ms = null;
	}

	@Test
	public void testAdd() {
		LocalDate expectedDate = new LocalDate(2014, 11, 11);
		cp.commandProcess("add remember to get milk on 11/11/2014");
		ms = cp.getMemoryStore();
		
		assertEquals(2, ms.count());
		assertEquals("remember to get milk on 11/11/2014", ms.get("2").getTitle());
		assertEquals(expectedDate, ms.get("2").getEndDate());
	}
	
	@Test
	public void testAddCategory() {
		LocalDate expectedDate = calcNextFriday(new LocalDate());
		cp.commandProcess("add remember to get present for jia yeeee next Friday +Birthday");
		ms = cp.getMemoryStore();
		
		assertEquals(2, ms.count());
		assertEquals("remember to get present for jia yeeee next Friday +Birthday", ms.get("2").getTitle());
		assertEquals(expectedDate, ms.get("2").getEndDate());
		assertEquals("+Birthday", ms.get("2").getCategory());
	}
	
	@Test
	public void testRead() {
		ms = cp.getMemoryStore();
		assertEquals(1, ms.count());
	}
	
	@Test
	public void testUpdate() {
		LocalDate expectedDate = calcNextFriday(new LocalDate());
		cp.commandProcess("edit 1 CS2103 IVLE quiz due on Friday");
		ms = cp.getMemoryStore();
		
		assertEquals(1, ms.count());
		assertEquals("CS2103 IVLE quiz due on Friday", ms.get("1").getTitle());
		assertEquals(expectedDate, ms.get("1").getEndDate());
	}
	
	@Test
	public void testUpdateCategory() {
		LocalDate expectedDate = calcNextFriday(new LocalDate());
		cp.commandProcess("edit 1 CS2103 IVLE quiz due on Friday +Homework");
		ms = cp.getMemoryStore();
		
		assertEquals(1, ms.count());
		assertEquals("CS2103 IVLE quiz due on Friday +Homework", ms.get("1").getTitle());
		assertEquals(expectedDate, ms.get("1").getEndDate());
		assertEquals("+Homework", ms.get("1").getCategory());
	}
	
	@Test
	public void testDelete() {
		cp.commandProcess("delete 1");
		ms = cp.getMemoryStore();
		
		assertEquals(1, ms.count());
		assertTrue(ms.get("1").isDeleted());
	}
	
	private LocalDate calcNextFriday(LocalDate d) {
	    if (d.getDayOfWeek() >= DateTimeConstants.FRIDAY) {
	        d = d.plusWeeks(1);
	    }
	    return d.withDayOfWeek(DateTimeConstants.FRIDAY);
	}

}

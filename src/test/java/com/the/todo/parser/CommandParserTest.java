/**
 * This file is part of TheTODO, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2014 TheTODO
 * Copyright (c) Poh Wei Cheng <calvinpohwc@gmail.com>
 *				 Chen Kai Hsiang <kaihsiang95@gmail.com>
 *				 Khin Wathan Aye <y.caiyun@gmail.com>
 *				 Neo Eng Tai <neoengtai@gamil.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.the.todo.parser;

import static org.junit.Assert.*;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.the.todo.command.ToDoAdd;
import com.the.todo.storage.InMemoryStore;

public class CommandParserTest {

	private InMemoryStore ms;

	@Before
	public void setUp() throws Exception {
		ms = new InMemoryStore();
		CommandParser.processCommand(ms, "add CS2013 IVLE quiz");
	}

	@After
	public void tearDown() throws Exception {
		ms = null;
	}

	@Test
	public void testAdd() {
		LocalDate expectedDate = new LocalDate(2014, 11, 11);
		CommandParser.processCommand(ms,
				"add remember to get milk on 11/11/2014");

		assertEquals(2, ms.count());
		assertEquals("remember to get milk on 11/11/2014", ms.get("2")
				.getTitle());
		assertEquals(expectedDate, ms.get("2").getEndDate());
	}

	@Test
	public void testAddCategory() {
		LocalDate expectedDate = calcNextFriday(new LocalDate());
		CommandParser.processCommand(ms,
				"add remember to get present on Friday +Birthday");

		assertEquals(2, ms.count());
		assertEquals("remember to get present on Friday +Birthday", ms.get("2")
				.getTitle());
		assertEquals(expectedDate, ms.get("2").getEndDate());
		assertEquals("+Birthday", ms.get("2").getCategory());
	}

	@Test
	public void testAddCategoryRandomPlace1() {
		LocalDate expectedDate = calcNextFriday(new LocalDate());
		CommandParser.processCommand(ms,
				"add remember to get present on Friday +Birthday");

		assertEquals(2, ms.count());
		assertEquals("remember to get present on Friday +Birthday", ms.get("2")
				.getTitle());
		assertEquals(expectedDate, ms.get("2").getEndDate());
		assertEquals("+Birthday", ms.get("2").getCategory());
	}

	@Test
	public void testAddCategoryRandomPlace2() {
		LocalDate expectedDate = calcNextFriday(new LocalDate());
		CommandParser.processCommand(ms,
				"add remember to get present +Birthday on Friday");

		assertEquals(2, ms.count());
		assertEquals("remember to get present +Birthday on Friday", ms.get("2")
				.getTitle());
		assertEquals(expectedDate, ms.get("2").getEndDate());
		assertEquals("+Birthday", ms.get("2").getCategory());
	}

	@Test
	public void testAddCategoryRandomPlace3() {
		LocalDate expectedDate = calcNextFriday(new LocalDate());
		CommandParser.processCommand(ms,
				"add +Birthday remember to get present on Friday");

		assertEquals(2, ms.count());
		assertEquals("+Birthday remember to get present on Friday", ms.get("2")
				.getTitle());
		assertEquals(expectedDate, ms.get("2").getEndDate());
		assertEquals("+Birthday", ms.get("2").getCategory());
	}

	@Test
	public void testRead() {
		assertEquals(1, ms.count());
	}

	@Test
	public void testUpdate() {
		LocalDate expectedDate = calcNextFriday(new LocalDate());
		CommandParser.processCommand(ms,
				"edit 1 CS2103 IVLE quiz due on Friday");

		assertEquals(1, ms.count());
		assertEquals("CS2103 IVLE quiz due on Friday", ms.get("1").getTitle());
		assertEquals(expectedDate, ms.get("1").getEndDate());
	}

	@Test
	public void testUpdateCategory() {
		LocalDate expectedDate = calcNextFriday(new LocalDate());
		CommandParser.processCommand(ms,
				"edit 1 CS2103 IVLE quiz due on Friday +Homework");

		assertEquals(1, ms.count());
		assertEquals("CS2103 IVLE quiz due on Friday +Homework", ms.get("1")
				.getTitle());
		assertEquals(expectedDate, ms.get("1").getEndDate());
		assertEquals("+Homework", ms.get("1").getCategory());
	}

	@Test
	public void testUpdateCategoryRandomPlace1() {
		LocalDate expectedDate = calcNextFriday(new LocalDate());
		CommandParser.processCommand(ms,
				"edit 1 CS2103 IVLE quiz due on Friday +Homework");

		assertEquals(1, ms.count());
		assertEquals("CS2103 IVLE quiz due on Friday +Homework", ms.get("1")
				.getTitle());
		assertEquals(expectedDate, ms.get("1").getEndDate());
		assertEquals("+Homework", ms.get("1").getCategory());
	}

	@Test
	public void testUpdateCategoryRandomPlace2() {
		LocalDate expectedDate = calcNextFriday(new LocalDate());
		CommandParser.processCommand(ms,
				"edit 1 +Homework CS2103 IVLE quiz due on Friday");

		assertEquals(1, ms.count());
		assertEquals("+Homework CS2103 IVLE quiz due on Friday", ms.get("1")
				.getTitle());
		assertEquals(expectedDate, ms.get("1").getEndDate());
		assertEquals("+Homework", ms.get("1").getCategory());
	}

	@Test
	public void testUpdateCategoryRandomPlace3() {
		LocalDate expectedDate = calcNextFriday(new LocalDate());
		CommandParser.processCommand(ms,
				"edit 1 CS2103 IVLE quiz +Homework due on Friday");

		assertEquals(1, ms.count());
		assertEquals("CS2103 IVLE quiz +Homework due on Friday", ms.get("1")
				.getTitle());
		assertEquals(expectedDate, ms.get("1").getEndDate());
		assertEquals("+Homework", ms.get("1").getCategory());
	}

	@Test
	public void testDelete() {
		CommandParser.processCommand(ms, "delete 1");

		assertEquals(1, ms.count());
		assertTrue(ms.get("1").isDeleted());
	}
	
	@Test
	public void testComplete() {
		CommandParser.processCommand(ms, "complete 1");

		assertEquals(1, ms.count());
		assertTrue(ms.get("1").isCompleted());
	}

	private LocalDate calcNextFriday(LocalDate d) {
		if (d.getDayOfWeek() >= DateTimeConstants.FRIDAY) {
			d = d.plusWeeks(1);
		}
		return d.withDayOfWeek(DateTimeConstants.FRIDAY);
	}

}

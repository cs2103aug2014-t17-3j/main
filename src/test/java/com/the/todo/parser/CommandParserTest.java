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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.the.todo.Logic;
import com.the.todo.storage.ToDoStore;

public class CommandParserTest {

	private Logic appLogic;
	private ToDoStore todoStorage;

	@Before
	public void setUp() throws Exception {
		appLogic = new Logic();
		appLogic.processCommand("add CS2013 IVLE quiz");
		todoStorage = appLogic.getTodoStorage();
	}

	@After
	public void tearDown() throws Exception {
		appLogic = null;
		todoStorage = null;
	}

	@Test
	public void testAdd() {
		LocalDate expectedDate = new LocalDate(2014, 11, 11);
		appLogic.processCommand("add remember to get milk on 11/11/2014");
		todoStorage = appLogic.getTodoStorage();

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get milk on 11/11/2014", todoStorage.get("2").getTitle());
		assertEquals(expectedDate, todoStorage.get("2").getEndDate());
	}

	@Test
	public void testAddCategory() {
		LocalDate expectedDate = calcNextFriday(new LocalDate());
		appLogic.processCommand("add remember to get present on Friday +Birthday");

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get present on Friday", todoStorage.get("2")
				.getTitle());
		assertEquals(expectedDate, todoStorage.get("2").getEndDate());
		assertEquals("+Birthday", todoStorage.get("2").getCategory());
	}

	@Test
	public void testAddCategoryRandomPlace1() {
		LocalDate expectedDate = calcNextFriday(new LocalDate());
		appLogic.processCommand("add remember to get present on Friday +Birthday");

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get present on Friday", todoStorage.get("2")
				.getTitle());
		assertEquals(expectedDate, todoStorage.get("2").getEndDate());
		assertEquals("+Birthday", todoStorage.get("2").getCategory());
	}

	@Test
	public void testAddCategoryRandomPlace2() {
		LocalDate expectedDate = calcNextFriday(new LocalDate());
		appLogic.processCommand("add remember to get present +Birthday on Friday");

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get present on Friday", todoStorage.get("2")
				.getTitle());
		assertEquals(expectedDate, todoStorage.get("2").getEndDate());
		assertEquals("+Birthday", todoStorage.get("2").getCategory());
	}

	@Test
	public void testAddCategoryRandomPlace3() {
		LocalDate expectedDate = calcNextFriday(new LocalDate());
		appLogic.processCommand("add +Birthday remember to get present on Friday");

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get present on Friday", todoStorage.get("2")
				.getTitle());
		assertEquals(expectedDate, todoStorage.get("2").getEndDate());
		assertEquals("+Birthday", todoStorage.get("2").getCategory());
	}

	@Test
	public void testRead() {
		assertEquals(1, todoStorage.count());
	}

	@Test
	public void testUpdate() {
		LocalDate expectedDate = calcNextFriday(new LocalDate());
		appLogic.processCommand("edit 1 CS2103 IVLE quiz due on Friday");

		assertEquals(1, todoStorage.count());
		assertEquals("CS2103 IVLE quiz due on Friday", todoStorage.get("1").getTitle());
		assertEquals(expectedDate, todoStorage.get("1").getEndDate());
	}

	@Test
	public void testUpdateCategory() {
		LocalDate expectedDate = calcNextFriday(new LocalDate());
		appLogic.processCommand("edit 1 CS2103 IVLE quiz due on Friday +Homework");

		assertEquals(1, todoStorage.count());
		assertEquals("CS2103 IVLE quiz due on Friday", todoStorage.get("1")
				.getTitle());
		assertEquals(expectedDate, todoStorage.get("1").getEndDate());
		assertEquals("+Homework", todoStorage.get("1").getCategory());
	}

	@Test
	public void testUpdateCategoryRandomPlace1() {
		LocalDate expectedDate = calcNextFriday(new LocalDate());
		appLogic.processCommand("edit 1 CS2103 IVLE quiz due on Friday +Homework");

		assertEquals(1, todoStorage.count());
		assertEquals("CS2103 IVLE quiz due on Friday", todoStorage.get("1")
				.getTitle());
		assertEquals(expectedDate, todoStorage.get("1").getEndDate());
		assertEquals("+Homework", todoStorage.get("1").getCategory());
	}

	@Test
	public void testUpdateCategoryRandomPlace2() {
		LocalDate expectedDate = calcNextFriday(new LocalDate());
		appLogic.processCommand("edit 1 +Homework CS2103 IVLE quiz due on Friday");

		assertEquals(1, todoStorage.count());
		assertEquals("CS2103 IVLE quiz due on Friday", todoStorage.get("1")
				.getTitle());
		assertEquals(expectedDate, todoStorage.get("1").getEndDate());
		assertEquals("+Homework", todoStorage.get("1").getCategory());
	}

	@Test
	public void testUpdateCategoryRandomPlace3() {
		LocalDate expectedDate = calcNextFriday(new LocalDate());
		appLogic.processCommand("edit 1 CS2103 IVLE quiz +Homework due on Friday");

		assertEquals(1, todoStorage.count());
		assertEquals("CS2103 IVLE quiz due on Friday", todoStorage.get("1")
				.getTitle());
		assertEquals(expectedDate, todoStorage.get("1").getEndDate());
		assertEquals("+Homework", todoStorage.get("1").getCategory());
	}

	@Test
	public void testDelete() {
		appLogic.processCommand("delete 1");

		assertEquals(1, todoStorage.count());
		assertTrue(todoStorage.get("1").isDeleted());
	}
	
	@Test
	public void testComplete() {
		appLogic.processCommand("complete 1");

		assertEquals(1, todoStorage.count());
		assertTrue(todoStorage.get("1").isCompleted());
	}

	private LocalDate calcNextFriday(LocalDate d) {
		if (d.getDayOfWeek() >= DateTimeConstants.FRIDAY) {
			d = d.plusWeeks(1);
		}
		return d.withDayOfWeek(DateTimeConstants.FRIDAY);
	}

}

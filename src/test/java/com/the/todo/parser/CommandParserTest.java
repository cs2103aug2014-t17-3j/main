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
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.the.todo.Logic;
import com.the.todo.io.FileHandler;
import com.the.todo.storage.ToDoStore;

public class CommandParserTest {

	private Logic appLogic;
	private ToDoStore todoStorage;
	
	private static final int YEAR = LocalDateTime.now().getYear();
	private static final int MONTH = LocalDateTime.now().getMonthOfYear();
	private static final int DAY = LocalDateTime.now().getDayOfMonth();
	private static final int HOUR = 0;
	private static final int MIN = 0;
	
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
		LocalDateTime expectedDate = new LocalDateTime(2014, 11, 11, 0, 0);
		appLogic.processCommand("add remember to get milk on 11/11/2014");
		todoStorage = appLogic.getTodoStorage();

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get milk on 11/11/2014", todoStorage.getAll().get(1).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(1).getEndDate());
	}

	@Test
	public void testAddCategory() {
		LocalDateTime expectedDate = calcNextFriday(new LocalDateTime(YEAR, MONTH, DAY, HOUR, MIN));
		appLogic.processCommand("add remember to get present on Friday +Birthday");

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get present on Friday", todoStorage.getAll().get(1)
				.getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(1).getEndDate());
		assertEquals("+Birthday", todoStorage.getAll().get(1).getCategory());
	}

	@Test
	public void testAddCategoryRandomPlace1() {
		LocalDateTime expectedDate = calcNextFriday(new LocalDateTime(YEAR, MONTH, DAY, HOUR, MIN));
		appLogic.processCommand("add remember to get present on Friday +Birthday");

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get present on Friday", todoStorage.getAll().get(1)
				.getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(1).getEndDate());
		assertEquals("+Birthday", todoStorage.getAll().get(1).getCategory());
	}

	@Test
	public void testAddCategoryRandomPlace2() {
		LocalDateTime expectedDate = calcNextFriday(new LocalDateTime(YEAR, MONTH, DAY, HOUR, MIN));
		appLogic.processCommand("add remember to get present +Birthday on Friday");

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get present on Friday", todoStorage.getAll().get(1)
				.getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(1).getEndDate());
		assertEquals("+Birthday", todoStorage.getAll().get(1).getCategory());
	}

	@Test
	public void testAddCategoryRandomPlace3() {
		LocalDateTime expectedDate = calcNextFriday(new LocalDateTime(YEAR, MONTH, DAY, HOUR, MIN));
		appLogic.processCommand("add +Birthday remember to get present on Friday");

		assertEquals(2, todoStorage.count());
		assertEquals("remember to get present on Friday", todoStorage.getAll().get(1)
				.getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(1).getEndDate());
		assertEquals("+Birthday", todoStorage.getAll().get(1).getCategory());
	}

	@Test
	public void testRead() {
		assertEquals(1, todoStorage.count());
	}

	@Test
	public void testUpdate() {
		LocalDateTime expectedDate = calcNextFriday(new LocalDateTime(YEAR, MONTH, DAY, HOUR, MIN));
		appLogic.processCommand("edit 1 CS2103 IVLE quiz due on Friday");

		assertEquals(1, todoStorage.count());
		assertEquals("CS2103 IVLE quiz due on Friday", todoStorage.getAll().get(0).getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getEndDate());
	}

	@Test
	public void testUpdateCategory() {
		LocalDateTime expectedDate = calcNextFriday(new LocalDateTime(YEAR, MONTH, DAY, HOUR, MIN));
		appLogic.processCommand("edit 1 CS2103 IVLE quiz due on Friday +Homework");

		assertEquals(1, todoStorage.count());
		assertEquals("CS2103 IVLE quiz due on Friday", todoStorage.getAll().get(0)
				.getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getEndDate());
		assertEquals("+Homework", todoStorage.getAll().get(0).getCategory());
	}

	@Test
	public void testUpdateCategoryRandomPlace1() {
		LocalDateTime expectedDate = calcNextFriday(new LocalDateTime(YEAR, MONTH, DAY, HOUR, MIN));
		appLogic.processCommand("edit 1 CS2103 IVLE quiz due on Friday +Homework");

		assertEquals(1, todoStorage.count());
		assertEquals("CS2103 IVLE quiz due on Friday", todoStorage.getAll().get(0)
				.getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getEndDate());
		assertEquals("+Homework", todoStorage.getAll().get(0).getCategory());
	}

	@Test
	public void testUpdateCategoryRandomPlace2() {
		LocalDateTime expectedDate = calcNextFriday(new LocalDateTime(YEAR, MONTH, DAY, HOUR, MIN));
		appLogic.processCommand("edit 1 +Homework CS2103 IVLE quiz due on Friday");

		assertEquals(1, todoStorage.count());
		assertEquals("CS2103 IVLE quiz due on Friday", todoStorage.getAll().get(0)
				.getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getEndDate());
		assertEquals("+Homework", todoStorage.getAll().get(0).getCategory());
	}

	@Test
	public void testUpdateCategoryRandomPlace3() {
		LocalDateTime expectedDate = calcNextFriday(new LocalDateTime(YEAR, MONTH, DAY, HOUR, MIN));
		appLogic.processCommand("edit 1 CS2103 IVLE quiz +Homework due on Friday");

		assertEquals(1, todoStorage.count());
		assertEquals("CS2103 IVLE quiz due on Friday", todoStorage.getAll().get(0)
				.getTitle());
		assertEquals(expectedDate, todoStorage.getAll().get(0).getEndDate());
		assertEquals("+Homework", todoStorage.getAll().get(0).getCategory());
	}

	@Test
	public void testDelete() {
		appLogic.processCommand("delete 1");

		assertEquals(0, todoStorage.count());
	}
	
	@Test
	public void testComplete() {
		appLogic.processCommand("complete 1");

		assertEquals(1, todoStorage.count());
		assertTrue(todoStorage.getAll().get(0).isCompleted());
	}

	private LocalDateTime calcNextFriday(LocalDateTime d) {
		if (d.getDayOfWeek() >= DateTimeConstants.FRIDAY) {
			d = d.plusWeeks(1);
		}
		return d.withDayOfWeek(DateTimeConstants.FRIDAY);
	}

}

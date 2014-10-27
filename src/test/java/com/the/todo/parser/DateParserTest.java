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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.joestelmach.natty.DateGroup;

public class DateParserTest {

	List<DateGroup> groups = null;
	
	@Test
	public void parseTest1() throws Exception {
		String input = "study for exams";
		List<DateGroup> groups = DateParser.parse(input);
		for (DateGroup group : groups) {
			System.out.println(group.getDates());
		}
		assertEquals(0, groups.size());
	}

	@Test
	public void parseTest2() throws Exception {
		String input = "study for exams on 25/02/14";

		List<DateGroup> groups = DateParser.parse(input);
		for (DateGroup group : groups) {
			System.out.println(group.getDates());
		}
		assertEquals(1, groups.get(0).getDates().size());
	}
	
	@Test
	public void parseTest3() throws Exception {
		String input = "study for exams on Friday 2pm";

		List<DateGroup> groups = DateParser.parse(input);
		for (DateGroup group : groups) {
			System.out.println(group.getDates());
		}
		assertEquals(1, groups.get(0).getDates().size());
	}
	
	@Test
	public void parseTest4() throws Exception {
		String input = "study for exams on today 2pm to tomorrow 6pm";

		List<DateGroup> groups = DateParser.parse(input);
		for (DateGroup group : groups) {
			System.out.println(group.getDates());
		}
		assertEquals(2, groups.get(0).getDates().size());
	}	

	@Test
	public void checkValidDatesTest1() {
		String input = "study for exams on 29/02/2016";
		assertTrue(DateParser.checkValidDates(input));
	}

	@Test
	public void checkValidDatesTest2() {
		String input = "study for exams on 29/02/2014";
		assertFalse(DateParser.checkValidDates(input));
	}

	@Test
	public void checkValidDatesTest3() {
		String input = "study for exams on 29-01-14";
		assertTrue(DateParser.checkValidDates(input));
	}

	@Test
	public void changeDateStringsFormatTest1() {
		String expected = "study for exams from 2014/08/04 to 2014/08/30";
		String actual = DateParser
				.changeDateStringsFormat("study for exams from 04/08/2014 to 30/08/2014");

		assertEquals(expected, actual);
	}

	@Test
	public void changeDateStringsFormatTest2() {
		String expected = "study for exams from 2014/08/04 to 2014/02/28";
		String actual = DateParser
				.changeDateStringsFormat("study for exams from 2014/08/04 to 28/2/2014");

		assertEquals(expected, actual);
	}

	@Test
	public void changeDateStringsFormatTest3() {
		String expected = "study for exams from 29/02/14 to 2015/02/28";
		String actual = DateParser
				.changeDateStringsFormat("study for exams from 29/02/14 to 28/2/2015");

		assertEquals(expected, actual);
	}

}
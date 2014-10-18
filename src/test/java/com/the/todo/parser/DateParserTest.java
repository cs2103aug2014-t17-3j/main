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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.ocpsoft.prettytime.nlp.parse.DateGroup;

public class DateParserTest {

	private LocalDateTime date = null;
	List<DateGroup> groups = null;
	private LocalDateTime expectedDate = null;

	@Test
	public void dateTest() {
		assertNull(DateParser.parseDate(""));

		groups = DateParser.parseDate("study on 29/9/2014");
		date = new LocalDateTime(groups.get(0).getDates().get(0));
		expectedDate = new LocalDateTime(2014, 9, 29, 0, 0);
		assertEquals(expectedDate, date);

		groups = DateParser.parseDate("study on 2016/2/15");
		date = new LocalDateTime(groups.get(0).getDates().get(0));
		expectedDate = new LocalDateTime(2016, 2, 15, 0, 0);
		assertEquals(expectedDate, date);

		groups = DateParser.parseDate("study on 11/28/2014");
		date = new LocalDateTime(groups.get(0).getDates().get(0));
		expectedDate = new LocalDateTime(2014, 11, 28, 0, 0);
		assertEquals(expectedDate, date);

		groups = DateParser.parseDate("study on Christmas");
		date = new LocalDateTime(groups.get(0).getDates().get(0));
		expectedDate = new LocalDateTime(2014, 12, 25, 0, 0);
		assertEquals(expectedDate, date);

		groups = DateParser.parseDate("easter study");
		date = new LocalDateTime(groups.get(0).getDates().get(0));
		expectedDate = new LocalDateTime(2015, 4, 5, 0, 0);
		assertEquals(expectedDate, date);
	}
	
	@Test
	public void changeDateStringsFormatTest() {
		String expected = "study for exams from 2014/08/04 to 2014/08/30";
		String actual = DateParser.changeDateStringsFormat("study for exams from 04/08/2014 to 30/08/2014");
		
		assertEquals(expected, actual);
	}
	
//	@Test
//	public void validTest(){
//		date = DateParser.parseDate("study on 29/2/2015").toLocalDate();
//		assertFalse(DateParser.getIsValid());
//		
//		date = DateParser.parseDate("study on 1/5/2015").toLocalDate();
//		assertTrue(DateParser.getIsValid());
//	}

}
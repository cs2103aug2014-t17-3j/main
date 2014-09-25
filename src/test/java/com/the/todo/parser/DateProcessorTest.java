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

import org.joda.time.LocalDate;
import org.junit.Test;

public class DateProcessorTest {

	private LocalDate date = null;
	private LocalDate expectedDate = null;
	private DateProcessor processingString = new DateProcessor();

	@Test
	public void dateTest() {
		date = processingString.stringProcess("");
		assertNull(date);

		date = processingString.stringProcess("study on 29/9/2014");
		expectedDate = new LocalDate(2014, 9, 29);
		assertEquals(expectedDate, date);

		date = processingString.stringProcess("study on 2016/2/15");
		expectedDate = new LocalDate(2016, 2, 15);
		assertEquals(expectedDate, date);

		date = processingString.stringProcess("study on 11/28/2014");
		expectedDate = new LocalDate(2014, 11, 28);
		assertEquals(expectedDate, date);

		date = processingString.stringProcess("study on Christmas");
		expectedDate = new LocalDate(2014, 12, 25);
		assertEquals(expectedDate, date);

		date = processingString.stringProcess("easter study");
		expectedDate = new LocalDate(2015, 4, 5);
		assertEquals(expectedDate, date);

		// date = processingString.stringProcess("study on labor day");
		// expectedDate = new LocalDate(2015, 5, 1);
		// assertEquals(expectedDate, date);

		// date = processingString.stringProcess("study on 29/2/2015");
		// expectedDate = new LocalDate(2015, 2, 29);
		// assertEquals(expectedDate, date);
	}

}
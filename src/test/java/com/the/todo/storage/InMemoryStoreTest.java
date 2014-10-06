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

package com.the.todo.storage;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.the.todo.model.ToDo;

public class InMemoryStoreTest {

	ToDo todo1;
	ToDo todo2;
	private ToDoStore emptyStore;
	private ToDoStore populatedStore;
	
	@Before
	public void setUp() throws Exception {
		todo1 = new ToDo("remember the milk");
		todo2 = new ToDo("do IVLE quiz!");
		todo2.setCompleted(true);
		emptyStore = new InMemoryStore();
		populatedStore = new InMemoryStore();
		todo1 = populatedStore.save(todo1);
		todo2 = populatedStore.save(todo2);
	}
	
	@After
	public void tearDown() throws Exception {
		emptyStore = null;
		populatedStore = null;
	}

	@Test
	public void testGetAll() {
		assertTrue(emptyStore.getAll().isEmpty());
		Collection<ToDo> todos = populatedStore.getAll();
		assertTrue(todos.contains(todo1));
		assertTrue(todos.contains(todo2));
	}

	@Test
	public void testGet() {
		assertEquals(todo1, populatedStore.get(todo1.getId()));
		assertEquals(todo2, populatedStore.get(todo2.getId()));
	}

	@Test
	public void testSave() {
		ToDo todo = new ToDo("Simple test");
		todo = emptyStore.save(todo);
		assertEquals(todo, emptyStore.get(todo.getId()));
	}

	@Test
	public void testUpdate() {
		ToDo todo;
		todo = populatedStore.get(todo1.getId());
		todo.setCompleted(true);
		populatedStore.update(todo.getId(), todo);
		todo = populatedStore.get(todo.getId());
		assertEquals("remember the milk", todo.getTitle());
		assertTrue(todo.isCompleted());
	}

	@Test
	public void testDelete() {
		populatedStore.delete(todo1.getId());
		assertTrue(populatedStore.get(todo1.getId()).isDeleted());
	}

	@Test
	public void testCount() {
		assertEquals(0, emptyStore.count());
		assertEquals(2, populatedStore.count());
	}

}

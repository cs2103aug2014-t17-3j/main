package com.the.todo.storage;

import java.util.Collection;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.the.todo.model.ToDo;

public class InMemoryStoreTest extends TestCase {

	ToDo todo1;
	ToDo todo2;
	private ToDoStore emptyStore;
	private ToDoStore populatedStore;

	@Before
	public void setUp() throws Exception {
		todo1 = new ToDo();
		todo1.setTitle("remember the milk");
		todo2 = new ToDo();
		todo2.setTitle("do IVLE quiz!");
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
		assertEquals(todo1, populatedStore.get(0));
		assertEquals(todo2, populatedStore.get(1));
	}

	@Test
	public void testSave() {
		ToDo todo = new ToDo();
		todo.setTitle("testing");
		todo = emptyStore.save(todo);
		assertEquals(todo, emptyStore.get(0));
	}

	@Test
	public void testUpdate() {
		ToDo todo;
		todo = populatedStore.get(0);
		todo.setCompleted(true);
		populatedStore.update(0, todo);
		todo = populatedStore.get(0);
		assertEquals("remember the milk", todo.getTitle());
		assertTrue(todo.isCompleted());
	}

	@Test
	public void testDelete() {
		populatedStore.delete(0);
		assertEquals(1, populatedStore.count());
	}

	@Test
	public void testCount() {
		assertEquals(0, emptyStore.count());
		assertEquals(2, populatedStore.count());
	}

}

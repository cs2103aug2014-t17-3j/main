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

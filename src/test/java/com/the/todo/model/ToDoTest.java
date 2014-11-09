package com.the.todo.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.joda.time.LocalDateTime;
import org.junit.Test;

//@author A0111815R

public class ToDoTest {
	
	private ToDo todo;

	@Test
	public void testFloatingTask() {
		todo = new ToDo("Test Floating Task!");
		
		assertTrue(todo.isFloatingToDo());
		assertEquals("Test Floating Task!", todo.getTitle());
	}
	
	@Test
	public void testDeadlineTask() {
		todo = new ToDo("Test Deadline Task!", new LocalDateTime());
		
		assertTrue(todo.isDeadlineToDo());
		assertEquals("Test Deadline Task!", todo.getTitle());
	}
	
	@Test
	public void testTimedTask() {
		todo = new ToDo("Test Timed Task!", new LocalDateTime(), new LocalDateTime().plusDays(3));
		
		assertTrue(todo.isTimedToDo());
		assertEquals("Test Timed Task!", todo.getTitle());
	}


}
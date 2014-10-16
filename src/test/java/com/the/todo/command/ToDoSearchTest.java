package com.the.todo.command;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;

import com.the.todo.model.ToDo;
import com.the.todo.storage.InMemoryStore;

/*
 * THIS IS NOT A STRICT UNIT TEST
 * This test uses InMemoryStore as a storage stub for testing ToDoSearch
 */

public class ToDoSearchTest {

	private ToDo todo1 = new ToDo("Lorem");
	private ToDo todo2 = new ToDo("ipsum dolor sit amet");

	InMemoryStore storeStub;
	ByteArrayOutputStream output;

	@Before
	public void setUp() {
		storeStub = new InMemoryStore();
		storeStub.save(todo1);
		storeStub.save(todo2);

		output = new ByteArrayOutputStream();
		System.setOut(new PrintStream(output));
	}

	@Test
	public void test() {
		searchTest("rem", "Lorem\r\n");
		searchTest("test", "");
		searchTest("sum dol", "ipsum dolor sit amet\r\n");
		searchTest("or", "Lorem\r\n"
				+ "ipsum dolor sit amet\r\n");
	}

	public void searchTest(String query, String expected) {
		ToDoSearch test = new ToDoSearch(storeStub, query);
		test.execute();
		assertEquals(expected, output.toString());
		output.reset();
	}
}

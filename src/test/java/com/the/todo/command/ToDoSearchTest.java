package com.the.todo.command;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

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
		searchTest("or", "");
		searchTest("Lorem", "Lorem\r\n");
		searchTest("amet", "ipsum dolor sit amet\r\n");
		searchTest("dolor sit", "ipsum dolor sit amet\r\n");
	}

	public void searchTest(String query, String expected) {
		ArrayList<ToDo> updateListStub = new ArrayList<ToDo>();

		ToDoSearch test = new ToDoSearch(storeStub, query, updateListStub);
		test.execute();
		assertEquals(expected, output.toString());
		output.reset();
	}
}

package com.the.todo.command;

import static org.junit.Assert.*;

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
	private ToDo todo2 = new ToDo("ipsum dolor sit");
	private ToDo todo3 = new ToDo("dolor sit amet");

	ArrayList<ToDo> updateListStub;
	InMemoryStore storeStub;

	@Before
	public void setUp() {
		storeStub = new InMemoryStore();
		storeStub.save(todo1);
		storeStub.save(todo2);
		storeStub.save(todo3);

		updateListStub = new ArrayList<ToDo>();
	}

	@Test
	public void test() {
		searchTest("or");
		searchTest("Lorem", "Lorem");
		searchTest("amet", "dolor sit amet");
		searchTest("dolor sit", "ipsum dolor sit", "dolor sit amet");
	}

	public void searchTest(String query, String... expected) {
		ArrayList<String> results = new ArrayList<String>();

		ToDoSearch test = new ToDoSearch(storeStub, query, updateListStub);
		test.execute();

		for (ToDo todo : updateListStub) {
			results.add(todo.getTitle());
		}

		assertArrayEquals(expected, results.toArray());
	}
}

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
	private ToDo todo4 = new ToDo("amet consectetur adipiscing elit.");

	ArrayList<ToDo> updateListStub;
	InMemoryStore storeStub;

	@Before
	public void setUp() {
		todo1.setCategory("+search");
		todo2.setCategory("+add");
		todo3.setCategory("+test");
		todo4.setCategory("+test");

		storeStub = new InMemoryStore();
		storeStub.save(todo1);
		storeStub.save(todo2);
		storeStub.save(todo3);
		storeStub.save(todo4);

		updateListStub = new ArrayList<ToDo>();
	}

	@Test
	public void test() {
		/*This is a boundary case for empty input.*/
		searchTest("");
		
		/*Search that returns no results*/
		searchTest("or");
		searchTest("+");
		searchTest("or +search");
		
		/*Search for values at the start/end*/
		searchTest("Lorem ", "Lorem");
		searchTest("amet", "dolor sit amet", "amet consectetur adipiscing elit.");
		searchTest("elit.", "amet consectetur adipiscing elit.");
		
		/*Search using multiple keywords*/
		searchTest("dolor sit", "ipsum dolor sit", "dolor sit amet");

		/*Search with only category specified*/
		searchTest("+search", "Lorem");
		searchTest("+test", "dolor sit amet",
				"amet consectetur adipiscing elit.");

		/*Search with both keywords and category specified*/
		searchTest("dolor sit +add", "ipsum dolor sit");
		searchTest("amet +test", "dolor sit amet",
				"amet consectetur adipiscing elit.");

	}

	public void searchTest(String query, String... expected) {
		ArrayList<String> results = new ArrayList<String>();

		ToDoSearch test = new ToDoSearch(storeStub, updateListStub, query);
		test.execute();

		for (ToDo todo : updateListStub) {
			results.add(todo.getTitle());
		}

		assertArrayEquals(expected, results.toArray());
	}
}

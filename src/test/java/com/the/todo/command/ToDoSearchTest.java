package com.the.todo.command;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.the.todo.model.ToDo;
import com.the.todo.model.ToDo.Priority;
import com.the.todo.storage.InMemoryStore;

/**
 * THIS IS NOT A STRICT UNIT TEST
 * This test uses InMemoryStore as a storage stub for testing ToDoSearch
 */

//@author A0119764W

public class ToDoSearchTest {

	private ToDo todo0, todo1, todo2, todo3, todo4, todo5;
	ArrayList<ToDo> updateListStub;
	InMemoryStore storeStub;

	@Before
	public void setUp() {
		todo0 = new ToDo("Lorem");
		
		todo1 = new ToDo("Lorem");
		todo1.setCategory("+search");
		
		todo2 = new ToDo("ipsum dolor sit");
		todo2.setPriority(Priority.MEDIUM);
		
		todo3 = new ToDo("ipsum dolor sit");
		todo3.setCategory("+add");
		todo3.setPriority(Priority.LOW);
		
		todo4 = new ToDo("dolor sit amet");
		todo4.setCategory("+test");
		todo4.setPriority(Priority.MEDIUM);
		
		todo5 = new ToDo("amet consectetur adipiscing elit.");
		todo5.setCategory("+test");
		todo5.setPriority(Priority.HIGH);

		storeStub = new InMemoryStore();
		storeStub.save(todo0);
		storeStub.save(todo1);
		storeStub.save(todo2);
		storeStub.save(todo3);
		storeStub.save(todo4);
		storeStub.save(todo5);

		updateListStub = new ArrayList<ToDo>();
	}

	@Test
	public void test() {
		/*This is a boundary case for empty/null input.*/
		searchTest("");
		searchTest(null);
		
		/*Search that returns no results*/
		searchTest("or");
		searchTest("+");
		searchTest("Or +search");
		
		/*Search for values at the start/end*/
		searchTest(" lorem   ", todo0, todo1);
		searchTest("amet", todo4, todo5);
		searchTest("elit.", todo5);
		
		/*Search using multiple keywords*/
		searchTest("  doLor  sit", todo2, todo3, todo4);

		/*Search with only category specified*/
		searchTest("+SeaRch", todo1);
		searchTest("+test", todo4, todo5);
		
		/*Search with only priority specified*/
		searchTest("+meDiUm", todo2, todo4);
		
		/*Search with both keywords and category specified*/
		searchTest("dolor  sit   +add", todo3);
		searchTest("amet +test", todo4, todo5);
		
		/*Search with keywords and priority specified*/
		searchTest("dolor sit +low", todo3);
		
		/*Search with category and priority specified*/
		searchTest("+high +test", todo5);
		
		/*This is another boundary case for empty/null input.*/
		/*Updatelist should not be changed (i.e. should be same as previous test)*/
		searchTest("", todo5);
		searchTest(null, todo5);
	}

	public void searchTest(String query, ToDo... expected) {
		ToDoSearch test = new ToDoSearch(storeStub, updateListStub, query);
		test.execute();

		assertArrayEquals(expected, updateListStub.toArray());
	}
}

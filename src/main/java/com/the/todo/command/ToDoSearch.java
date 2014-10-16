package com.the.todo.command;

import java.util.ArrayList;

import com.the.todo.command.CommandStatus.Status;
import com.the.todo.model.ToDo;
import com.the.todo.storage.ToDoStore;

public class ToDoSearch extends ToDoCommand {
	ToDoStore todoStorage;
	String query;

	public ToDoSearch(ToDoStore todoStorage, String query) {
		super();
		this.undoable = false;
		this.todoStorage = todoStorage;
		this.query = query;
	}

	@Override
	protected CommandStatus performExecute() {
		ArrayList<ToDo> todoList = (ArrayList<ToDo>) todoStorage.getAll();
		ArrayList<ToDo> result = new ArrayList<ToDo>();

		for (ToDo todo : todoList) {
			if (todo.getTitle().contains(query)) {
				result.add(todo);
			}
		}

		// For testing purpose
		for (ToDo todo : result) {
			System.out.println(todo.getTitle());
		}

		return new CommandStatus(Status.SUCCESS,
				"HOW TO PASS RESULT BACK TO LOGIC LEH?!");
	}

	@Override
	protected CommandStatus performUndo() {
		return null;
	}

}

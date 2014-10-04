package com.the.todo.parser;

import com.the.todo.model.ToDo;
import com.the.todo.storage.ToDoStore;

public class AddCommand implements Command {

	private ToDoStore todoStorage;
	private ToDo todo;
	
	public AddCommand(ToDoStore todoStorage, ToDo todo) {
		this.todoStorage = todoStorage;
		this.todo = todo;
	}
	
	@Override
	public void execute() {
		this.todoStorage.save(this.todo);
	}

	@Override
	public void undo() {
	}

}

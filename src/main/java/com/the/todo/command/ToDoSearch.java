package com.the.todo.command;

import java.util.ArrayList;
import java.util.List;

import com.the.todo.command.CommandStatus.Status;
import com.the.todo.model.ToDo;
import com.the.todo.storage.ToDoStore;
import com.the.todo.util.StringUtil;

public class ToDoSearch extends ToDoCommand {
	ToDoStore todoStorage;
	String query;
	List<ToDo> updateList;

	public ToDoSearch(ToDoStore todoStorage, String query, List<ToDo> updateList) {
		super();
		this.undoable = false;
		this.todoStorage = todoStorage;
		this.query = query;
		this.updateList = updateList;
	}

	@Override
	protected CommandStatus performExecute() {
		ArrayList<ToDo> todoList = (ArrayList<ToDo>) todoStorage.getAll();
		String titleTemp;
		String queryTemp = StringUtil.padString(query);
		
		updateList.clear();

		for (ToDo todo : todoList) {
			titleTemp = StringUtil.padString(todo.getTitle());
			if (titleTemp.contains(queryTemp)) {
				updateList.add(todo);
			}
		}

		// For testing purpose
		for (ToDo todo : updateList) {
			System.out.println(todo.getTitle());
		}

		return new CommandStatus(Status.SUCCESS);
	}

	@Override
	protected CommandStatus performUndo() {
		return null;
	}
}

package com.the.todo.command;

import com.the.todo.command.CommandStatus.Status;
import com.the.todo.model.ToDo;
import com.the.todo.storage.ToDoStore;
import com.the.todo.util.StringUtil;

public class ToDoComplete extends ToDoCommand{
	
	private static final String EXECUTE_DOES_NOT_EXIST = "It seems like ToDo %s does not exist.";
	private static final String EXECUTE_ILLEGAL_ARGUMENT = "Mmm ... Seems like you are missing some argument.";
	private static final String EXECUTE_ERROR = "An error occured while updating ToDo.";
	private static final String EXECUTE_SUCCESS = "A great success updating ToDo: %s";

	private ToDoStore todoStorage;
	private ToDo todo;
	private String input;

	public ToDoComplete(ToDoStore todoStorage, String input) {
		super();
		this.todoStorage = todoStorage;
		this.input = input;
		this.undoable = true;
	}

	@Override
	protected CommandStatus performExecute() {
		String todoId = input;

		if (todoId.length() != 1) {
			return new CommandStatus(Status.ERROR, EXECUTE_ILLEGAL_ARGUMENT);
		}

		this.todo = todoStorage.get(todoId);

		if (this.todo == null) {
			return new CommandStatus(Status.ERROR, String.format(
					EXECUTE_DOES_NOT_EXIST, todoId));
		}

		this.todo = isCompleteToDo(this.todo);

		if (this.todo == null) {
			return new CommandStatus(Status.ERROR, EXECUTE_ERROR);
		}

		todoStorage.update(this.todo.getId(), this.todo);
		return new CommandStatus(Status.SUCCESS, String.format(EXECUTE_SUCCESS,
				todo.getId()));
	}

	@Override
	protected CommandStatus performUndo() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private ToDo isCompleteToDo(ToDo todo) {
		
		todo.setCompleted(true);

		return todo;
	}

}

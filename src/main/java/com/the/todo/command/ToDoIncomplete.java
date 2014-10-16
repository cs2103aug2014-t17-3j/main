package com.the.todo.command;

import com.the.todo.command.CommandStatus.Status;
import com.the.todo.model.ToDo;
import com.the.todo.storage.ToDoStore;

public class ToDoIncomplete extends ToDoCommand{
	
	private static final String EXECUTE_DOES_NOT_EXIST = "It seems like ToDo %s does not exist.";
	private static final String EXECUTE_ILLEGAL_ARGUMENT = "Mmm ... Seems like you are missing some argument.";
	private static final String EXECUTE_ERROR = "An error occured while updating ToDo.";
	private static final String EXECUTE_SUCCESS = "A great success updating ToDo: %s";

	private ToDoStore todoStorage;
	private ToDo todo;

	public ToDoIncomplete(ToDoStore todoStorage, ToDo todo) {
		super();
		this.todoStorage = todoStorage;
		this.todo = todo;
		this.undoable = true;
	}

	@Override
	protected CommandStatus performExecute() {

		if (this.todo == null) {
			return new CommandStatus(Status.ERROR, String.format(
					EXECUTE_DOES_NOT_EXIST, ""));
		}

		this.todo = isIncompleteToDo(this.todo);
		System.out.println("Im here");

		if (this.todo == null) {
			return new CommandStatus(Status.ERROR, EXECUTE_ERROR);
		}
		
		//todoStorage.save(this.todo.getId(), this.todo);
		return new CommandStatus(Status.SUCCESS, String.format(EXECUTE_SUCCESS,
				""));
	}

	@Override
	protected CommandStatus performUndo() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private ToDo isIncompleteToDo(ToDo todo) {
		todo.setCompleted(false);
		return todo;
	}

}

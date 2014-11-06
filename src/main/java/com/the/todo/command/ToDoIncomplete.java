package com.the.todo.command;

import com.the.todo.command.CommandStatus.Status;
import com.the.todo.model.ToDo;
import com.the.todo.storage.ToDoStore;

public class ToDoIncomplete extends ToDoCommand {

	private static final String EXECUTE_DOES_NOT_EXIST = "ToDo %s does not exist.";
	private static final String EXECUTE_ILLEGAL_ARGUMENT = "Seems like you are missing somethings.";
	private static final String EXECUTE_ERROR = "An error occured while updating ToDo.";
	private static final String EXECUTE_SUCCESS = "Successful";

	private ToDoStore todoStorage;
	private ToDo todo;
	private ToDo newtodo;

	public ToDoIncomplete(ToDoStore todoStorage, ToDo todo) {
		super();
		this.todoStorage = todoStorage;
		this.todo = todo;
		this.undoable = true;
	}

	@Override
	protected CommandStatus performExecute() {
		
		newtodo = new ToDo(todo);
		todoStorage.update(newtodo.getId(), newtodo);

		if (this.newtodo == null) {
			return new CommandStatus(Status.ERROR, String.format(
					EXECUTE_DOES_NOT_EXIST, ""));
		}

		if(!newtodo.isCompleted()) {
			return new CommandStatus(Status.ERROR);
		} else {
			this.newtodo = isIncompleteToDo(this.newtodo);
		}
	
		if (this.newtodo == null) {
			return new CommandStatus(Status.ERROR, EXECUTE_ERROR);
		}

		// todoStorage.save(this.todo.getId(), this.todo);
		return new CommandStatus(Status.SUCCESS, String.format(EXECUTE_SUCCESS,
				""));
	}

	@Override
	protected CommandStatus performUndo() {
		todoStorage.update(newtodo.getId(), newtodo);
		this.newtodo = isCompleteToDo(this.newtodo);

		if (this.newtodo == null) {
			return new CommandStatus(Status.ERROR, EXECUTE_ERROR);
		}

		// todoStorage.save(this.todo.getId(), this.todo);
		return new CommandStatus(Status.SUCCESS, String.format(EXECUTE_SUCCESS,
				""));
	}

	private ToDo isIncompleteToDo(ToDo todo) {
		todo.setCompleted(false);
		return todo;
	}

	private ToDo isCompleteToDo(ToDo todo) {
		todo.setCompleted(true);
		return todo;
	}
}

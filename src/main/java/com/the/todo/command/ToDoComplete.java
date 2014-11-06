package com.the.todo.command;

import com.the.todo.command.CommandStatus.Status;
import com.the.todo.model.ToDo;
import com.the.todo.storage.ToDoStore;

public class ToDoComplete extends ToDoCommand {

	private static final String EXECUTE_DOES_NOT_EXIST = "It seems like ToDo %s does not exist.";
	private static final String EXECUTE_ILLEGAL_ARGUMENT = "Mmm ... Seems like you are missing some argument.";
	private static final String EXECUTE_ERROR = "An error occured while updating ToDo.";
	private static final String EXECUTE_SUCCESS = "A great success updating ToDo: %s";

	private ToDoStore todoStorage;
	private ToDo todo;
	private ToDo newtodo;

	public ToDoComplete(ToDoStore todoStorage, ToDo todo) {
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

		this.newtodo = isCompleteToDo(this.newtodo);

		if (this.newtodo == null) {
			return new CommandStatus(Status.ERROR, EXECUTE_ERROR);
		}

		return new CommandStatus(Status.SUCCESS, String.format(EXECUTE_SUCCESS,
				""));
	}

	@Override
	protected CommandStatus performUndo() {
		todoStorage.update(newtodo.getId(), newtodo);
		this.newtodo = undoCompleteToDo(this.newtodo);

		if (this.newtodo == null) {
			return new CommandStatus(Status.ERROR, EXECUTE_ERROR);
		}

		// todoStorage.save(this.todo.getId(), this.todo);
		return new CommandStatus(Status.SUCCESS, String.format(EXECUTE_SUCCESS,
				""));
	}

	private ToDo isCompleteToDo(ToDo todo) {
		todo.setCompleted(true);
		return todo;
	}

	private ToDo undoCompleteToDo(ToDo todo) {
		todo.setCompleted(false);
		return todo;
	}

}

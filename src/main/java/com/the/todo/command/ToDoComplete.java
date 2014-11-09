package com.the.todo.command;

import com.the.todo.command.CommandStatus.Status;
import com.the.todo.model.ToDo;
import com.the.todo.storage.ToDoStore;

//@author A0111780N

public class ToDoComplete extends ToDoCommand {

	private static final String EXECUTE_DOES_NOT_EXIST = "ToDo %s does not exist.";
	private static final String EXECUTE_ILLEGAL_ARGUMENT = "Seems like you are missing somethings.";
	private static final String EXECUTE_ERROR = "An error occured while updating ToDo.";
	private static final String EXECUTE_SUCCESS = "Successful";
	private static final String EXECUTE_DONE = "Task had been completed";

	private ToDoStore todoStorage;
	private ToDo newtodo;

	public ToDoComplete(ToDoStore todoStorage, ToDo todo) {
		super();
		this.todoStorage = todoStorage;
		this.todo = todo;
		this.undoable = true;
	}

	/* (non-Javadoc)
	 * @see com.the.todo.command.ToDoCommand#performExecute()
	 * This method will check whether the task exist. If the task does not 
	 * exist it will send return a error message. It will also check for duplicated commands,
	 * if the task had been completed and the user entered complete command again the method 
	 * will feedback an error message.
	 */
	@Override
	protected CommandStatus performExecute() {

		newtodo = new ToDo(todo);
		todoStorage.update(newtodo.getId(), newtodo);

		if (this.newtodo == null) {
			return new CommandStatus(Status.ERROR, String.format(
					EXECUTE_DOES_NOT_EXIST, ""));
		}

		if (newtodo.isCompleted()) {
			return new CommandStatus(Status.ERROR, EXECUTE_DONE);
		} else {
			this.newtodo = isCompleteToDo(this.newtodo);
		}

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

	@Override
	public ToDo getTodo (){
		return newtodo;
	}
}

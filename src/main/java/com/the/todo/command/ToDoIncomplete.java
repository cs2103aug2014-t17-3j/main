package com.the.todo.command;

import com.the.todo.command.CommandStatus.Status;
import com.the.todo.model.ToDo;
import com.the.todo.storage.ToDoStore;

//@author A0112969W

public class ToDoIncomplete extends ToDoCommand {

	private static final String EXECUTE_DOES_NOT_EXIST = "ToDo %s does not exist.";
	private static final String EXECUTE_ILLEGAL_ARGUMENT = "Seems like you are missing somethings.";
	private static final String EXECUTE_ERROR = "An error occured while updating ToDo.";
	private static final String EXECUTE_SUCCESS = "Successful";
	private static final String EXECUTE_NOTDONE = "Task had been uncompleted";

	private ToDoStore todoStorage;
	private ToDo newtodo;

	public ToDoIncomplete(ToDoStore todoStorage, ToDo todo) {
		super();
		this.todoStorage = todoStorage;
		this.todo = todo;
		this.undoable = true;
	}

	/* (non-Javadoc)
	 * @see com.the.todo.command.ToDoCommand#performExecute()
	 * This method will check whether the task exist. If the task does not 
	 * exist it will send return a error message. It will also check for duplicated commands,
	 * if the task had been uncompleted and the user entered incomplete command again the method 
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

		if(!newtodo.isCompleted()) {
			return new CommandStatus(Status.ERROR, EXECUTE_NOTDONE);
		} else {
			this.newtodo = isIncompleteToDo(this.newtodo);
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
		this.newtodo = isCompleteToDo(this.newtodo);

		if (this.newtodo == null) {
			return new CommandStatus(Status.ERROR, EXECUTE_ERROR);
		}

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
	
	@Override
	public ToDo getTodo (){
		return newtodo;
	}
}

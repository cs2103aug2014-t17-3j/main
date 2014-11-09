package com.the.todo.command;

import java.util.EmptyStackException;
import java.util.Stack;

import com.the.todo.command.CommandStatus.Status;
import com.the.todo.model.ToDo;
import com.the.todo.storage.ToDoStore;

//@author A0111815R

public class ToDoUndo extends ToDoCommand {

	private ToDoStore todoStorage;
	private Stack<ToDoCommand> undoStack;

	private static final String EXECUTE_ERROR_EMPTY = "There is nothing to undo.";

	public ToDoUndo(ToDoStore todoStorage, Stack<ToDoCommand> undoStack) {
		this.todoStorage = todoStorage;
		this.undoStack = undoStack;
	}

	@Override
	protected CommandStatus performExecute() {
		try {
			ToDoCommand lastCommand = undoStack.peek();
			todo = lastCommand.getTodo();
			CommandStatus status = lastCommand.undo();
			undoStack.pop();
			return status;
		} catch (EmptyStackException ex) {
			return new CommandStatus(Status.ERROR, EXECUTE_ERROR_EMPTY);
		}
	}

	@Override
	protected CommandStatus performUndo() {
		return null;
	}

	@Override
	public ToDo getTodo (){
		return todo;
	}
}

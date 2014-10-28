package com.the.todo.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import com.the.todo.command.CommandStatus.Status;
import com.the.todo.model.ToDo;
import com.the.todo.parser.CategoryPriorityParser;
import com.the.todo.storage.ToDoStore;

public class ToDoSearch extends ToDoCommand {
	private static final String EXECUTE_ILLEGAL_ARGUMENT = "Please enter a valid input.";

	ToDoStore todoStorage;
	String query;
	List<ToDo> updateList;

	public ToDoSearch(ToDoStore todoStorage, List<ToDo> updateList, String query) {
		super();
		this.todoStorage = todoStorage;
		this.updateList = updateList;
		this.query = query;
		
		this.undoable = false;
	}

	@Override
	protected CommandStatus performExecute() {

		if (query.equals("") || query == null) {
			return new CommandStatus(Status.ERROR, EXECUTE_ILLEGAL_ARGUMENT);
		}

		ArrayList<ToDo> allToDos = (ArrayList<ToDo>) todoStorage.getAll();

		String category = CategoryPriorityParser.parse(query);
		List<ToDo> todoWithCategory = searchByCategory(allToDos, category);

		String queryWithoutCategory = CategoryPriorityParser.removeStringFromTitle(query,
				category);
		List<ToDo> results = searchByKeywords(todoWithCategory,
				queryWithoutCategory);

		updateList.clear();
		for (ToDo todo : results) {
			updateList.add(todo);
		}

		return new CommandStatus(Status.SUCCESS);
	}

	private List<ToDo> searchByCategory(List<ToDo> todos, String category) {
		// assert category to have this format "+"....
		List<ToDo> results = new ArrayList<ToDo>(todos);
		ListIterator<ToDo> i = results.listIterator();

		if (category != null) {
			while (i.hasNext()) {
				ToDo nextToDo = i.next();
				if (nextToDo.getCategory() == null || !nextToDo.getCategory().equals(category)) {
					i.remove();
				}
			}
		}
		return results;
	}

	private List<ToDo> searchByKeywords(List<ToDo> todos, String query) {
		List<ToDo> results = new ArrayList<ToDo>(todos);

		if (query.equals("") || query == null) {
			return results;
		}

		ListIterator<ToDo> i = results.listIterator();
		while (i.hasNext()) {
			ToDo nextToDo = i.next();
			if (!containsAll(nextToDo.getTitle(), query)) {
				i.remove();
			}
		}

		return results;
	}

	private boolean containsAll(String data, String query) {
		String[] tokenizedData = data.split(" ");
		String[] tokenizedQuery = query.split(" ");
		List<String> tokenizedQueryList = new ArrayList<String>(
				Arrays.asList(tokenizedQuery));

		for (int i = 0; i < tokenizedData.length
				&& tokenizedQueryList.size() != 0; i++) {
			ListIterator<String> j = tokenizedQueryList.listIterator();
			while (j.hasNext()) {
				if (j.next().equals(tokenizedData[i])) {
					j.remove();
					break;
				}
			}
		}

		if (tokenizedQueryList.size() == 0) {
			return true;
		}
		return false;
	}

	@Override
	protected CommandStatus performUndo() {
		return null;
	}
}

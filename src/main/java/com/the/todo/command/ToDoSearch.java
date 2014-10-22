package com.the.todo.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import com.the.todo.command.CommandStatus.Status;
import com.the.todo.model.ToDo;
import com.the.todo.parser.CategoryParser;
import com.the.todo.storage.ToDoStore;

public class ToDoSearch extends ToDoCommand {
	private static final String EXECUTE_ILLEGAL_ARGUMENT = "Please enter a valid input.";

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
		if (query.equals("") || query == null) {
			return new CommandStatus(Status.ERROR, EXECUTE_ILLEGAL_ARGUMENT);
		}

		ArrayList<ToDo> allToDos = (ArrayList<ToDo>) todoStorage.getAll();

		String category = CategoryParser.parse(query);
		List<ToDo> todoWithCategory = searchByCategory(allToDos, category);

		String queryWithoutCategory = CategoryParser.removeCategory(query,
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
		//assert category to have this format "+"....
		List<ToDo> results = new ArrayList<ToDo>(todos);
		ListIterator<ToDo> i = results.listIterator();

		if (category != null) {
			while (i.hasNext()) {
				ToDo nextToDo = i.next();
				if (!nextToDo.getCategory().equals(category)) {
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
			String[] tokenizedTitle = nextToDo.getTitle().split(" ");
			String[] tokenizedQuery = query.split(" ");
			List<String> tokenizedQueryList = new ArrayList<String>();
			Collections.addAll(tokenizedQueryList, tokenizedQuery);

			//TODO extract this part into a method: containsAll
			for (int j = 0; j < tokenizedTitle.length
					&& tokenizedQueryList.size() != 0; j++) {
				ListIterator<String> k = tokenizedQueryList.listIterator();
				while (k.hasNext()) {
					if (k.next().equals(tokenizedTitle[j])) {
						k.remove();
						break;
					}
				}
			}
			if (tokenizedQueryList.size() != 0) {
				i.remove();
			}
		}

		return results;
	}

	@Override
	protected CommandStatus performUndo() {
		return null;
	}
}

package com.the.todo.parser;

import java.util.Scanner;

import org.joda.time.LocalDate;

import com.the.todo.model.ToDo;
import com.the.todo.storage.InMemoryStore;

public class CommandHandler {
	
	private static Scanner sc = new Scanner(System.in);
	private static InMemoryStore ms = new InMemoryStore();

	public void commandHandle() {
//		System.out.print(COMMAND);
//		commands = sc.next();
//		commands = commands.toLowerCase();
//		String description = sc.nextLine();
//		
//		toDo = new ToDo(description);
//		if(commands == "delete")
//		{
//			id = Integer.parseInt(description);
//
//		}
//		LocalDate date = stringParser.stringProcess(description);
////		toDo = new ToDo(description);
////		commandProcess(commands, toDo, date);
	}
	
	public void commandProcess(String command) {
		String[] inputs = command.split(" ", 2);
		
		switch (inputs[0].trim().toLowerCase()) {
			case "add":
				ToDo todo = processAdd(inputs[1]);
				ms.save(todo);
			case "read":
				ms.getAll();
				break;
			case "delete":
				ms.delete(inputs[1]);
				break;
			case "edit":
				break;
		}
		
		for(ToDo todo : ms.getAll()) {
			System.out.println("ID: " + todo.getId());
			System.out.println("Title: " + todo.getTitle());
			System.out.println("Date: " + todo.getEndDate());
			System.out.println("Completed: " + todo.isCompleted());
			System.out.println("Delete: " + todo.isDeleted());
		}
	}

	private ToDo processAdd(String input) {
		ToDo todo = new ToDo(input);
		StringProcess sp = new StringProcess();
		LocalDate date = sp.stringProcess(input);
		todo.setEndDate(date);
		return todo;
	}
	
}

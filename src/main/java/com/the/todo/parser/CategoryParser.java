package com.the.todo.parser;

import com.the.todo.model.ToDo;

public class CategoryParser {
	
	public static ToDo categoryParser(String input, ToDo todo) {
		int subIndex = input.indexOf('+');
		int endIndex;
		String subString;
		if (subIndex != -1) {
			String beforeSubString = input.substring(subIndex).trim();
			endIndex = beforeSubString.indexOf(" ");
			if(endIndex != -1) {
				subString = beforeSubString.substring(0, endIndex);
			}else{
				subString = input.substring(subIndex).trim();
			}
			todo.setCategory(subString);
		}
		return todo;
	}
	
}

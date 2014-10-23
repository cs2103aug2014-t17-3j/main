package com.the.todo.util;

public class CommandUtil {
	
	public static String getFirstPhrase(String input) {
		return StringUtil.splitString(input, "\\s+", 2)[0].toLowerCase();
	}
	
	public static String getParams(String command, String input) {
		String params = input.replaceFirst(command, "").trim();
		
		return params;
	}

}

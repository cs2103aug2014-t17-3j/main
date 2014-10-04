package com.the.todo.util;

public class StringUtil {

	public static String[] splitString(String input, String regex) {
		return splitString(input, regex, 0);
	}
	
	public static String[] splitString(String input, String regex, int limit) {
		return input.trim().split(regex, limit);
	}

}

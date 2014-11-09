package com.the.todo;

import com.the.todo.util.CommandUtil;


public class ToDoHint {

	final String ADD_HINT = "add [Task] from [Start Date] to/on/by [End Date] at [Time] +[Category] +[Priority]";
	final String SEARCH_HINT = "search [Keyword]/[Category]";
	final String DELETE_HINT = "delete [ID]";
	final String  EDIT_HINT= "edit [ID] -title [title] -category [Category] -startdate [Start Date] -enddate [End Date] -priority [Priority] ";
	final String VIEW_HINT = "view all/completed/incomplete";
	final String COMPLETE_HINT = "complete [ID]";
	final String INCOMPLETE_HINT = "incomplete [ID]";
	private String string ;
	
	public ToDoHint( String recentlyTypedStr){ 
		this.string = recentlyTypedStr;		

	}

	public String getHints(){ 

		switch(determineCommand(string)){ 
		case "add":			
			return ADD_HINT; 
		case "search": 
			return SEARCH_HINT;
		case "view":			
			return VIEW_HINT; 
		case "delete": 
			return DELETE_HINT;
		case "edit": 
			return EDIT_HINT;
		case "complete": 
			return COMPLETE_HINT;
		case "incomplete": 
			return INCOMPLETE_HINT;
		default: return "";
		} 


	}

	private String determineCommand(String s){
		String[] str= {"add","search","delete","edit","view","complete","incomplete"} ;
		if (s.isEmpty()){
			return "invalid";
		}
		if (s.contains(" ")){// full command expected 
			s= CommandUtil.getFirstPhrase(s);
			for (int i=0 ; i<str.length;i++){ 
				if (str[i].equals(s)){ 
					return str[i];
				} 
			}
		return "invalid";	
		
		}
		for (int i=0 ; i<str.length;i++){ 
			if (str[i].startsWith(s)){ 
				return str[i];
			} 
		}


		return "invalid";
	}


}

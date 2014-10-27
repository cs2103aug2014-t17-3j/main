package com.the.todo;


public class ToDoHint {

	final String ADD_HINT = "add [Task] on [Date] at [Time]";
	final String SEARCH_HINT = "search [keyword]";
	final String DELETE_HINT = "delete [ID]";
	final String  EDIT_HINT= "edit [ID] +title [title]";
	final String VIEW_HINT = "view all/completed/incompleted";
	final String COMPLETE_HINT = "complete [ID]";
	final String INCOMPLETE_HINT = "incomplete [ID]";
	String string ;

	public ToDoHint( String recentlyTypedStr){ 
		this.string = recentlyTypedStr; 

	}

	public String getHints(){ 

		switch(determineCommand(string)){ 
		case "add":			
			return ADD_HINT; 
		case "view":			
			return VIEW_HINT; 
		case "delete": 
			return DELETE_HINT;
		case "complete": 
			return COMPLETE_HINT;
		case "incomplete": 
			return INCOMPLETE_HINT;
		case "invalid" :return"";
			default: return "";
		} 


	}

	private String determineCommand(String s){
		String[] str= {"add","search","delete","edit","view","complete","incomplete"} ;
		
		for (int i=0 ; i<str.length;i++){ 
		
				if (str[i].startsWith(s)){ 
					 return str[i];
				}
				}

	
			return "invalid";
		}
		

}

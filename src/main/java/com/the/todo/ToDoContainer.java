package com.the.todo;

import java.io.IOException;

import com.the.todo.model.ToDo;

import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ToDoContainer extends AnchorPane {

	@FXML
	private Label todoID;
	@FXML
	private Label todoTitle;
	@FXML
	private Label todoDate;
	@FXML
	private Label todoMisc;
	
	public ToDoContainer() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
				"/fxml/todoContainer.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}
	
	public ToDoContainer (ToDo todo) throws Exception{
		this();
		if (!isValidTodo(todo)){
			throw new Exception("Invalid todo");
		} else {
			setID(todo.getId());
			setTitle(todo.getTitle());	
		}
		
	}
	
	private void setID (String id){
		todoID.setText(id);
	}
	
	private void setTitle (String title){
		todoTitle.setText(title);
	}
	
	private void setDate (String date){
		todoDate.setText(date);
	}
	
	private void setMisc (String misc){
		todoMisc.setText(misc);
	}
	
	/**
	 * Checks whether a given ToDo object is valid. A ToDo is valid only if its ID and Title is not null
	 * @param todo
	 * @return 
	 */
	private boolean isValidTodo (ToDo todo){
		if (todo.getId() == null){
			return false;
		}
		if (todo.getTitle() == null){
			return false;
		}
		
		return true;
	}
}

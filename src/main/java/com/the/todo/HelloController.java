package com.the.todo;

import javafx.application.Platform;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.the.todo.parser.CommandParser;
import com.the.todo.parser.CommandParser.CommandType;
import com.the.todo.storage.InMemoryStore;
import com.the.todo.command.CommandStatus;
import com.the.todo.command.ToDoAdd;
import com.the.todo.command.ToDoDelete;
import com.the.todo.command.ToDoEdit;
import com.the.todo.model.ToDo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class HelloController {
	
	private static final KeyCode [] RESERVED_KEYS = {KeyCode.UP, KeyCode.DOWN, KeyCode.LEFT, KeyCode.RIGHT};

	@FXML
	private Label mainLabel;
	@FXML
	private VBox mainVBox;
	@FXML 
	private TextField mainInput ; 
	
	private InMemoryStore memstore = new InMemoryStore();
	
	@FXML
	void initialize(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		mainLabel.setText(dateFormat.format(date));
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				mainInput.requestFocus();
			}
		});
				 
	}
	
	public void processInput(){
		CommandStatus status;
		String userInput = mainInput.getText();
		
		mainInput.clear();
		mainVBox.getChildren().clear();
		
		CommandType commandType = CommandParser.getCommandType(userInput);
		switch (commandType) {
			case ADD:
				String addParam = CommandParser.getTitle(userInput);
				status = new ToDoAdd(memstore, addParam).execute();
				updateUI(status.getMessage());
				break;
				
			case READ:
				updateUI("All tasks", memstore.getAll());
				break;
				
			case EDIT:
				String editParam = CommandParser.getTitle(userInput);
				status = new ToDoEdit(memstore, editParam).execute();
				updateUI(status.getMessage());
				break;
				
			case DELETE:
				String id = CommandParser.getTitle(userInput);
				status = new ToDoDelete(memstore, id).execute();
				updateUI(status.getMessage());
				break;
				
			case INVALID:
				// Fallthrough
				
			default:
				updateUI("Can don't be funny?");
		}
	}
	
	/**
	 * @param label Text to be displayed by mainLabel
	 */
	public void updateUI (String label){
		mainLabel.setText(label);
	}
	
	/**
	 * @param label Text to be displayed by mainLabel
	 * @param todoItems List of ToDo to be displayed in mainVbox
	 */
	public void updateUI(String label, Collection<ToDo> todoItems){
		updateUI(label);
		
		ArrayList<Label> itemsList = new ArrayList<Label>();
		
		for (ToDo todo : todoItems){
			Label temp = new Label(todo.toString());
			itemsList.add(temp);
		}
		mainVBox.getChildren().setAll(itemsList);
	}
	
	public void processKeyEvents(KeyEvent keyevent){
		for (KeyCode reservedKeyCode : RESERVED_KEYS){
			if (keyevent.getCode() == reservedKeyCode){
				
				//TODO Implement actions for reserved keys
				
				keyevent.consume();
				break;
			}
		}
	}	
}
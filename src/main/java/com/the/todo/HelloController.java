package com.the.todo;

import javafx.application.Platform;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.the.todo.parser.CommandParser;
import com.the.todo.storage.InMemoryStore;
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
	
	private String inputCommand;
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
	
	public void populateVbox(Collection<ToDo> items) {
		ArrayList<Label> itemsList = new ArrayList<Label>();
		
		for (ToDo todo : items){
			Label label = new Label(todo.toString());
			itemsList.add(label);
		}
		mainVBox.getChildren().setAll(itemsList);
	}	
	
	public void processInput(){
		inputCommand = mainInput.getText();
		 
		CommandParser.commandProcess(memstore, inputCommand);
		mainInput.clear(); 
		
		populateVbox(memstore.getAll());
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
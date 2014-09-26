package com.the.todo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.the.todo.parser.CommandParser;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class HelloController {

	@FXML
	private Label mainLabel;
	@FXML
	private VBox mainVBox;
	@FXML 
	private TextField mainInput ; 
	
	private String inputCommand;
	private CommandParser command = new CommandParser(); 
	
	@FXML
	void initialize(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		mainLabel.setText(dateFormat.format(date));
				 
	}
	
	public void sayHello() {
		TextField text = new TextField("test");
		mainVBox.getChildren().add(text);
	}	
	
	public void processInput(){
		inputCommand = mainInput.getText();
		 
		command.commandProcess(inputCommand);
		mainInput.clear(); 
		 
		 
		

		 



	} 
	
}
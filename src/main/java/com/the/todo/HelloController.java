package com.the.todo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	void initialize(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		mainLabel.setText(dateFormat.format(date));
	}
	
	public void sayHello() {
		TextField text = new TextField("test");
		mainVBox.getChildren().add(text);
	}	
}
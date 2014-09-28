package com.the.todo;

import javafx.application.Application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Hello world!
 *
 */
public class App extends Application {
	
	public static void main(String[] args) throws Exception {
		System.out.println("Hello World!");
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
        String fxmlFile = "/fxml/todoUI.fxml";
        
        FXMLLoader loader = new FXMLLoader();
        Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));

        Scene scene = new Scene(rootNode, 400, 200);
        scene.getStylesheets().add("/styles/styles.css");
        
        final HelloController control = loader.getController();
        scene.addEventFilter(KeyEvent.ANY, new EventHandler<KeyEvent>() {
        	@Override
        	public void handle(KeyEvent event){
        		control.processKeyEvents(event);
        	}
		});

        stage.setTitle("Hello JavaFX and Maven");
        stage.setScene(scene);
        stage.show();
    
        
        
        
	}
}

/**
 * This file is part of TheTODO, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2014 TheTODO
 * Copyright (c) Poh Wei Cheng <calvinpohwc@gmail.com>
 *				 Chen Kai Hsiang <kaihsiang95@gmail.com>
 *				 Khin Wathan Aye <y.caiyun@gmail.com>
 *				 Neo Eng Tai <neoengtai@gamil.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.the.todo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import com.the.todo.command.CommandStatus;
import com.the.todo.model.ToDo;

public class MainToDoController {

	private static final KeyCode[] RESERVED_KEYS = { KeyCode.UP, KeyCode.DOWN,
		KeyCode.LEFT, KeyCode.RIGHT };

	@FXML
	private Label promptLabel;
	@FXML
	private VBox mainVBox;	
	@FXML
	private TextField mainInput;
	@FXML
	private ScrollPane mainScrollpane;
	@FXML
	private Button minimizeButton;

	private static Logic appLogic;

	private FadeTransition fadeOut;

	@FXML
	void initialize() {
		DateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");
		Date date = new Date();

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				mainInput.requestFocus();
			}
		});

		fadeOut = new FadeTransition(Duration.millis(3000), promptLabel);
		appLogic = new Logic();
		updateUI(appLogic.getTodoList());
	}

	public void processInput() {
		String userInput = mainInput.getText();
		processInput(userInput);

	}
	//override
	public void processInput(String userInput){ 
		CommandStatus status = appLogic.processCommand(userInput);

		switch (status.getStatus()) {
		case SUCCESS:
			updateUI(appLogic.getTodoList());
			showPrompt(status.getMessage());
			break;

		case ERROR:
			// Fallthrough

		case INVALID:
			// Fallthrough

		default:
			showPrompt(status.getMessage());
			break;
		}
	}

	public void showPrompt(String str) {
		if (!str.isEmpty()) {
			promptLabel.setText(str);
			promptLabel.setOpacity(1);

			fadeOut.setToValue(0.0);
			fadeOut.playFromStart();
		}
	}

	public void clearUI() {
		mainInput.clear();
		mainVBox.getChildren().clear();
	}

	/**
	 * @param label
	 *            Text to be displayed by mainLabel
	 * @param todoItems
	 *            List of ToDo to be displayed in mainVbox
	 */
	public void updateUI(List<ToDo> todoItems) {
		clearUI();
		int index = 1;


		ArrayList<Node> contentsToDisplay = new ArrayList<Node>();
		ArrayList<Character> i= new ArrayList<Character>();

		if (todoItems.isEmpty()) {
			Label temp = new Label("No items to show.");
			contentsToDisplay.add(temp);
		} else {
			// Implement main label here (Date-ToDo, search, view)


			for (ToDo todo : todoItems) {
				try {
					ToDoContainer temp = new ToDoContainer(index, todo);
					contentsToDisplay.add(temp);
					detectCheckBoxChanges(temp);
					index++;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		mainVBox.getChildren().setAll(contentsToDisplay);	
		
	}

	private void detectCheckBoxChanges(ToDoContainer container){ 
		container.getCheckedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> ov,
					Boolean old_val, Boolean new_val) {
				if (new_val)						
					processInput("complete " + container.getID());
				else 
					processInput("incomplete "+ container.getID());
			}
		});
		}


	public void minimizeWindow() {
		Stage stage = (Stage) minimizeButton.getScene().getWindow();
		stage.hide();
	}

	public void processKeyEvents(KeyEvent keyevent) {
		for (KeyCode reservedKeyCode : RESERVED_KEYS) {

			if (keyevent.isControlDown() && keyevent.getCode()== KeyCode.Z){
				mainInput.setText("undo\n");
				mainInput.positionCaret(4);
			}
			if (keyevent.getCode() == reservedKeyCode) {

				// TODO Implement actions for reserved keys
				/*
				 * if (keyevent.getCode() == KeyCode.UP) {
				 * mainScrollpane.setVvalue(mainScrollpane.getVvalue()-1); } if
				 * (keyevent.getCode() == KeyCode.DOWN) {
				 * mainScrollpane.setVvalue(mainScrollpane.getVvalue()+1); }
				 */

				keyevent.consume();
				break;
			}
		}
	}
}

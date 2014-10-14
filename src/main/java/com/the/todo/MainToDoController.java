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
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import com.the.todo.command.CommandStatus;
import com.the.todo.model.ToDo;

public class MainToDoController {

	private static final KeyCode[] RESERVED_KEYS = { KeyCode.UP, KeyCode.DOWN,
			KeyCode.LEFT, KeyCode.RIGHT };

	@FXML
	private Label mainLabel, promptLabel;
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
		updateUI(dateFormat.format(date), appLogic.getTodoList());

	}

	public void processInput() {

		String userInput = mainInput.getText();

		mainInput.clear();
		mainVBox.getChildren().clear();

		CommandStatus status = appLogic.processCommand(userInput);
		updateUI(status.getMessage(), appLogic.getTodoList());
		showPrompt(status.getMessage());
	}

	public void showPrompt(String str) {
		promptLabel.setVisible(true);
		promptLabel.setText(str);

		fadeOut.setToValue(0.0);
		fadeOut.playFromStart();
	}

	/**
	 * @param label
	 *            Text to be displayed by mainLabel
	 */
	public void updateUI(String label) {
		mainLabel.setText(label);
	}

	/**
	 * @param label
	 *            Text to be displayed by mainLabel
	 * @param todoItems
	 *            List of ToDo to be displayed in mainVbox
	 */
	public void updateUI(String label, List<ToDo> todoItems) {
		updateUI(label);

		if (todoItems.isEmpty()) {
			return;
		}

		ArrayList<ToDoContainer> itemsList = new ArrayList<ToDoContainer>();
		int index = 1;
		for (ToDo todo : todoItems) {
			try {
				ToDoContainer temp = new ToDoContainer(index, todo);
				itemsList.add(temp);
				index++;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		mainVBox.getChildren().setAll(itemsList);
	}

	public void minimizeWindow() {
		Stage stage = (Stage) minimizeButton.getScene().getWindow();
		stage.hide();
	}

	public void processKeyEvents(KeyEvent keyevent) {
		for (KeyCode reservedKeyCode : RESERVED_KEYS) {

			if (keyevent.getCode() == reservedKeyCode) {

				// TODO Implement actions for reserved keys
				/*
				 * if (keyevent.getCode() == KeyCode.UP) {
				 * mainScrollpane.setVvalue(mainScrollpane.getVvalue()-1); } if
				 * (keyevent.getCode() == KeyCode.DOWN) {
				 * mainScrollpane.setVvalue(mainScrollpane.getVvalue()+1); }
				 */
				promptLabel.setVisible(false);
				keyevent.consume();
				break;
			}
		}
	}
}

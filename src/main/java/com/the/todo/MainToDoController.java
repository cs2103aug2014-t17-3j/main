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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.layout.AnchorPane;
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
	private VBox mainVBox, bbb;
	@FXML
	private TextField mainInput;
	@FXML
	private ScrollPane mainScrollpane;
	@FXML
	private Button minimizeButton;

	private AnchorPane anPane;
	private FadeTransition fadeOut;

	private static Logic appLogic;

	@FXML
	void initialize() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				mainInput.requestFocus();
			}
		});

		fadeOut = new FadeTransition(Duration.millis(3000), promptLabel);
		appLogic = new Logic();
		updateUI(appLogic.getToDoMapDisplay());
	}

	public void processInput() {
		String userInput = mainInput.getText();
		processInput(userInput);
	}

	// override
	public void processInput(String userInput) {
		CommandStatus status = appLogic.processCommand(userInput);

		switch (status.getStatus()) {
		case SUCCESS:
			updateUI(appLogic.getToDoMapDisplay());
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
		ArrayList<Character> i = new ArrayList<Character>();

		if (todoItems.isEmpty()) {
			Label temp = new Label("No items to show.");
			contentsToDisplay.add(temp);
		} else {
			// Implement main label here (Date-ToDo, search, view)

			for (ToDo todo : todoItems) {
				try {
					SimpleStringProperty checkedStatus = new SimpleStringProperty(
							"");
					ToDoContainer temp = new ToDoContainer(index, todo);
					contentsToDisplay.add(temp);
					temp.getCheckedStatus().addListener(
							new ChangeListener<String>() {
								@Override
								public void changed(
										ObservableValue<? extends String> ov,
										String old_val, String new_val) {
									checkedStatus.set(new_val);
									showPrompt(checkedStatus.toString());
									processInput("complete "
											+ checkedStatus.toString().charAt(
													checkedStatus.toString()
															.length() - 2));
								}
							});

					index++;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		mainVBox.getChildren().setAll(contentsToDisplay);

	}
	
	public void updateUI(Map<LocalDate, List<ToDo>> todoItems) {
		clearUI();
		
		int index = 1;
		ArrayList<Node> contentsToDisplay = new ArrayList<Node>();
		ArrayList<Character> i = new ArrayList<Character>();

		if (todoItems == null || todoItems.isEmpty()) {
			Label temp = new Label("No items to show.");
			contentsToDisplay.add(temp);
		} else {
			Label lblDate;
			
			for (Entry<LocalDate, List<ToDo>> entry : todoItems.entrySet()) {
				System.out.println(entry.getKey() + " = " + entry.getValue());
				if (entry.getKey().equals(ToDo.INVALID_DATE.toLocalDate())) {
					lblDate = new Label("Someday");
				} else {
					lblDate = new Label(entry.getKey().toString(DateTimeFormat.forPattern("EEEE, dd MMMM yyyy")));
				}
				contentsToDisplay.add(lblDate);
	
				for (ToDo todo : entry.getValue()) {
					try {
						ToDoContainer item = new ToDoContainer(index, todo);
						contentsToDisplay.add(item);
						index++;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

//		else {
//			// Implement main label here (Date-ToDo, search, view)
//			for (Entry<LocalDateTime, List<ToDo>> entry : todoItems.entrySet()) {
//				try {
//					SimpleStringProperty checkedStatus = new SimpleStringProperty(
//							"");
//					ToDoContainer temp = new ToDoContainer(index, todo);
//					contentsToDisplay.add(temp);
//					temp.getCheckedStatus().addListener(
//							new ChangeListener<String>() {
//								@Override
//								public void changed(
//										ObservableValue<? extends String> ov,
//										String old_val, String new_val) {
//									checkedStatus.set(new_val);
//									showPrompt(checkedStatus.toString());
//									processInput("complete "
//											+ checkedStatus.toString().charAt(
//													checkedStatus.toString()
//															.length() - 2));
//								}
//							});
//
//					index++;
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//			}
//		}
		mainVBox.getChildren().setAll(contentsToDisplay);
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

				keyevent.consume();
				break;
			}
		}
	}
}

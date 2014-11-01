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

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import com.the.todo.command.CommandStatus;
import com.the.todo.model.ToDo;
import com.the.todo.task.ReminderTask;

public class MainToDoController {

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
	@FXML
	private SplitPane mainPane;
	@FXML
	private Label hintLabel;

	private FadeTransition fadeOut;

	private Logic appLogic = Logic.getInstance();

	private ArrayList<String> commandHistory = new ArrayList<String>();
	private int currentHistoryIndex;

	private ObservableList<Node> oldVBoxitems;
	
	@FXML
	void initialize() {
		mainScrollpane.setFitToWidth(true);
		fadeOut = new FadeTransition(Duration.millis(3000), promptLabel);

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				mainInput.requestFocus();
				updateUI(appLogic.getToDoMapDisplay());
				generateAllReminders();
			}
		});
	}

	public void processInput() {
		String userInput = mainInput.getText();
		
		commandHistory.add(userInput);
		currentHistoryIndex = commandHistory.size();

		processInput(userInput);
	}

	// override
	public void processInput(String userInput) {
		CommandStatus status = appLogic.processCommand(userInput);

		switch (status.getStatus()) {
		case SUCCESS:
			updateUI(appLogic.getToDoMapDisplay());
			ObservableList<Node> ol = mainVBox.getChildren();
			for (Node node : ol){
				System.out.println(node.toString());
				System.out.println(node.getLayoutY());
			}
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

		if (todoItems.isEmpty()) {
			Label temp = new Label("No items to show.");
			contentsToDisplay.add(temp);
		} else {
			// Implement main label here (Date-ToDo, search, view)

			for (ToDo todo : todoItems) {
				try {
					ToDoContainer temp = new ToDoContainer(index, todo);
					contentsToDisplay.add(temp);
					temp.getCheckedProperty().addListener(
							new ChangeListener<Boolean>() {
								@Override
								public void changed(
										ObservableValue<? extends Boolean> ov,
										Boolean old_val, Boolean new_val) {
									if (new_val) {
										processInput("complete " + temp.getID());
									} else {
										processInput("incomplete "
												+ temp.getID());
									}
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
		ObservableList<Node> newVBoxItems = mainVBox.getChildren();

		if (todoItems == null || todoItems.isEmpty()) {
			Label temp = new Label("No items to show.");
			newVBoxItems.add(temp);
		} else {
			Label lblDate;

			for (Entry<LocalDate, List<ToDo>> entry : todoItems.entrySet()) {
				System.out.println(entry.getKey() + " = " + entry.getValue());
				lblDate = createGroupLabel(entry.getKey());
				newVBoxItems.add(lblDate);

				for (ToDo todo : entry.getValue()) {
					try {
						ToDoContainer item = new ToDoContainer(index, todo);
						detectCheckBoxChanges(item);
						newVBoxItems.add(item);
						index++;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}	
	}

	private void detectCheckBoxChanges(ToDoContainer container) {
		container.getCheckedProperty().addListener(
				new ChangeListener<Boolean>() {
					@Override
					public void changed(ObservableValue<? extends Boolean> ov,
							Boolean old_val, Boolean new_val) {
						if (new_val) {
							processInput("complete " + container.getID());
						} else {
							processInput("incomplete " + container.getID());
						}
					}
				});
	}

	public void minimizeWindow() {
		Stage stage = (Stage) minimizeButton.getScene().getWindow();
		stage.hide();
	}
	public Label createGroupLabel(LocalDate date) {
		
		LocalDate currentDate = new LocalDate();
		Label label = new Label(date.toString(		DateTimeFormat.forPattern("EEEE, dd MMMM yyyy")));
		
		if (date.equals(ToDo.INVALID_DATE.toLocalDate())) {
			label = new Label("Someday");
		}
		
		if (date.isBefore(currentDate) ){		
			label.setStyle("-fx-background-color: #FF5050;");
		}

		label.getStyleClass().add("groupLabel");
		label.setMaxWidth(Double.MAX_VALUE);
		return label;

	}
	
	public void processKeyEvents(KeyEvent keyevent) {

		if (keyevent.getEventType() == KeyEvent.KEY_PRESSED
				&& keyevent.isControlDown()) {

			if (keyevent.getCode() == KeyCode.UP) {
				mainScrollpane.setVvalue(mainScrollpane.getVvalue() - 0.1);
			} else if (keyevent.getCode() == KeyCode.DOWN) {
				mainScrollpane.setVvalue(mainScrollpane.getVvalue() + 0.1);
			} else if (keyevent.getCode() == KeyCode.Z) {
				//processInput("undo");
				System.out.println("vbox height:" + mainVBox.getHeight());
				ObservableList<Node> ol = mainVBox.getChildren();
				for (Node node : ol){
					System.out.println(node.toString());
					System.out.println(node.getLayoutY());
				}
				
			}
		
			
		}

		else if (keyevent.getEventType() == KeyEvent.KEY_PRESSED){
			if (keyevent.getCode() == KeyCode.UP) {
				if (currentHistoryIndex > 0) {
					currentHistoryIndex--;
					mainInput.setText(commandHistory.get(currentHistoryIndex));				
				}
			} else if (keyevent.getCode() == KeyCode.DOWN) {
				if (currentHistoryIndex < commandHistory.size() - 1) {
					currentHistoryIndex++;
					mainInput.setText(commandHistory.get(currentHistoryIndex));
				}
			}
		}

		// For showing hints
		if (keyevent.getEventType() == KeyEvent.KEY_TYPED && mainInput.isFocused())  {
			String incompleteCommand = mainInput.getText()
					+ keyevent.getCharacter();
			if(incompleteCommand.indexOf("\b")== incompleteCommand.length()-1){ //backspace char at end 
				if (incompleteCommand.length()>0){
					incompleteCommand = incompleteCommand.substring(0, incompleteCommand.length()-1);
				}
			}				
			ToDoHint hint = new ToDoHint(incompleteCommand);
			String str = hint.getHints();
			hintLabel.setText(str);
		}
	}
	
	private void generateAllReminders() {
		LocalDateTime currentDateTime = new LocalDateTime();
		LocalDateTime tomorrowDateTime = new LocalDateTime().plusDays(1);
		
		for (ToDo todo : appLogic.getTodoStorage().getAll()) {
			if (todo.isDeadlineToDo()) {
				if (todo.getEndDate().isBefore(tomorrowDateTime) && todo.getEndDate().isAfter(currentDateTime)) {
					new ReminderTask(todo, currentDateTime);
				}
			}
		}
	}
	
	private void scrollToUpdatedArea (){
		
	}
}

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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import org.joda.time.LocalDateTime;

import com.the.todo.Logic.DateCategory;
import com.the.todo.command.CommandStatus;
import com.the.todo.command.CommandStatus.Status;
import com.the.todo.model.ToDo;
import com.the.todo.task.ReminderTask;

public class MainToDoController {

	@FXML
	private BorderPane mainPane;
	@FXML
	private Pane appTitleIcon;
	@FXML
	private Button minimizeButton;
	@FXML
	private ScrollPane mainScrollpane;
	@FXML
	private VBox mainVBox;
	@FXML
	private Label promptLabel;
	@FXML
	private TextField mainInput;
	@FXML
	private Label hintLabel;

	private Logic appLogic = Logic.getInstance();
	private CommandHistory commandHistory = new CommandHistory();
	private FadeTransition fadeOut;
	private ArrayList<Object> oldItems = new ArrayList<Object>();

	/************************ ALL HANDLERS ***************************/
	private EventHandler<KeyEvent> ctrlUpHandler;
	private EventHandler<KeyEvent> ctrlDownHandler;
	private EventHandler<KeyEvent> ctrlZHandler;
	private EventHandler<KeyEvent> upHandler;
	private EventHandler<KeyEvent> downHandler;
	private ChangeListener<Boolean> mainInputFocusListener;
	private ChangeListener<String> inputChangeListener;

	// @author A0119764W
	@FXML
	void initialize() {
		mainPane.applyCss();
		mainPane.layout();

		mainScrollpane.setFitToWidth(true);
		appTitleIcon.minWidthProperty().bind(appTitleIcon.heightProperty());

		fadeOut = new FadeTransition(Duration.millis(1500), promptLabel);
		fadeOut.setDelay(Duration.millis(2000));

		initilizeHandlers();
		mainPane.addEventFilter(KeyEvent.KEY_PRESSED, ctrlUpHandler);
		mainPane.addEventFilter(KeyEvent.KEY_PRESSED, ctrlDownHandler);
		mainPane.addEventFilter(KeyEvent.KEY_PRESSED, ctrlZHandler);

		mainInput.addEventFilter(KeyEvent.KEY_PRESSED, upHandler);
		mainInput.addEventFilter(KeyEvent.KEY_PRESSED, downHandler);
		mainInput.focusedProperty().addListener(mainInputFocusListener);
		mainInput.textProperty().addListener(inputChangeListener);

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				mainInput.requestFocus();
				updateUI(appLogic.getToDoMapDisplay());
				oldItems.addAll(mapToList(appLogic.getToDoMapDisplay()));
				scrollToToday();
				generateAllReminders();
			}
		});
	}

	// @author A0112969W
	/**
	 * Processes user input upon enter pressed.
	 */
	public void processInput() {
		String userInput = mainInput.getText();
		if (userInput == null || userInput.equals("")) {
			return;
		}

		commandHistory.add(userInput);

		processInput(userInput);
	}

	public void processInput(String userInput) {
		CommandStatus status = appLogic.processCommand(userInput);

		switch (status.getStatus()) {
		case SUCCESS:
			Map<DateCategory, List<ToDo>> newDisplayMap = appLogic
					.getToDoMapDisplay();
			List<Object> newDisplayList = mapToList(newDisplayMap);

			switch (appLogic.getLastCommand()) {
			/* SINGLE CHANGES COMMAND */
			case ADD:
			case EDIT:
			case COMPLETE:
			case INCOMPLETE:
			case UNDO:
				ToDo lastChangedToDo = appLogic.getLastChangedToDo();
				int changedPosition = newDisplayList.indexOf(lastChangedToDo);

				updateUI(newDisplayMap);
				if (changedPosition != -1) {
					scrollToIndex(changedPosition);
					highlightItem(changedPosition);
				}

				break;

			case DELETE:
				updateUI(newDisplayMap);
				showPrompt("Deleted successfully", Status.SUCCESS);
				break;

			/* COMMAND THAT GIVES CUSTOMIZED VIEW */
			case VIEW:
			case SEARCH:
				updateUI(newDisplayMap);
				scrollToIndex(0);
				break;

			case INVALID:
			default:
				break;
			}

			updateOldItems(newDisplayList);

			break;

		case ERROR:
			// Fallthrough

		case INVALID:
			// Fallthrough

		default:
			showPrompt(status.getMessage(), Status.INVALID);
			break;
		}
	}

	// @author A0119764W
	private void updateOldItems(List<Object> newDisplayList) {
		oldItems.clear();
		oldItems.addAll(newDisplayList);
	}

	// @author A0119764W
	private void highlightItem(int index) {
		mainVBox.getChildren().get(index)
				.setStyle("-fx-background-color: #E8F5E9;");
	}

	// @author A0112969W
	public void showPrompt(String str, Status status) {
		if (status.equals(Status.SUCCESS)) {
			promptLabel.setStyle("-fx-background-color: #43A047");
		} else {
			promptLabel.setStyle("-fx-background-color: #B71C1C");
		}

		if (!str.isEmpty()) {
			promptLabel.setText(str);

			Platform.runLater(new Runnable() {
				public void run() {
					promptLabel.setOpacity(1);
					fadeOut.setToValue(0.0);
					fadeOut.playFromStart();
				}
			});
		}
	}

	// @author A0119764W
	public void clearUI() {
		mainInput.clear();
		mainVBox.getChildren().clear();
	}

	// @author A0112969W
	public void updateUI(Map<DateCategory, List<ToDo>> todoItems) {
		clearUI();

		int index = 1;
		ObservableList<Node> newVBoxItems = mainVBox.getChildren();

		if (todoItems == null || todoItems.isEmpty()) {
			Label temp = new Label("No items to show.");
			newVBoxItems.add(temp);
		} else {
			Label lblDate;

			for (Entry<DateCategory, List<ToDo>> entry : todoItems.entrySet()) {
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

	// @author A0112969W
	/**
	 * Listens the change in CheckedProperty of the check box of each task and
	 * passes it as a command.
	 * 
	 * @param ToDoContainer
	 *            which is a collection of fxml components of each task
	 */
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

	// @author A0119764W
	public void minimizeWindow() {
		Stage stage = (Stage) minimizeButton.getScene().getWindow();
		stage.hide();
	}

	// @author A0119764W
	public Label createGroupLabel(DateCategory date) {
		Label label = new Label(date.toString());

		if (date.equals(DateCategory.OVERDUE)) {
			label.setStyle("-fx-background-color: #FF5050;");
		}

		label.getStyleClass().add("groupLabel");
		label.setMaxWidth(Double.MAX_VALUE);

		return label;
	}

	// @author A0111815R
	private void generateAllReminders() {
		LocalDateTime currentDateTime = new LocalDateTime();
		LocalDateTime tomorrowDateTime = new LocalDateTime().plusDays(1);

		for (ToDo todo : appLogic.getTodoStorage().getAll()) {
			if (todo.isDeadlineToDo()) {
				if (todo.getEndDate().isBefore(tomorrowDateTime)
						&& todo.getEndDate().isAfter(currentDateTime)) {
					new ReminderTask(todo, currentDateTime);
				}
			}
		}
	}

	// @author A0119764W
	private void scrollToIndex(int index) {
		mainScrollpane.applyCss();
		mainScrollpane.layout();

		double vBoxHeight = mainVBox.getHeight();
		double scrollPaneHeight = mainScrollpane.getViewportBounds()
				.getHeight();

		double yValue = mainVBox.getChildren().get(index).getLayoutY();
		double vValue = 0;

		if (vBoxHeight > scrollPaneHeight) {
			vValue = yValue / (vBoxHeight - scrollPaneHeight);
		}

		mainScrollpane.setVvalue(vValue);
	}

	// @author A0119764W
	/**
	 * Converts a map into a list.
	 * 
	 * @param map
	 * @return a list of items in the map.
	 */
	private List<Object> mapToList(Map<DateCategory, List<ToDo>> map) {
		List<Object> list = new ArrayList<Object>();

		for (Entry<DateCategory, List<ToDo>> entry : map.entrySet()) {
			list.add(entry.getKey());
			for (ToDo todo : entry.getValue()) {
				list.add(todo);
			}
		}
		return list;
	}

	// @author A0119764W
	private void scrollToToday() {
		int todayIndex = oldItems.indexOf(DateCategory.TODAY);
		if (todayIndex == -1) {
			todayIndex = 0;
		}
		scrollToIndex(todayIndex);
	}

	// @author A0112969W
	private void initilizeHandlers() {
		ctrlUpHandler = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyevent) {
				if (keyevent.getCode().equals(KeyCode.UP)
						&& keyevent.isControlDown()) {
					mainScrollpane.setVvalue(mainScrollpane.getVvalue() - 0.1);
				}
			}
		};

		ctrlDownHandler = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyevent) {
				if (keyevent.getCode().equals(KeyCode.DOWN)
						&& keyevent.isControlDown()) {
					mainScrollpane.setVvalue(mainScrollpane.getVvalue() + 0.1);
				}
			}
		};

		ctrlZHandler = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyevent) {
				if (keyevent.getCode().equals(KeyCode.Z)
						&& keyevent.isControlDown()) {
					processInput("undo");
				}
			}
		};

		upHandler = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyevent) {
				if (keyevent.getCode().equals(KeyCode.UP)
						&& !keyevent.isControlDown()) {
					String prevCmd = commandHistory.previous();
					if (prevCmd != null) {
						mainInput.setText(prevCmd);
					}
					mainInput.end();
					keyevent.consume();
				}
			}
		};

		downHandler = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyevent) {
				if (keyevent.getCode().equals(KeyCode.DOWN)
						&& !keyevent.isControlDown()) {
					String nextCmd = commandHistory.next();
					if (nextCmd != null) {
						mainInput.setText(nextCmd);
					} else {
						mainInput.setText("");
					}
					mainInput.end();
					keyevent.consume();
				}
			}
		};

		mainInputFocusListener = new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {

				if (oldValue == true && newValue == false) {
					mainInput.requestFocus();
				}
			}
		};

		inputChangeListener = new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> ov,
					String oldStr, String newStr) {
				ToDoHint hint = new ToDoHint(appLogic, newStr);
				String str = hint.getHints();
				hintLabel.setText(str);
			}
		};
	}
}

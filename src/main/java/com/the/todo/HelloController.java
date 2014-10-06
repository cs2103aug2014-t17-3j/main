package com.the.todo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import com.the.todo.command.CommandStatus;
import com.the.todo.model.ToDo;
import com.the.todo.parser.CommandParser;
import com.the.todo.storage.InMemoryStore;

public class HelloController {

	private static final KeyCode[] RESERVED_KEYS = { KeyCode.UP, KeyCode.DOWN,
			KeyCode.LEFT, KeyCode.RIGHT };

	@FXML
	private Label mainLabel;
	@FXML
	private VBox mainVBox;
	@FXML
	private TextField mainInput;

	private InMemoryStore memstore = new InMemoryStore();

	@FXML
	void initialize() {
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

	public void processInput() {
		String userInput = mainInput.getText();

		mainInput.clear();
		mainVBox.getChildren().clear();

		CommandStatus status = CommandParser
				.processCommand(memstore, userInput);
		updateUI(status.getMessage());
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
	public void updateUI(String label, Collection<ToDo> todoItems) {
		updateUI(label);

		ArrayList<Label> itemsList = new ArrayList<Label>();

		for (ToDo todo : todoItems) {
			Label temp = new Label(todo.toString());
			itemsList.add(temp);
		}
		mainVBox.getChildren().setAll(itemsList);
	}

	public void processKeyEvents(KeyEvent keyevent) {
		for (KeyCode reservedKeyCode : RESERVED_KEYS) {
			if (keyevent.getCode() == reservedKeyCode) {

				// TODO Implement actions for reserved keys

				keyevent.consume();
				break;
			}
		}
	}
}
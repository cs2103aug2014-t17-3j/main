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
import com.the.todo.storage.InMemoryStore;

public class MainToDoController {

	private static final KeyCode[] RESERVED_KEYS = { KeyCode.UP, KeyCode.DOWN,
			KeyCode.LEFT, KeyCode.RIGHT };

	@FXML
	private Label mainLabel;
	@FXML
	private VBox mainVBox;
	@FXML
	private TextField mainInput;

	private static Logic appLogic;

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

		appLogic = new Logic();
	}

	public void processInput() {
		String userInput = mainInput.getText();

		mainInput.clear();
		mainVBox.getChildren().clear();

		CommandStatus status = appLogic.processCommand(userInput);
		updateUI(status.getMessage(), appLogic.getTodoCollection());
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

		if (todoItems.isEmpty()) {
			return;
		}

		ArrayList<ToDoContainer> itemsList = new ArrayList<ToDoContainer>();

		for (ToDo todo : todoItems) {
			try {
				ToDoContainer temp = new ToDoContainer(todo);
				itemsList.add(temp);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
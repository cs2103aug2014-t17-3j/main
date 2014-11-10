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

import java.io.IOException;
import java.util.ArrayList;

import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;

import com.the.todo.model.ToDo;
import com.the.todo.model.ToDo.Priority;
import com.the.todo.model.ToDo.Type;

public class ToDoContainer extends AnchorPane {
	private static final String DATE_PATTERN = "dd-MM-yy";
	private static final String TIME_PATTERN = "h:mm a";

	private ToDo todo;
	@FXML
	private Label todoID;
	@FXML
	private Label todoTitle;
	@FXML
	private HBox todoDate;
	@FXML
	private Label todoMisc;
	@FXML
	private CheckBox completeChkBox;
	@FXML
	private HBox hbox = new HBox();

	private int id;

	// @author A0112969W
	public ToDoContainer() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
				"/fxml/todoContainer.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();

		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	public ToDoContainer(int id, ToDo todo) throws Exception {
		this();
		this.todo = todo;
		this.id = id;
		if (!isValidTodo(todo)) {
			throw new Exception("Invalid todo");
		} else {
			setID(id);
			setTitle(todo);
			setDate(todo);
			setCategory(todo.getCategory());
			setComplete(todo.isCompleted());
			setType();
			setPriority();
		}

	}

	// @author A0112969W
	private void setID(int id) {
		todoID.setText(String.valueOf(id));
	}

	// @author A0112969W
	private void setTitle(ToDo todo) {
		todoTitle.setText(todo.getTitle());
	}

	// @author A0119764W
	private void setDate(ToDo todo) {
		Type type = todo.getType();
		String startDate = null;
		String startTime = null;
		String endDate = null;
		String endTime = null;
		ArrayList<Node> displayItems = new ArrayList<Node>();

		if (type == Type.TIMED) {
			startDate = todo.getStartDate().toString(DATE_PATTERN);
			startTime = todo.getStartDate().toString(TIME_PATTERN);
			endDate = todo.getEndDate().toString(DATE_PATTERN);
			endTime = todo.getEndDate().toString(TIME_PATTERN);
		} else if (type == Type.DEADLINE) {
			endDate = todo.getEndDate().toString(DATE_PATTERN);
			endTime = todo.getEndDate().toString(TIME_PATTERN);
		}

		if (startDate != null && startTime != null) {
			ImageView calendar1 = new ImageView("/images/calendar.png");
			ImageView clock1 = new ImageView("/images/clock.png");

			displayItems.add(calendar1);
			displayItems.add(createDateText(startDate));
			displayItems.add(clock1);
			displayItems.add(createDateText(startTime));
			displayItems.add(new Text(" to "));
		}

		if (endDate != null && endTime != null) {
			ImageView calendar2 = new ImageView("/images/calendar.png");
			ImageView clock2 = new ImageView("/images/clock.png");
			displayItems.add(calendar2);
			displayItems.add(createDateText(endDate));
			displayItems.add(clock2);
			displayItems.add(createDateText(endTime));
		}
		todoDate.getChildren().setAll(displayItems);
	}

	// @author A0112969W
	private void setCategory(String category) {
		if (category != null)
			todoMisc.setText(category);
	}

	private void setComplete(Boolean isCompleted) {
		completeChkBox.setSelected(isCompleted);
		if (todo.isTimedToDo())
			completeChkBox.setDisable(true);
	}

	// @author A0112969W
	/**
	 * Add a label indicating the type of task behind the title.
	 */
	private void setType() {
		Type type = todo.getType();
		Node typeTag;

		if (type.equals(Type.DEADLINE)) {
			typeTag = createTag("Deadline", "lightgreen");
		} else if (type.equals(Type.FLOATING)) {
			typeTag = createTag("Floating", "yellow");
		} else {
			typeTag = createTag("Timed", "lightblue");
		}

		todoTitle.setContentDisplay(ContentDisplay.RIGHT);
		hbox.getChildren().add(typeTag);
		todoTitle.setGraphic(hbox);
	}

	// @author A0119764W
	/**
	 * Add a label indicating the priority of task behind the title.
	 */
	private void setPriority() {
		Priority priority = todo.getPriority();

		// tag design
		Node priorityTag;
		if (priority == Priority.HIGH) {
			priorityTag = createTag("HIGH", "red");
		} else if (priority == Priority.MEDIUM) {
			priorityTag = createTag("MEDIUM", "blue");
		} else {
			priorityTag = createTag("LOW", "pink");
		}
		hbox.getChildren().add(priorityTag);
		hbox.setSpacing(5);
	}

	// @author A0112969W
	/**
	 * Creates a node object to be used to display priority and type of the
	 * ToDo.
	 * 
	 * @param text
	 * @param color
	 * @return A label node with background color of the parameter color and
	 *         relative text color
	 *
	 */
	private Node createTag(String text, String color) {
		Label tag = new Label(text);

		tag.setStyle("-fx-background-color:" + color + ";"
				+ "-fx-background-radius: 1em;" + "-fx-font-size: 0.95em;"
				+ "-fx-padding: 0 5 0 5 ;" + "background:" + color + ";"
				+ "-fx-text-fill: ladder(background, white 49%, black 50%);");
		return tag;
	}

	// @author A0119764W
	/**
	 * Checks whether a given ToDo object is valid. A ToDo is valid only if its
	 * Title is not null
	 * 
	 * @param todo
	 * @return
	 */
	private boolean isValidTodo(ToDo todo) {
		if (todo.getTitle() == null) {
			return false;
		}

		return true;
	}

	private Text createDateText(String startDate) {
		return TextBuilder.create().text(startDate).styleClass("DateTime")
				.build();
	}

	// @author A0112969W
	public BooleanProperty getCheckedProperty() {
		return completeChkBox.selectedProperty();

	}

	// @author A0112969W
	public int getID() {
		return id;
	}

	// @author A0119764W
	public ToDo getToDo() {
		return this.todo;
	}

	// @author A0119764W generated
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ToDoContainer other = (ToDoContainer) obj;
		if (todo == null) {
			if (other.todo != null)
				return false;
		} else if (!todo.equals(other.todo))
			return false;
		if (todo.isCompleted() != other.todo.isCompleted())
			return false;
		return true;
	}

}

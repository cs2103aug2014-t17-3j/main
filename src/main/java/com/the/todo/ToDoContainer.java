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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import org.joda.time.LocalDateTime;

import com.the.todo.model.ToDo;

public class ToDoContainer extends AnchorPane {

	@FXML
	private Label todoID;
	@FXML
	private Label todoTitle;
	@FXML
	private Label todoDate;
	@FXML
	private Label todoMisc;
	@FXML 
	private CheckBox completeChkBox; 

	public ToDoContainer() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
				"/fxml/todoContainer.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		//super.setPrefWidth(1000);
		

		try {
			fxmlLoader.load();
			
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	public ToDoContainer(int id, ToDo todo) throws Exception {
		this();
		if (!isValidTodo(todo)) {
			throw new Exception("Invalid todo");
		} else {
			setID(id);
			setTitle(todo.getTitle());
			setDate(todo.getEndDate());
			setComplete(todo.isCompleted());
		}

	}

	private void setID(int id) {
		todoID.setText(String.valueOf(id));
	}

	private void setTitle(String title) {
		todoTitle.setText(title);
	}

	private void setDate(LocalDateTime date) {
		if (date != null)
			todoDate.setText(date.toString());
	}

	private void setMisc(String misc) {
		todoMisc.setText(misc);
	}
	private void setComplete(Boolean isCompleted){
		if(isCompleted){
			completeChkBox.setSelected(true);
			this.setDisable(true);
		} 
		else 
			completeChkBox.setSelected(false);
	}

	/**
	 * Checks whether a given ToDo object is valid. A ToDo is valid only if its
	 * ID and Title is not null
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
	
}

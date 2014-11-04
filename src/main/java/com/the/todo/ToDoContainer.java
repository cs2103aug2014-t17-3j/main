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

import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import com.the.todo.model.ToDo;
import com.the.todo.model.ToDo.Priority;
import com.the.todo.model.ToDo.Type;

public class ToDoContainer extends AnchorPane {

	private ToDo todo;
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



	private int id; 
	HBox hbox = new HBox();

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

	private void setID(int id) {
		todoID.setText(String.valueOf(id));
	}

	private void setTitle(ToDo todo) {

		todoTitle.setText(todo.getTitle());		


	}

	private void setDate(ToDo todo){
		LocalDateTime startDate,endDate; 
		startDate = todo.getStartDate();
		endDate = todo.getEndDate();

		if (todo.isDeadlineToDo()){
			todoDate.setText(formatDate(endDate)); 
		}
		if (todo.isTimedToDo()){
			todoDate.setText(formatDate(endDate)); 
		}


	}

	private Node createTag(String text, String color){
		Label tag= new Label(text);

		tag.setStyle("-fx-background-color:"+ color+ ";"
				+ "-fx-background-radius: 1em;"
				+"-fx-font-size: 10;"
				+ "-fx-padding: 0 5 0 5 ;"
				);
		return tag;
	}




	private String formatDate(LocalDateTime date){ 
		//String dateStr = date.toLocalDate().toString(DateTimeFormat.forPattern("dd MMMM yyyy"));
		String timeStr = date.toLocalTime().toString(DateTimeFormat.forPattern("hh:mm aa"));
		return "Time: "+ timeStr;
	}

	private void setCategory(String category) {
		if (category != null)
			todoMisc.setText(category);
	}

	private void setComplete(Boolean isCompleted){
		if(isCompleted){
			completeChkBox.setSelected(true);
		} 
		else{ 
			completeChkBox.setSelected(false);
		}
	}

	private void setType(){
		Type type = todo.getType();
		Node typeTag ;

		if (type.equals(Type.DEADLINE)){ 
			typeTag=createTag("Deadline","lightgreen");
		}
		else if (type.equals(Type.FLOATING)){ 
			typeTag= createTag("Floating","yellow");			
		}
		else {
			typeTag=createTag("Timed","lightblue");
		}

		todoTitle.setContentDisplay(ContentDisplay.RIGHT);
		hbox.getChildren().add(typeTag);
		todoTitle.setGraphic(hbox);

	}
	private void setPriority(){

		//Solid border design 
		Priority priority = todo.getPriority();
		switch (priority){
		case HIGH:
			todoID.setStyle("-fx-border-color: red;");		
			break;
		case LOW:
			todoID.setStyle("-fx-border-color: black;");
			break;
		case MEDIUM:
			todoID.setStyle("-fx-border-color: blue;");
			break;
		}

		//		//tag design		
		//			Node priorityTag; 
		//			if (priority == Priority.HIGH)
		//				priorityTag= createTag("HIGH","red");				
		//			else if (priority == Priority.MEDIUM)			
		//				priorityTag=createTag("MEDIUM","blue");
		//			else 
		//				priorityTag=createTag("LOW","grey");			
		//			hbox.getChildren().add(tag);
		//			hbox.setSpacing(5);		
		//					
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

	public BooleanProperty getCheckedProperty(){
		return completeChkBox.selectedProperty(); 

	}
	public int getID(){ 
		return id; 
	}

	public ToDo getToDo (){
		return this.todo;
	}

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
		return true;
	}

}

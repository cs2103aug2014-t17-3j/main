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

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.imageio.ImageIO;

public class App extends Application {
	
	
	private static final String MAIN_FXML = "/fxml/MainToDo.fxml";
	private static final String MAIN_STYLE = "/styles/styles.css";
	private static final String ICON_TRAY = "/images/ic_thetodo.jpg";
	private static final int MAIN_FRAME_HEIGHT = 800;
	private static final int MAIN_FRAME_WIDTH = 600;
	
	private Stage primaryStage;

	public static void main(String[] args) throws Exception {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		this.primaryStage = stage;
		Platform.setImplicitExit(false);
		javax.swing.SwingUtilities.invokeLater(this::addAppToTray);
		
		primaryStage.initStyle(StageStyle.UNDECORATED);

		FXMLLoader loader = new FXMLLoader();
		Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream(
				MAIN_FXML));

		Scene scene = new Scene(rootNode, MAIN_FRAME_HEIGHT, MAIN_FRAME_WIDTH);
		scene.getStylesheets().add(MAIN_STYLE);

		final MainToDoController control = loader.getController();
		scene.addEventFilter(KeyEvent.ANY, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				control.processKeyEvents(event);
			}
		});
		
		primaryStage.focusedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
		        if (t && !t1) {
		        	//primaryStage.setIconified(true);
		        	primaryStage.hide();
		        }
		    }
		});

		primaryStage.setTitle("TheTODO");
		primaryStage.setScene(scene);
		primaryStage.show();

	}
	
	private void addAppToTray() {
		try {
			java.awt.Toolkit.getDefaultToolkit();

			if (!java.awt.SystemTray.isSupported()) {
				System.out.println("System tray is not supported.");
				Platform.exit();
			}

			java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();
			java.awt.Image image = ImageIO.read(getClass().getResourceAsStream(
					ICON_TRAY));
			java.awt.TrayIcon trayIcon = new java.awt.TrayIcon(image);

			trayIcon.addActionListener(event -> Platform
					.runLater(this::showStage));

			java.awt.MenuItem openItem = new java.awt.MenuItem("hello, world");
			openItem.addActionListener(event -> Platform
					.runLater(this::showStage));

			java.awt.Font defaultFont = java.awt.Font.decode(null);
			java.awt.Font boldFont = defaultFont.deriveFont(java.awt.Font.BOLD);
			openItem.setFont(boldFont);

			java.awt.MenuItem exitItem = new java.awt.MenuItem("Quit");
			exitItem.addActionListener(event -> {
				Platform.exit();
				tray.remove(trayIcon);
			});

			final java.awt.PopupMenu popup = new java.awt.PopupMenu();
			popup.add(openItem);
			popup.addSeparator();
			popup.add(exitItem);
			trayIcon.setPopupMenu(popup);

			tray.add(trayIcon);
		} catch (java.awt.AWTException | IOException e) {
			System.out.println("Unable to init system tray");
			e.printStackTrace();
		}
	}

	private void showStage() {
		if (primaryStage != null) {
			primaryStage.show();
			primaryStage.toFront();
		}
	}
}

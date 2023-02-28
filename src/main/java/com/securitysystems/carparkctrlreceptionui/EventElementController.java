package com.securitysystems.carparkctrlreceptionui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class EventElementController {

	protected Scene applicationScene;
	protected boolean isMonitoringView;

	// so that this controller has access to the main scene
	public void setApplicationScene(Scene scene) {
		this.applicationScene = scene;
	}
	public void setIsMonitoringView(boolean isMonitoringView) {
		this.isMonitoringView = isMonitoringView;
	}
	@FXML
	public void handleClick(MouseEvent event) {
		System.out.println("EVENT ELEM CLICKED");
		// change colour and load event data in UI
		System.out.println(applicationScene);

		Node eventElement = event.getPickResult().getIntersectedNode();
		// set eventElement to the correct parent with the userData
		while (eventElement.getId() == null || !eventElement.getId().equals("event-element")) {
			eventElement = eventElement.getParent();
		}

		EventElement.selectInScrollpane((AnchorPane) eventElement, this.isMonitoringView, this.applicationScene);
	}
}

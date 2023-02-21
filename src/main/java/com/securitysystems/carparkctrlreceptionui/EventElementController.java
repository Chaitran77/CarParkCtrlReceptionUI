package com.securitysystems.carparkctrlreceptionui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class EventElementController {

	@FXML
	protected void handleClick(MouseEvent event) {
		System.out.println("EVENT ELEM CLICKED");
//		change colour and load event data in UI
//		GridPane eventElement = (GridPane) event.getSource();
//		GridPane eventElement = (GridPane) event.getPickResult().getIntersectedNode();
//		eventElement.setStyle("-fx-background-color:yellow;");
	}
}

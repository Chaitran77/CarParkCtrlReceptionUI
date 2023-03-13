package com.securitysystems.carparkctrlreceptionui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class CarparkMonitoringController {

	public static Scene applicationScene;
	public void setApplicationScene(Scene scene) {
		CarparkMonitoringController.applicationScene = scene;
	}

	@FXML
	protected void handleOpenGateButton() {
		System.out.println("open gate button clicked");
		Alert confimation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to open the gate for...?\n\n");
		confimation.show();
	}

	@FXML
	protected void handleSearchButtonClick() {
		SearchUtils.performSearch(CarparkMonitoringController.applicationScene);
	}

	@FXML
	protected void changeMonitoringSortType() throws IOException {
		ObservableList<Node> eventElements = ((VBox) applicationScene.lookup("#events-container")).getChildren();
		Log[] logs = new Log[eventElements.size()];

		for (int i = 0; i < eventElements.size(); i++) {
			Log eventLog = (Log) eventElements.get(i).getUserData();
			logs[i] = eventLog;
		}

		Button changeMonitoringSortButton = (Button) applicationScene.lookup("#sort-monitoring-scrollpane-button");

		if (changeMonitoringSortButton.getText().equals("Sort by: Entry time, descending")) {
			SearchUtils.performSort(logs, "entry_timestamp", "ascending");
			changeMonitoringSortButton.setText("Sort by: Entry time, ascending");
		} else if (changeMonitoringSortButton.getText().equals("Sort by: Entry time, ascending")) {
			SearchUtils.performSort(logs, "exit_timestamp", "descending");
			changeMonitoringSortButton.setText("Sort by: Exit time, descending");
		} else if (changeMonitoringSortButton.getText().equals("Sort by: Exit time, descending")) {
			SearchUtils.performSort(logs, "exit_timestamp", "ascending");
			changeMonitoringSortButton.setText("Sort by: Exit time, ascending");
		} else if (changeMonitoringSortButton.getText().equals("Sort by: Exit time, ascending")) {
			SearchUtils.performSort(logs, "entry_timestamp", "descending");
			changeMonitoringSortButton.setText("Sort by: Entry time, descending");
		}

		LabelSetters.clearEventsContainer(true, applicationScene);
		for (int i = 0; i < logs.length; i++) {
			EventElement.loadIntoScrollpane(logs[i], true, false, applicationScene);
		}
	}

}
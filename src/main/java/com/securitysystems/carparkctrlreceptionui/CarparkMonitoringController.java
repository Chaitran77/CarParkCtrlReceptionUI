package com.securitysystems.carparkctrlreceptionui;

import javafx.fxml.FXML;
import javafx.scene.Scene;

public class CarparkMonitoringController {

	public static Scene applicationScene;
	public void setApplicationScene(Scene scene) {
		CarparkMonitoringController.applicationScene = scene;
	}

	@FXML
	protected void handleOpenGateButton() {
		System.out.println("open gate button clicked");
	}

	@FXML
	protected void handleSearchButtonClick() {
		SearchUtils.performSearch(CarparkMonitoringController.applicationScene);
	}

}
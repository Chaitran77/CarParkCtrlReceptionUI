package com.securitysystems.carparkctrlreceptionui;

import javafx.fxml.FXML;
import javafx.scene.Scene;

public class CarparkMonitoringController {

	public Scene monitoringScene;
	public void setMonitoringScene(Scene scene) {
		this.monitoringScene = scene;
	}

	@FXML
	protected void handleOpenGateButton() {
		System.out.println("open gate button clicked");
	}

	@FXML
	protected void handleSearchButtonClick() {
		SearchUtils.performSearch();
	}
}
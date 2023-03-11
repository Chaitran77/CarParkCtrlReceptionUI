package com.securitysystems.carparkctrlreceptionui;

import javafx.scene.Scene;

public class Settings {

	public static int getUpdateInterval(Scene applicationScene) {
		return Integer.parseInt(((IntegerInputField) applicationScene.lookup("#updateIntervalInput")).getText());
	}

	public static void setUpdateInterval(Scene applicationScene, int updateIntervalSeconds) {
		((IntegerInputField) applicationScene.lookup("#updateIntervalInput")).setText(String.valueOf(updateIntervalSeconds));
	}

	public static int getNumberEventsToFetchForMonitoring(Scene applicationScene) {
		return Integer.parseInt(((IntegerInputField) applicationScene.lookup("#numberEventsToFetchForMonitoringInput")).getText());
	}

	public static void setNumberEventsToFetchForMonitoring(Scene applicationScene, int numberOfEvents) {
		((IntegerInputField) applicationScene.lookup("#numberEventsToFetchForMonitoringInput")).setText(String.valueOf(numberOfEvents));
	}

	public static int getNumberEventsToSearchFrom(Scene applicationScene) {
		return Integer.parseInt(((IntegerInputField) applicationScene.lookup("#numberEventsToSearchFromInput")).getText());
	}

	public static void setNumberEventsToSearchFrom(Scene applicationScene, int numberOfEvents) {
		((IntegerInputField) applicationScene.lookup("#numberEventsToSearchFromInput")).setText(String.valueOf(numberOfEvents));
	}
}

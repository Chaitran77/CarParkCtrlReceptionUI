package com.securitysystems.carparkctrlreceptionui;

/*
what EventElement would be if AnchorPane could be subclassed

public class EventElement extends AnchorPane {
	public final Log log;
	public final AnchorPane eventElement;

	public EventElement(Log logObject) throws IOException {
		super();

		this.log = logObject;
		this.eventElement = FXMLLoader.load(getClass().getResource("/carpark-monitoring-view/EventElement.fxml"));

		((Label) this.eventElement.lookup("#numberplate-label")).setText((this.log.Numberplate!=null)?(this.log.Numberplate):("Unknown"));
		((Label) this.eventElement.lookup("#entry-timestamp-label")).setText((this.log.EntryTimestamp!=null)?(this.log.EntryTimestamp.toString()):("Unknown"));
		((Label) this.eventElement.lookup("#exit-timestamp-label")).setText((this.log.ExitTimestamp!=null)?(this.log.ExitTimestamp.toString()):("Unknown"));
	}
}
*/

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class EventElement {
	// class for the following method for use on click and focus events
	public static void selectInScrollpane(AnchorPane eventElement, Scene monitoringScene) {
		Log log = (Log) eventElement.getUserData();

		System.out.println(log);
		LabelSetters.resetEventElements(monitoringScene);
		eventElement.lookup("#event-element-grid").setStyle("-fx-background-color: -blue-bg-color;");

		LabelSetters.setCurrentLogSectionLabels(log, monitoringScene, true);
		LabelSetters.setSnapshotImages(log, monitoringScene);
	}
}

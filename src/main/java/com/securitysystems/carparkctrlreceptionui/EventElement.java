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

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class EventElement {
	// class for the following method for use on click and focus events
	public static void selectInScrollpane(AnchorPane eventElement, boolean isMonitoringScrollpane, Scene applicationScene) {
		Log log = (Log) eventElement.getUserData();

		System.out.println(log);
		System.out.println(isMonitoringScrollpane);

		LabelSetters.resetEventElementStyles(applicationScene, isMonitoringScrollpane); // set all elements to base style
		eventElement.lookup("#event-element-grid").setStyle("-fx-background-color: -event-selected-color;"); // modify (highlight) style of this element
		LabelSetters.setSnapshotImages(log, isMonitoringScrollpane, applicationScene);

		if (isMonitoringScrollpane) {
			LabelSetters.MonitoringView.setCurrentLogSectionLabels(log, applicationScene, true);
		} else {
			LabelSetters.SearchView.setCurrentLogSectionLabels(log, applicationScene);
		}
	}

	public static void loadIntoScrollpane(Log log, boolean isMonitoringScrollpane, boolean isLatestEvent, Scene applicationScene) throws IOException {
		// original plan:
		// To prevent use of a global array to store the current Log objects, an extended AnchorPane, EventElement, will be responsible for its own Log object containing the information of the Log it represents
		// then when the user clicks on an EventElement, the Log is used to populate the labels in the window to show its information (times, numberplate, images, etc...)
		// however, due to the bad design of JavaFX (https://stackoverflow.com/a/32199349/7169383), it is impossible to subclass components including the AnchorPane.
		// Therefore, the setUserData() method must be used (even though this is not the proper use case)

		FXMLLoader eventElementLoader = new FXMLLoader(EventElement.class.getResource("/carpark-monitoring-view/EventElement.fxml"));
		AnchorPane newEventElement = eventElementLoader.load();

		Node newEventElementGrid = newEventElement.lookup("#event-element-grid");

		newEventElement.focusedProperty().addListener((observableValue, prevState, newState) -> {
//			TODO: DOCUMENT THIS
			if (newState) {
				System.out.println("Focused");
				// handle click
				EventElement.selectInScrollpane(newEventElement, isMonitoringScrollpane, applicationScene);
			} else {
				System.out.println("Unfocused");
				if (!log.KnownVehicle) {
					newEventElementGrid.setStyle("-fx-background-color: -event-unknown-vehicle-color;");
				}
			}
		});

		// set colour styling
		if (!log.KnownVehicle) {
			newEventElementGrid.setStyle("-fx-background-color: -event-unknown-vehicle-color;");
		}
		if (isLatestEvent) {
			newEventElementGrid.setStyle(newEventElementGrid.getStyle() + " -fx-border-width: 3px; -fx-border-color: -event-latest-color;");
		}

		newEventElement.setUserData(log);
		((EventElementController)eventElementLoader.getController()).setApplicationScene(applicationScene); // Referenced this answer on passing parameters to FXML controllers: https://stackoverflow.com/a/30815504/7169383
		((EventElementController)eventElementLoader.getController()).setIsMonitoringView(isMonitoringScrollpane);

		LabelSetters.setEventElementLabels(log, newEventElement);
		((VBox) applicationScene.lookup((isMonitoringScrollpane)?("#events-container"):("#search-events-container"))).getChildren().add(newEventElement);
	}
}

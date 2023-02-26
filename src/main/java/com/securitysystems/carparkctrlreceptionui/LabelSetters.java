package com.securitysystems.carparkctrlreceptionui;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.time.Duration;
import java.time.Instant;

public class LabelSetters {

	// Labels and views are looked up (Label().lookup()) when they need to be set, never stored to keep memory footprint low
	public static void setEventElementLabels(Log log, AnchorPane eventElement) {
		// not storing label elements in variables since they are only used once
		// ternary conditionals used here to evaluate event's properties and pass the correct value to setText() - condensed 15 lines to 3
		/*  original code for 1 element:
	        if (event.Numberplate == null) {
	            ((Label) newEventElement.lookup("#numberplate-label")).setText("Unknown");
	        } else {
	            ((Label) newEventElement.lookup("#numberplate-label")).setText(event.Numberplate);
	        }
		*/
		((Label) eventElement.lookup("#numberplate-label")).setText((log.Numberplate!=null)?(log.Numberplate):("Unknown"));
		((Label) eventElement.lookup("#entry-timestamp-label")).setText((log.EntryTimestamp!=null)?(log.EntryTimestamp.toString()):("Unknown"));
		((Label) eventElement.lookup("#exit-timestamp-label")).setText((log.ExitTimestamp!=null)?(log.ExitTimestamp.toString()):("Unknown"));
	}
	public static void setCurrentLogSectionLabels(Log log, Scene monitoringScene, boolean isSelectedEvent) {
		((Label) monitoringScene.lookup("#current-data-heading")).setText((isSelectedEvent)?("Selected Event: "):("Last Detection: "));
		((Label) monitoringScene.lookup("#last-event-numberplate-label")).setText((log.Numberplate!=null)?(log.Numberplate):("Unknown"));


		if (log.EntryTimestamp == null && log.ExitTimestamp == null) {
			((Label) monitoringScene.lookup("#last-event-elapsed-time-label")).setText("Unknown entry and exit time");
			return;
		}
		// SOURCE - documentation: https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html#between-java.time.temporal.Temporal-java.time.temporal.Temporal-
		if (log.EntryTimestamp != null) {
			Duration timeElapsed = Duration.between(log.EntryTimestamp.toInstant(), Instant.now());
			((Label) monitoringScene.lookup("#last-event-elapsed-time-label")).setText("Entered " + timeElapsed.toHoursPart() + " hour(s) and " + timeElapsed.toMinutesPart() + " minutes ago");
		} else {
			Duration timeElapsed = Duration.between(log.ExitTimestamp.toInstant(), Instant.now());
			((Label) monitoringScene.lookup("#last-event-elapsed-time-label")).setText("Exited " + timeElapsed.toHoursPart() + " hour(s) and " + timeElapsed.toMinutesPart() + " minutes ago");
		}
	}

	public static void setCarparkStatisticsSectionLabels(Carpark carpark, Scene monitoringScene) {
		((Label) monitoringScene.lookup("#remaining-spaces-label")).setText((carpark.FreeSpaces!=null)?(carpark.FreeSpaces.toString()):("Unknown"));
		((Label) monitoringScene.lookup("#used-spaces-label")).setText((carpark.FreeSpaces!=null&&carpark.TotalSpaces!=null) ? String.valueOf((carpark.TotalSpaces-carpark.FreeSpaces)):("Unknown"));
		((Label) monitoringScene.lookup("#total-spaces-label")).setText((carpark.TotalSpaces!=null)?(carpark.TotalSpaces.toString()):("Unknown"));
	}

	public static void setUILabelsToUnknown(Scene monitoringScene) { // for all UI labels that are dependent on server data
		((Label) monitoringScene.lookup("#remaining-spaces-label")).setText("Unknown");
		((Label) monitoringScene.lookup("#used-spaces-label")).setText("Unknown");
		((Label) monitoringScene.lookup("#total-spaces-label")).setText("Unknown");
		((Label) monitoringScene.lookup("#last-event-numberplate-label")).setText("Unknown");
		((Label) monitoringScene.lookup("#last-event-elapsed-time-label")).setText("Unknown");

		VBox eventsContainer = (VBox) monitoringScene.lookup("#events-container");

		for (int i = 0; i < eventsContainer.getChildren().size(); i++) {
			((Label)eventsContainer.getChildren().get(i).lookup("#numberplate-label")).setText("Unknown");
			((Label)eventsContainer.getChildren().get(i).lookup("#entry-timestamp-label")).setText("Unknown");
			((Label)eventsContainer.getChildren().get(i).lookup("#exit-timestamp-label")).setText("Unknown");
		}
	}

	public static void setSnapshotImages(Log log, Scene monitoringScene) {
		// clear both images
		((ImageView) monitoringScene.lookup("#entry-snapshot-view")).setImage(null);
		((ImageView) monitoringScene.lookup("#exit-snapshot-view")).setImage(null);

		// set whichever images are present
		if (log.EntryImageBase64 != null) {
			((ImageView) monitoringScene.lookup("#entry-snapshot-view")).setImage(new Image("data:image/jpeg;base64," + log.EntryImageBase64));
		}
		if (log.ExitImageBase64 != null) {
			((ImageView) monitoringScene.lookup("#exit-snapshot-view")).setImage(new Image("data:image/jpeg;base64," + log.ExitImageBase64));
		}
	}

}

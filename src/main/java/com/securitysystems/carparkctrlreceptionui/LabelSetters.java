package com.securitysystems.carparkctrlreceptionui;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

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
		((Label) eventElement.lookup("#numberplate-label")).setText((log.Numberplate!=null&&!log.Numberplate.equals(""))?(log.Numberplate):("Unknown"));
		((Label) eventElement.lookup("#entry-timestamp-label")).setText((log.EntryTimestamp!=null)?(LabelSetters.formatDate(log.EntryTimestamp)):("Unknown"));
		((Label) eventElement.lookup("#exit-timestamp-label")).setText((log.ExitTimestamp!=null)?(LabelSetters.formatDate(log.ExitTimestamp)):("Not yet exited."));
	}

	public static void resetEventElementStyles(Scene monitoringScene, boolean isMonitoringView) {
		ObservableList<Node> eventsElements = ((VBox) monitoringScene.lookup((isMonitoringView)?("#events-container"):("#search-events-container"))).getChildren();

		for (int i = 0; i < eventsElements.size(); i++) {
			if (((Log)eventsElements.get(i).getUserData()).KnownVehicle){
				eventsElements.get(i).lookup("#event-element-grid").setStyle("-fx-background-color: -event-known-vehicle-color;");
			} else {
				eventsElements.get(i).lookup("#event-element-grid").setStyle("-fx-background-color: -event-unknown-vehicle-color;");
			}
		}
	}

	public static void setSnapshotImages(Log log, boolean isMonitoringView, Scene applicationScene) {
		// clear both images
		((ImageView) applicationScene.lookup((isMonitoringView)?("#entry-snapshot-view"):("#search-entry-snapshot-view"))).setImage(null);
		((ImageView) applicationScene.lookup((isMonitoringView)?("#exit-snapshot-view"):("#search-exit-snapshot-view"))).setImage(null);

		// set whichever images are present
		if (log.EntryImageBase64 != null) {
			((ImageView) applicationScene.lookup((isMonitoringView)?("#entry-snapshot-view"):("#search-entry-snapshot-view"))).setImage(new Image("data:image/jpeg;base64," + log.EntryImageBase64));
		}
		if (log.ExitImageBase64 != null) {
			((ImageView) applicationScene.lookup((isMonitoringView)?("#exit-snapshot-view"):("#search-exit-snapshot-view"))).setImage(new Image("data:image/jpeg;base64," + log.ExitImageBase64));
		}
	}

	public static void clearEventsContainer(boolean isMonitoringView, Scene applicationScene) {
		VBox eventsContainer = (VBox) applicationScene.lookup((isMonitoringView)?("#events-container"):("#search-events-container"));
		eventsContainer.getChildren().clear();
	}

	public static void setLoadingLabelVisibility(boolean isVisible, Scene applicationScene) {
		applicationScene.lookup("#global-loading-label").setVisible(isVisible);
	}

	public static class MonitoringView {
		public static void setCurrentLogSectionLabels(Log log, Scene monitoringScene, boolean isSelectedEvent) {
			((Label) monitoringScene.lookup("#current-data-heading")).setText((isSelectedEvent)?("Selected Event: "):("Last Detection: "));
			((Label) monitoringScene.lookup("#last-event-numberplate-label")).setText((log.Numberplate!=null&&!log.Numberplate.equals(""))?(log.Numberplate):("Unknown"));
			((Label) monitoringScene.lookup("#is-known-vehicle-label")).setText((log.KnownVehicle)?("Authorised vehicle"):("Unauthorised vehicle"));
			((Label) monitoringScene.lookup("#tenant-label")).setText((log.tenant.Forename == null)?("Unknown tenant"):(log.tenant.Forename + " " + log.tenant.Surname));


			if (log.EntryTimestamp == null && log.ExitTimestamp == null) {
				((Label) monitoringScene.lookup("#last-event-elapsed-time-label")).setText("Unknown entry and exit time");
				return;
			}
			// SOURCE - documentation: https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html#between-java.time.temporal.Temporal-java.time.temporal.Temporal-
			// try and display exit time first since it is always in the future compared to entry. If not exited, display entry time.
			if (log.ExitTimestamp != null) {
				Duration timeElapsed = Duration.between(log.ExitTimestamp.toInstant(), Instant.now());
				((Label) monitoringScene.lookup("#last-event-elapsed-time-label")).setText("Exited " + timeElapsed.toHoursPart() + " hour(s) and " + timeElapsed.toMinutesPart() + " minutes ago");

			} else {
				Duration timeElapsed = Duration.between(log.EntryTimestamp.toInstant(), Instant.now());
				((Label) monitoringScene.lookup("#last-event-elapsed-time-label")).setText("Entered " + timeElapsed.toHoursPart() + " hour(s) and " + timeElapsed.toMinutesPart() + " minutes ago");
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

		public static void setOpenGateButtonState(Scene monitoringScene, boolean disabled) {
			monitoringScene.lookup("#open-gate-button").setDisable(disabled);
		}

	}

	public static class SearchView {
		public static void setCurrentLogSectionLabels(Log log, Scene applicationScene) {

			if (log.EntryTimestamp == null && log.ExitTimestamp == null) {
				((Label) applicationScene.lookup("#last-event-elapsed-time-label")).setText("Unknown entry and exit time");
				return;
			}
			// SOURCE - documentation: https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html#between-java.time.temporal.Temporal-java.time.temporal.Temporal-
			if (log.EntryTimestamp != null) {
				Duration timeElapsed = Duration.between(log.EntryTimestamp.toInstant(), Instant.now());
				((Label) applicationScene.lookup("#last-event-elapsed-time-label")).setText("Entered " + timeElapsed.toHoursPart() + " hour(s) and " + timeElapsed.toMinutesPart() + " minutes ago");
			} else {
				Duration timeElapsed = Duration.between(log.ExitTimestamp.toInstant(), Instant.now());
				((Label) applicationScene.lookup("#last-event-elapsed-time-label")).setText("Exited " + timeElapsed.toHoursPart() + " hour(s) and " + timeElapsed.toMinutesPart() + " minutes ago");
			}

			((Label) applicationScene.lookup("#search-event-id-label")).setText((log.EventID !=null)?(log.EventID.toString()):("Unknown"));
			((Label) applicationScene.lookup("#search-camera-id-label")).setText((log.CameraID !=null)?(log.CameraID.toString()):("Unknown"));
			((Label) applicationScene.lookup("#search-numberplate-label")).setText((log.Numberplate!=null)?(log.Numberplate):("Unknown"));
			((Label) applicationScene.lookup("#search-entry-time-label")).setText((log.EntryTimestamp!=null)?(log.EntryTimestamp.toString()):("Unknown"));
			((Label) applicationScene.lookup("#search-exit-time-label")).setText((log.ExitTimestamp!=null)?(log.ExitTimestamp.toString()):("Unknown"));
			((Label) applicationScene.lookup("#search-known-vehicle-label")).setText((log.KnownVehicle)?("Yes"):("No"));
			((Label) applicationScene.lookup("#search-tenant-name-label")).setText((log.KnownVehicle != false)?(log.tenant.getFullName()):("Unknown"));



		}

	}

	protected static String formatDate(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("E HH:mm:ss dd/MM/yyyy");
		return formatter.format(date);
	}

}

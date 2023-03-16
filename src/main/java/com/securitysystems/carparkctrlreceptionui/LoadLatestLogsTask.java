package com.securitysystems.carparkctrlreceptionui;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.TimerTask;

public class LoadLatestLogsTask extends TimerTask {

	private final Scene applicationScene;

	public LoadLatestLogsTask(Scene applicationScene) {
		this.applicationScene = applicationScene;
	}

	public void loadInUI(Log event, boolean isLatestEvent) throws IOException {
		EventElement.loadIntoScrollpane(event, true, isLatestEvent, this.applicationScene);

		// set the Last Detection section's labels if this is the most recent event
		if (isLatestEvent) {
			LabelSetters.MonitoringView.setCurrentLogSectionLabels(event, this.applicationScene, false);
			CarparkManagementApplication.selectedLog = event;
		}

	}

	public void loadInUI(Carpark carpark) throws IOException {
		LabelSetters.MonitoringView.setCarparkStatisticsSectionLabels(carpark, this.applicationScene);
	}


	@Override
	public void run() {
		System.out.println("RUNNING TASK");

		Platform.runLater(() -> {
			Log[] events;
			Carpark carpark;

			LabelSetters.setLoadingLabelVisibility(true, applicationScene);

			try {

				events = HttpRequester.getLogs(Settings.getNumberEventsToFetchForMonitoring(applicationScene));
				carpark = HttpRequester.getCarpark();
				System.out.println(events.length + " EVENTS RECEIVED");

				// update events
				LabelSetters.clearEventsContainer(true, this.applicationScene);

				// update most recent detection (entry/exit) labels with first element (most recent)
				// Events ScrollBox should be populated with most recent first therefore forward for loop
				for (int i = 0; i < events.length; i++) {
					Log event = events[i];
					event.setTenant();
					loadInUI(event, i == 0);
				}

				// set snapshot image to be the image(s) of the most recent Log event
				LabelSetters.setSnapshotImages(events[0], true, applicationScene);

				// update carpark statistic labels
				this.loadInUI(carpark);


			} catch (IOException | NullPointerException | Error exception) {
				// network error or connection refused: Notify user
				System.out.println("TASK FAILED");

				// set labels to unknown
				LabelSetters.MonitoringView.setUILabelsToUnknown(this.applicationScene);

				Alert errorAlert = new Alert(Alert.AlertType.WARNING, "Some or all data could not be updated due to either a network or server error.\n\n" + exception.getMessage(), new ButtonType("Close"));
				errorAlert.setTitle("Carpark Management System error");
				errorAlert.setHeaderText("Network or server error");
				errorAlert.initStyle(StageStyle.UNIFIED);
//https://stackoverflow.com/questions/66237267/set-icon-for-overall-application-windows-in-javafx
				Stage stage = (Stage) errorAlert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image(CarparkManagementApplication.class.getResource("/carpark-monitoring-view/images/application-icon.png").toString()));

				errorAlert.show();
				exception.printStackTrace();
			}

			LabelSetters.setLoadingLabelVisibility(false, applicationScene);


		});

	}
}

package com.securitysystems.carparkctrlreceptionui;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.TimerTask;

public class LoadLatestLogsTask extends TimerTask {

	private final Scene applicationScene;

	public LoadLatestLogsTask(Scene applicationScene) {
		this.applicationScene = applicationScene;

		GridPane monitoringViewGridpane = (GridPane)applicationScene.lookup("#monitoring-view-gridpane");
		// width of each image is 40% of the gridpane's width ((100%-20%)/2). EventsContainer's width is 20%.
		// image preserveRatios are set to true so heightProperty left unset.
		// used documentation https://openjfx.io/javadoc/11/javafx.graphics/javafx/scene/image/ImageView.html#fitWidthProperty()
		((ImageView) applicationScene.lookup("#entry-snapshot-view")).fitWidthProperty().bind(monitoringViewGridpane.widthProperty().multiply(.4));
		((ImageView) applicationScene.lookup("#exit-snapshot-view")).fitWidthProperty().bind(monitoringViewGridpane.widthProperty().multiply(.4));

	}

	public void clearEventsContainer() {
		VBox eventsContainer = (VBox) this.applicationScene.lookup("#events-container");
		eventsContainer.getChildren().clear();
	}
	public void loadInUI(Log event, boolean isLatestEvent) throws IOException {

		// original plan:
		// To prevent use of a global array to store the current Log objects, an extended AnchorPane, EventElement, will be responsible for its own Log object containing the information of the Log it represents
		// then when the user clicks on an EventElement, the Log is used to populate the labels in the window to show its information (times, numberplate, images, etc...)
		// however, due to the bad design of JavaFX (https://stackoverflow.com/a/32199349/7169383), it is impossible to subclass components including the AnchorPane.
		// Therefore, the setUserData() method must be used (even though this is not the proper use case)

		FXMLLoader eventElementLoader = new FXMLLoader(getClass().getResource("/carpark-monitoring-view/EventElement.fxml"));
		AnchorPane newEventElement = eventElementLoader.load();

		newEventElement.focusedProperty().addListener((observableValue, prevState, newState) -> {
//			TODO: DOCUMENT THIS
			if (newState) {
				System.out.println("Focussed");
				// handle click
				EventElement.selectInScrollpane(newEventElement, applicationScene);
			} else {
				System.out.println("Unfocussed");
			}
		});

		newEventElement.setUserData(event);
		((EventElementController)eventElementLoader.getController()).setMonitoringScene(this.applicationScene); // Referenced this answer on passing parameters to FXML controllers: https://stackoverflow.com/a/30815504/7169383

		LabelSetters.setEventElementLabels(event, newEventElement);
		((VBox) this.applicationScene.lookup("#events-container")).getChildren().add(newEventElement);

		// set the Last Detection section's labels if this is the most recent event
		if (isLatestEvent) LabelSetters.setCurrentLogSectionLabels(event, this.applicationScene, false);

	}

	public void loadInUI(Carpark carpark) throws IOException {
		LabelSetters.setCarparkStatisticsSectionLabels(carpark, this.applicationScene);
	}


	@Override
	public void run() {
		System.out.println("RUNNING TASK");

		Platform.runLater(() -> {
			Log[] events;
			Carpark carpark;

			try {

				events = HttpRequester.getLogs(5);
				carpark = HttpRequester.getCarpark();
				System.out.println(events.length + " EVENTS RECEIVED");

				// update events
				clearEventsContainer();

				// update most recent detection (entry/exit) labels with first element (most recent)
				// Events ScrollBox should be populated with most recent first therefore forward for loop
				for (int i = 0; i < events.length; i++) {
					Log event = events[i];
					loadInUI(event, i == 0);
				}

				// set snapshot image to be the images of the most recent Log event
				LabelSetters.setSnapshotImages(events[0], applicationScene);

				// update carpark statistic labels
				this.loadInUI(carpark);

			} catch (IOException | NullPointerException | Error exception) { // network error or connection refused: Notify user
				System.out.println("TASK FAILED");

				// set labels to unknown
				LabelSetters.setUILabelsToUnknown(this.applicationScene);

				Alert errorAlert = new Alert(Alert.AlertType.WARNING, "Some or all data could not be updated due to either a network or server error.\n\n" + exception.getMessage(), new ButtonType("Close"));
				errorAlert.show();
				exception.printStackTrace();
			}

		});

	}
}

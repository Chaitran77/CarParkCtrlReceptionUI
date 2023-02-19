package com.securitysystems.carparkctrlreceptionui;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.TimerTask;

public class loadLatestLogsTask extends TimerTask {

	private final VBox eventsContainer;
	private final ImageView snapshotView;

	public loadLatestLogsTask(VBox eventsContainer, ImageView snapshotView) {
		this.eventsContainer = eventsContainer;
		this.snapshotView = snapshotView;
	}

	public VBox getClearedEventsContainer() {
		eventsContainer.getChildren().clear();
		return eventsContainer;
	}
	public void loadInUI(Log event) throws IOException {

		AnchorPane newEventElement = (AnchorPane) FXMLLoader.load(getClass().getResource("/carpark-monitoring-view/EventElement.fxml"));
		// not storing label elements in variables since they are only used once
		// ternary conditionals used here to evaluate event's properties and pass the correct value to setText() - condensed 12 lines to 3

//        original code for 1 element
//        if (event.Numberplate == null) {
//            ((Label) newEventElement.lookup("#numberplate-label")).setText("Unknown");
//        } else {
//            ((Label) newEventElement.lookup("#numberplate-label")).setText(event.Numberplate);
//        }
		System.out.println(event.EventID);
		((Label) newEventElement.lookup("#numberplate-label")).setText((event.Numberplate!=null)?(event.Numberplate):("Unknown"));
		((Label) newEventElement.lookup("#entry-timestamp-label")).setText((event.EntryTimestamp!=null)?(event.EntryTimestamp.toString()):("Unknown"));
		((Label) newEventElement.lookup("#exit-timestamp-label")).setText((event.ExitTimestamp!=null)?(event.ExitTimestamp.toString()):("Unknown"));
		eventsContainer.getChildren().add(newEventElement);

	}

	@Override
	public void run() {
		System.out.println("RUNNING TASK");

		Platform.runLater(() -> {
			try {
				// firstly fetch and update events
				VBox eventsContainer = getClearedEventsContainer();
				Log[] events = HttpRequester.getLogs(5);
				System.out.println(events.length + " EVENTS RECEIVED");

				for (Log event : events) loadInUI(event);

				// set snapshot image to be the EntryImage of the most recent Log event
				if (events[events.length-1].EntryImageBase64 != null) {
					snapshotView.setImage(new Image("data:image/jpeg;base64," + events[events.length - 1].EntryImageBase64));
				}
				// then update carpark statistic labels

			} catch (IOException | NullPointerException exception) { // network error or connection refused: Notify user
				System.out.println("TASK FAILED");
				Alert errorAlert = new Alert(Alert.AlertType.WARNING, "Some or all data could not be updated due to either a network or server error.\n\n" + exception.getMessage());
				errorAlert.show();
				exception.printStackTrace();
			}
		});

	}
}

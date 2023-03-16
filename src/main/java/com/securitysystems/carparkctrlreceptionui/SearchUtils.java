package com.securitysystems.carparkctrlreceptionui;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;

public class SearchUtils {

	public static Hashtable<String, Object> getSearchParameters(Scene applicationScene) {
		Hashtable<String,Object> searchParameters = new Hashtable<>();
		searchParameters.put("numberplate", ((TextField)applicationScene.lookup("#search-term-field")).getText());
		return searchParameters;
	}

	public static void performSearch(Scene applicationScene) {
		/* serach numberplate displays most recent logs for that numberplate
		 * 1) Get search parameters from inputs
		 * 2) get Logs from server
		 * 3)
		 * */
		Hashtable<String, Object> searchParameters = getSearchParameters(applicationScene);
		System.out.println(searchParameters);
		System.out.println(searchParameters.get("numberplate").toString());
		LabelSetters.setLoadingLabelVisibility(true, applicationScene);

		try {
			Log[] logs = HttpRequester.getLogs(Settings.getNumberEventsToSearchFrom(applicationScene));
			logs = BinarySearch.binarySearch(logs, searchParameters.get("numberplate").toString());
//			MergeSort.mergeSort(logs, "numberplate");
			System.out.println(Arrays.toString(logs));
			LabelSetters.clearEventsContainer(false, applicationScene);

			if (logs.length == 0) {
				// no logs with matching numberplate's were found
				Alert logsNotFoundAlert = new Alert(Alert.AlertType.INFORMATION, "No events with matching numberplate's could be found.\n\n", new ButtonType("Okay"));
				Stage stage = (Stage) logsNotFoundAlert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image(CarparkManagementApplication.class.getResource("/carpark-monitoring-view/images/application-icon.png").toString()));
				logsNotFoundAlert.setTitle("Carpark Management System search");
				logsNotFoundAlert.setHeaderText("Search returned no results");
				logsNotFoundAlert.initStyle(StageStyle.UNIFIED);
				logsNotFoundAlert.show();
			}

			for (int i = 0; i < logs.length; i++) {
				logs[i].setTenant();
				EventElement.loadIntoScrollpane(logs[i], false, false, applicationScene);
			}
		} catch (IOException exception) {
			Alert errorAlert = new Alert(Alert.AlertType.WARNING, "Some or all data could not be updated due to either a network or server error.\n\n" + exception.getMessage(), new ButtonType("Close"));
			Stage stage = (Stage) errorAlert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(CarparkManagementApplication.class.getResource("/carpark-monitoring-view/images/application-icon.png").toString()));
			errorAlert.show();
			exception.printStackTrace();
		}

		LabelSetters.setLoadingLabelVisibility(false, applicationScene);


	}

	public static void performSort(Log[] logs, String by, String direction) {
		MergeSort.mergeSort(logs, by, direction); // Log[] sortedLogs =
	}

}

package com.securitysystems.carparkctrlreceptionui;

import javafx.scene.Scene;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Hashtable;

public class SearchUtils {
//	public Log searchEventID(int EventID) {
//		// binary search for EventID
//
//	}


	public static Hashtable<String, Object> getSearchParameters(Scene applicationScene) {
		Hashtable<String,Object> searchParameters = new Hashtable<String, Object>();
		searchParameters.put("numberplate", ((TextField)applicationScene.lookup("#search-term-field")).getText());
		return searchParameters;
	}

	public static void performSearch(Scene applicationScene) {
		System.out.println(getSearchParameters(applicationScene));
		performSort(applicationScene); // for now
		/* serach numberplate displays most recent logs for that numberplate
		 * 1) Get search parameters from inputs
		 * 2) get Logs from server
		 * 3)
		 * */
	}

	public static void performSort(Scene applicationScene) {


		try {
			Log[] logs = HttpRequester.getLogs(10);
			MergeSort.mergeSort(logs); // Log[] sortedLogs =
			LabelSetters.clearEventsContainer(false, applicationScene);
			for (int i = 0; i < logs.length; i++) {
				EventElement.loadIntoScrollpane(logs[i], false, applicationScene);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	public static Log[] mergeSort(Log[] logsToSort, String sortVariable) {

		switch (sortVariable.toLowerCase()) {
			case "entrytimestamp":
				break;
			case "exittimestamp":
				break;
			case "knownvehicle":
				break;
		}
		return new Log[0];
	}
}

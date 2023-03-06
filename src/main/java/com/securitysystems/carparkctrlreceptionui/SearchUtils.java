package com.securitysystems.carparkctrlreceptionui;

import javafx.scene.Scene;
import javafx.scene.control.TextField;

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
		Hashtable<String, Object> searchParameters = getSearchParameters(applicationScene);
		System.out.println(searchParameters);
		System.out.println(searchParameters.get("numberplate").toString());

		try {
			Log[] logs = HttpRequester.getLogs(10);
			logs = BinarySearch.binarySearch(logs, searchParameters.get("numberplate").toString());
//			MergeSort.mergeSort(logs, "numberplate");
			System.out.println(Arrays.toString(logs));
			LabelSetters.clearEventsContainer(false, applicationScene);
			for (int i = 0; i < logs.length; i++) {
				EventElement.loadIntoScrollpane(logs[i], false, false, applicationScene);
			}
//			performSort(applicationScene, logs); // for now
		} catch (IOException e) {
			e.printStackTrace();
		}


		/* serach numberplate displays most recent logs for that numberplate
		 * 1) Get search parameters from inputs
		 * 2) get Logs from server
		 * 3)
		 * */
	}

	public static void performSort(Scene applicationScene, Log[] logs, String by) {
		MergeSort.mergeSort(logs, by); // Log[] sortedLogs =
	}

}

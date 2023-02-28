package com.securitysystems.carparkctrlreceptionui;

import javafx.util.Pair;

import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;

public class SearchUtils {
//	public Log searchEventID(int EventID) {
//		// binary search for EventID
//
//	}


	public static Hashtable<String, Object> getSearchParameters() {
		return new Hashtable<String, Object>();
	}

	public static void performSearch() {
		getSearchParameters();

		try {
			Log[] unsortedLogs = HttpRequester.getLogs(10);
			MergeSort.mergeSort(unsortedLogs); // Log[] sortedLogs =
		} catch (IOException e) {
			e.printStackTrace();
		}

		/* serach numberplate displays most recent logs for that numberplate
		* 1) Get search parameters from inputs
		* 2) get Logs from server
		* 3)
		* */
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

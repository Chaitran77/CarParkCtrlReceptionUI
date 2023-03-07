package com.securitysystems.carparkctrlreceptionui;

import java.util.Arrays;

public class BinarySearch {

	// different method name so not confused with a constructor method
	public static Log[] binarySearch(Log[] logsToSearch, String itemToFind) {
		// for binary search to work, Logs must already be sorted, so:
		MergeSort.mergeSort(logsToSearch, "numberplate", "descending");
		System.out.println("AFTER MERGE SORT\n" + Arrays.toString(logsToSearch));
		return search(logsToSearch, itemToFind);
	}
	private static Log[] search(Log[] logsToSearch, String itemToFind) {
		// wrote this binary search from scratch

		int midIndex = logsToSearch.length/2;
		if (logsToSearch.length == 1 && logsToSearch[midIndex].Numberplate.compareToIgnoreCase(itemToFind) != 0) {
			// if last log and not correct one, doesn't exist
			return new Log[0];
		}

		// is the middle Log's numberplate alphabetically before the numberplate to find?
		if (logsToSearch[midIndex].Numberplate.compareToIgnoreCase(itemToFind) < 0) {
			// middle NP is alphabetically before: Rerun but from middle to end of array (discard LHS)
			System.out.println("MID LESS THAN ACTUAL");
			return search(Arrays.copyOfRange(logsToSearch, midIndex, logsToSearch.length), itemToFind);
		} else if (logsToSearch[midIndex].Numberplate.compareToIgnoreCase(itemToFind) > 0) {
			// middle NP is alphabetically after: Rerun but from middle to start of array (discard RHS)
			System.out.println("MID MORE THAN ACTUAL");
			return search(Arrays.copyOfRange(logsToSearch, 0, midIndex), itemToFind);
		} else {
			System.out.println("MATCHING LOG " + logsToSearch[midIndex].toString());
			// a matching Log has been found. Could be adjacent to other matching Logs, so need to find index bounds
			int upperBound = midIndex;
			int lowerBound = midIndex;
			// increment upper bound if next adjacent Log is matching
			for (int i = 0; i < logsToSearch.length-1-midIndex; i++) { // max number of iterations = total elements - midIndex
				if (i == logsToSearch.length) break;
				if (logsToSearch[i+1].Numberplate.compareToIgnoreCase(itemToFind) != 0) break; // break if next np not equal
				upperBound++; // same numberplate
			}
			// decrement lower bound if previous Log is matching
			for (int i = midIndex; i >= 0; i--) {
				if (i==0) break;
				if (logsToSearch[i-1].Numberplate.compareToIgnoreCase(itemToFind) != 0) break; // break is next (prev.) np not equal
				lowerBound--; // same numberplate
			}
			System.out.println("upper: " + upperBound + " lower " + lowerBound);

			if (upperBound == lowerBound) return new Log[]{logsToSearch[midIndex]};
			return Arrays.copyOfRange(logsToSearch, lowerBound, upperBound-1); // "to" parameter is exclusive hence +1

		}
	}
}

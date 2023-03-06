package com.securitysystems.carparkctrlreceptionui;

import java.util.Arrays;
import java.util.Date;

public class MergeSort {

    // REFERENCE: https://www.geeksforgeeks.org/merge-sort/
    // This is (mostly) my own implementation of a merge sort based on the pseudocode algorithm given on the above page.
    public static void mergeSort(Log[] logsToSort, String by) {

        // could not get merge sort for numberplates to work (could not figure out why),
        // so replaced with following and a comparison method in the Log class with help of: https://stackoverflow.com/a/48418903/7169383
        if (by.equals("numberplate")) {
            Arrays.sort(logsToSort, Log::compareNumberplate);
            return;
        }

        // merge sort by timestamps works perfectly and is as follows:
        int length = logsToSort.length;

        if (length < 2) {
            return;
        }
        System.out.println("PRINTING ARRAY");
        int midIndex = length/2;
        Log[] leftArray = new Log[midIndex];
        Log[] rightArray = new Log[length-midIndex];

        // Split logsToSearch: Add all elements up to midIndex-1 to leftArray and from midIndex to end to rightArray
        for (int i = 0; i < midIndex; i++) {
            leftArray[i] = logsToSort[i];
        }

        for (int i = midIndex; i < length; i++) {
            rightArray[i-midIndex] = logsToSort[i];
        }
        System.out.println(Arrays.toString(leftArray) + " " + Arrays.toString(rightArray));

        mergeSort(leftArray, by);
        mergeSort(rightArray, by);
        System.out.println(Arrays.toString(merge(logsToSort, leftArray, rightArray, by)));

    }

    private static Log[] merge(Log[] logsToMerge, Log[] leftArray, Log[] rightArray, String by) {
        int leftLength = leftArray.length;
        int rightLength = rightArray.length;

        int i=0, j=0, k=0;
        while (i < leftLength && j < rightLength) {
            try {
                switch (by) {
                    case "entry_timestamp":
                        Date entryDate1 = leftArray[i].EntryTimestamp;
                        Date entryDate2 = rightArray[i].EntryTimestamp;

                        if (entryDate1.before(entryDate2)) {
                            logsToMerge[k++] = leftArray[i++];
                        } else {
                            logsToMerge[k++] = rightArray[j++];
                        }
                        break;
                    case "exit_timestamp":
                        Date exitDate1 = leftArray[i].ExitTimestamp;
                        Date exitDate2 = rightArray[i].ExitTimestamp;

                        if (exitDate1.before(exitDate2)) {
                            logsToMerge[k++] = leftArray[i++];
                        } else {
                            logsToMerge[k++] = rightArray[j++];
                        }
                        break;
                    /* following is original code for numberplate comparison, however could not figure out why it wasn't working and ran out of time
                    case "numberplate":
                        if (leftArray[i].Numberplate.compareToIgnoreCase(rightArray[i].Numberplate) < 0) {
                            System.out.println(leftArray[i].Numberplate + " is before or equal to" + rightArray[i].Numberplate);
                            logsToMerge[k++] = rightArray[i++];
                        } else {
                            System.out.println(leftArray[i].Numberplate + " is after" + rightArray[i].Numberplate);
                            logsToMerge[k++] = leftArray[j++];
                        }
                        break;
                    */
                    // no default case necessary


                }

            } catch (Exception e){
                e.printStackTrace();
                // one of the values is null: Can't compare unknown time so add to right array and push to end
                logsToMerge[k++] = rightArray[j++];
            }

        }

        // combine resulting arrays
        while (i < leftLength) {
            logsToMerge[k++] = leftArray[i++];
        }
        while (j < rightLength) {
            logsToMerge[k++] = rightArray[j++];
        }
        return logsToMerge;
    }
}

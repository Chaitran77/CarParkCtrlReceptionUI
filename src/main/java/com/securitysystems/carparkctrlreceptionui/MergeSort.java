package com.securitysystems.carparkctrlreceptionui;

import java.util.Arrays;
import java.util.Date;

public class MergeSort {

    public static void mergeSort(Log[] logsToSort) {
        int length = logsToSort.length;
        int midIndex = length/2;

        if (length < 2) return;

        Log[] leftArray = new Log[midIndex];
        Log[] rightArray = new Log[length-midIndex];

        for (int i = 0; i < midIndex; i++) {
            leftArray[i] = logsToSort[i];
        }

        for (int i = midIndex; i < length; i++) {
            rightArray[i-midIndex] = logsToSort[i];
        }

        mergeSort(leftArray);
        mergeSort(rightArray);
        System.out.println(Arrays.toString(merge(logsToSort, leftArray, rightArray, length)));
    }

    public static Log[] merge(Log[] logsToMerge, Log[] leftArray, Log[] rightArray, int totalLength) {
        int leftLength = leftArray.length;
        int rightLength = rightArray.length;

        int i=0, j=0, k=0;
        while (i < leftLength && j < rightLength) {
            Date entryDate1 = leftArray[i].EntryTimestamp;
            Date entryDate2 = rightArray[i].EntryTimestamp;

            try {
                if (entryDate1.before(entryDate2)) {
                    logsToMerge[k++] = leftArray[i++];
                } else {
                    logsToMerge[k++] = rightArray[j++];
                }
            } catch (Exception e){
                e.printStackTrace();
                // one of the dates is null
                logsToMerge[k++] = rightArray[j++];
            }

        }

        // combine resulting arrays
        while (i < leftLength) {
            logsToMerge[k++] = leftArray[i++];
        }
        while (k < rightLength) {
            logsToMerge[k++] = rightArray[j++];
        }
        return logsToMerge;
    }
}

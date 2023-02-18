package com.securitysystems.carparkctrlreceptionui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;

public class HttpRequester {
    public static void getLogs(int count) {
//        Gson g = new Gson();
//        try {
//            String data = executeGetRequest("");
//            if (data != null) {
//                Log[] logs = g.fromJson(data, Log.class);
//            }
//        } catch (IOException e) {
//
//        }

        System.out.println(executeGetRequest("http://81.107.245.60:80/"));
    }

    public static void getLatestLog() {

    }

    public static void getVehicleTenantRecords() {

    }

    private static String executeGetRequest(String url) throws IOException {
        URL request = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) request.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode(); // triggers request to be sent and response to be received
        switch (responseCode) {
            case HttpURLConnection.HTTP_OK:
                return readResponse(connection);
            case HttpURLConnection.HTTP_BAD_REQUEST:
                return readResponse(connection);

        }
        return null;
    }

    private static void executePostRequest() {

    }

    private static String readResponse(HttpURLConnection connection) throws IOException {
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String currentLine;
        StringBuilder responseString = new StringBuilder();


        while (true) {
            currentLine = inputReader.readLine();
            if (currentLine == null) break;
            responseString.append(currentLine);
        }

        inputReader.close();
        return responseString.toString();
    }
}

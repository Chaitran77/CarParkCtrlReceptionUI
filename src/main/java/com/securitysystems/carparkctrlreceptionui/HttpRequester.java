package com.securitysystems.carparkctrlreceptionui;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequester {
	public static Log[] getLogs(int count) throws IOException, NullPointerException {

		Gson g = new Gson();

		// if the data is null (server refused to connect or sent nothing) trigger catch block, otherwise deserialize JSON and return Log[]
		String data = executeGetRequest("http://81.107.245.60:80/logData/" + count);
		System.out.println(String.format("%." + 5000 + "s", data));
		if (data == null) throw new NullPointerException("Server refused to connect");
		return g.fromJson(data, Log[].class);
	}

//    public static void getLatestLog() {
//
//    }

//    public static void getVehicleTenantRecords() {
//
//    }

	private static String executeGetRequest(String url) throws IOException {
		System.out.println("THE URL IS " + url);
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

package com.securitysystems.carparkctrlreceptionui;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpRequester {

	public static String[] credentials = null; // credentials[0] == username, credentials[1] == passwordhash

	// exceptions are not handled here since this is a utility class. They are thrown, then handled in the methods they were called in
	public static Log[] getLogs(int count) throws IOException, NullPointerException {

		Gson g = new Gson();

		// if the data is null (server refused to connect or sent nothing) trigger catch block, otherwise deserialize JSON and return Log[]

		String data = executeGetRequest("http://81.107.245.60:80/logData/" + count);
		int numberOfBytes = data.getBytes(StandardCharsets.UTF_8).length;
		System.out.println(numberOfBytes + " bytes = " + numberOfBytes/1000000d + " megabytes");

		if (data == null) throw new NullPointerException("Server refused to connect");
		return g.fromJson(data, Log[].class);
	}

	public static Carpark getCarpark() throws IOException, NullPointerException {
		Gson g = new Gson();
		String data = executeGetRequest("http://81.107.245.60:80/carparkStatistics");
		if (data == null) throw new NullPointerException("Server refused to connect");
		return g.fromJson(data, Carpark.class);
	}

	public static Tenant getTenantDataFromLogID(int log_id) throws IOException, NullPointerException {
		Gson g = new Gson();
		String data = executeGetRequest("http://81.107.245.60:80/tenantDataFromLogID/" + log_id);
		if (data == null) throw new NullPointerException("Server refused to connect");
		return g.fromJson(data, Tenant.class);
	}

	public static String getLoginSalt(String username) throws IOException, Error {
		return executeGetRequest("http://81.107.245.60:80/loginSalt?username=" + username);
	}

	public static void login(String username, String hashedPassword) throws Exception { // both exception types declared explicitly for clarity
		HttpRequester.credentials = new String[]{username, hashedPassword};

		HttpURLConnection loginConnection = executePostRequest("http://81.107.245.60:80/login", null);
		int responseCode = loginConnection.getResponseCode();

		switch (responseCode) {
			case 200:
				// login successful (valid credentials), assign the result.get()<String[]> to HttpRequester.credentials for use in all subsequent requests
				return;
			case 403:
			default:
				throw new Exception(loginConnection.getContent().toString());
		}
	}

	public static void postOpenGate(Log selectedLog) throws Exception {
		int responseCode = 0;
		HttpURLConnection openGateConnection = null;
		try {
			openGateConnection = executePostRequest("http://81.107.245.60/openGate/" + selectedLog.EventID, new Object());
			String response = readResponse(openGateConnection);
			// gate successfully opened and Log record modified
		} catch (IOException e) {
			if (openGateConnection.getResponseCode() == 403) {
				throw new Exception("Cannot open gate for a vehicle that has already entered or is authorised");
//			default:
//				// error occurred: Gate could not be opened or user level not admin
//				throw new Exception(response);
			}
		}


	}

	private static String executeGetRequest(String url) throws IOException, Error {
		System.out.println("THE URL IS " + url);
		URL request = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) request.openConnection();

		// Add credentials, if route requires creds but none (credentials == null) or invalid creds provided, then error message will display
		if (credentials != null) {
			connection.setRequestProperty("username", credentials[0]);
			connection.setRequestProperty("passwordhash", credentials[1]);
		}

		connection.setRequestMethod("GET");

		int responseCode = connection.getResponseCode(); // triggers request to be sent and response to be received

		switch (responseCode) {
			case HttpURLConnection.HTTP_OK:
			case HttpURLConnection.HTTP_BAD_REQUEST:
				return readResponse(connection);
			case HttpURLConnection.HTTP_FORBIDDEN:
				throw new Error(connection.getResponseMessage() + ": " + connection.getContent().toString());
			default:
				return null;
		}
	}

	private static HttpURLConnection executePostRequest(String url, Object body) throws IOException {
		System.out.println("THE URL IS " + url);
		URL request = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) request.openConnection();

		// Add credentials
		if (credentials != null) {
			connection.setRequestProperty("username", credentials[0]);
			connection.setRequestProperty("passwordhash", credentials[1]);
		}

		// let the server know that JSON is in the POST body
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestMethod("POST");

		// the response code and any data sent back can be read from connection
		if (body == null) return connection;
		// otherwise send the body (no cases where necessary therefore not implemented)
		return connection;
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

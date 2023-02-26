package com.securitysystems.carparkctrlreceptionui;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class Hasher {
	public static String getHashedString(String inputValue) {
		// using SHA256 to hash values in this project

		// SOURCE: https://stackoverflow.com/a/18340262/7169383, Google's Guava library
		return Hashing.sha256().hashString(inputValue, StandardCharsets.UTF_8).toString();
	}
}

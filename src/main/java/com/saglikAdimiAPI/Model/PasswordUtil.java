package com.saglikAdimiAPI.Model;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {
	private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	public static String hashPassword(String password) {
		return encoder.encode(password);
	}

	public static boolean verifyPassword(String rawPassword, String hashedPassword) {
		return encoder.matches(rawPassword, hashedPassword);
	}
}

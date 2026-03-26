package com.auth.sample.session.config;

import org.springframework.stereotype.Component;

import com.auth.spi.PasswordVerifier;

@Component
public class PlainTextPasswordVerifier implements PasswordVerifier {

	@Override
	public boolean matches(String rawPassword, String storedHash) {
		return storedHash != null && storedHash.equals(rawPassword);
	}
}

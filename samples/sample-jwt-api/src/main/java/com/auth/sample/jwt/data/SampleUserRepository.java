package com.auth.sample.jwt.data;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.auth.api.model.User;

@Component
public class SampleUserRepository {

	private final Map<String, User> users = Map.of(
		"admin", new User("user-admin", "admin", "admin", List.of("ADMIN")),
		"user", new User("user-basic", "user", "user", List.of("USER"))
	);

	public Optional<User> findByUsername(String username) {
		return Optional.ofNullable(users.get(username));
	}
}

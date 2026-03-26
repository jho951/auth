package com.auth.sample.session.data;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.auth.api.model.User;

@Component
public class SampleUserRepository {

	private final Map<String, User> users = Map.of(
		"alice", new User("session-alice", "alice", "alice", List.of("USER")),
		"admin", new User("session-admin", "admin", "admin", List.of("ADMIN"))
	);

	public Optional<User> findByUsername(String username) {
		return Optional.ofNullable(users.get(username));
	}
}

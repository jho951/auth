package com.auth.sample.session.config;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.auth.api.model.User;
import com.auth.sample.session.data.SampleUserRepository;
import com.auth.spi.UserFinder;

@Component
public class SampleUserFinder implements UserFinder {

	private final SampleUserRepository repository;

	public SampleUserFinder(SampleUserRepository repository) {
		this.repository = repository;
	}

	@Override
	public Optional<User> findByUsername(String username) {
		return repository.findByUsername(username);
	}
}

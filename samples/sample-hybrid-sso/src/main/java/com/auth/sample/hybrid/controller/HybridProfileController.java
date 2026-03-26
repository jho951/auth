package com.auth.sample.hybrid.controller;

import com.auth.api.model.Principal;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hybrid")
public class HybridProfileController {

	@GetMapping("/me")
	public Principal me(@AuthenticationPrincipal Principal principal) {
		return principal;
	}
}

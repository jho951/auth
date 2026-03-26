package com.auth.sample.hybrid.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.auth.api.model.OAuth2UserIdentity;
import com.auth.api.model.Principal;
import com.auth.spi.OAuth2PrincipalResolver;

@Component
public class SampleOAuth2PrincipalResolver implements OAuth2PrincipalResolver {

	@Override
	public Principal resolve(OAuth2UserIdentity identity) {
		return new Principal(identity.getProvider() + "-" + identity.getProviderUserId(), List.of("HYBRID"));
	}
}

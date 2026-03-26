package com.auth.hybrid;

import com.auth.api.model.Principal;
import com.auth.spi.TokenService;
import com.auth.session.SessionAuthenticationProvider;
import java.util.Optional;
import java.util.Objects;

/**
 * Simple hybrid provider that prefers JWT and falls back to session authentication.
 */
public final class DefaultHybridAuthenticationProvider implements HybridAuthenticationProvider {

    private final TokenService tokenService;
    private final SessionAuthenticationProvider sessionAuthenticationProvider;

    public DefaultHybridAuthenticationProvider(TokenService tokenService, SessionAuthenticationProvider sessionAuthenticationProvider) {
        this.tokenService = Objects.requireNonNull(tokenService, "tokenService");
        this.sessionAuthenticationProvider = Objects.requireNonNull(sessionAuthenticationProvider, "sessionAuthenticationProvider");
    }

    @Override
    public Optional<Principal> authenticate(HybridAuthenticationContext context) {
        Optional<Principal> jwtPrincipal = context.accessToken()
            .flatMap(this::verifyToken);
        if (jwtPrincipal.isPresent()) {
            return jwtPrincipal;
        }
        return context.sessionId().flatMap(sessionAuthenticationProvider::authenticate);
    }

    private Optional<Principal> verifyToken(String token) {
        try {
            return Optional.of(tokenService.verifyAccessToken(token));
        } catch (RuntimeException ex) {
            return Optional.empty();
        }
    }
}

package com.auth.hybrid;

import com.auth.api.model.Principal;
import java.util.List;
import java.util.Optional;

/**
 * Composition helper that delegates across multiple {@link HybridAuthenticationProvider} instances.
 */
public final class CompositeAuthenticationProvider implements HybridAuthenticationProvider {

    private final List<HybridAuthenticationProvider> delegates;

    public CompositeAuthenticationProvider(List<HybridAuthenticationProvider> delegates) {
        this.delegates = List.copyOf(delegates);
    }

    @Override
    public Optional<Principal> authenticate(HybridAuthenticationContext context) {
        for (HybridAuthenticationProvider provider : delegates) {
            Optional<Principal> principal = provider.authenticate(context);
            if (principal.isPresent()) {
                return principal;
            }
        }
        return Optional.empty();
    }
}

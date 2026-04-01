package com.auth.config.hybrid;

import com.auth.hybrid.DefaultHybridAuthenticationProvider;
import com.auth.hybrid.HybridAuthenticationProvider;
import com.auth.spi.TokenService;
import com.auth.session.SessionAuthenticationProvider;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@AutoConfigureAfter({
	com.auth.config.AuthAutoConfiguration.class,
	com.auth.session.config.AuthSessionAutoConfiguration.class
})
@ConditionalOnBean(TokenService.class)
public class AuthHybridAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(HybridAuthenticationProvider.class)
    public HybridAuthenticationProvider hybridAuthenticationProvider(
        TokenService tokenService,
        SessionAuthenticationProvider sessionAuthenticationProvider
    ) {
        return new DefaultHybridAuthenticationProvider(tokenService, sessionAuthenticationProvider);
    }
}

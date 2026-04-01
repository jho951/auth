package com.auth.session.config;

import com.auth.session.DefaultSessionAuthenticationProvider;
import com.auth.session.DefaultSessionCookieExtractor;
import com.auth.session.IdentitySessionPrincipalMapper;
import com.auth.session.SessionAuthenticationProvider;
import com.auth.session.SessionCookieExtractor;
import com.auth.session.SessionIdGenerator;
import com.auth.session.SessionPrincipalMapper;
import com.auth.session.SessionService;
import com.auth.session.SessionStore;
import com.auth.session.SecureRandomSessionIdGenerator;
import com.auth.session.SimpleSessionStore;
import com.auth.session.security.SessionAuthenticationFilter;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(AuthSessionProperties.class)
public class AuthSessionAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(SessionStore.class)
    public SessionStore sessionStore() {
        return new SimpleSessionStore();
    }

    @Bean
    @ConditionalOnMissingBean(SessionIdGenerator.class)
    public SessionIdGenerator sessionIdGenerator() {
        return new SecureRandomSessionIdGenerator();
    }

    @Bean
    @ConditionalOnMissingBean(SessionPrincipalMapper.class)
    public SessionPrincipalMapper sessionPrincipalMapper() {
        return new IdentitySessionPrincipalMapper();
    }

    @Bean
    @ConditionalOnMissingBean(SessionAuthenticationProvider.class)
    public SessionAuthenticationProvider sessionAuthenticationProvider(
        SessionStore sessionStore,
        SessionPrincipalMapper principalMapper
    ) {
        return new DefaultSessionAuthenticationProvider(sessionStore, principalMapper);
    }

    @Bean
    @ConditionalOnMissingBean(SessionCookieExtractor.class)
    public SessionCookieExtractor sessionCookieExtractor(AuthSessionProperties props) {
        return new DefaultSessionCookieExtractor(props.getCookieName());
    }

    @Bean
    @ConditionalOnMissingBean(SessionService.class)
    public SessionService sessionService(
        SessionStore sessionStore,
        SessionIdGenerator generator,
        AuthSessionProperties props
    ) {
        return new SessionService(sessionStore, generator, props.getTtl());
    }

    @Bean
    @ConditionalOnMissingBean(SessionAuthenticationFilter.class)
    public SessionAuthenticationFilter sessionAuthenticationFilter(
        SessionCookieExtractor cookieExtractor,
        SessionAuthenticationProvider provider
    ) {
        return new SessionAuthenticationFilter(cookieExtractor, provider);
    }
}

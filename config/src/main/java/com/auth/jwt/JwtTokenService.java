package com.auth.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

import com.auth.exception.AuthException;
import com.auth.exception.ErrorCode;
import com.auth.model.Principal;

import auth.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * 기본 JWT TokenService 구현체 (HS256)
 * - apps(admin 등)에서는 TokenService를 직접 구현하지 않고 설정(auth.jwt.*)만 주입하면 된다.
 */
public class JwtTokenService implements TokenService {

    private final Key key;
    private final long accessSeconds;
    private final long refreshSeconds;

    public JwtTokenService(String secret, long accessSeconds, long refreshSeconds) {
        if (secret == null || secret.isBlank()) {
            throw new AuthException(ErrorCode.INVALID_REQUEST, "auth.jwt.secret must not be blank");
        }
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) {
            throw new AuthException(ErrorCode.INVALID_REQUEST, "auth.jwt.secret must be at least 32 bytes for HS256");
        }
        this.key = Keys.hmacShaKeyFor(bytes);
        this.accessSeconds = accessSeconds;
        this.refreshSeconds = refreshSeconds;
    }

    @Override
    public String issueAccessToken(Principal principal) {
        return buildToken(principal, accessSeconds, "access");
    }

    @Override
    public String issueRefreshToken(Principal principal) {
        return buildToken(principal, refreshSeconds, "refresh");
    }

    @Override
    public Principal verifyAccessToken(String token) {
        return parseAndToPrincipal(token, "access");
    }

    @Override
    public Principal verifyRefreshToken(String token) {
        return parseAndToPrincipal(token, "refresh");
    }

    private String buildToken(Principal principal, long ttlSeconds, String type) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + (ttlSeconds * 1000L));

        return Jwts.builder()
            .setSubject(principal.getUserId())
            .claim("roles", principal.getRoles())
            .claim("token_type", type)
            .setIssuedAt(now)
            .setExpiration(exp)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    private Principal parseAndToPrincipal(String token, String expectedType) {
        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

            String type = claims.get("token_type", String.class);
            if (type == null || !type.equals(expectedType)) {
                throw new AuthException(ErrorCode.INVALID_TOKEN, "invalid token type");
            }

            String userId = claims.getSubject();
            Object rolesObj = claims.get("roles");

            List<String> roles =
                (rolesObj instanceof List<?> list)
                    ? list.stream().map(String::valueOf).toList()
                    : List.of();

            return new Principal(userId, roles);

        } catch (AuthException e) {
            throw e;
        } catch (JwtException | IllegalArgumentException e) {
            throw new AuthException(ErrorCode.INVALID_TOKEN, "invalid/expired token", e);
        }
    }
}

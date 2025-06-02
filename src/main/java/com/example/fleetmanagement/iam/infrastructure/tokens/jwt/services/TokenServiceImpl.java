package com.example.fleetmanagement.iam.infrastructure.tokens.jwt.services;

import com.example.fleetmanagement.iam.infrastructure.tokens.jwt.BearerTokenService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

/**
 * The token service implementation.
 * This class is responsible for generating and extracting the bearer token.
 */
@Service
public class TokenServiceImpl implements BearerTokenService {
    private final Logger LOGGER = LoggerFactory.getLogger(TokenServiceImpl.class);
    private static final String AUTHORIZATION_PARAMETER_NAME = "Authorization";
    private static final String BEARER_TOKEN_PREFIX = "Bearer ";
    private static final int TOKEN_START_INDEX = 7;

    @Value("${authorization.jwt.secret}")
    private String secret;

    @Value(("${authorization.jwt.expiration.days}"))
    private int expirationDays;

    /**
     * Get the signing key.
     *
     * @return the {@link SecretKey} signing key
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Build the token with default parameters.
     *
     * @param username the username
     * @return the token
     */
    private String buildTokenWithDefaultParameters(String username) {
        var issuedAt = new Date();
        var expiration = DateUtils.addDays(issuedAt, expirationDays);
        var key = getSigningKey();
        return Jwts.builder()
                .subject(username)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    /**
     * Extract all claims from the token.
     *
     * @param token the token
     * @return the {@link Claims} claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Extract the claim from the token.
     *
     * @param token          the token
     * @param claimResolvers the {@link Function} claim resolvers
     * @param <T>            the type of the claim
     * @return the claim
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimResolvers) {
        return claimResolvers.apply(extractAllClaims(token));
    }

    /**
     * Check if the token is present in the authorization parameter.
     *
     * @param authorizationParameter the authorization parameter
     * @return true if the token is present, false otherwise
     */
    private boolean isTokenPresentIn(String authorizationParameter) {
        return StringUtils.hasText(authorizationParameter);
    }

    /**
     * Check if the bearer token is present in the authorization parameter.
     *
     * @param authorizationParameter the authorization parameter
     * @return true if the bearer token is present, false otherwise
     */
    private boolean isBearerTokenIn(String authorizationParameter) {
        return authorizationParameter.startsWith(BEARER_TOKEN_PREFIX);
    }

    /**
     * Extract the token from the authorization parameter.
     *
     * @param authorizationParameter the authorization parameter
     * @return the token
     */
    private String extractTokenFrom(String authorizationParameter) {
        return authorizationParameter.substring(TOKEN_START_INDEX);
    }

    /**
     * Get the authorization parameter from the request.
     *
     * @param request the {@link HttpServletRequest} request
     * @return the authorization parameter
     */
    private String getAuthorizationParameterFrom(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION_PARAMETER_NAME);
    }

    /**
     * Extract the bearer token from the request.
     *
     * @param request the {@link HttpServletRequest} request
     * @return the bearer token
     */
    @Override
    public String getBearerTokenFrom(HttpServletRequest request) {
        String authorizationParameter = getAuthorizationParameterFrom(request);
        if (isTokenPresentIn(authorizationParameter) && isBearerTokenIn(authorizationParameter)) {
            return extractTokenFrom(authorizationParameter);
        }
        return null;
    }

    /**
     * Generate the bearer token.
     *
     * @param authentication the {@link Authentication} authentication
     * @return the bearer token
     */
    @Override
    public String generateToken(Authentication authentication) {
        return buildTokenWithDefaultParameters(authentication.getName());
    }

    /**
     * Generate a token for the given username.
     *
     * @param username the username
     * @return the generated token
     */
    @Override
    public String generateToken(String username) {
        return buildTokenWithDefaultParameters(username);
    }

    /**
     * Extract the username from the token.
     *
     * @param token the token
     * @return the username
     */
    @Override
    public String getUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Validate the token.
     *
     * @param token the token
     * @return true if the token is valid, false otherwise
     */
    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            LOGGER.info("Token is valid");
            return true;
        } catch (SignatureException e) {
            LOGGER.error("Invalid token signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            LOGGER.error("Invalid token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            LOGGER.error("Token has expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            LOGGER.error("Unsupported token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error("Token is empty: {}", e.getMessage());
        }
        return false;
    }
}

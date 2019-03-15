package com.aaroncarlson.polls.security;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Following utility class will be used for generating a JWT after a user logs in successfully, and
 * validates the JWT sent in the Authorization header of the request
 */
@Slf4j
@Component
public class JwtTokenProvider {

    // Read from application.properties file
    @Value("${app.jwtSecret}")
    private String jwtSecret;
    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    public String generateToken(Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException exception) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException exception) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException exception) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException exception) {
            log.error("Unsupported JWT Token");
        } catch (IllegalArgumentException exception) {
            log.error("JWT claims string is empty");
        }
        return false;
    }
}

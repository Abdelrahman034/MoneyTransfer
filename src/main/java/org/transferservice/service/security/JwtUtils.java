package org.transferservice.service.security;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.transferservice.exception.custom.InvalidJwtException;
import org.transferservice.model.BlacklistToken;
import org.transferservice.repository.BlacklistTokenRepository;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
public class JwtUtils {

    @Autowired
    private BlacklistTokenRepository blacklistTokenRepository;

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration.ms}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {

        CustomerDetailsImpl userPrincipal = (CustomerDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public String getUserTypeFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().get("type", String.class);
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        } catch (SignatureException e) {
            log.error("JWT signature does not match locally computed signature: {}", e.getMessage());
        }
        return false;
    }

    public void invalidateJwtToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new InvalidJwtException("Token cannot be null or empty");
        }

//            String tokenUserName;
//            try {
//                tokenUserName = this.getUserNameFromJwtToken(token);
//            } catch (Exception e) {
//                log.error("Failed to parse JWT token: {}", e.getMessage());
//                throw new InvalidJwtException("Invalid JWT token", e);
//            }

//            if (!userName.equals(tokenUserName)) {
//                throw new InvalidJwtException("Token does not match the user");
//            }

        if (!isTokenBlacklisted(token)) {
            BlacklistToken blacklistToken = new BlacklistToken();
            blacklistToken.setToken(token);
            blacklistToken.setRevokedAt(LocalDateTime.now());

            try {
                blacklistTokenRepository.save(blacklistToken);
            } catch (Exception e) {
                log.error("Failed to save token to blacklist: {}", e.getMessage());
                throw new InvalidJwtException("Failed to save token to blacklist", e);
            }
        }
    }



    public boolean isTokenBlacklisted(String token) {
        Optional<BlacklistToken> optionalBlacklistToken = blacklistTokenRepository.findByToken(token);
        return optionalBlacklistToken.isPresent();
    }

}

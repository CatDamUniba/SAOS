package CatDam.SAOS2025.security;

import CatDam.SAOS2025.security.services.DettagliUtente;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
//import io.jsonwebtoken.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class JwtUtils {
    private static final Logger logger = Logger.getLogger(JwtUtils.class.getName());
    
    @Value("${CatDam.SAOS2025.app.jwtSecret}")
    private String jwtSecret;
    @Value("${CatDam.SAOS2025.app.jwtExpirationMs}")
    private int jwtExpirationMs;
    
    private Key getSigningKey() {
    	return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
    public String generateJwtToken(Authentication authentication) {
        DettagliUtente userPrincipal = (DettagliUtente) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, getSigningKey())
                .compact();
    }
    public String generateJwtToken(String username) {
        return Jwts.builder()
                .setSubject((username))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512,  getSigningKey())
                .compact();
    }
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey( getSigningKey()).build().parseClaimsJws(token).getBody().getSubject();
    }
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey( getSigningKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.log(Level.SEVERE,"JWT Token INVALIDO: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.log(Level.SEVERE,"JWT token è SCADUTO: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.log(Level.SEVERE,"JWT token  NON E' SUPPORTATO: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.log(Level.SEVERE,"JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
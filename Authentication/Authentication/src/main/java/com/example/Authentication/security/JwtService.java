package com.example.Authentication.security;

//package com.example.auth.security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

@Service
public class JwtService {

 private final SecretKey key;
 private final long jwtExpirationMs;

 public JwtService(
         @Value("${app.jwt.secret}") String secret,
         @Value("${app.jwt.expiration-ms}") long jwtExpirationMs
 ) {
     this.key = Keys.hmacShaKeyFor(secret.getBytes()); // HS256 key.[web:24][web:20]
     this.jwtExpirationMs = jwtExpirationMs;
 }

 @SuppressWarnings("deprecation")
public String generateToken(String username, String role) {
     long now = System.currentTimeMillis();
     Date issuedAt = new Date(now);
     Date expiry = new Date(now + jwtExpirationMs);

     return Jwts.builder()
             .setSubject(username)
             .addClaims(Map.of("role", role))
             .setIssuedAt(issuedAt)
             .setExpiration(expiry)
             .signWith(key) // HS256.[web:24][web:20]
             .compact();
 }

 public String extractUsername(String token) {
     return parseClaims(token).getSubject();
 }

 public String extractRole(String token) {
     Object role = parseClaims(token).get("role");
     return role != null ? role.toString() : null;
 }

 public boolean isTokenValid(String token, String username) {
     String subject = extractUsername(token);
     Date expiration = parseClaims(token).getExpiration();
     return subject.equals(username) && expiration.after(new Date());
 }

 private Claims parseClaims(String token) {
     return Jwts.parser()
             .verifyWith(key)
             .build()
             .parseSignedClaims(token)
             .getPayload();
 }
}

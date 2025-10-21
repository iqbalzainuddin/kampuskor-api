package com.kampuskor.restservice.utils.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {
  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.expiration}")
  private Long expiration;

  private SecretKey getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  private Claims extractAllClaims(String token) {
      return Jwts.parser()
              .verifyWith(getSigningKey())
              .build()
              .parseSignedClaims(token)
              .getPayload();
  }

  // Generate Token
  public String generateToken(Long id, String role) {
    return Jwts.builder()
      .subject(id.toString())
      .claim("rt", role)
      .issuedAt(new Date())
      .expiration(new Date(System.currentTimeMillis() + expiration))
      .signWith(getSigningKey())
      .compact();
  }

  // Extract Username
  public Long extractId(String token) {
      return Long.parseLong(extractAllClaims(token).getSubject());
  }

  // Extract Role Type (rt)
  public String extractRole(String token) {
    return extractAllClaims(token).get("rt", String.class);
  }

  // Validate Token
  public boolean validateToken(String token) {
      try {
          Jwts.parser().verifyWith(getSigningKey()).build().parse(token);
          return true;
      } catch (JwtException e) {
          return false;
      }
  }
}

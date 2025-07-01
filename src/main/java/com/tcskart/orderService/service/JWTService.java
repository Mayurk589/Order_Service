package com.tcskart.orderService.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

 // Inject the secret key from application.properties
 @Value("${jwt.secret.key}")
 private String secretKey;

 /**
  * Generates a JWT token containing user ID, email, and role claims.
  * @param userId The unique ID of the user.
  * @param email The user's email, used as the subject of the token.
  * @param role The user's role (e.g., "CUSTOMER", "ADMIN").
  * @return The generated JWT token string.
  */
 public String generateToken(Long userId, String email, String role) {
     Map<String, Object> claims = new HashMap<>();
     claims.put("userId", userId); // Custom claim for userId
     claims.put("role", role);     // Custom claim for role

     return Jwts.builder()
             .setClaims(claims)           // Set the custom claims
             .setSubject(email)           // Set the subject (typically user's identifier)
             .setIssuedAt(new Date())     // Set the token issuance time
             .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // Token expires in 1 hour
             .signWith(getKey())          // Sign the token with the secret key
             .compact();                  // Build and compact the token into a string
 }

 // Retrieves the signing key from the Base64-encoded secret key
 private SecretKey getKey() {
     if (secretKey == null || secretKey.isEmpty()) {
         throw new IllegalStateException("JWT Secret Key is not initialized. Please configure 'jwt.secret.key' in application.properties.");
     }
     byte[] keyBytes = Decoders.BASE64.decode(secretKey); // Decode Base64 key
     return Keys.hmacShaKeyFor(keyBytes); // Create HMAC-SHA key
 }

 // Extracts the subject (email) from the token
 public String extractUserEmail(String token) {
     return extractClaim(token, Claims::getSubject);
 }

 // Extracts the 'role' custom claim from the token
 public String extractRole(String token) {
     return extractClaim(token, claims -> claims.get("role", String.class));
 }

 // Extracts the 'userId' custom claim from the token
 public Long extractUserId(String token) {
     return extractClaim(token, claims -> claims.get("userId", Long.class));
 }

 // Generic method to extract any claim using a Claims resolver function
 private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
     final Claims claims = extractAllClaims(token); // Get all claims
     return claimsResolver.apply(claims);           // Apply the resolver function
 }

 // Parses and validates the token, returning all claims
 private Claims extractAllClaims(String token) {
     return Jwts.parser()
             .verifyWith(getKey()) // Verify token signature with the secret key
             .build()
             .parseSignedClaims(token) // Parse the signed claims
             .getPayload(); // Get the claims payload
 }

 /**
  * Validates a JWT token against a UserDetails object.
  * Checks if the token's email matches UserDetails username and if the token is not expired.
  */
 public boolean validateToken(String token, UserDetails userDetails) {
     final String userEmail = extractUserEmail(token);
     // We validate against the username (email) and expiration.
     // Role authorization is handled by Spring Security later.
     return (userEmail.equals(userDetails.getUsername()) && !isTokenExpired(token));
 }

 // Checks if the token's expiration date is before the current date
 private boolean isTokenExpired(String token) {
     return extractExpiration(token).before(new Date());
 }

 // Extracts the expiration date from the token
 private Date extractExpiration(String token) {
     return extractClaim(token, Claims::getExpiration);
 }
}
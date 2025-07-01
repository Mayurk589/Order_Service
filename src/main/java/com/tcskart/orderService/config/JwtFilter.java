package com.tcskart.orderService.config;


import com.tcskart.orderService.bean.UserPrincipal; // Import UserPrincipal
import com.tcskart.orderService.service.JWTService;
import com.tcskart.orderService.service.JwtBasedUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Autowired
    private JWTService jwtService;

    @Autowired
    private JwtBasedUserDetailsService jwtBasedUserDetailsService; // Inject the specialized UDS

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String userEmail = null;
        String userRole = null;
        Long userId = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                userEmail = jwtService.extractUserEmail(token);
                userRole = jwtService.extractRole(token);
                userId = jwtService.extractUserId(token);
                logger.debug("JWT Extracted - Email: {}, Role: {}, UserId: {}", userEmail, userRole, userId);

            } catch (ExpiredJwtException e) {
                logger.warn("JWT Expired: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                response.getWriter().write("Authorization token has expired.");
                return;
            } catch (SignatureException | MalformedJwtException e) {
                logger.warn("JWT Invalid Signature or Malformed: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
                response.getWriter().write("Invalid or malformed authorization token.");
                return;
            } catch (Exception e) {
                logger.error("Error processing JWT: {}", e.getMessage(), e);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
                response.getWriter().write("Error occurred while processing authorization token: " + e.getMessage());
                return;
            }
        }

        // If email is extracted and no authentication exists in context
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Build UserDetails from extracted JWT claims using our specialized UDS
            // This is crucial: we build UserDetails based on token, not DB lookup
            UserDetails userDetails = jwtBasedUserDetailsService.buildUserDetailsFromClaims(userEmail, userRole, userId);

            // Validate token (primarily checks expiration and matching user email)
            if (jwtService.validateToken(token, userDetails)) {
                logger.debug("JWT Validated successfully for user: {}", userEmail);

                // Create an authentication object
                // We use UserPrincipal (which implements UserDetails) here
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // Set authentication details for auditing/logging
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authentication in Spring Security Context
                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.debug("SecurityContextHolder updated with authentication for user: {}", userEmail);

            } else {
                logger.warn("JWT validation failed for user: {}", userEmail);
                // If validation fails (e.g., token doesn't match email, though rare with signature check)
                // Spring Security will default to unauthorized later if no other mechanism handles it
            }
        }

        filterChain.doFilter(request, response); // Continue the filter chain
    }
}
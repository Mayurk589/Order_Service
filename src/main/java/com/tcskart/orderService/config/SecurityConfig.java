package com.tcskart.orderService.config;

import com.tcskart.orderService.service.JwtBasedUserDetailsService; // Use the JWT-based UDS
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // Enables @PreAuthorize
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy; // For stateless sessions
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Needed for PasswordEncoder bean
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // To position our JWT filter

@Configuration
@EnableWebSecurity // Enables Spring Security's web security support
@EnableMethodSecurity // Enables method-level security (e.g., @PreAuthorize)
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter; // Our custom JWT filter

    @Autowired
    private JwtBasedUserDetailsService jwtBasedUserDetailsService; // Our special UserDetailsService

    // PasswordEncoder is still a required bean for DaoAuthenticationProvider,
    // even though we don't do password authentication in this service directly.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    // Defines the AuthenticationProvider that Spring Security will use.
    // In a resource service, it's primarily to integrate with UserDetailsService
    // for setting up the Authentication object.
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(jwtBasedUserDetailsService); // Uses our JWT-based UDS
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for stateless APIs
                .authorizeHttpRequests(authorize -> authorize
                        // --- PATH-BASED ROLE ACCESS RULES ---
                        // Public endpoint - accessible without any token
                        .requestMatchers("/api/products/public").permitAll()

                        // Endpoints requiring ADMIN role
                        // Note: hasRole() automatically adds "ROLE_" prefix if missing, so "ADMIN" -> "ROLE_ADMIN"
                        .requestMatchers("/api/products/admin/**").hasRole("ADMIN")

                        // Endpoints requiring CUSTOMER or ADMIN roles
                        .requestMatchers("/api/products/customer/**").hasAnyRole("CUSTOMER", "ADMIN")

                        // All other requests (any other /api/products path not specified above)
                        // will require some form of authentication (i.e., a valid JWT)
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Essential for JWT (no sessions)
                )
                .authenticationProvider(authenticationProvider()) // Configure the authentication provider
                // Add our custom JWT filter BEFORE Spring's default authentication filter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}

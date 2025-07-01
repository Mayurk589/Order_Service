package com.tcskart.orderService.service;


import com.tcskart.orderService.bean.UserPrincipal; // Use the UserPrincipal for richer details
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


// This UserDetailsService doesn't load users from a database.
// It's a "dummy" implementation that assumes the user details
// are provided via the JWT claims by the JwtFilter.
@Service
public class JwtBasedUserDetailsService implements UserDetailsService {

    // This method is required by the UserDetailsService interface,
    // but in a JWT resource service, you typically don't fetch from a DB here.
    // Our JwtFilter builds the UserDetails using a dedicated method.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // This method should conceptually never be called if the JwtFilter correctly
        // builds the Authentication object directly from JWT claims.
        // If it somehow gets called, we'll return a basic UserDetails.
        return new org.springframework.security.core.userdetails.User(
            username,
            "", // No password needed for a resource service UserDetails
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")) // Provide a default role
        );
    }

    // This is the primary method used by our JwtFilter in the Product Service:
    // It constructs a UserDetails object using data already parsed from the JWT.
    public UserDetails buildUserDetailsFromClaims(String email, String role, Long userId) {
        // We use our custom UserPrincipal to store userId, email, and roles
        return new UserPrincipal(userId, email, role);
    }
}
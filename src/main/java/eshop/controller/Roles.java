package eshop.controller;

import org.springframework.security.core.Authentication;

final class Roles {

    private Roles() {
    }

    static boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }
}

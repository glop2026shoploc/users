package com.glop.users.config.security;

import lombok.Getter;

@Getter
public enum JWTRole {
    ADMIN("ADMIN"),CLIENT("CLIENT"),MERCHANT("MERCHANT");
    private final String role;
    JWTRole(String role) {
        this.role = role;
    }
}

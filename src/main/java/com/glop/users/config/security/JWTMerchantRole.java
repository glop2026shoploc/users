package com.glop.users.config.security;

import lombok.Getter;

@Getter
public enum JWTMerchantRole {
    OWNER("owner"),EMPLOYEE("employee");
    private final String role;
    JWTMerchantRole(String role) {
        this.role = role;
    }
}

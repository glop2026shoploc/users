package com.glop.users.in.restapi.payload.user;

import lombok.Getter;

@Getter
public enum UserRoleEnum {
    MERCHANT("merchant"),CLIENT("client"),ADMIN("admin");
    private final String role;
    UserRoleEnum(String role) {
        this.role = role;
    }
}

package com.glop.users.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationModel {
    String type;
    String token;

    public static AuthenticationModel bearer(String token) {
        return AuthenticationModel.builder()
                .type("Bearer")
                .token(token)
                .build();
    }
}

package com.glop.users.config.security;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;

@Builder
@Data
public class CustomUserDetails {
    private final Collection<? extends GrantedAuthority> authorities;
    private final UUID userId;
    private final String email;

}

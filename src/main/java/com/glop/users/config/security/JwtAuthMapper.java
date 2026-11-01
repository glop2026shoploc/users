package com.glop.users.config.security;

import com.glop.users.domain.model.UserModel;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthMapper {
    public CustomUserDetails toCustomUserDetails(UserModel userModel){
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (userModel.getRoles() != null) {
            for (String role : userModel.getRoles()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            }
        }
        return CustomUserDetails.builder()
                .email(userModel.getEmail())
                .authorities(authorities)
                .build();
    }
}

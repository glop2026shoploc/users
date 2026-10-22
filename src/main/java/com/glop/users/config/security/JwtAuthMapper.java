package com.glop.users.config.security;

import com.glop.users.domain.model.UserModel;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthMapper {
    public CustomUserDetails toCustomUserDetails(UserModel userModel){
        return CustomUserDetails.builder()
                .email(userModel.getEmail())
                .build();
    }
}

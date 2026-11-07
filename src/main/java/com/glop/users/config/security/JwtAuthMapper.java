package com.glop.users.config.security;

import com.glop.users.domain.model.EmployeeRole;
import com.glop.users.domain.model.UserModel;
import com.glop.users.domain.model.UserRole;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtAuthMapper {
    public CustomUserDetails toCustomUserDetails(UserModel userModel){
        return CustomUserDetails.builder()
                .userId(userModel.getUserId())
                .email(userModel.getEmail())
                .authorities(List.of())
                .build();
    }

    public JWTMerchantRole toJWTMerchantRole(EmployeeRole employeeRole) {
        return switch (employeeRole){
            case OWNER -> JWTMerchantRole.OWNER;
            case EMPLOYEE -> JWTMerchantRole.EMPLOYEE;
        };
    }
}

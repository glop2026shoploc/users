package com.glop.users.domain.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.context.annotation.Bean;

@Data
@Builder
public class AuthenticationModel {
    String type;
    String token;
}

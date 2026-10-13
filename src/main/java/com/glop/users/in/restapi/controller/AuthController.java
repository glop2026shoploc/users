package com.glop.users.in.restapi.controller;

import com.glop.users.InvalidCredentialsException;
import com.glop.users.config.security.JwtUtils;
import com.glop.users.in.restapi.payload.CredentialsPayload;
import com.glop.users.in.restapi.payload.TokenResponse;
import com.glop.users.out.jpadata.entity.UserEntity;
import com.glop.users.out.jpadata.repository.UserRepository;
import com.glop.users.domain.port.in.AuthPortIn;
import com.glop.users.in.restapi.mapper.AuthMapper;
import com.glop.users.in.restapi.payload.UserCreationPayload;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@SecurityRequirements
public class AuthController {
    private final AuthPortIn authPortIn;
    private final AuthMapper authMapper;

    @PostMapping("/register")
    public TokenResponse register(@RequestBody UserCreationPayload user) throws BadRequestException {
        return authMapper.toTokenResponse(authPortIn.register(authMapper.toUserModel(user)));

    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody CredentialsPayload credentialsPayload) throws Exception {
        return authMapper.toTokenResponse(authPortIn.login(authMapper.toUserModel(credentialsPayload)));
    }
}

package com.glop.users.in.restapi.controller;

import com.glop.users.in.restapi.payload.auth.CredentialsPayload;
import com.glop.users.in.restapi.payload.auth.TokenResponse;
import com.glop.users.domain.port.in.AuthPortIn;
import com.glop.users.in.restapi.mapper.AuthMapper;
import com.glop.users.in.restapi.payload.auth.UserCreationPayload;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@SecurityRequirements
public class AuthController {
    private final AuthPortIn authPortIn;
    private final AuthMapper authMapper;

    @PostMapping("/register")
    public TokenResponse register(@Valid @RequestBody UserCreationPayload user) throws BadRequestException {
        TokenResponse tokenResponse = authMapper.toTokenResponse(authPortIn.register(authMapper.toUserModel(user)));
        log.info("User account created successfully.");
        return tokenResponse;
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody CredentialsPayload credentialsPayload) throws Exception {
        return authMapper.toTokenResponse(authPortIn.login(authMapper.toUserModel(credentialsPayload)));
    }

}

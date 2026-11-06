package com.glop.users.in.restapi.controller;

import com.glop.users.domain.port.in.AuthPortIn;
import com.glop.users.in.restapi.mapper.AuthMapper;
import com.glop.users.in.restapi.payload.auth.CredentialsPayload;
import com.glop.users.in.restapi.payload.auth.TokenResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@SecurityRequirements
public class AuthController {
    private final AuthPortIn authPortIn;
    private final AuthMapper authMapper;

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody CredentialsPayload credentialsPayload) {
        return authMapper.toTokenResponse(authPortIn.login(authMapper.toCredentialsModel(credentialsPayload)));
    }
}

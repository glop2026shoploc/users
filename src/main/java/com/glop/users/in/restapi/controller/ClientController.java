package com.glop.users.in.restapi.controller;

import com.glop.users.config.security.CustomUserDetails;
import com.glop.users.domain.model.AuthenticationModel;
import com.glop.users.domain.port.in.ClientPortIn;
import com.glop.users.in.restapi.mapper.AddressMapper;
import com.glop.users.in.restapi.mapper.AuthMapper;
import com.glop.users.in.restapi.mapper.ClientMapper;
import com.glop.users.in.restapi.payload.auth.TokenResponse;
import com.glop.users.in.restapi.payload.client.ClientCreationPayload;
import com.glop.users.in.restapi.payload.client.ClientRegistrationPayload;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientPortIn clientPortIn;
    private final ClientMapper clientMapper;
    private final AuthMapper authMapper;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirements
    public TokenResponse register(@Valid @RequestBody ClientRegistrationPayload payload) {
        AuthenticationModel authentication = clientPortIn.register(
                clientMapper.toClient(payload));
        return authMapper.toTokenResponse(authentication);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponse create(@AuthenticationPrincipal CustomUserDetails me, @Valid @RequestBody ClientCreationPayload payload) {
        AuthenticationModel authentication = clientPortIn.create(
                me.getUserId(),clientMapper.toClient(payload));
        return authMapper.toTokenResponse(authentication);
    }
}

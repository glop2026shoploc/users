package com.glop.users.in.restapi.controller;

import com.glop.users.config.security.CustomUserDetails;
import com.glop.users.domain.model.ShopkeeperModel;
import com.glop.users.domain.port.in.ShopkeeperPortIn;
import com.glop.users.in.restapi.mapper.AuthMapper;
import com.glop.users.in.restapi.mapper.ShopkeeperMapper;
import com.glop.users.in.restapi.payload.auth.TokenResponse;
import com.glop.users.in.restapi.payload.shopkeeper.ShopCreationPayload;
import com.glop.users.in.restapi.payload.shopkeeper.ShopkeeperRegistrationPayload;
import com.glop.users.in.restapi.payload.shopkeeper.ShopkeeperResponsePayload;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shopkeeper")
@RequiredArgsConstructor
public class ShopkeeperController {
    private final ShopkeeperPortIn shopkeeperPortIn;
    private final ShopkeeperMapper shopkeeperMapper;
    private final AuthMapper authMapper;

    /** Utilisateur non connecté : crée le compte et le commerce. */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirements
    public TokenResponse register(@Valid @RequestBody ShopkeeperRegistrationPayload payload) {
        ShopkeeperModel shop = shopkeeperMapper.toShopkeeperModel(payload);
        return authMapper.toTokenResponse(shopkeeperPortIn.register(shop));
    }

    /** Utilisateur connecté : rattache un commerce à son compte. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShopkeeperResponsePayload create(@AuthenticationPrincipal CustomUserDetails principal,
                                            @Valid @RequestBody ShopCreationPayload payload) {
        ShopkeeperModel created = shopkeeperPortIn.create(principal.getUserId(), shopkeeperMapper.toShopkeeperModel(payload));
        return shopkeeperMapper.toShopkeeperResponsePayload(created);
    }
}

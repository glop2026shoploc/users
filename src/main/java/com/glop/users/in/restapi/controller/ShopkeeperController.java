package com.glop.users.in.restapi.controller;

import com.glop.users.domain.model.ShopkeeperModel;
import com.glop.users.domain.model.ShopkeeperStatus;
import com.glop.users.domain.port.in.ShopkeeperPortIn;
import com.glop.users.in.restapi.mapper.ShopkeeperMapper;
import com.glop.users.in.restapi.payload.shopkeeper.ShopkeeperRegistrationPayload;
import com.glop.users.in.restapi.payload.shopkeeper.ShopkeeperResponsePayload;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shopkeeper")
@RequiredArgsConstructor
@SecurityRequirements
public class ShopkeeperController {
    private final ShopkeeperPortIn shopkeeperPortIn;
    private final ShopkeeperMapper shopkeeperMapper;

    @PostMapping("/register")
    public ShopkeeperResponsePayload register(@RequestBody ShopkeeperRegistrationPayload shopkeeper) throws BadRequestException {
        ShopkeeperModel shopkeeperModel = shopkeeperMapper.toShopkeeperModel(shopkeeper);
        return shopkeeperMapper.toShopkeeperResponsePayload(shopkeeperPortIn.register(shopkeeperModel));
    }

    @GetMapping("/admin/waiting")
    @SecurityRequirement(name = "bearerAuth")
    public List<ShopkeeperResponsePayload> getWaitingShopkeepers() {
        return shopkeeperMapper.toShopkeeperResponsePayloads(shopkeeperPortIn.getWaitingShopkeepers());
    }

    @PatchMapping("/admin/{shopkeeperId}/status")
    @SecurityRequirement(name = "bearerAuth")
    public ShopkeeperResponsePayload updateStatus(@PathVariable Long shopkeeperId, @RequestParam ShopkeeperStatus status) throws BadRequestException {
        return shopkeeperMapper.toShopkeeperResponsePayload(shopkeeperPortIn.updateStatus(shopkeeperId, status));
    }
}

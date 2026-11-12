package com.glop.users.domain.service;

import com.glop.users.domain.model.*;
import com.glop.users.domain.port.in.ShopkeeperPortIn;
import com.glop.users.domain.port.out.ReferentialPortOut;
import com.glop.users.domain.port.out.ShopPortOut;
import com.glop.users.domain.port.out.ShopkeeperPortOut;
import com.glop.users.domain.port.out.UserPortOut;
import com.glop.users.exception.UnknownReferenceException;
import com.glop.users.exception.UserNotFoundException;
import com.glop.users.exception.credential.ShopAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ShopkeeperService implements ShopkeeperPortIn {
    private final AccountService accountService;
    private final UserPortOut userPortOut;
    private final ShopkeeperPortOut shopkeeperPortOut;
    private final ShopPortOut shopPortOut;
    private final ReferentialPortOut referentialPortOut;
    private final AuthService authService;

    @Override
    public AuthenticationModel register(ShopkeeperModel shop) {
        checkCity(shop); // avant la création du compte : rien n'est créé si la ville est inconnue

        UserModel owner = accountService.createAccount(shop.getOwner());
        create(owner, shop);

        return authService.login(owner.getUserId(), UserRole.MERCHANT);
    }

    @Override
    public ShopkeeperModel create(UUID userId, ShopkeeperModel shop) {
        UserModel owner = userPortOut.findById(userId).orElseThrow(UserNotFoundException::new);
        checkCity(shop);

        return create(owner, shop);
    }

    private void checkCity(ShopkeeperModel shop) {
        if (!referentialPortOut.cityExists(shop.getAddress().getCityModel().getId())) {
            throw new UnknownReferenceException("Ville inconnue.");
        }
    }

    private ShopkeeperModel create(UserModel owner, ShopkeeperModel shop) {
        if (shopkeeperPortOut.existsByOwner(owner.getUserId())) {
            throw new ShopAlreadyExistsException("Cet utilisateur gère déjà un commerce.");
        }
        shop.setOwner(owner);
        shop.setStatus(ShopkeeperStatus.WAITING);

        ShopkeeperModel created = shopPortOut.create(shop);
        shopkeeperPortOut.addOwner(owner.getUserId(), created.getShopId());

        created.setOwner(owner);
        return created;
    }
}

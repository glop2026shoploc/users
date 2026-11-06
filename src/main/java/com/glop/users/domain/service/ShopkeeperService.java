package com.glop.users.domain.service;

import com.glop.users.domain.model.AuthenticationModel;
import com.glop.users.domain.model.ShopkeeperModel;
import com.glop.users.domain.model.ShopkeeperStatus;
import com.glop.users.domain.model.UserModel;
import com.glop.users.domain.port.in.ShopkeeperPortIn;
import com.glop.users.domain.port.out.ReferentialPortOut;
import com.glop.users.domain.port.out.ShopkeeperPortOut;
import com.glop.users.domain.port.out.TokenPortOut;
import com.glop.users.domain.port.out.UserPortOut;
import com.glop.users.exception.ShopAlreadyExistsException;
import com.glop.users.exception.UnknownReferenceException;
import com.glop.users.exception.UserNotFoundException;
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
    private final ReferentialPortOut referentialPortOut;
    private final TokenPortOut tokenPortOut;

    @Override
    public AuthenticationModel register(ShopkeeperModel shop) {
        checkShop(shop); // avant la création du compte : rien n'est créé si le magasin est invalide

        UserModel owner = accountService.createAccount(shop.getOwner());
        create(owner, shop);

        return AuthenticationModel.bearer(tokenPortOut.generateToken(owner));
    }

    @Override
    public ShopkeeperModel create(UUID userId, ShopkeeperModel shop) {
        UserModel owner = userPortOut.findById(userId).orElseThrow(UserNotFoundException::new);
        checkShop(shop);

        return create(owner, shop);
    }

    private void checkShop(ShopkeeperModel shop) {
        if (shopkeeperPortOut.existsBySiret(shop.getSiret())) {
            throw new ShopAlreadyExistsException("Un commerce existe déjà avec ce SIRET.");
        }
        if (!referentialPortOut.categoryExists(shop.getCategoryId())) {
           shop.setCategoryId(null);
        }
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
        return shopkeeperPortOut.create(shop);
    }
}

package com.glop.users.domain.port.in;

import com.glop.users.domain.model.AuthenticationModel;
import com.glop.users.domain.model.ShopkeeperModel;

import java.util.UUID;

public interface ShopkeeperPortIn {
    /** Utilisateur non connecté : crée le compte et le magasin, renvoie un token. */
    AuthenticationModel register(ShopkeeperModel shopkeeperModel);

    /** Utilisateur connecté : rattache un nouveau magasin à son compte. */
    ShopkeeperModel create(UUID userId, ShopkeeperModel shopkeeperModel);
}

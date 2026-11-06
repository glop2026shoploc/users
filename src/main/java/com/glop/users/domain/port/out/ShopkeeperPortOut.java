package com.glop.users.domain.port.out;

import com.glop.users.domain.model.ShopkeeperModel;

import java.util.UUID;

public interface ShopkeeperPortOut {
    boolean existsBySiret(String siret);

    boolean existsByOwner(UUID userId);

    /** Crée l'adresse, le magasin et rattache son propriétaire comme OWNER. */
    ShopkeeperModel create(ShopkeeperModel shopkeeperModel);
}

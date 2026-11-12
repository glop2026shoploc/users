package com.glop.users.domain.port.out;

import java.util.UUID;

/** Rattachement des utilisateurs à leur magasin (table employee). */
public interface ShopkeeperPortOut {
    boolean existsByOwner(UUID userId);

    /** Associe l'utilisateur au magasin {@code shopId} (géré par catalogue) en tant que OWNER. */
    void addOwner(UUID userId, UUID shopId);
}

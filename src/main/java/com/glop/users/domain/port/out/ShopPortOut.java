package com.glop.users.domain.port.out;

import com.glop.users.domain.model.ShopkeeperModel;

/** Les magasins sont gérés par le service catalogue. */
public interface ShopPortOut {
    /**
     * Crée le magasin et son adresse dans catalogue.
     *
     * @throws com.glop.users.exception.credential.ShopAlreadyExistsException si le SIRET est déjà utilisé
     * @throws com.glop.users.exception.CatalogueException si catalogue est injoignable ou refuse la requête
     */
    ShopkeeperModel create(ShopkeeperModel shopkeeperModel);
}

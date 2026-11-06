package com.glop.users.exception;

/** SIRET déjà enregistré, ou utilisateur déjà propriétaire d'un magasin. */
public class ShopAlreadyExistsException extends RuntimeException {
    public ShopAlreadyExistsException(String message) {
        super(message);
    }
}

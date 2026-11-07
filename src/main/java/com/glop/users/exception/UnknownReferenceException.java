package com.glop.users.exception;

/** Une donnée de référence (catégorie, ville…) envoyée par le client n'existe pas. */
public class UnknownReferenceException extends RuntimeException {
    public UnknownReferenceException(String message) {
        super(message);
    }
}

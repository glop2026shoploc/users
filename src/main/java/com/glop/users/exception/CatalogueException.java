package com.glop.users.exception;

/** Le service catalogue est injoignable ou a refusé la requête. */
public class CatalogueException extends RuntimeException {
    public CatalogueException(String message) {
        super(message);
    }

    public CatalogueException(String message, Throwable cause) {
        super(message, cause);
    }
}

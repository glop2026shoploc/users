package com.glop.users.exception;

/** Ce compte est déjà client. */
public class ClientAlreadyExistsException extends RuntimeException {
    public ClientAlreadyExistsException(String message) {
        super(message);
    }
}

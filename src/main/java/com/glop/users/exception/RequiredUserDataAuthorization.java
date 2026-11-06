package com.glop.users.exception;

/** Ce compte est déjà client. */
public class RequiredUserDataAuthorization extends RuntimeException {
    public RequiredUserDataAuthorization(String message) {
        super(message);
    }
}

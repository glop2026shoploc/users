package com.glop.users.exception.credential;

/** Ce compte est déjà client. */
public class InvalidCredentialRoleException extends RuntimeException {
    public InvalidCredentialRoleException(String message) {
        super(message);
    }
    public InvalidCredentialRoleException() {
        super("L'utilisateur n'a pas le role indiqué.");
    }
}

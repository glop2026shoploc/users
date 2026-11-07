package com.glop.users.exception.credential;

/** Ce compte est déjà client. */
public class InvalidCredentialRoleNotClientException extends InvalidCredentialRoleException {
    public InvalidCredentialRoleNotClientException(String message) {
        super(message);
    }
    public InvalidCredentialRoleNotClientException() {
        super("L'utilisateur n'est pas un client.");
    }

}

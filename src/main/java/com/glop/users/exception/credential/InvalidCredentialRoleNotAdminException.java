package com.glop.users.exception.credential;

/** Ce compte est déjà client. */
public class InvalidCredentialRoleNotAdminException extends InvalidCredentialRoleException {
    public InvalidCredentialRoleNotAdminException(String message) {
        super(message);
    }
    public InvalidCredentialRoleNotAdminException() {
        super("L'utilisateur n'est pas un administrateur.");
    }
}

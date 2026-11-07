package com.glop.users.exception.credential;

/** Ce compte est déjà client. */
public class InvalidCredentialRoleNotMerchantException extends InvalidCredentialRoleException {
    public InvalidCredentialRoleNotMerchantException(String message) {
        super(message);
    }
    public InvalidCredentialRoleNotMerchantException() {
        super("L'utilisateur n'est pas un commerçant.");
    }
}

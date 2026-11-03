package com.glop.users.exception;

public class AlreadyRegisterException extends RuntimeException {
    public AlreadyRegisterException(String message) {
        super(message);
    }
    public AlreadyRegisterException() {
        super("Un utilisateur avec le même mail est déjà enregistré.");
    }
}

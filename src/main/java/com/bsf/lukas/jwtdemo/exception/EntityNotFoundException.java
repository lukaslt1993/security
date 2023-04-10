package com.bsf.lukas.jwtdemo.exception;

public class EntityNotFoundException extends EntityException {

    public EntityNotFoundException(String message, Object id) {
        super(message, id);
    }
}

package com.bsf.lukas.jwtdemo.exception;

public class EntityCreationException extends EntityException {

    public EntityCreationException(String message, Object id) {
        super(message, id);
    }
}

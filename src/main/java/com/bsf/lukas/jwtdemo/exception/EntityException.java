package com.bsf.lukas.jwtdemo.exception;

public abstract class EntityException extends RuntimeException {

    public EntityException(String message, Object id) {
        super(String.format("%s; id = %s", message, id));
    }
}

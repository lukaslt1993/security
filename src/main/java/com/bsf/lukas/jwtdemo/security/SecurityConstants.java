package com.bsf.lukas.jwtdemo.security;

import java.util.concurrent.TimeUnit;

public class SecurityConstants {

    public enum Role {
        ADMIN("ADMIN"),
        USER("USER");

        private final String value;

        Role(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static final long EXPIRATION_TIME = TimeUnit.DAYS.toMillis(10);
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

}

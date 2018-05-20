package com.ufoscout.coreutils.auth;

/**
 * Unauthorized Exception thrown when an invalid user calls a protected end point
 *
 * @author Francesco Cina'
 */
public class UnauthenticatedException extends RuntimeException {

        public UnauthenticatedException(String message) {
                super(message);
        }
}

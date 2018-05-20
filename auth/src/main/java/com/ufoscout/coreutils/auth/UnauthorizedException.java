package com.ufoscout.coreutils.auth;

/**
 * Unauthorized Exception thrown when an invalid user calls a protected end point
 *
 * @author Francesco Cina'
 */
public class UnauthorizedException extends RuntimeException {

        public UnauthorizedException(String message) {
                super(message);
        }
}
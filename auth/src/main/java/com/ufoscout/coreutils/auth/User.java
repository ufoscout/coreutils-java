package com.ufoscout.coreutils.auth;

import java.util.List;

/**
 * Context containing the profile of the caller of a protected end point
 *
 * @author Francesco Cina'
 */
public class User<R> {

    private final String username;
    private final R roles;

    public User(String username, R roles) {
        this.username = username;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public R getRoles() {
        return roles;
    }

}

package com.ufoscout.coreutils.auth;

import java.util.List;

/**
 * Context containing the profile of the caller of a protected end point
 *
 * @author Francesco Cina'
 */
public class UserContext {

    private final String username;
    private final String[] roles;

    public UserContext(String username, String[] roles) {
        this.username = username;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public String[] getRoles() {
        return roles;
    }

}

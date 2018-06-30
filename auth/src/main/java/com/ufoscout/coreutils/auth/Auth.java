package com.ufoscout.coreutils.auth;

import java.util.Collections;
import java.util.Map;

/**
 * Context containing the profile of the caller of a protected end point
 *
 * @author Francesco Cina'
 */
public class Auth {

    private static Long DEFAULT_ID = -1L;
    private static String DEFAULT_USERNAME = "";
    private static String[] DEFAULT_ROLES = new String[0];
    private static Map<String, Object> DEFAULT_PROPERTIES = Collections.emptyMap();

    private final Long id;
    private final String username;
    private final String[] roles;
    private final Map<String, Object> properties;

    public Auth() {
        this(DEFAULT_ID, DEFAULT_USERNAME);
    }

    public Auth(Long id, String username) {
        this(id, username, DEFAULT_ROLES);
    }

    public Auth(Long id, String username, String[] roles) {
        this(id, username, roles, DEFAULT_PROPERTIES);
    }

    public Auth(Long id, String username, String[] roles, Map<String, Object> properties) {
        this.id = id;
        this.username = username;
        this.roles = roles;
        this.properties = properties;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String[] getRoles() {
        return roles;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }
}

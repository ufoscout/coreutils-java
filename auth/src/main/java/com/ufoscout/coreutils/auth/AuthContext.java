package com.ufoscout.coreutils.auth;

import java.util.Map;

public final class AuthContext {

    private final User user;
    private final Map<String, String[]> permissionsMap;

    public AuthContext( User user,  Map<String, String[]> permissionsMap) {
        this.user = user;
        this.permissionsMap = permissionsMap;
    }
    
    public final AuthContext isAuthenticated() {
        String username = this.user.getUsername();
        if (username.length() == 0) {
            throw new UnauthenticatedException("User needs to be authenticated.");
        } else {
            return this;
        }
    }

    
    public final AuthContext hasRole( String role) {
        this.isAuthenticated();
        if (this.booleanHasRole(role)) {
            return this;
        } else {
            throw new UnauthorizedException("User [" + this.user.getUsername() + "] does not have the required roles.");
        }
    }

    
    public final AuthContext hasAnyRole( String... roles) {
        this.isAuthenticated();

        for(int i = 0; i < roles.length; ++i) {
            String role = roles[i];
            if (this.booleanHasRole(role)) {
                return this;
            }
        }

        throw new UnauthorizedException("User [" + this.user.getUsername() + "] does not have the required roles.");
    }

    
    public final AuthContext hasAllRoles( String... roles) {
        this.isAuthenticated();

        for(int i = 0; i < roles.length; ++i) {
            String role = roles[i];
            if (!this.booleanHasRole(role)) {
                throw new UnauthorizedException("User [" + this.user.getUsername() + "] does not have the required roles.");
            }
        }

        return this;
    }

    
    public final AuthContext hasPermission( String permission) {
        this.isAuthenticated();
        if (this.booleanHasPermission(permission)) {
            return this;
        } else {
            throw new UnauthorizedException("User [" + this.user.getUsername() + "] does not have the required permissions.");
        }
    }

    
    public final AuthContext hasAnyPermission( String... permissions) {
        this.isAuthenticated();

        for(int i = 0; i < permissions.length; ++i) {
            String permission = permissions[i];
            if (this.booleanHasPermission(permission)) {
                return this;
            }
        }

        throw new UnauthorizedException("User [" + this.user.getUsername() + "] does not have the required permissions.");
    }

    
    public final AuthContext hasAllPermissions( String... permissions) {
        this.isAuthenticated();

        for(int i = 0; i < permissions.length; ++i) {
            String permission = permissions[i];
            if (!this.booleanHasPermission(permission)) {
                throw new UnauthorizedException("User [" + this.user.getUsername() + "] does not have the required permissions.");
            }
        }

        return this;
    }

    private final boolean booleanHasRole(String role) {
        String[] roles = this.user.getRoles();

        for(int var3 = 0; var3 < roles.length; ++var3) {
            String userRole = roles[var3];
            if (userRole.equals(role)) {
                return true;
            }
        }

        return false;
    }

    private final boolean booleanHasPermission(String permission) {
        String[] permissionRoles = this.permissionsMap.get(permission);
        if (permissionRoles != null) {

            for(int i = 0; i < permissionRoles.length; ++i) {
                String permissionRole = permissionRoles[i];
                if (this.booleanHasRole(permissionRole)) {
                    return true;
                }
            }
        }

        return false;
    }

    
    public final User getUser() {
        return this.user;
    }

}

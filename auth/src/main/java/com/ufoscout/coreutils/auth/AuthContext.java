package com.ufoscout.coreutils.auth;

import java.util.ArrayList;
import java.util.List;

public final class AuthContext<R, U extends Auth<R>> {

    private final U user;
    private final List<String> userRoles = new ArrayList<>();
    private final List<String> userPermissions = new ArrayList<>();

    public AuthContext( U user, AuthDecoder<R> authDecoder) {
        this.user = user;
        for (Role role: authDecoder.decode(user.getRoles())) {
            userRoles.add(role.getName());
            for (String permission : role.getPermissions()) {
                userPermissions.add(permission);
            }
        }
    }
    
    public final AuthContext<R, U> isAuthenticated() {
        String username = this.user.getUsername();
        if (username.length() == 0) {
            throw new UnauthenticatedException("User needs to be authenticated.");
        } else {
            return this;
        }
    }

    
    public final AuthContext<R, U> hasRole( String role) {
        this.isAuthenticated();
        if (this.booleanHasRole(role)) {
            return this;
        } else {
            throw new UnauthorizedException("User [" + this.user.getUsername() + "] does not have the required roles.");
        }
    }

    
    public final AuthContext<R, U> hasAnyRole( String... roles) {
        this.isAuthenticated();

        for(int i = 0; i < roles.length; ++i) {
            String role = roles[i];
            if (this.booleanHasRole(role)) {
                return this;
            }
        }

        throw new UnauthorizedException("User [" + this.user.getUsername() + "] does not have the required roles.");
    }

    
    public final AuthContext<R, U> hasAllRoles( String... roles) {
        this.isAuthenticated();

        for(int i = 0; i < roles.length; ++i) {
            String role = roles[i];
            if (!this.booleanHasRole(role)) {
                throw new UnauthorizedException("User [" + this.user.getUsername() + "] does not have the required roles.");
            }
        }

        return this;
    }

    
    public final AuthContext<R, U> hasPermission( String permission) {
        this.isAuthenticated();
        if (this.booleanHasPermission(permission)) {
            return this;
        } else {
            throw new UnauthorizedException("User [" + this.user.getUsername() + "] does not have the required permissions.");
        }
    }

    
    public final AuthContext<R, U> hasAnyPermission( String... permissions) {
        this.isAuthenticated();

        for(int i = 0; i < permissions.length; ++i) {
            String permission = permissions[i];
            if (this.booleanHasPermission(permission)) {
                return this;
            }
        }

        throw new UnauthorizedException("User [" + this.user.getUsername() + "] does not have the required permissions.");
    }

    
    public final AuthContext<R, U> hasAllPermissions( String... permissions) {
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
        return userRoles.contains(role);
    }

    private final boolean booleanHasPermission(String permission) {
        return userPermissions.contains(permission);
    }

    public final U getAuth() {
        return this.user;
    }

}

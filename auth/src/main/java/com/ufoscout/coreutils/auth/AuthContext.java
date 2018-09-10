package com.ufoscout.coreutils.auth;

import java.util.ArrayList;
import java.util.List;

public final class AuthContext {

    private final Auth user;
    private final List<String> userRoles = new ArrayList<>();
    private final List<String> userPermissions = new ArrayList<>();

    public AuthContext( Auth user, RolesProvider authDecoder) {
        this.user = user;
        for (Role role: authDecoder.getByName(user.getRoles())) {
            userRoles.add(role.getName());
            for (String permission : role.getPermissions()) {
                userPermissions.add(permission);
            }
        }
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

    public final AuthContext isOwner(Owned obj) {
        if (this.user.getId().equals(obj.getOwnerId())) {
            return this;
        } else {
            throw new UnauthorizedException("User [" + this.user.getUsername() + "] is not the owner. User id: [" + this.user.getId() + "], owner id: [" + obj.getOwnerId() + "]");
        }
    }

    public final AuthContext isOwnerOrHasRole(Owned obj, String role) {
        if (this.user.getId().equals(obj.getOwnerId())) {
            return this;
        } else {
            if (booleanHasRole(role)) {
                return this;
            }
            throw new UnauthorizedException("User [" + this.user.getUsername() + "] is not the owner and does not have role [" + role + "]. User id: [" + this.user.getId() + "], owner id: [" + obj.getOwnerId() + "]");
        }
    }

    public final AuthContext isOwnerOrHasPermission(Owned obj, String permission) {
        if (this.user.getId().equals(obj.getOwnerId())) {
            return this;
        } else {
            if (booleanHasPermission(permission)) {
                return this;
            }
            throw new UnauthorizedException("User [" + this.user.getUsername() + "] is not the owner and does not have permission [" + permission + "]. User id: [" + this.user.getId() + "], owner id: [" + obj.getOwnerId() + "]");
        }
    }

    private final boolean booleanHasRole(String role) {
        return userRoles.contains(role);
    }

    private final boolean booleanHasPermission(String permission) {
        return userPermissions.contains(permission);
    }

    public final Auth getAuth() {
        return this.user;
    }

}

package com.ufoscout.coreutils.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class AuthContext {

    private final AuthProfile user;

    public AuthContext( Auth user, RolesProvider authDecoder) {
        final List<String> userRoles = new ArrayList<>();
        final List<String> userPermissions = new ArrayList<>();
        for (Role role: authDecoder.getByName(user.getRoles())) {
            userRoles.add(role.getName());
            for (String permission : role.getPermissions()) {
                userPermissions.add(permission);
            }
        }
        this.user = new AuthProfile(user, userRoles, userPermissions);
    }
    
    public final AuthContext isAuthenticated() {
        if (!user.isAuthenticated()) {
            throw new UnauthenticatedException("User needs to be authenticated.");
        } else {
            return this;
        }
    }

    
    public final AuthContext hasRole( String role) {
        if (user.hasRole(role)) {
            return this;
        } else {
            throw new UnauthorizedException("User [" + this.user.auth.getUsername() + "] does not have the required roles.");
        }
    }

    
    public final AuthContext hasAnyRole( String... roles) {
        if (user.hasAnyRole(roles)) {
            return this;
        } else {
            throw new UnauthorizedException("User [" + this.user.auth.getUsername() + "] does not have the required roles.");
        }
    }

    
    public final AuthContext hasAllRoles( String... roles) {
        if (user.hasAllRoles(roles)) {
            return this;
        } else {
            throw new UnauthorizedException("User [" + this.user.auth.getUsername() + "] does not have the required roles.");
        }
    }

    
    public final AuthContext hasPermission( String permission) {
        if (user.hasPermission(permission)) {
            return this;
        } else {
            throw new UnauthorizedException("User [" + this.user.auth.getUsername() + "] does not have the required permissions.");
        }
    }

    
    public final AuthContext hasAnyPermission( String... permissions) {
        if (user.hasAnyPermission(permissions)) {
            return this;
        } else {
            throw new UnauthorizedException("User [" + this.user.auth.getUsername() + "] does not have the required permissions.");
        }
    }

    
    public final AuthContext hasAllPermissions( String... permissions) {
        if (user.hasAllPermissions(permissions)) {
            return this;
        } else {
            throw new UnauthorizedException("User [" + this.user.auth.getUsername() + "] does not have the required permissions.");
        }
    }

    public final AuthContext isOwner(Owned obj) {
        if (user.isOwner(obj)) {
            return this;
        } else {
            throw new UnauthorizedException("User [" + this.user.auth.getUsername() + "] is not the owner. User id: [" + this.user.auth.getId() + "], owner id: [" + obj.getOwnerId() + "]");
        }
    }

    public final AuthContext isOwnerOrHasRole(Owned obj, String role) {
        if (user.isOwner(obj)) {
            return this;
        } else {
            if (user.hasRole(role)) {
                return this;
            }
            throw new UnauthorizedException("User [" + this.user.auth.getUsername() + "] is not the owner and does not have role [" + role + "]. User id: [" + this.user.auth.getId() + "], owner id: [" + obj.getOwnerId() + "]");
        }
    }

    public final AuthContext isOwnerOrHasPermission(Owned obj, String permission) {
        if (user.isOwner(obj)) {
            return this;
        } else {
            if (user.hasPermission(permission)) {
                return this;
            }
            throw new UnauthorizedException("User [" + this.user.auth.getUsername() + "] is not the owner and does not have permission [" + permission + "]. User id: [" + this.user.auth.getId() + "], owner id: [" + obj.getOwnerId() + "]");
        }
    }

    public final Auth getAuth() {
        return this.user.auth;
    }

    final AuthProfile getAuthProfile() {
        return this.user;
    }

    public final AuthContext all(Function<AuthProfile, Boolean> ...auths) {
        if (user.all(auths)) {
            return this;
        }
        throw new UnauthorizedException("User [" + this.user.auth.getUsername() + "] does not have all requirements.");
    }

    public final AuthContext any(Function<AuthProfile, Boolean> ...auths) {
        if (user.any(auths)) {
            return this;
        }
        throw new UnauthorizedException("User [" + this.user.auth.getUsername() + "] has no one of the requirements.");
    }

    public class AuthProfile {
        private final Auth auth;
        private final List<String> userRoles;
        private final List<String> userPermissions;

        AuthProfile(Auth user,
                List<String> userRoles,
                List<String> userPermissions) {
            this.auth = user;
            this.userRoles = userRoles;
            this.userPermissions = userPermissions;
        }


        public final boolean isAuthenticated() {
            return !this.auth.getUsername().isEmpty();
        }


        public final boolean hasRole( String role) {
            return user.userRoles.contains(role);
        }


        public final boolean hasAnyRole( String... roles) {
            for(int i = 0; i < roles.length; ++i) {
                String role = roles[i];
                if (this.hasRole(role)) {
                    return true;
                }
            }
            return false;
        }


        public final boolean hasAllRoles( String... roles) {
            for(int i = 0; i < roles.length; ++i) {
                String role = roles[i];
                if (!this.hasRole(role)) {
                    return false;
                }
            }
            return true;
        }

        public final boolean hasPermission( String permission) {
            return user.userPermissions.contains(permission);
        }


        public final boolean hasAnyPermission( String... permissions) {
            for(int i = 0; i < permissions.length; ++i) {
                String permission = permissions[i];
                if (this.hasPermission(permission)) {
                    return true;
                }
            }
            return false;
        }


        public final boolean hasAllPermissions( String... permissions) {
            for(int i = 0; i < permissions.length; ++i) {
                String permission = permissions[i];
                if (!this.hasPermission(permission)) {
                    return false;
                }
            }
            return true;
        }

        public final boolean isOwner(Owned obj) {
            return this.auth.getId().equals(obj.getOwnerId());
        }

        public final boolean isOwnerWithRoles(Owned obj, String ...roles) {
            return this.auth.getId().equals(obj.getOwnerId()) && this.hasAllRoles(roles);
        }

        public final boolean isOwnerWithPermission(Owned obj, String ...permissions) {
            return this.auth.getId().equals(obj.getOwnerId()) && this.hasAllPermissions(permissions);
        }

        public final Auth getAuth() {
            return this.auth;
        }

        public final boolean all(Function<AuthProfile, Boolean> ...auths) {
            for (Function<AuthProfile, Boolean> auth : auths) {
                if (!auth.apply(user)) {
                    return false;
                }
            }
            return true;
        }

        public final boolean any(Function<AuthProfile, Boolean> ...auths) {
            for (Function<AuthProfile, Boolean> auth : auths) {
                if (auth.apply(user)) {
                    return true;
                }
            }
            return false;
        }
    }

}

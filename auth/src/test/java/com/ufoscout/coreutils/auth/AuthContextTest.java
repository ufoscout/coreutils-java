package com.ufoscout.coreutils.auth;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class AuthContextTest extends BaseTest {

    @Test
    public final void shouldBeAuthenticated() {
        Auth user = new Auth(0L, "name", new String[0]);
        AuthContext authContext = new AuthContext(user, new Dec(new HashMap<>()));
        authContext.isAuthenticated();
    }

    @Test
    public final void shouldBeNotAuthenticated() {
        assertThrows(UnauthenticatedException.class,
                ()->{
                    Auth user = new Auth(0L, "", new String[0]);
                    AuthContext authContext = new AuthContext(user, new Dec());
                    authContext.isAuthenticated();
                });
    }

    @Test
    public final void shouldHaveRole() {
        Auth user = new Auth(0L, "name", new String[]{"ADMIN"});
        AuthContext authContext = new AuthContext(user, new Dec());
        authContext.hasRole("ADMIN");
    }

    @Test
    public final void shouldHaveRole2() {
        Auth user = new Auth(0L, "name", new String[]{"ADMIN", "USER"});
        AuthContext authContext = new AuthContext(user, new Dec());
        authContext.hasRole("USER");
    }

    @Test
    public final void shouldNotHaveRole() {
        assertThrows(UnauthorizedException.class,
                ()->{
                    Auth user = new Auth(0L, "name", new String[]{"ADMIN"});
                    AuthContext authContext = new AuthContext(user, new Dec());
                    authContext.hasRole("USER");
                });
    }

    @Test
    public final void shouldHaveAnyRole() {
        Auth user = new Auth(0L, "name", new String[]{"ADMIN", "USER"});
        AuthContext authContext = new AuthContext(user, new Dec());
        authContext.hasAnyRole(new String[]{"USER", "FRIEND"});
    }

    @Test
    public final void shouldNotHaveAnyRole() {
        assertThrows(UnauthorizedException.class,
                ()->{
                    Auth user = new Auth(0L, "name", new String[]{"ADMIN", "OWNER"});
                    AuthContext authContext = new AuthContext(user, new Dec());
                    authContext.hasAnyRole(new String[]{"USER", "FRIEND"});
                });
    }

    @Test
    public final void shouldHaveAllRoles() {
        Auth user = new Auth(0L, "name", new String[]{"ADMIN", "USER", "FRIEND"});
        AuthContext authContext = new AuthContext(user, new Dec());
        authContext.hasAllRoles(new String[]{"USER", "FRIEND"});
    }

    @Test
    public final void shouldNotHaveAllRoles() {
        assertThrows(UnauthorizedException.class,
                ()->{
                    Auth user = new Auth(0L, "name", new String[]{"ADMIN", "USER"});
                    AuthContext authContext = new AuthContext(user, new Dec());
                    authContext.hasAllRoles(new String[]{"USER", "FRIEND"});
                });
    }

    @Test
    public final void shouldHavePermissions() {
        Map<String, List<String>> permissions = new HashMap<>();
        permissions.put("delete", Arrays.asList("OWNER", "ADMIN"));
        Auth user = new Auth(0L, "name", new String[]{"ADMIN"});
        AuthContext authContext = new AuthContext(user, new Dec(permissions));
        authContext.hasPermission("delete");
    }

    @Test
    public final void shouldHavePermission2() {
        Map<String, List<String>> permissions = new HashMap<>();
        permissions.put("delete", Arrays.asList("OWNER", "ADMIN"));
        Auth user = new Auth(0L, "name", new String[]{"ADMIN", "USER"});
        AuthContext authContext = new AuthContext(user, new Dec(permissions));
        authContext.hasPermission("delete");
    }

    @Test
    public final void shouldNotHavePermission() {
        assertThrows(UnauthorizedException.class,
                ()->{
                    Map<String, List<String>> permissions = new HashMap<>();
                    permissions.put("delete", Arrays.asList("OWNER"));
                    Auth user = new Auth(0L, "name", new String[]{"ADMIN", "USER"});
                    AuthContext authContext = new AuthContext(user, new Dec(permissions));
                    authContext.hasPermission("delete");
                });
    }

    @Test
    public final void shouldHaveAnyPermission() {
        Map<String, List<String>> permissions = new HashMap<>();
        permissions.put("delete", Arrays.asList("OWNER"));
        permissions.put("superDelete", Arrays.asList("ADMIN"));
        Auth user = new Auth(0L, "name", new String[]{"ADMIN", "USER"});
        AuthContext authContext = new AuthContext(user, new Dec(permissions));
        authContext.hasAnyPermission("delete", "superDelete");
    }

    @Test
    public final void shouldNotHaveAnyPermission() {
        assertThrows(UnauthorizedException.class,
                ()->{
                    Map<String, List<String>> permissions = new HashMap<>();
                    permissions.put("delete", Arrays.asList("OWNER", "ADMIN"));
                    permissions.put("superDelete", Arrays.asList("ADMIN"));
                    Auth user = new Auth(0L, "name", new String[]{"USER"});
                    AuthContext authContext = new AuthContext(user, new Dec(permissions));
                    authContext.hasAnyPermission("delete", "superAdmin");
                });
    }

    @Test
    public final void shouldHaveAllPermissions() {
        Map<String, List<String>> permissions = new HashMap<>();
        permissions.put("delete", Arrays.asList("OWNER", "USER"));
        permissions.put("superDelete", Arrays.asList("ADMIN"));
        Auth user = new Auth(0L, "name", new String[]{"ADMIN", "USER"});
        AuthContext authContext = new AuthContext(user, new Dec(permissions));
        authContext.hasAllPermissions("delete", "superDelete");
    }

    @Test
    public final void shouldNotHaveAllPermissions() {
        assertThrows(UnauthorizedException.class,
                ()->{
                    Map<String, List<String>> permissions = new HashMap<>();
                    permissions.put("delete", Arrays.asList("OWNER"));
                    permissions.put("superDelete", Arrays.asList("ADMIN"));
                    Auth user = new Auth(0L, "name", new String[]{"ADMIN", "USER"});
                    AuthContext authContext = new AuthContext(user, new Dec(permissions));
                    authContext.hasAllPermissions(new String[]{"delete", "superDelete"});
                });
    }

    @Test
    public final void shouldBeTheOwner() {
        Map<String, List<String>> permissions = new HashMap<>();
        Auth user = new Auth(0L, "name", new String[]{""});
        AuthContext authContext = new AuthContext(user, new Dec(permissions));
        assertNotNull(authContext.isOwner(new Owneable(0L)));
    }

    @Test
    public final void shouldNotBeTheOwner() {
        assertThrows(UnauthorizedException.class,
                ()-> {
                    Map<String, List<String>> permissions = new HashMap<>();
                    Auth user = new Auth(0L, "name", new String[]{""});
                    AuthContext authContext = new AuthContext(user, new Dec(permissions));
                    assertNotNull(authContext.isOwner(new Owneable(1L)));
                });
    }

    @Test
    public final void shouldBeAllowedIfNotTheOwnerButHasRole() {
        Map<String, List<String>> permissions = new HashMap<>();
        Auth user = new Auth(0L, "name", new String[]{"ROLE_1"});
        AuthContext authContext = new AuthContext(user, new Dec(permissions));
        assertNotNull(authContext.isOwnerOrHasRole(new Owneable(1L), "ROLE_1"));
    }

    @Test
    public final void shouldBeAllowedIfTheOwnerButNotHasRole() {
        Map<String, List<String>> permissions = new HashMap<>();
        Auth user = new Auth(0L, "name", new String[]{"ROLE_1"});
        AuthContext authContext = new AuthContext(user, new Dec(permissions));
        assertNotNull(authContext.isOwnerOrHasRole(new Owneable(0L), "ROLE_2"));
    }

    @Test
    public final void shouldNotBeAllowedBecauseNotTheOwnerAndNotTheRole() {
        assertThrows(UnauthorizedException.class,
                ()-> {
                    Map<String, List<String>> permissions = new HashMap<>();
                    Auth user = new Auth(0L, "name", new String[]{"ROLE_1"});
                    AuthContext authContext = new AuthContext(user, new Dec(permissions));
                    assertNotNull(authContext.isOwnerOrHasRole(new Owneable(1L), "ROLE_2"));
                });
    }

    @Test
    public final void shouldBeAllowedIfNotTheOwnerButHasPermission() {
        Map<String, List<String>> permissions = new HashMap<>();
        permissions.put("access", Arrays.asList("ROLE_1"));
        Auth user = new Auth(0L, "name", new String[]{"ROLE_1"});
        AuthContext authContext = new AuthContext(user, new Dec(permissions));
        assertNotNull(authContext.isOwnerOrHasPermission(new Owneable(1L), "access"));
    }

    @Test
    public final void shouldBeAllowedIfTheOwnerButNotHasPermission() {
        Map<String, List<String>> permissions = new HashMap<>();
        permissions.put("access", Arrays.asList("ROLE_1"));
        Auth user = new Auth(0L, "name", new String[]{"ROLE_1"});
        AuthContext authContext = new AuthContext(user, new Dec(permissions));
        assertNotNull(authContext.isOwnerOrHasPermission(new Owneable(0L), "access_2"));
    }

    @Test
    public final void shouldNotBeAllowedBecauseNotTheOwnerAndNotThePermission() {
        assertThrows(UnauthorizedException.class,
                ()-> {
                    Map<String, List<String>> permissions = new HashMap<>();
                    permissions.put("access", Arrays.asList("ROLE_1"));
                    Auth user = new Auth(0L, "name", new String[]{"ROLE_1"});
                    AuthContext authContext = new AuthContext(user, new Dec(permissions));
                    assertNotNull(authContext.isOwnerOrHasPermission(new Owneable(1L), "access_2"));
                });
    }

    @Test
    public final void shouldMatchAll() {
        Auth user = new Auth(110L, "name", new String[]{"ADMIN", "USER"});
        Owneable obj = new Owneable(110L);
        AuthContext authContext = new AuthContext(user, new Dec());

        authContext.isAuthenticated()
                .all(
                        (auth) -> auth.isOwnerWithRoles(obj, "USER"),
                        (auth) -> auth.hasAllRoles("ADMIN", "USER")
                );
    }

    @Test
    public final void shouldNotMatchAll() {
        assertThrows(UnauthorizedException.class,
                ()->{
                    Auth user = new Auth(110L, "name", new String[]{"USER"});
                    Owneable obj = new Owneable(110L);
                    AuthContext authContext = new AuthContext(user, new Dec());

                    authContext.isAuthenticated()
                            .all(
                                    (auth) -> auth.isOwner(obj),
                                    (auth) -> auth.hasAllRoles("ADMIN", "USER")
                            );
        });
    }

    @Test
    public final void shouldMatchAny() {
        Auth user = new Auth(110L, "name", new String[]{"ADMIN", "USER"});
        Owneable obj = new Owneable(220L);
        AuthContext authContext = new AuthContext(user, new Dec());

        authContext.isAuthenticated()
                .any(
                        (auth) -> auth.isOwnerWithRoles(obj, "USER"),
                        (auth) -> auth.hasAnyRole("ADMIN", "ONE", "TWO")
                );
    }

    @Test
    public final void shouldNotMatchAny() {
        assertThrows(UnauthorizedException.class,
                ()->{
                    Auth user = new Auth(110L, "name", new String[]{"ADMIN", "USER"});
                    Owneable obj = new Owneable(220L);
                    AuthContext authContext = new AuthContext(user, new Dec());

                    authContext.isAuthenticated()
                            .any(
                                    (auth) -> auth.isOwner(obj),
                                    (auth) -> auth.hasAnyPermission("ADMIN")
                            );
        });
    }

    class Dec implements RolesProvider {

        private final Map<String, List<String>> permissions;

        Dec() { this(new HashMap<>()); }
        Dec(Map<String, List<String>> permissions) { this.permissions = permissions; }

        @Override
        public List<Role> getAll() {
            return null;
        }

        @Override
        public List<Role> getByName(String... userRoles) {
            List<Role> result = new ArrayList<>();

            for (String userRole : userRoles) {
                List<String> userPerms = new ArrayList<>();
                for ( Entry<String, List<String>> perm : permissions.entrySet() ) {
                    if (perm.getValue().contains(userRole)) {
                        userPerms.add(perm.getKey());
                    }
                }
                result.add(new Role(userRole, userPerms.toArray(new String[0])));
            }
            return result;
        }
    }



    class Owneable implements Owned {

        private Long ownerId;

        Owneable(Long ownerId) {
            this.ownerId = ownerId;
        }

        @Override
        public Long getOwnerId() {
            return this.ownerId;
        }
    }

}
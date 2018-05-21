package com.ufoscout.coreutils.auth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class AuthContextTest extends BaseTest {

    @Test
    public final void shouldBeAuthenticated() {
        User<String[]> user = new User<>("name", new String[0]);
        AuthContext<String[]> authContext = new AuthContext<String[]>(user, new Dec(new HashMap<>()));
        authContext.isAuthenticated();
    }

    @Test
    public final void shouldBeNotAuthenticated() {
        assertThrows(UnauthenticatedException.class,
                ()->{
                    User user = new User("", new String[0]);
                    AuthContext authContext = new AuthContext(user, new Dec());
                    authContext.isAuthenticated();
                });
    }

    /*
    @Test
    public final void shouldBeNotAuthenticatedEvenIfHasRole() {
        assertThrows(UnauthenticatedException.class,
                ()->{
                    User user = new User("", new String[]{"ADMIN"});
                    AuthContext authContext = new AuthContext(user, new Dec());
                    authContext.hasRole("ADMIN");
                });
    }

    @Test
    public final void shouldHaveRole() {
        User user = new User("name", new String[]{"ADMIN"});
        AuthContext authContext = new AuthContext(user, new Dec());
        authContext.hasRole("ADMIN");
    }

    @Test
    public final void shouldHaveRole2() {
        User user = new User("name", new String[]{"ADMIN", "USER"});
        AuthContext authContext = new AuthContext(user, new Dec());
        authContext.hasRole("USER");
    }

    @Test
    public final void shouldNotHaveRole() {
        assertThrows(UnauthorizedException.class,
                ()->{
                    User user = new User("name", new String[]{"ADMIN"});
                    AuthContext authContext = new AuthContext(user, new HashMap<>());
                    authContext.hasRole("USER");
                });
    }

    @Test
    public final void shouldHaveAnyRole() {
        User user = new User("name", new String[]{"ADMIN", "USER"});
        AuthContext authContext = new AuthContext(user, new HashMap<>());
        authContext.hasAnyRole(new String[]{"USER", "FRIEND"});
    }

    @Test
    public final void shouldNotHaveAnyRole() {
        assertThrows(UnauthorizedException.class,
                ()->{
                    User user = new User("name", new String[]{"ADMIN", "OWNER"});
                    AuthContext authContext = new AuthContext(user, new HashMap<>());
                    authContext.hasAnyRole(new String[]{"USER", "FRIEND"});
                });
    }

    @Test
    public final void shouldHaveAllRoles() {
        User user = new User("name", new String[]{"ADMIN", "USER", "FRIEND"});
        AuthContext authContext = new AuthContext(user, new HashMap<>());
        authContext.hasAllRoles(new String[]{"USER", "FRIEND"});
    }

    @Test
    public final void shouldNotHaveAllRoles() {
        assertThrows(UnauthorizedException.class,
                ()->{
                    User user = new User("name", new String[]{"ADMIN", "USER"});
                    AuthContext authContext = new AuthContext(user, new HashMap<>());
                    authContext.hasAllRoles(new String[]{"USER", "FRIEND"});
                });
    }

    @Test
    public final void shouldBeNotAuthenticatedEvenIfHasPermission() {
        assertThrows(UnauthenticatedException.class,
                ()->{
                    Map<String, String[]> permissions = new HashMap<>();
                    permissions.put("delete", new String[]{"OWNER", "ADMIN"});
                    User user = new User("", new String[]{"ADMIN"});
                    AuthContext authContext = new AuthContext(user, permissions);
                    authContext.hasPermission("delete");
                });
    }

    @Test
    public final void shouldHavePermissions() {
        Map<String, String[]> permissions = new HashMap<>();
        permissions.put("delete", new String[]{"OWNER", "ADMIN"});
        User user = new User("name", new String[]{"ADMIN"});
        AuthContext authContext = new AuthContext(user, (Map)permissions);
        authContext.hasPermission("delete");
    }

    @Test
    public final void shouldHavePermission2() {
        Map<String, String[]> permissions = new HashMap<>();
        permissions.put("delete", new String[]{"OWNER", "ADMIN"});
        User user = new User("name", new String[]{"ADMIN", "USER"});
        AuthContext authContext = new AuthContext(user, (Map)permissions);
        authContext.hasPermission("delete");
    }

    @Test
    public final void shouldNotHavePermission() {
        assertThrows(UnauthorizedException.class,
                ()->{
                    Map<String, String[]> permissions = new HashMap<>();
                    permissions.put("delete", new String[]{"OWNER"});
                    User user = new User("name", new String[]{"ADMIN", "USER"});
                    AuthContext authContext = new AuthContext(user, (Map)permissions);
                    authContext.hasPermission("delete");
                });
    }

    @Test
    public final void shouldHaveAnyPermission() {
        Map<String, String[]> permissions = new HashMap<>();
        permissions.put("delete", new String[]{"OWNER"});
        permissions.put("superDelete", new String[]{"ADMIN"});
        User user = new User("name", new String[]{"ADMIN", "USER"});
        AuthContext authContext = new AuthContext(user, (Map)permissions);
        authContext.hasAnyPermission(new String[]{"delete", "superDelete"});
    }

    @Test
    public final void shouldNotHaveAnyPermission() {
        assertThrows(UnauthorizedException.class,
                ()->{
                    Map<String, String[]> permissions = new HashMap<>();
                    permissions.put("delete", new String[]{"OWNER", "ADMIN"});
                    permissions.put("superDelete", new String[]{"ADMIN"});
                    User user = new User("name", new String[]{"USER"});
                    AuthContext authContext = new AuthContext(user, (Map)permissions);
                    authContext.hasAnyPermission(new String[]{"delete", "superAdmin"});
                });
    }

    @Test
    public final void shouldHaveAllPermissions() {
        Map<String, String[]> permissions = new HashMap<>();
        permissions.put("delete", new String[]{"OWNER", "USER"});
        permissions.put("superDelete", new String[]{"ADMIN"});
        User user = new User("name", new String[]{"ADMIN", "USER"});
        AuthContext authContext = new AuthContext(user, (Map)permissions);
        authContext.hasAllPermissions(new String[]{"delete", "superDelete"});
    }

    @Test
    public final void shouldNotHaveAllPermissions() {
        assertThrows(UnauthorizedException.class,
                ()->{
                    Map<String, String[]> permissions = new HashMap<>();
                    permissions.put("delete", new String[]{"OWNER"});
                    permissions.put("superDelete", new String[]{"ADMIN"});
                    User user = new User("name", new String[]{"ADMIN", "USER"});
                    AuthContext authContext = new AuthContext(user, (Map)permissions);
                    authContext.hasAllPermissions(new String[]{"delete", "superDelete"});
                });
    }
    */

    class Dec implements AuthDecoder<String[]> {

        private final Map<String, String[]> roles;

        Dec() { this(new HashMap<>()); }
        Dec(Map<String, String[]> roles) { this.roles = roles; }

        @Override
        public List<Role> decode(String[] userRoles) {
            int count = 0;
            List<Role> result = new ArrayList<>();

            for (String userRole : userRoles) {
                if (roles.containsKey(userRole)) {
                    result.add(new Role(count++, userRole, roles.get(userRole)));
                }
            }
            return result;
        }
    }

}
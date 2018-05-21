package com.ufoscout.coreutils.auth;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;

public final class AuthContextTest extends BaseTest {

    @Test
    public final void shouldBeAuthenticated() {
        Auth<String[]> user = new User("name", new String[0]);
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
                    AuthContext authContext = new AuthContext(user, new Dec());
                    authContext.hasRole("USER");
                });
    }

    @Test
    public final void shouldHaveAnyRole() {
        User user = new User("name", new String[]{"ADMIN", "USER"});
        AuthContext authContext = new AuthContext(user, new Dec());
        authContext.hasAnyRole(new String[]{"USER", "FRIEND"});
    }

    @Test
    public final void shouldNotHaveAnyRole() {
        assertThrows(UnauthorizedException.class,
                ()->{
                    User user = new User("name", new String[]{"ADMIN", "OWNER"});
                    AuthContext authContext = new AuthContext(user, new Dec());
                    authContext.hasAnyRole(new String[]{"USER", "FRIEND"});
                });
    }

    @Test
    public final void shouldHaveAllRoles() {
        User user = new User("name", new String[]{"ADMIN", "USER", "FRIEND"});
        AuthContext authContext = new AuthContext(user, new Dec());
        authContext.hasAllRoles(new String[]{"USER", "FRIEND"});
    }

    @Test
    public final void shouldNotHaveAllRoles() {
        assertThrows(UnauthorizedException.class,
                ()->{
                    User user = new User("name", new String[]{"ADMIN", "USER"});
                    AuthContext authContext = new AuthContext(user, new Dec());
                    authContext.hasAllRoles(new String[]{"USER", "FRIEND"});
                });
    }

    @Test
    public final void shouldBeNotAuthenticatedEvenIfHasPermission() {
        assertThrows(UnauthenticatedException.class,
                ()->{
                    Map<String, List<String>> permissions = new HashMap<>();
                    permissions.put("delete", Arrays.asList("OWNER", "ADMIN"));
                    User user = new User("", new String[]{"ADMIN"});
                    AuthContext authContext = new AuthContext(user, new Dec(permissions));
                    authContext.hasPermission("delete");
                });
    }

    @Test
    public final void shouldHavePermissions() {
        Map<String, List<String>> permissions = new HashMap<>();
        permissions.put("delete", Arrays.asList("OWNER", "ADMIN"));
        User user = new User("name", new String[]{"ADMIN"});
        AuthContext authContext = new AuthContext(user, new Dec(permissions));
        authContext.hasPermission("delete");
    }

    @Test
    public final void shouldHavePermission2() {
        Map<String, List<String>> permissions = new HashMap<>();
        permissions.put("delete", Arrays.asList("OWNER", "ADMIN"));
        User user = new User("name", new String[]{"ADMIN", "USER"});
        AuthContext authContext = new AuthContext(user, new Dec(permissions));
        authContext.hasPermission("delete");
    }

    @Test
    public final void shouldNotHavePermission() {
        assertThrows(UnauthorizedException.class,
                ()->{
                    Map<String, List<String>> permissions = new HashMap<>();
                    permissions.put("delete", Arrays.asList("OWNER"));
                    User user = new User("name", new String[]{"ADMIN", "USER"});
                    AuthContext authContext = new AuthContext(user, new Dec(permissions));
                    authContext.hasPermission("delete");
                });
    }

    @Test
    public final void shouldHaveAnyPermission() {
        Map<String, List<String>> permissions = new HashMap<>();
        permissions.put("delete", Arrays.asList("OWNER"));
        permissions.put("superDelete", Arrays.asList("ADMIN"));
        User user = new User("name", new String[]{"ADMIN", "USER"});
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
                    User user = new User("name", new String[]{"USER"});
                    AuthContext authContext = new AuthContext(user, new Dec(permissions));
                    authContext.hasAnyPermission("delete", "superAdmin");
                });
    }

    @Test
    public final void shouldHaveAllPermissions() {
        Map<String, List<String>> permissions = new HashMap<>();
        permissions.put("delete", Arrays.asList("OWNER", "USER"));
        permissions.put("superDelete", Arrays.asList("ADMIN"));
        User user = new User("name", new String[]{"ADMIN", "USER"});
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
                    User user = new User("name", new String[]{"ADMIN", "USER"});
                    AuthContext authContext = new AuthContext(user, new Dec(permissions));
                    authContext.hasAllPermissions(new String[]{"delete", "superDelete"});
                });
    }

    @Getter
    @AllArgsConstructor
    class User implements Auth<String[]> {
        private String username;
        private String[] roles;
    }

    class Dec implements AuthDecoder<String[]> {

        private final Map<String, List<String>> permissions;

        Dec() { this(new HashMap<>()); }
        Dec(Map<String, List<String>> permissions) { this.permissions = permissions; }

        @Override
        public List<Role> decode(String[] userRoles) {
            int count = 0;
            List<Role> result = new ArrayList<>();

            for (String userRole : userRoles) {
                List<String> userPerms = new ArrayList<>();
                for ( Entry<String, List<String>> perm : permissions.entrySet() ) {
                    if (perm.getValue().contains(userRole)) {
                        userPerms.add(perm.getKey());
                    }
                }
                result.add(new Role(count++, userRole, userPerms.toArray(new String[0])));
            }
            return result;
        }
    }

}
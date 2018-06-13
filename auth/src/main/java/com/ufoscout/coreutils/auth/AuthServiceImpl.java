package com.ufoscout.coreutils.auth;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class AuthServiceImpl<R, U extends Auth<R>> implements AuthService<R, U> {

    private final RolesProvider provider;
    private final RolesEncoder<R> encoder;
    private volatile RoleStore store;

    public AuthServiceImpl(RolesProvider provider, RolesEncoder<R> encoder) {
        this.provider = provider;
        this.encoder = encoder;
    }

    @Override
    public void start() {
        refresh();
    }

    @Override
    public void refresh() {
        synchronized (this) {

            List<Role> newRoles = provider.getAll();
            int max = newRoles.stream().mapToInt(Role::getId).max().orElse(0);

            List<Role> roles = new ArrayList<>();
            Map<String, Role> rolesMap = new HashMap<>();
            Role[] rolesArray = new Role[max+1];

            roles.addAll(provider.getAll());

            roles.forEach(role -> {
                rolesArray[role.getId()] = role;
                rolesMap.put(role.getName(), role);
            });

            this.store = new RoleStore(Collections.unmodifiableList(roles), Collections.unmodifiableMap(rolesMap), rolesArray);
        }
    }

    @Override
    public R encode(String... roleNames) {
        return encoder.encode(store, roleNames);
    }

    @Override
    public List<Role> decode(R roles) {
        return encoder.decode(store, roles);
    }

    @Override
    public AuthContext<R, U> auth(U user) {
        return new AuthContext<>(user, this);
    }

    RoleStore getRolesStore() {
        return this.store;
    }
}

package com.ufoscout.coreutils.auth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthServiceImpl<R> implements AuthService<R> {

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
    public AuthContext<R> auth(User<R> user) {
        return new AuthContext<>(user, this);
    }

    RoleStore getRolesStore() {
        return this.store;
    }
}

package com.ufoscout.coreutils.auth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthServiceImpl implements AuthService {

    private final RolesProvider provider;
    private volatile RoleStore store;
    private volatile RolesEncoder encoder;

    public AuthServiceImpl(RolesProvider provider) {
        this.provider = provider;
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
            this.encoder = new RolesEncoderImpl(this.store);
        }
    }

    @Override
    public RolesEncoder encoder() {
        return encoder;
    }

    @Override
    public AuthContext auth(User user) {
        return null;
    }

    RoleStore getRolesStore() {
        return this.store;
    }
}

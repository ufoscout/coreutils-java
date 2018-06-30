package com.ufoscout.coreutils.auth;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class AuthServiceImpl implements AuthService {

    private final RolesProvider provider;
    private volatile RoleStore store;

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
        }
    }

    @Override
    public AuthContext auth(Auth user) {
        return new AuthContext(user, this);
    }

    RoleStore getRolesStore() {
        return this.store;
    }

    @Override
    public List<Role> getAll() {
        return provider.getAll();
    }

    @Override
    public List<Role> getByName(String... roles) {
        return provider.getByName(roles);
    }
}

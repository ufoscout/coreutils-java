package com.ufoscout.coreutils.auth;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RolesEncoderImpl implements RolesEncoder {

    private final RoleStore store;

    RolesEncoderImpl(RoleStore store) {
        this.store = store;
    }

    @Override
    public long encode(String... roleNames) {
        long result = 0l;
        for (String roleName : roleNames) {
            Role role = store.byName.get(roleName);
            if (role == null) {
                log.warn("Role [{}] does not exists", roleName);
            } else {
                result = result | (1l << role.getId());
            }
        }
        return result;
    }

    @Override
    public List<Role> decode(long roles) {
        List<Role> result = new ArrayList<>();
        for (int i=0; i<store.byId.length; i++) {
            if ((roles & (1l << i)) != 0) {
                result.add(store.byId[i]);
            }
        }
        return result;
    }
}

package com.ufoscout.coreutils.auth;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RolesEncoderToLong implements RolesEncoder<Long> {

    @Override
    public Long encode(RoleStore store, String... roleNames) {
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
    public List<Role> decode(RoleStore store, Long roles) {
        long longRole = roles.longValue();
        List<Role> result = new ArrayList<>();
        for (int i=0; i<store.byId.length; i++) {
            if ((longRole & (1l << i)) != 0) {
                result.add(store.byId[i]);
            }
        }
        return result;
    }
}

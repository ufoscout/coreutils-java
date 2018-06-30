package com.ufoscout.coreutils.auth;

import java.util.List;

public interface RolesProvider {

    List<Role> getAll();

    /**
     * Returns the list of {@link Role}s by name
     * @param roles
     * @return
     */
    List<Role> getByName(String... roles);

}

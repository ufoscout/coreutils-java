package com.ufoscout.coreutils.auth;

import java.util.List;

public interface RolesEncoder<R> {

    /**
     * Returns the encoded representation of a set of {@link Role}s.
     *
     * @param roleNames
     * @return
     */
    R encode(RoleStore store, String... roleNames);

    /**
     * Returns the list of {@link Role}s from the encoded representation
     * @param roles
     * @return
     */
    List<Role> decode(RoleStore store, R roles);

}

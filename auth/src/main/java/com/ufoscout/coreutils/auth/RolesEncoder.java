package com.ufoscout.coreutils.auth;

import java.util.List;

public interface RolesEncoder {

    /**
     * Returns a numeric representation of a set of {@link Role}s.
     *
     * @param roleNames
     * @return
     */
    long encode(String... roleNames);

    /**
     * Returns the list of {@link Role}s from a numerical representation
     * @param roles
     * @return
     */
    List<Role> decode(long roles);

}

package com.ufoscout.coreutils.auth;

import java.util.List;

@FunctionalInterface
public interface AuthDecoder<R> {

    /**
     * Returns the list of {@link Role}s from the encoded representation
     * @param roles
     * @return
     */
    List<Role> decode(R roles);

}

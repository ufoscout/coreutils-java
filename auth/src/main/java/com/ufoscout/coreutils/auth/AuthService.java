package com.ufoscout.coreutils.auth;

public interface AuthService<R, U extends Auth<R>> extends AuthDecoder<R> {

    void start();

    void refresh();

    /**
     * Returns the encoded representation of a set of {@link Role}s.
     *
     * @param roleNames
     * @return
     */
    R encode(String... roleNames);

    AuthContext<R, U> auth(U user);

}

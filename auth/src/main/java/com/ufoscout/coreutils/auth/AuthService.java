package com.ufoscout.coreutils.auth;

public interface AuthService {

    void start();

    void refresh();

    RolesEncoder encoder();

    AuthContext auth(User user);

}

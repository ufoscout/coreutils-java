package com.ufoscout.coreutils.auth;

public interface AuthService extends RolesProvider {

    void start();

    void refresh();

    AuthContext auth(Auth user);

}

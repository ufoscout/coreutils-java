package com.ufoscout.coreutils.auth;

import java.util.List;

@FunctionalInterface
public interface RolesProvider {

    List<Role> getAll();

}

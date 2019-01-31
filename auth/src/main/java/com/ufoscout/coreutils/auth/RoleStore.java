package com.ufoscout.coreutils.auth;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class RoleStore {

    public final List<Role> list;
    public final Map<String, Role> byName;

}

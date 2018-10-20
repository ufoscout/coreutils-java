package com.ufoscout.coreutils.auth;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RoleStore {

    public final List<Role> list;
    public final Map<String, Role> byName;

}

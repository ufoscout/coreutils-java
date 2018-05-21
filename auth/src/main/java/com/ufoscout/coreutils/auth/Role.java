package com.ufoscout.coreutils.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Role {

    private int id;
    private String name;
    private String[] permissions;

}

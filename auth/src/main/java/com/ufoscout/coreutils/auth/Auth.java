package com.ufoscout.coreutils.auth;

/**
 * Context containing the profile of the caller of a protected end point
 *
 * @author Francesco Cina'
 */
public interface Auth<R> {

    String getUsername();

    R getRoles();

}

package com.ufoscout.coreutils.auth

fun AuthContext.all(vararg auths: (AuthContext.AuthProfile) -> Boolean): AuthContext {
    if (this.authProfile.all(*auths)) {
        return this
    }
    throw UnauthorizedException("User [" + this.auth.getUsername() + "] does not have all requirements.")
}

fun AuthContext.any(vararg auths: (AuthContext.AuthProfile) -> Boolean): AuthContext {
    if (this.authProfile.any(*auths)) {
        return this
    }
    throw UnauthorizedException("User [" + this.auth.getUsername() + "] has no one of the requirements.")
}

fun AuthContext.AuthProfile.all(vararg auths: (AuthContext.AuthProfile) -> Boolean): Boolean {
    for (auth in auths) {
        if (!auth.invoke(this)) {
            return false
        }
    }
    return true
}

fun AuthContext.AuthProfile.any(vararg auths: (AuthContext.AuthProfile) -> Boolean): Boolean {
    for (auth in auths) {
        if (auth.invoke(this)) {
            return true
        }
    }
    return false
}
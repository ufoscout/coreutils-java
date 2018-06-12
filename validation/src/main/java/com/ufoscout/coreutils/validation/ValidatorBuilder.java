package com.ufoscout.coreutils.validation;

import java.util.function.Function;

public interface ValidatorBuilder<T> {

    ValidatorBuilder<T> add(String key, String message, Function<T, Boolean> validate);

    /**
     * Create a new stateless Thread safe validator
     */
    Validator build();
}

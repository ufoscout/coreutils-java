package com.ufoscout.coreutils.validation.jsr303;

import com.ufoscout.coreutils.validation.Validator;
import com.ufoscout.coreutils.validation.ValidatorBuilder;

import java.util.function.Function;

public interface JSR303ValidatorBuilder<T> extends ValidatorBuilder<T> {

    @Override
    JSR303ValidatorBuilder<T> add(String key, String message, Function<T, Boolean> validate);

    JSR303ValidatorBuilder<T> groups(Class<?>... groups);

    /**
     * Create a new stateless Thread safe validator
     */
    @Override
    JSR303Validator build();
}

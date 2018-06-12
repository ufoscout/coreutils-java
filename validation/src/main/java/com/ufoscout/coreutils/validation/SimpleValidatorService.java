package com.ufoscout.coreutils.validation;

public class SimpleValidatorService implements ValidatorService {

    @Override
    public <T> ValidatorBuilder<T> validator() {
        return new SimpleValidator();
    }

}

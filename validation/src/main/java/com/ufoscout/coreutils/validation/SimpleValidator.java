package com.ufoscout.coreutils.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

class SimpleValidator<T> implements Validator<T>, ValidatorBuilder<T> {

    private final List<Rule> rules = new ArrayList<>();

    @Override
    public ValidationResult<T> validate(T data) {
        ValidationResultImpl<T> result = new ValidationResultImpl<T>(data);
        for (final Rule<T> validationRule : this.rules) {
            if (!validationRule.validate(data)) {
                result.addViolation(validationRule.key(), validationRule.message());
            }
        }
        return result;
    }

    @Override
    public T validateThrowException(T data) throws ValidationException {
        ValidationResult<T> result = validate(data);
        if (!result.getViolations().isEmpty()) {
            throw new ValidationException(result);
        }
        return data;
    }

    @Override
    public ValidatorBuilder<T> add(String key, String message, Function<T, Boolean> validate) {
        rules.add(Rule.rule(key, message, validate));
        return this;
    }

    @Override
    public Validator<T> build() {
        return this;
    }
}

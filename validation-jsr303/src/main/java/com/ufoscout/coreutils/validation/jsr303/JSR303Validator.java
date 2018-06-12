package com.ufoscout.coreutils.validation.jsr303;

import com.ufoscout.coreutils.validation.ValidationResult;
import com.ufoscout.coreutils.validation.Validator;

public interface JSR303Validator<T> extends Validator<T> {


    ValidationResult<T> validateProperty(T data, String propertyName);

}

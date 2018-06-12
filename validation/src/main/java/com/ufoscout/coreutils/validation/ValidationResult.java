package com.ufoscout.coreutils.validation;

import java.util.List;
import java.util.Map;

public interface ValidationResult<T> {
    T getValidatedBean();
    boolean success();
    Map<String, List<String>> getViolations();
}

package com.ufoscout.coreutils.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Feb 27, 2013
 *
 * @author Francesco Cina
 * @version $Revision
 */
public class ValidationResultImpl<T> implements ValidationResult<T>, ViolationManager {

	private final Map<String, List<String>> violations = new HashMap<>();
	private final T validatedBean;

	public ValidationResultImpl(final T validatedBean) {
		this.validatedBean = validatedBean;
	}

	@Override
	public T getValidatedBean() {
		return this.validatedBean;
	}

	@Override
	public Map<String, List<String>> getViolations() {
		return this.violations;
	}

    @Override
    public void addViolation(String key, String message) {
        if (!violations.containsKey(key)) {
            violations.put(key, new ArrayList<String>());
        }
        violations.get(key).add(message);
    }
}

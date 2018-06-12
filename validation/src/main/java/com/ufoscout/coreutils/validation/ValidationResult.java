package com.ufoscout.coreutils.validation;

import java.io.Serializable;
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
public class ValidationResult<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	private final Map<String, List<String>> violations;
	private final T validatedBean;

	public ValidationResult(final T validatedBean, final Map<String, List<String>> violations) {
		this.validatedBean = validatedBean;
		this.violations = violations;

	}

	public T getValidatedBean() {
		return this.validatedBean;
	}

	public Map<String, List<String>> getViolations() {
		return this.violations;
	}

}

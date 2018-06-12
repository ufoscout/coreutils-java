package com.ufoscout.coreutils.validation;

import java.util.List;
import java.util.Map;

/**
 * 
 * <class_description>
 * <p>
 * <b>notes</b>:
 * <p>
 * ON : Feb 27, 2013
 * 
 * @author Francesco Cina
 * @version $Revision
 */
public abstract class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private ValidationResult<?> validationResult;

	public ValidationException(final ValidationResult<?> validationResult) {
		super("Bean validation exception");
		this.validationResult = validationResult;
	}

	public Object getValidatedBean() {
		return validationResult.getValidatedBean();
	}

	public Map<String, List<String>> getViolations() {
		return validationResult.getViolations();
	}

}

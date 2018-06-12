package com.ufoscout.coreutils.validation.jsr303;

import com.ufoscout.coreutils.validation.ValidationResult;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * 
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Feb 27, 2013
 *
 * @author Francesco Cina
 * @version $Revision
 */
public class JSR303ValidationResult<T> {

	private final ValidationResult<T> validationResult;
	private final Set<ConstraintViolation<T>> violations;

	public JSR303ValidationResult(final ValidationResult<T> validationResult, final Set<ConstraintViolation<T>> violations) {
		this.validationResult = validationResult;
		this.violations = violations;
	}

	public ValidationResult<T> getValidationResult() {
		return this.validationResult;
	}

	public Set<ConstraintViolation<T>> getViolations() {
		return this.violations;
	}

}
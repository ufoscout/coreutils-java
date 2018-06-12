package com.ufoscout.coreutils.validation.jsr303;

import com.ufoscout.coreutils.validation.ValidationResult;
import com.ufoscout.coreutils.validation.ViolationException;

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
public class JSR303ViolationException extends ViolationException {

	private static final long serialVersionUID = 1L;
	private final Set<ConstraintViolation<?>> constraintViolations;

	public JSR303ViolationException(final ValidationResult<?> validationResult, final Set<ConstraintViolation<?>> constraintViolations) {
		super(validationResult.getValidatedBean(), validationResult.getViolations());
		this.constraintViolations = constraintViolations;
	}

	/**
	 * Set of constraint violations reported during a validation
	 *
	 * @return <code>Set</code> of <code>ConstraintViolation</code>
	 */
	public Set<ConstraintViolation<?>> getConstraintViolations() {
		return constraintViolations;
	}

}

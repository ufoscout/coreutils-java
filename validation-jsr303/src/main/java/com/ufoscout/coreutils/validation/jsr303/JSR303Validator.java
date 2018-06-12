package com.ufoscout.coreutils.validation.jsr303;

import com.ufoscout.coreutils.validation.ValidationResultImpl;
import com.ufoscout.coreutils.validation.ValidationRule;
import com.ufoscout.coreutils.validation.Validator;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * <class_description>
 * <p>
 * <b>notes</b>:
 * <p>
 * ON : Feb 27, 2013
 * 
 * @author Francesco Cina
 * @version $Revision
 */
public class JSR303Validator<T> implements Validator<T> {

	private final javax.validation.Validator validator;
	private final ValidationRule<T>[] validationRules;
	private Class<?>[] _groups;

	public JSR303Validator(ValidationRule<T>[] rules, final javax.validation.Validator validator) {
		this.validator = validator;
		this.validationRules = rules;
	}

	javax.validation.Validator getValidator() {
		return validator;
	}

	public ValidationResultImpl<T> validateProperty(T data, final String propertyName) {
		return doValidation(data, new ValidateAction<T>() {
			javax.validation.Validator _validator = getValidator();

			@Override
			public Set<ConstraintViolation<T>> validateWithGroups(final Class<?>... groups) {
				return _validator.validateProperty(data, propertyName, groups);
			}

			@Override
			public Set<ConstraintViolation<T>> validate() {
				return _validator.validateProperty(data, propertyName);
			}
		}).getValidationResult();
	}

	@Override
	public ValidationResultImpl<T> validate(T data) {
		return doValidation(data, new ValidateAction<T>() {
			javax.validation.Validator _validator = getValidator();

			@Override
			public Set<ConstraintViolation<T>> validateWithGroups(final Class<?>... groups) {
				return _validator.validate(data, groups);
			}

			@Override
			public Set<ConstraintViolation<T>> validate() {
				return _validator.validate(data);
			}
		}).getValidationResult();
	}

	@Override
	@SuppressWarnings({ "rawtypes" })
	public void validateThrowException(T data) {
		final JSR303ValidationResult validationResult = doValidation(data, new ValidateAction<T>() {
			javax.validation.Validator _validator = getValidator();

			@Override
			public Set<ConstraintViolation<T>> validateWithGroups(final Class<?>... groups) {
				return _validator.validate(data, groups);
			}

			@Override
			public Set<ConstraintViolation<T>> validate() {
				return _validator.validate(data);
			}
		});
		if (!validationResult.getValidationResult().getViolations().isEmpty()) {
			throw new JSR303ValidationException(validationResult.getValidationResult(), validationResult.getViolations());
		}
	}

	private JSR303ValidationResult<T> doValidation(final T data, final ValidateAction<T> action) {
		ValidationResultImpl<T> result = new ValidationResultImpl<T>(data);

		Set<ConstraintViolation<T>> violations;

		if ((_groups != null) && (_groups.length > 0)) {
			violations = action.validateWithGroups(_groups);
		} else {
			violations = action.validate();
		}

		final Iterator<ConstraintViolation<T>> iter = violations.iterator();
		while (iter.hasNext()) {
			final ConstraintViolation<T> violation = iter.next();
			final String key = violation.getPropertyPath().toString();
			final String error = violation.getMessage();
			result.addViolation(key, error);
		}

		for (final ValidationRule<T> validationRule : this.validationRules) {
			validationRule.validate(data, result);
		}
		return new JSR303ValidationResult<T>(result, violations);
	}

	public Validator<T> groups(final Class<?>... groups) {
		this._groups = groups;
		return this;
	}

	interface ValidateAction<T> {
		Set<ConstraintViolation<T>> validateWithGroups(Class<?>... groups);
		Set<ConstraintViolation<T>> validate();
	}

}

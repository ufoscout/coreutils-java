package com.ufoscout.coreutils.validation.jsr303;

import com.ufoscout.coreutils.validation.ValidationResult;
import com.ufoscout.coreutils.validation.ValidationRule;
import com.ufoscout.coreutils.validation.Validator;
import com.ufoscout.coreutils.validation.ViolationManager;

import javax.validation.ConstraintViolation;
import java.util.*;

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

	private final T data;
	private final javax.validation.Validator validator;
	private final List<ValidationRule<T>> validationRules = new ArrayList<ValidationRule<T>>();
	private Class<?>[] _groups;

	public JSR303Validator(final T data, final javax.validation.Validator validator) {
		this.data = data;
		this.validator = validator;
	}

	javax.validation.Validator getValidator() {
		return validator;
	}

	T getData() {
		return data;
	}

	public ValidationResult<T> validateProperty(final String propertyName) {
		return doValidation(new ValidateAction<T>() {
			javax.validation.Validator _validator = getValidator();
			T _data = getData();

			@Override
			public Set<ConstraintViolation<T>> validateWithGroups(final Class<?>... groups) {
				return _validator.validateProperty(_data, propertyName, groups);
			}

			@Override
			public Set<ConstraintViolation<T>> validate() {
				return _validator.validateProperty(_data, propertyName);
			}
		}).getValidationResult();
	}

	@Override
	public ValidationResult<T> validate() {
		return doValidation(new ValidateAction<T>() {
			javax.validation.Validator _validator = getValidator();
			T _data = getData();

			@Override
			public Set<ConstraintViolation<T>> validateWithGroups(final Class<?>... groups) {
				return _validator.validate(_data, groups);
			}

			@Override
			public Set<ConstraintViolation<T>> validate() {
				return _validator.validate(_data);
			}
		}).getValidationResult();
	}

	@Override
	public Validator<T> addRule(final ValidationRule<T> valdiationRule) {
		this.validationRules.add(valdiationRule);
		return this;
	}

	@Override
	@SuppressWarnings({ "rawtypes" })
	public void validateThrowException() {
		final JSR303ValidationResult validationResult = doValidation(new ValidateAction<T>() {
			javax.validation.Validator _validator = getValidator();
			T _data = getData();

			@Override
			public Set<ConstraintViolation<T>> validateWithGroups(final Class<?>... groups) {
				return _validator.validate(_data, groups);
			}

			@Override
			public Set<ConstraintViolation<T>> validate() {
				return _validator.validate(_data);
			}
		});
		if (!validationResult.getValidationResult().getViolations().isEmpty()) {
			throw new JSR303ValidationException(validationResult.getValidationResult(), validationResult.getViolations());
		}
	}

	protected void addError(final String key, final String error, final Map<String, List<String>> errors) {
		if (!errors.containsKey(key)) {
			errors.put(key, new ArrayList<String>());
		}
		errors.get(key).add(error);
	}

	private JSR303ValidationResult<T> doValidation(final ValidateAction<T> action) {
		final Map<String, List<String>> errors = new HashMap<String, List<String>>();

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
			addError(key, error, errors);
		}

		for (final ValidationRule<T> validationRule : this.validationRules) {
			validationRule.validate(this.data, new ViolationManager() {
				@Override
				public void addViolation(final Object validatedObject, final String key, final String message) {
					addError(key, message, errors);
				}
			});
		}
		return new JSR303ValidationResult<T>(new ValidationResult<T>(this.data, errors), violations);
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

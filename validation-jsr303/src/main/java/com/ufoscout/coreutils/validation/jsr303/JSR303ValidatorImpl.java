package com.ufoscout.coreutils.validation.jsr303;

import com.ufoscout.coreutils.validation.Rule;
import com.ufoscout.coreutils.validation.ValidationResultImpl;
import com.ufoscout.coreutils.validation.Validator;
import com.ufoscout.coreutils.validation.ValidatorBuilder;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

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
public class JSR303ValidatorImpl<T> implements JSR303Validator<T>, JSR303ValidatorBuilder<T> {

	private final javax.validation.Validator validator;
    private final List<Rule> rules = new ArrayList<>();
	private Class<?>[] _groups;

	public JSR303ValidatorImpl(final javax.validation.Validator validator) {
		this.validator = validator;
	}

	javax.validation.Validator getValidator() {
		return validator;
	}

	@Override
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
		if (!validationResult.getValidationResult().success()) {
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

		for (final Rule<T> validationRule : this.rules) {
            if (!validationRule.validate(data)) {
                result.addViolation(validationRule.key(), validationRule.message());
            }
		}
		return new JSR303ValidationResult<T>(result, violations);
	}

	@Override
	public JSR303ValidatorBuilder<T> groups(final Class<?>... groups) {
		this._groups = groups;
		return this;
	}

    @Override
    public JSR303ValidatorBuilder<T> add(String key, String message, Function<T, Boolean> validate) {
        rules.add(Rule.rule(key, message, validate));
        return this;
    }

    @Override
    public JSR303Validator build() {
        return this;
    }

    interface ValidateAction<T> {
		Set<ConstraintViolation<T>> validateWithGroups(Class<?>... groups);
		Set<ConstraintViolation<T>> validate();
	}

}

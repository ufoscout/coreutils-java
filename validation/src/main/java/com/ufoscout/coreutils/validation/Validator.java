package com.ufoscout.coreutils.validation;


/**
 * 
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Feb 27, 2013
 *
 * @author Francesco Cina
 * @version $Revision
 */
public interface Validator<T> {

	/**
	 * Validate the bean and return the {@link ValidationResult} as result of the validation
	 * @return
	 */
	ValidationResult<T> validate();

	/**
	 * Validate the bean. If there are validation errors a {@link ValidationException}
	 * containing the {@link ValidationResult} is thrown
	 * 
	 * @throws {@link ValidationException} if there are validation errors
	 */
	void validateThrowException() throws ValidationException;

	/**
	 * Add a custom {@link ValidationRule} to be used during the validation process.
	 * 
	 * @param validationRule
	 * @return
	 */
	Validator<T> addRule(ValidationRule<T> validationRule);

	/**
	 * Set the validation groups to use.
	 * Dafault is {@link javax.validation.groups.Default}
	 * 
	 * @param groups
	 * @return
	 */
	Validator<T> groups(Class<?>... groups);

	/**
	 * Validate a bean property.
	 *
	 * @param propertyName
	 * @return
	 */
	ValidationResult<T> validateProperty(String propertyName);

}

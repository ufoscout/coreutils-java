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
@FunctionalInterface
public interface ValidationRule<T> {

	void validate(T data, ViolationManager violationManager);

}

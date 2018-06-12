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
public interface ValidatorService {

	/**
	 * Validate an object and, if needed, return a map of validation errors
	 * 
	 * @param data
	 * @return
	 */
	<T> Validator<T> validator(T data);

}

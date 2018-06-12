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
public interface ViolationManager {

	/**
	 * Create a new violation entry.
	 * 
	 * @param validatedObject the object that has violations
	 * @param key the violation key
	 * @param message the violation message code
	 */
	void addViolation(Object validatedObject, String key, String message);

}

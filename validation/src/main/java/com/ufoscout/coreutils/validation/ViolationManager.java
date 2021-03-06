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
public interface ViolationManager {

	/**
	 * Create a new violation entry.
	 * 
	 * @param key the violation key
	 * @param message the violation message code
	 */
	void addViolation(String key, String message);

}

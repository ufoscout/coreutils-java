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
	 * Create a new validator builder
	 * 
	 * @return
	 */
	<T> ValidatorBuilder<T> validator();

}

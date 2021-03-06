package com.ufoscout.coreutils.validation.jsr303;

import com.ufoscout.coreutils.validation.ValidatorService;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

/**
 * 
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Feb 27, 2013
 *
 * @author Francesco Cina
 * @version $Revision
 */
public class JSR303ValidatorService implements ValidatorService {

	private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

	@Override
	public <T> JSR303ValidatorBuilder<T> validator() {
		return new JSR303ValidatorImpl<T>(factory.getValidator());
	}

}

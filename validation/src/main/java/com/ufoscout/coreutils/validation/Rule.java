package com.ufoscout.coreutils.validation;

import java.util.function.Function;

/**
 * 
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Feb 27, 2013
 *
 * @author Francesco Cina
 * @version $Revision
 */
public interface Rule<T> {

	static <T> Rule<T> rule(String key, String message, Function<T, Boolean> validate) {
		return new Rule<T>() {
			@Override
			public String key() {
				return key;
			}

			@Override
			public String message() {
				return message;
			}

			@Override
			public boolean validate(T data) {
				return validate.apply(data);
			}
		};
	}

	String key();
	String message();
	boolean validate(T data);

}

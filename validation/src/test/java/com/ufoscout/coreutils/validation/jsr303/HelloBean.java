package com.ufoscout.coreutils.validation.jsr303;

import javax.validation.constraints.NotNull;

/**
 * <class_description>
 * <p>
 * <b>notes</b>:
 * <p>
 * ON : Dec 10, 2012
 * 
 * @author cinafr
 * @version $Revision
 */
public class HelloBean {

	@NotNull
	String hello;

	@NotNull
	String helloTwo;

	@NotNull(groups=BeanCheck.class)
	String helloProfile;

}

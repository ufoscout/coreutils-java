package com.ufoscout.coreutils.validation.jsr303;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


/**
 * 
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Feb 27, 2013
 *
 * @author Francesco Cina
 * @version $Revision
 */
public class BeanOne {

	@NotNull
	String value;

	@Valid
	List<BeanOne> beanOneList = new ArrayList<BeanOne>();

	@Valid
	BeanOne innerBean;

}

package com.ufoscout.coreutils.validation.jsr303;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Feb 27, 2013
 *
 * @author Francesco Cina
 * @version $Revision
 */
public class BeanWithMap {

	@Valid
	Map<String, BeanOne> beanOneMap = new HashMap<String, BeanOne>();

}

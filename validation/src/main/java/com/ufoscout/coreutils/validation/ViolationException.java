package com.ufoscout.coreutils.validation;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 
 * <class_description>
 * <p>
 * <b>notes</b>:
 * <p>
 * ON : Feb 27, 2013
 * 
 * @author Francesco Cina
 * @version $Revision
 */
public abstract class ViolationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final Object validatedBean;
	private final Map<String, List<String>> violations;

	public ViolationException(final Object validatedBean,
			final Map<String, List<String>> violations) {
		super("Bean validation exception");
		this.validatedBean = validatedBean;
		this.violations = violations;
	}

	public Object getValidatedBean() {
		return validatedBean;
	}

	public Map<String, List<String>> getViolations() {
		return violations;
	}

	public List<String> getAllViolations() {
		final List<String> violationsList = new LinkedList<String>();
		if (violations!=null && !violations.isEmpty()) {
			for (final List<String> violationsByKey : violations.values()) {
				violationsList.addAll(violationsByKey);
			}
		}

		return violationsList;
	}

}

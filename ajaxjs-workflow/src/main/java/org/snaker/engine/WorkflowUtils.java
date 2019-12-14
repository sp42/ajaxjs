package org.snaker.engine;

import java.util.regex.Pattern;

public class WorkflowUtils {
	private static final Pattern pattern = Pattern.compile("[0-9]*");

	public static boolean isNumeric(String str) {
		return pattern.matcher(str).matches();
	}
}

package com.ajaxjs.javascript;

public interface Mapper {
	/**
	 * 
	 * @param value
	 * @return
	 */
	public Object parseValue(Object value);
	
	
	public <T> T parseSingleObject();
}

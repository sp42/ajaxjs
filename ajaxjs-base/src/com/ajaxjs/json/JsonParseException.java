package com.ajaxjs.json;

public class JsonParseException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer charNum = null;

	private Integer lineNum = null;

	private Integer colNum = null;

	private String desc = null;

	private Throwable cause = null;

	public JsonParseException() {
		super();
	}

	public JsonParseException(Integer charNum, Integer lineNum, Integer colNum, String message, Throwable cause) {
		this.charNum = charNum;
		this.colNum = colNum;
		this.lineNum = lineNum;
		this.desc = message;
		this.cause = cause;
	}

	public JsonParseException(Integer charNum, Integer lineNum, Integer colNum, String message) {
		this.charNum = charNum;
		this.colNum = colNum;
		this.lineNum = lineNum;
		this.desc = message;
	}

	public JsonParseException(Throwable cause) {
		super(cause);
	}

	public JsonParseException(String string) {
		super(string);
	}

	public String getMessage() {
		return "[char:" + charNum + ",line:" + lineNum + ",column:" + colNum + "]" + desc + (cause == null ? "" : cause.toString());
	}

	public String getLocalMessage() {
		return getMessage();
	}

	public String toString() {
		return getMessage();
	}

	public String toLocalString() {
		return getMessage();
	}
}

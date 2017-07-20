package com.ajaxjs.json.lexer;

import com.ajaxjs.json.JSONParser;

/**
 * 
 */
public class Token {
	public Token(int type, String typeName, String typeNameChinese) {
		this.type = type;
		this.typeName = typeName;
		this.typeNameChinese = typeNameChinese;
		
	}
	
	public Token(int type, String typeName, String typeNameChinese, Object javaValue) {
		this(type, typeName, typeNameChinese);
		this.javaValue = javaValue;
	}
	
	public Token(int type, String typeName, String typeNameChinese, Object javaValue, String value) {
		this(type, typeName, typeNameChinese, javaValue);
		this.value = value;
	}

	/**
	 * Token 类型，也可以理解为 id、index
	 */
	private int type;
	
	/**
	 * 类型名称
	 */
	private String typeName;
	
	/**
	 * 类型名称（中文版本）
	 */
	private String typeNameChinese;

	/**
	 * Token 值，一般指数字和字符串。这是 JSON 字符串上的那个原始值。
	 */
	private String value;
	
	/**
	 * Java 的值，由 value 转换而来。 
	 */
	private Object javaValue;
	
	public static final Token DESC 	= new Token(2, "DESC",  ":");
	public static final Token SPLIT = new Token(3, "SPLIT", ",");
	public static final Token ARRS 	= new Token(4, "ARRS",	"[");
	public static final Token OBJS 	= new Token(5, "OBJS",	"{");
	
	/**
	 * 数组结束
	 */
	public static final Token ARRE 	= new Token(6, "ARRE",	"]");
	
	/**
	 * 对象结束
	 */
	public static final Token OBJE 	= new Token(7, "OBJE",	"}");
	
	public static final Token FALSE = new Token(8, "FALSE",	"false", false);
	public static final Token TRUE 	= new Token(9, "TRUE",	"true",	 true);
	public static final Token NIL 	= new Token(10, "NIL",	"null",	 null);
	public static final Token BGN 	= new Token(11, "BGN",	"开始");
	public static final Token EOF 	= new Token(12, "EOF", 	"结束");
	
	public Object toJavaValue() {
		if(this == Token.TRUE || this == Token.FALSE || this == Token.NIL)
			return getJavaValue();
//		else if(this instanceof StringToken) {
//			return JSONParser.unescape(value);
//		} else if(this instanceof NumberToken) {
//			return value.indexOf('.') != -1 ? Double.parseDouble(value) :  Integer.parseInt(value);
//		} else 
//			throw new JsonParseException("获取 Java 值失败！");
		
		Object curValue = null;
		switch (getType()) {
			case 1:
				curValue = value.indexOf('.') != -1 ? Double.parseDouble(value) : Integer.parseInt(value);
				break;
			case 0:
				curValue = JSONParser.unescape(value);
				break;
		}
		return curValue;
	}

	@Override
	public String toString() {
		if (type > 1) {
			return "[" + getTypeName() + "]";
		} else {
			return "[" + getTypeName() + ":" + value + "]";
		}
	}

	public String toLocalString() {
		if (type > 1) {
			return "“" + getTypeNameChinese() + "”";
		} else {
			return "“" + getTypeNameChinese() + ":" + value + "”";
		}
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the javaValue
	 */
	public Object getJavaValue() {
		return javaValue;
	}

	/**
	 * @param javaValue the javaValue to set
	 */
	public void setJavaValue(Object javaValue) {
		this.javaValue = javaValue;
	}

	/**
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * @param typeName the typeName to set
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * @return the typeNameChinese
	 */
	public String getTypeNameChinese() {
		return typeNameChinese;
	}

	/**
	 * @param typeNameChinese the typeNameChinese to set
	 */
	public void setTypeNameChinese(String typeNameChinese) {
		this.typeNameChinese = typeNameChinese;
	}
}

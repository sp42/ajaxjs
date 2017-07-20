package com.ajaxjs.json.lexer;

import java.util.Stack;

import com.ajaxjs.json.JsonParseException;
import com.ajaxjs.json.JSONParser;

/**
 * 词法分析器
 */
public class Lexer {
	/**
	 * 当前行号
	 */
	private int lineNum = 0;
	
	/**
	 * 用于记录每一行的起始位置
	 */
	private Stack<Integer> colMarks = new Stack<Integer>();

	/**
	 * 用于报错的行游标
	 */
	private int startLine = 0;

	/**
	 * 用于报错的列游标
	 */
	private int startCol = 0;
	
	/**
	 * 当前字符游标
	 */
	private int cur = -1;
	/**
	 * 保存当前要解析的字符串
	 */
	private String str = null;
	
	/**
	 * 保存当前要解析的字符串的长度
	 */
	private int len = 0;

	/**
	 * JsonLex构造函数
	 * 
	 * @param str
	 *            要解析的字符串
	 */
	public Lexer(String str) {
		this.str = str;
		this.len = str.length();
		this.startLine = 0;
		this.startCol = 0;
		this.cur = -1;
		this.lineNum = 0;
		this.colMarks.push(0);
	}

	/**
	 * 获取当前游标所在的字符
	 * @return
	 */
	public char getCurChar() {
		return cur >= len - 1 ? 0 : str.charAt(cur);
	}

	/**
	 * 检查字符串是否结束
	 */
	private void checkEnd() {
		if (cur >= len - 1) 
			throw generateUnexpectedException("未预期的结束，字符串未结束");
	}

	/**
	 * 解析符号
	 * @param c
	 * @return
	 */
	public Token parseSymbol(char c) {
		switch (c) {
			case '[':
				return Token.ARRS;
			case ']':
				return Token.ARRE;
			case '{':
				return Token.OBJS;
			case '}':
				return Token.OBJE;
			case ',':
				return Token.SPLIT;
			case ':':
				return Token.DESC;
		}
		
		return null;
	}

	public JsonParseException generateUnexpectedException(String str) {
		return new JsonParseException(cur, startLine, startCol, str);
	}

	public JsonParseException generateUnexpectedException(String str, Throwable e) {
		return new JsonParseException(cur, startLine, startCol, str, e);
	}

	// str \"(\\\"|[^\"])*\"
	// def [_a-zA-Z][_a-zA-Z0-9]*
	// num -?[0-9]+(\.[0-9]+)?
	// space [ \t\n]+
	/**
	 * 获取下一个Token的主函数
	 * Next这个方法循环调用nextChar获取下一个字符，碰见某种类型的初始字符，就开始进入相应Token类型的处理函数中，最终返回Token类型的对象……
	 */
	public Token next() {
		if (lineNum == 0) {
			lineNum = 1;
			return Token.BGN;
		}
		
		char c;
		while ((c = nextChar()) != 0) {
			startLine = lineNum;
			startCol = getColNum();
			
			if (c == '"' || c == '\'') {
				return new StringToken(getStrValue(c));
			} else if (JSONParser.isLetterUnderline(c)) {
				return getDefToken();
			} else if (JSONParser.isNum(c) || c == '-') {
				return new NumberToken(getNumValue());
			} else if (JSONParser.isSpace(c)) {
				continue;
			} else {
				return parseSymbol(c);
			}
		}
		
		if (c == 0) 
			return Token.EOF;
		
		return null;
	}

	/**
	 * 获取字符串的值
	 * @param s
	 * @return
	 */
	private String getStrValue(char s) {
		int start = cur;
		char c;
		
		while ((c = nextChar()) != 0) {
			if (c == '\\') {// 跳过斜杠以及后面的字符
				c = nextChar();
			} else if (s == c) { // 遇到结束的 引号 结束了 返回这个字符串
				return str.substring(start + 1, cur);
			}
		}
		
		checkEnd();
		return null;
	}

	/**
	 * 获取数字的值
	 * @return
	 */
	private String getNumValue() {
		int start = cur;
		char c;
		
		while ((c = nextChar()) != 0) {
			if (!JSONParser.isDecimal(c)) 
				return str.substring(start, revertChar());
		}
		
		checkEnd();
		return null;
	}
	
	/**
	 * 用来处理 true、false、null 的值
	 * @return
	 */
	private Token getDefToken() {
		int start = cur;
		char c;
		
		while ((c = nextChar()) != 0) {
			if (!JSONParser.isNumLetterUnderline(c)) {
				String value = str.substring(start, revertChar());
				
				if ("true".equals(value)) {
					return Token.TRUE;
				} else if ("false".equals(value)) {
					return Token.FALSE;
				} else if ("null".equals(value)) {
					return Token.NIL;
				} else {
					return new Token(0, "STR", "字符串", null ,value);
				}
			}
		}
		
		checkEnd();
		return null;
	}

	/**
	 * 获取下一个字节，同时进行 行、列 计数
	 * 
	 * @return 下一个字节，结束时返回0
	 */
	private char nextChar() {
		if (cur >= len - 1)
			return 0;
		
		++cur;
		char c = str.charAt(cur);
		
		if (c == '\n') { // 遇到换行，记录一下所在行数，用于调试
			++lineNum;
			colMarks.push(cur);
		}
		
		return c;
	}

	/**
	 * 撤回一个字节，同时进行 行、列 计数，返回撤回前的字符游标
	 * 为什么要撤回？因为要先获取下一个字节才能得知是什么情况，知道之后，撤回。
	 * @return 下一个字节，结束时返回0
	 */
	private int revertChar() {
		if (cur <= 0) 
			return 0;
		
		int rcur = cur--;
		char c = str.charAt(rcur);
		
		if (c == '\n') {
			--lineNum;
			colMarks.pop();
		}
		
		return rcur;
	}

	public int getLineNum() {
		return lineNum;
	}

	public int getColNum() {
		return cur - colMarks.peek();
	}

	public int getCur() {
		return cur;
	}

	public String getStr() {
		return str;
	}

	public int getLen() {
		return len;
	}
}

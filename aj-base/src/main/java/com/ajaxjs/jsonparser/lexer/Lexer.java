/**
 * Copyright Sp42 frank@ajaxjs.com <frank@ajaxjs.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.jsonparser.lexer;

import java.util.Stack;

import com.ajaxjs.jsonparser.JsonParseException;

/**
 * 词法分析器
 */
public class Lexer extends BaseLexer {
	/**
	 * 当前行号
	 */
	private int lineNum;

	/**
	 * 用于记录每一行的起始位置
	 */
	private Stack<Integer> colMarks = new Stack<>();

	/**
	 * 用于报错的行游标
	 */
	private int startLine;

	/**
	 * 用于报错的列游标
	 */
	private int startCol;

	/**
	 * 当前字符游标
	 */
	private int cur = -1;
	/**
	 * 保存当前要解析的字符串
	 */
	private String str;

	/**
	 * 保存当前要解析的字符串的长度
	 */
	private int len;

	/**
	 * JsonLex构造函数
	 * 
	 * @param str 要解析的字符串
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
	 * 
	 * @return 当前字符
	 */
	public char getCurChar() {
		return cur >= len - 1 ? 0 : str.charAt(cur);
	}

	/**
	 * 检查字符串是否结束
	 */
	private void checkEnd() {
		if (cur >= len - 1)
			throw exceptionFactory("未预期的结束，字符串未结束");
	}

	// str \"(\\\"|[^\"])*\"
	// def [_a-zA-Z][_a-zA-Z0-9]*
	// num -?[0-9]+(\.[0-9]+)?
	// space [ \t\n]+
	/**
	 * 获取下一个Token的主函数 Next这个方法循环调用nextChar获取下一个字符，碰见某种类型的初始字符，就开始进入相应Token类型的处理函数中
	 * @return 最终返回Token类型的对象
	 */
	public Token next() {
		if (lineNum == 0) {
			lineNum = 1;
			return Tokens.BGN;
		}

		char c;
		while ((c = nextChar()) != 0) {
			startLine = lineNum;
			startCol = getColNum();

			if (c == '"' || c == '\'') {
				return new StringToken(getStrValue(c));
			} else if (isLetterUnderline(c)) {
				return getValueToken();
			} else if (isNum(c) || c == '-') {
				return new NumberToken(getNumValue());
			} else if (isSpace(c)) {
				continue;
			} else {
				return parseSymbol(c);
			}
		}

		if (c == 0)
			return Tokens.EOF;

		return null;
	}

	/**
	 * 获取字符串的值
	 * 
	 * @param s 传入的字符
	 * @return 字符串的值
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
	 * 
	 * @return 数字的值
	 */
	private String getNumValue() {
		int start = cur;
		char c;

		while ((c = nextChar()) != 0) {
			if (!isDecimal(c))
				return str.substring(start, revertChar());
		}

		checkEnd();
		return null;
	}

	/**
	 * 用来处理 true、false、null 和对象 key 的值
	 * 
	 * @return true、false、null 的 Token
	 */
	private Token getValueToken() {
		int start = cur;
		char c;

		while ((c = nextChar()) != 0) {
			if (!isNumLetterUnderline(c)) {
				String value = str.substring(start, revertChar());

				if ("true".equals(value)) {
					return Tokens.TRUE;
				} else if ("false".equals(value)) {
					return Tokens.FALSE;
				} else if ("null".equals(value)) {
					return Tokens.NIL;
				} else {
					return new StringToken(value); // 对象的 KEY 提示：StringToken 的二义性，既可作对象的 KEY，又可作字符串
				}
			}
		}

		checkEnd();
		return null;
	}

	/**
	 * 解析符号
	 * 
	 * @param c 字符
	 * @return Token
	 */
	private Token parseSymbol(char c) {
		switch (c) {
		case '[':
			return Tokens.ARRS;
		case ']':
			return Tokens.ARRE;
		case '{':
			return Tokens.OBJS;
		case '}':
			return Tokens.OBJE;
		case ',':
			return Tokens.SPLIT;
		case ':':
			return Tokens.DESC;
		default:
			return null;
		}
	}

	/**
	 * 获取下一个字节，同时进行 行、列 计数
	 * 
	 * @return 下一个字节，结束时返回 0
	 */
	private char nextChar() {
		if (cur >= len - 1)
			return 0;

		++cur;
		char c = str.charAt(cur);

		if (c == '\n') { // 遇到换行，记录一下所在行数，用于调试
			lineNum++;
			colMarks.push(cur);
		}

		return c;
	}

	/**
	 * 撤回一个字节，同时进行 行、列 计数，返回撤回前的字符游标 为什么要撤回？因为要先获取下一个字节才能得知是什么情况，知道之后，撤回。
	 * 
	 * @return 下一个字节，结束时返回0
	 */
	private int revertChar() {
		if (cur <= 0)
			return 0;

		int rcur = cur--;
		
		if (str.charAt(rcur) == '\n') {
			lineNum--;
			colMarks.pop();
		}

		return rcur;
	}

	/**
	 * 抛出一个 JsonParseException 异常
	 * 
	 * @param msg	异常信息
	 * @return JsonParseException 异常
	 */
	public JsonParseException exceptionFactory(String msg) {
		return new JsonParseException(cur, startLine, startCol, msg);
	}

	/**
	 * 抛出一个 JsonParseException 异常
	 * 
	 * @param msg	异常信息
	 * @param e 	异常对象
	 * @return JsonParseException 异常
	 */
	public JsonParseException exceptionFactory(String msg, Throwable e) {
		return new JsonParseException(cur, startLine, startCol, msg, e);
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

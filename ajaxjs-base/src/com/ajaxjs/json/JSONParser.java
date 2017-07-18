package com.ajaxjs.json;

import com.ajaxjs.json.lexer.Lexer;
import com.ajaxjs.json.lexer.Token;
import com.ajaxjs.json.syntax.StateMachine;

public class JSONParser {
	
	public static Object parse(String str) {
		if (str == null || "".equals(str.trim()))
			return null;
		else
			return (new StateMachine(str)).parse();
	}

	public static void main(String[] args) {
		// String str =
		// "{\"key1\":\"value1\",\"key2\":\"value2\",\"key3\":\"value3\",\"key4\":[{\"a1\":\"1\",\"a2\":\"2\",\"a3\":\"3\",\"subChildA\":[{\"suba1\":\"3040\",\"suba2\":\"brebb\",\"suba3\":\"fbre\"},{\"suba1\":\"erbrrt\",\"suba2\":\"be4\",\"suba3\":\"5yh5\"},{\"suba1\":\"g445h\",\"suba2\":\"43th\",\"suba3\":\"r5yj4\"}],\"subChildB\":{\"suY1\":\"30L40\",\"suY2\":\"bre00bb\",\"suY3\":\"fbFGFre\",\"subChildA\":[{\"suba1\":\"3040\",\"suba2\":\"brebb\",\"suba3\":\"fbre\"},{\"suba1\":\"erbrrt\",\"suba2\":\"be4\",\"suba3\":\"5yh5\"},{\"suba1\":\"g445h\",\"suba2\":\"43th\",\"suba3\":\"r5yj4\"}]}},{\"a1\":\"s\",\"a2\":\"D\",\"a3\":\"F\"},{\"a1\":\"Q\",\"a2\":\"R\",\"a3\":\"T\"}],\"key5\":[{\"b1\":\"11\",\"b2\":\"21\",\"b3\":\"31\"},{\"b1\":\"3er\",\"b2\":\"3gt\",\"b3\":\"y7u\"},{\"b1\":\"H\",\"b2\":\"Y\",\"b3\":\"R\"}],\"key6\":\"uuid\",\"key7\":{\"vx1\":\"HwH\",\"vx2\":\"YrY\",\"vx3\":\"ReR\"}}";
		String str = "{\n\ta:[1,-23333,-0.3,0.17,5.2,\"\\u82B1\\u6979~\"],\n\tb:[\"a\tbc\",\"12  3\",\"4,5\\\"6\",{\n\t\t\t\t\tx:1,\n\t\t\t\t\ty:\"cc\\ncc\"\n\t\t\t\t},4.56],\n\t\"text\":\"I'm OK~\",\n\t\"1-2\":234,\n\tmybool:false,\n\tmynull:null,\n\tmyreal:true\n}\n";
		// JSON����
		Object o = parse(str);
		System.out.println(str);
		
		Lexer jl = new Lexer(str);
		Token tk = null;
		while((tk=jl.next())!=Token.EOF){
			System.out.println(tk);
		}
		System.out.println(o);
	}
	
	/**
	 * 是否空格字符
	 * @param c
	 * @return
	 */
	public static boolean isSpace(char c) {
		return c == ' ' || c == '\t' || c == '\n';
	}
	
	/**
	 * 是否字符或者下划线
	 * @param c
	 * @return
	 */
	public static boolean isLetterUnderline(char c) {
		return (c >= 'a' && c <= 'z') || c == '_';
	}
	
	/**
	 * 是否数字字符
	 * @param c
	 * @return
	 */
	public static boolean isNum(char c) {
		return c >= '0' && c <= '9';
	}
	
	/**
	 * 是否数字字符或小数
	 * @param c
	 * @return
	 */
	public static boolean isDecimal(char c) {
		return isNum(c) || c == '.';
	}
	
	/**
	 * 是否字符或者下划线或下划线
	 * @param c
	 * @return
	 */
	public static boolean isNumLetterUnderline(char c) {
		return isLetterUnderline(c) || isNum(c) || c == '_';
	}

	/**
	 * 转义
	 * @param str
	 * @return
	 */
	public static String unescape(String str) {
		StringBuffer sb = new StringBuffer();
		
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == '\\') {
				c = str.charAt(++i);// 游标前进一个字符
				switch (c) {
					case '"':
						sb.append('"');
						break;
					case '\\':
						sb.append('\\');
						break;
					case '/':
						sb.append('/');
						break;
					case 'b':
						sb.append('\b');
						break;
					case 'f':
						sb.append('\f');
						break;
					case 'n':
						sb.append('\n');
						break;
					case 'r':
						sb.append('\r');
						break;
					case 't':
						sb.append('\t');
						break;
					case 'u':
						String hex = str.substring(i + 1, i + 5);
						sb.append((char) Integer.parseInt(hex, 16));
						i += 4;
						break;
					default:
						throw new RuntimeException("“\\”后面期待“\"\\/bfnrtu”中的字符，结果得到“" + c + "”");
				}
			} else {
				sb.append(c);
			}
		}
		
		return sb.toString();
	}
}

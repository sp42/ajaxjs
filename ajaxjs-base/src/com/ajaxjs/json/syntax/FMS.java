package com.ajaxjs.json.syntax;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.ajaxjs.json.JsonParseException;
import com.ajaxjs.json.lexer.Lexer;
import com.ajaxjs.json.lexer.Token;

public class FMS implements States {
	/**
	 * 词法分析器实例
	 */
	private Lexer lex;
	
	/**
	 * 堆栈管理器
	 */
	private Operator opt;
	
	/**
	 * 当前状态
	 */
	private State status;
	
	/**
	 * 
	 * @param jsonStr JSON 字符串
	 */
	public FMS(String jsonStr) {
		if (jsonStr == null)
			throw new NullPointerException();
		
		lex = new Lexer(jsonStr);
		opt = new Operator(lex);
	}

	/**
	 * 开始解析 JSON 字符串
	 * @return MAP|LIST
	 */
	public Object parse() {
		status = BGN;
		State oldStatus = status; // 上一个状态
		
		Token tk;
		while ((tk = lex.next()) != Token.EOF) {
			if (tk == null) 
				throw lex.generateUnexpectedException("发现不能识别的token：“" + lex.getCurChar() + "”");
			
			if (status == VAL || status == EOF || status == ERR) 
				throw lex.generateUnexpectedException( "当前状态【" + oldStatus.getDescription() + "】,期待【结束】;却返回" + tk.toLocalString());
			
			oldStatus = status;
			status = states[oldStatus.getId()][tk.getType()];
			
			if (status == ERR) 
				throw lex.generateUnexpectedException("当前状态【" + oldStatus.getDescription() + "】,期待【" + (oldStatus.getExpectDescription() == null ? "结束" : oldStatus.getExpectDescription()) + "】;却返回" + tk.toLocalString());
			
			Method m = TKOL[tk.getType()];
			try {				
				if (m != null){ // 输入 Token 操作 有点像 js 的 call/apply
					status = (State) m.invoke(opt, oldStatus, status, tk);
				}
				
				m = status.getHandler();
				
				if (m != null){ // 目标状态操作
					status =  (State) m.invoke(opt, oldStatus, status, tk);
				}
			} catch (IllegalArgumentException e) {
				throw lex.generateUnexpectedException("【反射调用】传入非法参数", e);
			} catch (IllegalAccessException e) {
				throw lex.generateUnexpectedException("【反射调用】私有方法无法调用", e);
			} catch (InvocationTargetException e) {
				if (e.getTargetException() instanceof JsonParseException) 
					throw (JsonParseException) e.getTargetException();
				 else 
					throw lex.generateUnexpectedException("运行时异常", e);
			}
		}
		
		return opt.getCurValue();
	}
	
	/**
	 * Token 输入操作列表
	 */
	/*INPUT —— STR NUM DESC SPLIT ARRS OBJS ARRE OBJE FALSE TRUE NIL BGN*/
	private static final Method[] TKOL = {
			  null, null, null, null, Operator.getMethod("arrs"), Operator.getMethod("objs"), null, null, null, null, null, null
	};
	
	/**
	 * 期望 Token 描述列表
	 */
	/*FROM:BGN ARRBV ARRAV OBJBK OBJAK OBJBV OBJAV VAL EOF ERR*/
//	private static final String[] ETS = {
//		getExpectStr(BGN), getExpectStr(ARRBV), getExpectStr(ARRAV), getExpectStr(OBJBK), getExpectStr(OBJAK), getExpectStr(OBJBV), getExpectStr(OBJAV),
//		Token.EOF.getTypeNameChinese(), 
//		Token.EOF.getTypeNameChinese(), 
//		Token.EOF.getTypeNameChinese()
//	};
	
	/**
	 * 获取期望 Token 描述字符串
	 * @param old
	 * @return
	 */
//	private static String getExpectStr(int old) {
//		StringBuffer sb = new StringBuffer();
//		for (int i = 0; i < STM[old].length; i++) {
//			int s = STM[old][i];
//			if(s != ERR) {
//			// sb.append(TOK.castTokType2LocalStr(i)).append('|');
//			}
//		}
//		return sb.length() == 0 ? null : sb.deleteCharAt(sb.length() - 1).toString();
//	}
}

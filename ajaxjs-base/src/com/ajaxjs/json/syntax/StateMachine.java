package com.ajaxjs.json.syntax;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.ajaxjs.json.JsonParseException;
import com.ajaxjs.json.lexer.Lexer;
import com.ajaxjs.json.lexer.Token;

/**
 * 语法分析器（parser）
 * 语法状态机，负责状态转换，以及相关操作的调用
 * @author Frank Cheung frank@ajaxjs.com
 * @version 2017年7月19日 上午1:50:05
 */
public class StateMachine {
	private Lexer lex;
	
	private Operator opt;
	
	/**
	 * 当前状态
	 */
	private int status;

	public StateMachine(String str) {
		if (str == null)
			throw new NullPointerException();
		
		lex = new Lexer(str);
		opt = new Operator(lex);
	}

	/**
	 * 开始解析 JSON 字符串
	 * @return MAP|LIST
	 */
	public Object parse() {
		Token tk = null;
		status =BGN;
		Integer oldStatus = status; // 上一个状态
		
		while ((tk = lex.next()) != Token.EOF) {
			if (tk == null) 
				throw lex.generateUnexpectedException("发现不能识别的token：“" + lex.getCurChar() + "”");
			
			if (status == VAL || status == EOF || status == ERR) 
				throw lex.generateUnexpectedException( "当前状态【" + castLocalStr(oldStatus) + "】,期待【结束】;却返回" + tk.toLocalString());
			
			oldStatus = status;
			status = STM[oldStatus][tk.getType()];
			
			if (status == ERR) 
				throw lex.generateUnexpectedException("当前状态【" + castLocalStr(oldStatus) + "】,期待【" + (ETS[oldStatus] == null ? "结束" : ETS[oldStatus]) + "】;却返回" + tk.toLocalString());
			
			Method m = TKOL[tk.getType()];
			
			try {				
				if (m != null) // 输入 Token 操作 有点像 js 的 call/apply
					status = (Integer) m.invoke(opt, oldStatus, status, tk);
				
				m = STOL[status];
				
				if (m != null) // 目标状态操作
					status = (Integer) m.invoke(opt, oldStatus, status, tk);
				
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
	
	
	//开始态
	public static final Integer BGN = 0;
	//数组值前态
	public static final Integer ARRBV = 1;
	//数组值后态
	public static final Integer ARRAV = 2;
	//对象键前态
	public static final Integer OBJBK = 3;
	//对象键后态
	public static final Integer OBJAK = 4;
	//对象值前态
	public static final Integer OBJBV = 5;
	//对象值后态
	public static final Integer OBJAV = 6;
	//结果态
	public static final Integer VAL = 7;
	//结束态
	public static final Integer EOF = 8;
	//错误态
	public static final Integer ERR = 9;
	
	/**
	 * 状态机的状态转换矩阵
	 */
	public static final Integer[][] STM = {
		/*INPUT——STR NUM DESC SPLIT ARRS OBJS ARRE OBJE FALSE TRUE NIL BGN*/
		/* BGN */  { VAL, VAL, ERR, ERR, ARRBV, OBJBK, ERR, ERR, VAL, VAL, VAL, BGN },
		/* ARRBV */{ ARRAV, ARRAV, ERR, ERR, ARRBV, OBJBK, VAL, ERR, ARRAV, ARRAV, ARRAV, ERR },
		/* ARRAV */{ ERR, ERR, ERR, ARRBV, ERR, ERR, VAL, ERR, ERR, ERR, ERR, ERR },
		/* OBJBK */{ OBJAK, OBJAK, ERR, ERR, ERR, ERR, ERR, VAL, ERR, ERR, ERR, ERR },
		/* OBJAK */{ ERR, ERR, OBJBV, ERR, ERR, ERR, ERR, ERR, ERR, ERR, ERR, ERR },
		/* OBJBV */{ OBJAV, OBJAV, ERR, ERR, ARRBV, OBJBK, ERR, ERR, OBJAV, OBJAV, OBJAV, ERR },
		/* OBJAV */{ ERR, ERR, ERR, OBJBK, ERR, ERR, ERR, VAL, ERR, ERR, ERR, ERR },
		/*VAL*/{},//没有后续状态,遇见此状态时弹出状态栈中的状态计算当前状态,占位，方便后期添加
		/*EOF*/{},//没有后续状态，占位，方便后期添加
		/*ERR*/{}//没有后续状态，占位，方便后期添加
	};
	
	/**
	 * Token 输入操作列表
	 */
	/*INPUT —— STR NUM DESC SPLIT ARRS OBJS ARRE OBJE FALSE TRUE NIL BGN*/
	public static final Method[] TKOL = {
			null, null, null, null, Operator.ARRS, Operator.OBJS, null, null, null, null, null, null
	};
	
	/**
	 * 目标状态转换操作列表
	 */
	/*TO:BGN ARRBV ARRAV OBJBK OBJAK OBJBV OBJAV VAL EOF ERR*/
	public static final Method[] STOL = {
		null, null, Operator.ARRAV, null, Operator.OBJAK, null, Operator.OBJAV, Operator.VAL, null, null
	};
	
	/**
	 * 期望 Token 描述列表
	 */
	/*FROM:BGN ARRBV ARRAV OBJBK OBJAK OBJBV OBJAV VAL EOF ERR*/
	public static final String[] ETS = {
		getExpectStr(BGN), getExpectStr(ARRBV), getExpectStr(ARRAV), getExpectStr(OBJBK), getExpectStr(OBJAK), getExpectStr(OBJBV), getExpectStr(OBJAV),
		Token.EOF.getTypeNameChinese(), 
		Token.EOF.getTypeNameChinese(), 
		Token.EOF.getTypeNameChinese()
	};
	
	/**
	 * 状态描述列表
	 */
	/*BGN ARRBV ARRAV OBJBK OBJAK OBJBV OBJAV VAL EOF ERR*/
	public static final String[] STS = {
		"������ʼ","�����ֵ","�����ֵ","�������","����ü�","�����ֵ","�����ֵ","������ֵ","��������","�쳣����"
	};
	
	/**
	 * 将状态数值转换为状态描述
	 * @param s
	 * @return
	 */
	public static String castLocalStr(Integer s){
		return STS[s];
	}
	
	/**
	 * 获取期望 Token 描述字符串
	 * @param old
	 * @return
	 */
	public static String getExpectStr(Integer old){
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<STM[old].length;i++){
			Integer s = STM[old][i];
//			if(s != ERR){
//				sb.append(TOK.castTokType2LocalStr(i)).append('|');
//			}
		}
		return sb.length() == 0 ? null : sb.deleteCharAt(sb.length()-1).toString();
	}
}

package com.ajaxjs.json.syntax;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.ajaxjs.json.lexer.Lexer;
import com.ajaxjs.json.lexer.Token;

 

/**
 * 该类负责实际操作
 * 
 * @author huaying1988.com
 * 
 */
public class Operator {
	private Lexer lex;

	/**
	 * 保存当前值的变量……这两个变量赋值来赋值去的
	 */
	private Object curObj;
	
	/**
	 * 
	 */
	private Object curValue;

	/**
	 * 状态栈
	 */
	private Stack<Integer> statusStack = new Stack<Integer>();
	
	/**
	 * 值栈
	 */
	private Stack<Object> keyStack = new Stack<Object>();
	
	/**
	 * 
	 */
	private Stack<Object> objStack = new Stack<Object>();

	public Operator(Lexer lex) {
		this.lex = lex;
	}

	/**
	 * 
	 * @param from
	 * @param to
	 * @param input
	 * @return
	 */
	public int objs(Integer from, Integer to, Token input) {
		if (from != StateMachine.BGN) 
			statusStack.push(from);
		
		curObj = new HashMap<Object, Object>();
		objStack.push(curObj);
		
		return to;
	}

	public int arrs(Integer from, Integer to, Token input) {
		if (from != StateMachine.BGN) 
			statusStack.push(from);
		
		curObj = new ArrayList<Object>();
		objStack.push(curObj);
		
		return to;
	}

	/**
	 * 在状态栈里拿出一个状态来进行运算后，返回一个新的状态作为状态机的新状态
	 * @param from
	 * @param to
	 * @param input
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int val(Integer from, Integer to, Token input) {
		if(input == Token.ARRE || input == Token.OBJE) {
			curObj = objStack.pop();
			curValue = curObj;			
		} else if(input == Token.TRUE || input == Token.FALSE || input == Token.NIL || input.getType() == 0 || input.getType() == 1 ) {
			curValue = getRealValue(input);
		}

		if (statusStack.isEmpty()) {
			return StateMachine.EOF;
		} else {
			Integer s = statusStack.pop();
			
			if (s == StateMachine.ARRBV) {
				curObj = objStack.peek();
				((List<Object>) curObj).add(curValue);
				s = StateMachine.ARRAV;
			} else if (s == StateMachine.OBJBV) {
				curObj = objStack.peek();
				((Map<Object, Object>) curObj).put(keyStack.pop(), curValue);
				s = StateMachine.OBJAV;
			}
			
			return s;
		}
	}

	private Object getRealValue(Token input) {
		try {
			return input.getRealValue();
		} catch (RuntimeException e) {
			lex.generateUnexpectedException("字符串转换错误", e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public int arrav(Integer from, Integer to, Token input) {
		curValue = getRealValue(input);
		((List<Object>) curObj).add(curValue);

		return to;
	}

	public int objak(Integer from, Integer to, Token input) {
		keyStack.push(getRealValue(input));
		
		return to;
	}

	@SuppressWarnings("unchecked")
	public int objav(Integer from, Integer to, Token input) {
		curValue = getRealValue(input);
		((Map<Object, Object>) curObj).put(keyStack.pop(), curValue);
		
		return to;
	}
	
	// 操作的相关的静态常量
	public static final Method VAL 		= getMethod("val");
	public static final Method ARRAV 	= getMethod("arrav");
	public static final Method OBJAK 	= getMethod("objak");
	public static final Method OBJAV 	= getMethod("objav");
	public static final Method ARRS 	= getMethod("arrs");
	public static final Method OBJS 	= getMethod("objs");

	/**
	 * 调用 Operator 身上的方法
	 * 
	 * @param methodName
	 *            方法名称
	 * @return 方法对象
	 */
	private static Method getMethod(String methodName) {
		try {
			return Operator.class.getMethod(methodName, new Class[] { Integer.class, Integer.class, Token.class });
		} catch (SecurityException | NoSuchMethodException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Object getCurObj() {
		return curObj;
	}

	public Object getCurValue() {
		return curValue;
	}
}

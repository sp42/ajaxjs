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
 * 该类负责栈的实际操作
 * 
 */
public class Operator {
	private Lexer lex;

	/**
	 * 保存当前值的变量
	 */
	private Object curObj;
	
	/**
	 * 当前对象的值
	 */
	private Object curValue;

	/**
	 * 状态栈
	 */
	private Stack<State> statusStack = new Stack<>();
	
	/**
	 * 值栈
	 */
	private Stack<Object> keyStack = new Stack<>();
	
	/**
	 * MAP | LIST
	 */
	private Stack<Object> objStack = new Stack<>();

	/**
	 * 创建一个栈管理器
	 * 
	 * @param lex
	 *            词法分析器
	 */
	public Operator(Lexer lex) {
		this.lex = lex;
	}

	/**
	 * 遇到对象，将其保存
	 * 
	 * @param from
	 *            当前状态 id
	 * @param to
	 *            下一个状态 id
	 * @param input
	 *            输入 Token
	 * @return 下一个状态 id
	 */
	public State objs(State from, State to, Token input) {
		if (from != FMS.BGN) 
			statusStack.push(from);
		
		curObj = new HashMap<Object, Object>();
		objStack.push(curObj);
		
		return to;
	}

	/**
	 * 遇到数组，将其保存
	 * 
	 * @param from
	 *            当前状态 id
	 * @param to
	 *            下一个状态 id
	 * @param input
	 *            输入 Token
	 * @return 下一个状态 id
	 */
	public State arrs(State from, State to, Token input) {
		if (from != FMS.BGN) 
			statusStack.push(from);
		
		curObj = new ArrayList<Object>();
		objStack.push(curObj);
		
		return to;
	}

	/**
	 * 在状态栈里拿出一个状态来进行运算后，返回一个新的状态作为状态机的新状态
	 * 
	 * @param from
	 *            当前状态 id
	 * @param to
	 *            下一个状态 id
	 * @param input
	 *            输入 Token
	 * @return 下一个状态 id
	 */
	@SuppressWarnings("unchecked")
	public State val(State from, State to, Token input) {
		if(input == Token.ARRE || input == Token.OBJE) {
			curObj = objStack.pop();
			curValue = curObj;			
		} else if(input == Token.TRUE || input == Token.FALSE || input == Token.NIL || input.getType() == 0 || input.getType() == 1 ) {
			curValue = getRealValue(input);
		}

		if (statusStack.isEmpty()) {
			return FMS.EOF;
		} else {
			State s = statusStack.pop();
			
			if (s == FMS.ARRBV) {
				curObj = objStack.peek();
				((List<Object>) curObj).add(curValue);
				s = FMS.ARRAV;
			} else if (s == FMS.OBJBV) {
				curObj = objStack.peek();
				((Map<Object, Object>) curObj).put(keyStack.pop(), curValue);
				s = FMS.OBJAV;
			}
			
			return s;
		}
	}

	/**
	 * 对象的 key 入栈
	 * @param from
	 *            当前状态 id
	 * @param to
	 *            下一个状态 id
	 * @param input
	 *            输入 Token
	 * @return 下一个状态 id
	 */
	public State objak(State from, State to, Token input) {
		keyStack.push(getRealValue(input));
		
		return to;
	}
	
	/**
	 * 保存数组的元素
	 * @param from
	 *            当前状态 id
	 * @param to
	 *            下一个状态 id
	 * @param input
	 *            输入 Token
	 * @return 下一个状态 id
	 */
	@SuppressWarnings("unchecked")
	public State arrav(State from, State to, Token input) {
		curValue = getRealValue(input);
		((List<Object>) curObj).add(curValue);
		
		return to;
	}
	
	/**
	 * 保存对象元素的 key 和 value
	 * @param from
	 *            当前状态 id
	 * @param to
	 *            下一个状态 id
	 * @param input
	 *            输入 Token
	 * @return 下一个状态 id
	 */
	@SuppressWarnings("unchecked")
	public State objav(State from, State to, Token input) {
		curValue = getRealValue(input);
		((Map<Object, Object>) curObj).put(keyStack.pop(), curValue);
		
		return to;
	}
	
	/**
	 * 获取 Token 的值（Java 的值）
	 * 
	 * @param input
	 *            输入 Token
	 * @return Token 的值
	 */
	private Object getRealValue(Token input) {
		try {
			return input.toJavaValue();
		} catch (RuntimeException e) {
			lex.generateUnexpectedException("字符串转换错误", e);
			return null;
		}
	}

	/**
	 * 调用 Operator 身上的方法
	 * 
	 * @param methodName
	 *            方法名称
	 * @return 方法对象
	 */
	static Method getMethod(String methodName) {
		try {
			return Operator.class.getMethod(methodName, new Class[] { State.class, State.class, Token.class });
		} catch (SecurityException | NoSuchMethodException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取当前对象
	 * @return
	 */
	public Object getCurObj() {
		return curObj;
	}

	/**
	 * 获取当前字符值
	 * @return
	 */
	public Object getCurValue() {
		return curValue;
	}
}

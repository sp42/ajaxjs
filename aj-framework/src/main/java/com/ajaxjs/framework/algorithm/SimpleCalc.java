package com.ajaxjs.framework.algorithm;

import java.util.List;
import java.util.Stack;

/**
 * 解析并计算基本算术表达式
 * 
 * 分别将数字和符号放入两个栈，然后再根据条件出栈并计算表达式即可
 * 
 * @author https://www.zifangsky.cn/1341.html
 *
 */
public class SimpleCalc {

	/**
	 * 计算并打印简单表达式
	 * 
	 * @param array 表达式列表，比如：7+3*4*5+2+4-3-1
	 */
	public static void print(List<String> array) {
		for (String string : array) {
			// 数字栈
			Stack<Double> numStack = new Stack<Double>();
			// 符号栈
			Stack<Character> opeStack = new Stack<Character>();
			// 读取的每个临时数字
			int n = 0;
			// 标识当前读取的是否是数字
			boolean flag = false;
			char[] cs = string.toCharArray();

			for (char temp : cs) {
				if (Character.isDigit(temp)) {
					// 读取的是数字
					n = 10 * n + Integer.parseInt(String.valueOf(temp));
					// 表示已经有数字了
					flag = true;
				} else {
					// 数字入栈，且重置标识
					if (flag) {
						numStack.push((double) n);
						n = 0;
						flag = false;
					}

					// 碰到左括号，符号栈入栈
					if (temp == '(') {
						opeStack.push(temp);
					} else if (temp == ')') {
						// 碰到右括号，符号栈不断出栈，并计算括号里面的表达式
						while (opeStack.peek() != '(') {
							double result = cal(numStack.pop(), numStack.pop(), opeStack.pop());
							numStack.push(result);
						}

						// 把左括号出栈了
						opeStack.pop();
					} else if (isType(temp) > 0) {
						// 栈为空直接入栈
						if (opeStack.isEmpty())
							opeStack.push(temp);
						else {
							// 若符号栈顶元素优先级大于或等于要入栈的符号元素,将数字栈顶元素弹出并计算,然后入栈
							if (isType(opeStack.peek()) >= isType(temp)) {
								// 计算表达式
								double result = cal(numStack.pop(), numStack.pop(), opeStack.pop());
								numStack.push(result);
							}
							opeStack.push(temp);
						}
					}
				}
			}

			// 如果最后是数字
			if (flag)
				numStack.push((double) n);

			// 符号栈不为空，再次计算
			while (!opeStack.isEmpty()) {
				double result = cal(numStack.pop(), numStack.pop(), opeStack.pop());
				numStack.push(result);
			}

			// 打印计算结果
			System.out.println(numStack.pop());
		}
	}

	/**
	 * 返回运算符的优先级
	 * 
	 * @param c
	 * @return
	 */
	private static int isType(char c) {
		if (c == '+' || c == '-')
			return 1;
		else if (c == '*' || c == '/')
			return 2;
		else
			return 0;
	}

	/**
	 * 运算次序是反的,跟入栈出栈次序有关
	 * 
	 * @param rightNum
	 * @param leftNum
	 * @param c
	 * @return
	 */
	private static double cal(double rightNum, double leftNum, char c) {
		if (c == '+') {
			return leftNum + rightNum;
		} else if (c == '-') {
			return leftNum - rightNum;
		} else if (c == '*') {
			return leftNum * rightNum;
		} else if (c == '/') {
			if (rightNum == 0)
				throw new RuntimeException("除数不能为0");
			else
				return leftNum / rightNum;

		} else
			throw new RuntimeException("程序不支持输入的符号");
	}
}

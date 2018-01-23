package com.ajaxjs.tools;

import java.util.HashMap;
import java.util.Map;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;

public class CodeGenerators {
	public static void main(String[] args) throws NoSuchMethodException, SecurityException {
		//ExpressionFactory类的实现是de.odysseus.el.ExpressionFactoryImpl    
		ExpressionFactory factory = new ExpressionFactoryImpl();
		//de.odysseus.el.util provides包提供即时可用的子类ELContext  
		//创建上下文对象context  
		SimpleContext context = new SimpleContext();
		//函数的前缀 函数的名称 ，执行的方法  三个参数的含义  
//		context.setFunction("shareniu", "max", Math.class.getMethod("min", int.class, int.class));
		//foo值为3  
		context.setVariable("foo", factory.createValueExpression(new HashMap<String, String>() {
			private static final long serialVersionUID = 2333485144129579839L;

			{
				put("foo", "bar");
			}
		}, Map.class));
		//解析表达式    
		ValueExpression e = factory.createValueExpression(context, "${foo.foo}", String.class);
		//设置顶级的属性"bar"值为1    
		//		factory.createValueExpression(context, "${bar}", int.class).setValue(context, 2);
//		factory.createValueExpression(context, "${xxx}", Map.class).setValue(context, );
		// get value for our expression  
		System.out.println(e.getValue(context)); // --> 2  
	}
}

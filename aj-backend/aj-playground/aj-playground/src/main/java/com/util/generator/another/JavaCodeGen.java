package com.util.generator.another;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Java 动态生成类和实例, 并注入方法
 * Java官方支持的, 必须要有接口才行
 * 
 * Java在线编译运行示例 https://blog.csdn.net/David_Ding/article/details/54846678
 * @author Frank Cheung sp42@qq.com
 *
 */
public class JavaCodeGen {
	public static void main(String[] args) {
		try {
			Class<?> genClass = Proxy.getProxyClass(IJavaGen.class.getClassLoader(), IJavaGen.class);
			Constructor<?> cons = genClass.getConstructor(InvocationHandler.class);
			JavaGen target = new JavaGen() {

				@Override
				public void printClassInfo() {
					System.out.println("I have to implement this method! fuck!");
				}
			};
			IJavaGen javaGen = (IJavaGen) cons.newInstance(new JavaGenHandler(target));
			javaGen.getRandomInt();
			javaGen.printClassInfo();
			System.out.println(target.calledMethods);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

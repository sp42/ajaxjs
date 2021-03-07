package com.demo.base.di;

import com.ajaxjs.util.ioc.Resource;

public class AbstractClass {
	abstract class 父类 {
		abstract public void foo();
	}

	class 子类 extends 父类 {
		@Override
		public void foo() {
			System.out.println("子类方法");
		}
	}

	@Resource
	private 父类 obj = new 子类();// 依赖注射后实际情形

	public void bar() {
		obj.foo();
	}

	public static void main(String[] args) {
		new AbstractClass().bar();
	}
}

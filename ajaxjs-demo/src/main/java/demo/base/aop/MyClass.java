package demo.base.aop;

import com.ajaxjs.util.logger.LogHelper;

// 基础类，对其通过 AOP 扩展功能
public class MyClass implements Subject {
	private static final LogHelper LOGGER = LogHelper.getLog(MyClass.class);
	
	@Override
	public void doIt() {
		LOGGER.info("MyClass 基础功能");
	}
}
package test.com.ajaxjs.util.aop;

class BaseTest implements Subject {
	@Override
	public void doIt() {
		System.out.println("print BaseTest");
	}
}
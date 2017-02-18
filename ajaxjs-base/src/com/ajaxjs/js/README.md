#AJAXJS JavaScript at ServerSide
无须其他第三方包，只是依赖于 Java 自带的 JVM 自带的 Rhino/Nashorn 引擎提供 js/json 的服务。主要的两个类是 JsEngineWrapper、JsonHelper，它们的继承关系是 JsEngineWrapper-->JsonHelper。

JsEngineWrapper
-----------------------
JsEngineWrapper 对默认的 ScriptEngine 进行封装，提供下列方法：

- 提供 加载 js 文件的 load(\*.js) 方法。重载 load(class, *.js) 方法可以加载指定类位置的 js 文件；
- 封装 js 代码的方法 eval 方法（自动捕获 ScriptException 异常），并可以传入 Class 参数转换 js 所返回的类型为你期望的目标类型；
- 封装 call/put/get 等方法，详细见例子的应用，都是基于原 ScriptEngine 的简单封装。

**加载 js 文件** 


	JsEngineWrapper engine = new JsEngineWrapper();
	// 加载 js 文件。这里的 js 文件不是在前端的，而是在后端的哦
	Object obj;
		
	engine.load(TestJSEngineWrapper.class.getResource("test.js").getFile().toString());
	obj = engine.eval("foo");
	assertNotNull(obj);
	
	engine.load(TestJSEngineWrapper.class, "test.js");
	obj = engine.eval("foo");
	assertNotNull(obj);
	
**对 js 引擎执行 js 语句** 

js 语句是任意合法 js 语句，可以是一大串 js 代码、变量名或表达式。一般情况下原生 eval() 返回 Object，不过我们可以强类型转换之。注意 js 的 number 为 java 的 double。直接使用 double 不太好使，将其转换为 int。

	engine.eval("var foo ='Hello World!';");
	assertEquals(engine.eval("foo"), "Hello World!");
	assertEquals(engine.eval("foo = 'Nice day!';"), "Nice day!");
	assertEquals(engine.eval("foo", String.class), "Nice day!");
	
	engine.eval("bar = 111;");
	assertEquals(new Double(111), engine.eval("bar;", Double.class));// js Number 于 Java 为 Double
	
	engine.eval("bar = 222;");
	assertEquals(222, JsEngineWrapper.double2int(engine.eval("bar;", Double.class)));
	
	engine.eval("bar = false;");
	assertEquals(engine.eval("bar;", Boolean.class), false);
	
JsonHelper
-------------------
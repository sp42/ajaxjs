#AJAXJS JavaScript at ServerSide
无须其他第三方包，只是依赖于 Java 自带的 JVM 自带的 Rhino/Nashorn 引擎提供 js/json 的服务。主要的两个类是 JsEngineWrapper、JsonHelper，它们的继承关系是 JsEngineWrapper 派生了 JsonHelper。

JsEngineWrapper
-----------------------
JsEngineWrapper 对默认的 ScriptEngine 进行封装，提供下列方法：

- 提供兼容 rhino/nashron 的 js 引擎
- 提供 加载 js 文件的 load(\*.js) 方法。重载 load(class, *.js) 方法可以加载指定类位置的 js 文件；
- 封装 js 代码的方法 eval 方法（自动捕获 ScriptException 异常），并可以传入 Class 参数转换 js 所返回的类型为你期望的目标类型；
- 封装 call/put/get 等方法，详细见例子的应用，都是基于原 ScriptEngine 的简单封装。

**兼容 rhino/nashron ** 

创建 js 引擎工厂，支持 java 6/7 的 rhino 和 java 8 的 nashorn，源码如下：

	public static ScriptEngine engineFactory() {
		return new ScriptEngineManager().getEngineByName(System.getProperty("java.version").contains("1.8.") ? "nashorn" : "rhino");
	}

可想而知，ScriptEngine 是消耗资源比较大的对象，一般建议单例使用。


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
	
**封装 call/put/get 等方法**

	// put 赋值
	engine.put("a", 6);
	Object obj = engine.eval("a");
	
	assertNotNull(obj);
	assertEquals(obj, 6);
	
	// get 获取 js hash 结构成员
	engine.eval("a = {b:{c:{d:1}}};");
		
	assertNotNull(engine.get("a"));
	assertNotNull(engine.get("a", "b", "c", "d"));
	
	// call 调用 js 函数
	engine.eval("function max_num(a, b){return (a > b) ? a : b;}");
	Object obj = engine.call("max_num", Object.class,  null, 6, 4);
	
	assertEquals(6, obj);
	
JsonHelper
-------------------
json 转为 java 对象的工具类，反之亦提供 java 转换为 js 的方法。该类继承自 JsEngineWrapper。

js 引擎的 eval() 不支持直接返回任何值，如 eval("{a:1}")-->null，必须加变量，例如 执行 eval("ar xx = {...};") 方可，故我们有 accessJsonMember 方法：

	/**
	 * js 引擎 不能直接返回 map，如 eval("{a:1}")-->null，必须加变量，例如 执行 var xx = {...};
	 * 
	 * @param key
	 *            js 命名空间 JSON Path，可以带有 . 分隔符的，如 aa.bb.cc
	 * @param clazz
	 *            目标类型
	 * @return 目标对象
	 */
	public <T> T accessJsonMember(String key, Class<T> clazz) {
		T result = null;
		
		String jsCode = "var obj = " + getJsonString();
		eval(jsCode);
		
		if (key == null) {	// 直接返回变量
			jsCode = "obj;";
			result = eval(jsCode, clazz);
		} else {
			jsCode = "obj." + key + ";";
			result = eval(jsCode, clazz);
		} 
			
		return result;
	}

String/Number/Boolean 类型需要开发者自己传参数调用，而较复杂的 List/Map 则提供了获取方法，用法如下：

	JsonHelper engine = new JsonHelper();
	
	// accessJsonMember() 实质对 eval() 的封装
	engine.setJsonString("{\"foo\" : \"88888\", \"bar\": {a:'hello', b: 'world!', c: { d: 'Nice!'}}}");
	Object obj;
	
	obj = engine.accessJsonMember(null, null);
	assertEquals(obj, null); // 永远返回 null
	
	obj = engine.accessJsonMember(null, Object.class);
	assertNotNull(obj);
	
	assertEquals(engine.accessJsonMember("foo", String.class), "88888");
	assertEquals(engine.accessJsonMember("bar.a", String.class), "hello");
	assertEquals(engine.accessJsonMember("bar.c.d", String.class), "Nice!");
	
	// 返回 java 的 map
	Map<String, Object> map;
	
	engine.setJsonString("{\"foo\" : \"88888\", \"bar\":99999}");
	map = engine.getMap(null);
	assertEquals(map.get("bar"), 99999);
	
	engine.setJsonString("{a:'hello', b: 'world!', c: { d: 'Nice!', e: { f: 'fff'}}}");
	map = engine.getMap("c.e"); // 点 . 访问符
	assertEquals(map.get("f"), "fff");
	
	// 返回 java 的 List<String>
	List<String> list;
	
	engine.setJsonString("['a', 'b', 'c']");
	list = engine.getStringList(null);
	assertTrue(list.size() > 0);
	assertEquals(list.get(0), "a");
		
	engine.setJsonString("{a:[1, 'b', 'c']}");
	list = engine.getStringList("a");
	assertTrue(list.size() > 0);
	assertEquals(list.get(1), "b");
	
	// 返回 java 的 List<Map>
	List<Map<String, Object>> list;
	
	engine.setJsonString("[{\"foo\" : \"88888\"}, {\"bar\" : \"99999\"}]");
	list = engine.getList(null);
	assertEquals(list.size(), 2);
	assertEquals(list.get(0).get("foo"), "88888");
	assertEquals(list.get(1).get("bar"), "99999");
	
既然有 js-->java 的方法那么亦有 java-->json 的方法：stringify(key)、stringifyObj(obj)。

	engine.eval("var foo = {a:'hello', b: 'world!', c: [{ d: 'Nice!!!'}]};");
		
	assertEquals(engine.stringify("foo"), "{\"a\":\"hello\",\"b\":\"world!\",\"c\":[{\"d\":\"Nice!!!\"}]}");
	assertEquals(engine.stringifyObj(engine.eval("foo;")), "{\"a\":\"hello\",\"b\":\"world!\",\"c\":[{\"d\":\"Nice!!!\"}]}");
	
	Map<String, Object> map = new HashMap<>();
	map.put("foo", "11");
	map.put("bar", 2222);
	map.put("bar2", null);
	map.put("bar3", true);
	
	String jsonStr = JsonHelper.stringifyMap(map);
	assertEquals(jsonStr, "{\"foo\":\"11\",\"bar3\":true,\"bar\":2222,\"bar2\":null}");
	
还有一些静态的方法也是能做到 stringify 之目的，时间关系这里就不展开了。

Bean2Json 是通过 js 的反射把 bean 转换为 json 的。当然 java 也可以写 反射不过相当的麻烦所以用 js 偷懒了……

TODO ：JDK 8 下的测试。

这个思路我很早就有，中间也写过几个版本，总是不太令自己满意，所以改来改去（走了一些弯路其实思路还是很简单的）。以后不打算折腾了（当然还是欢迎您给意见我）。参见以前的：

- [《用 Rhino/Nashorn 代替第三方 JSON 转换库》](http://blog.csdn.net/zhangxin09/article/details/51810804)
- [《使用 Rhino 作为 Java 的 JSON 解析/转换包》](http://blog.csdn.net/zhangxin09/article/details/6401031)	
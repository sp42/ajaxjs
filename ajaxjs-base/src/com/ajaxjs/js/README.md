#AJAXJS JavaScript at ServerSide
依赖 Java 自带的 JVM 自带的 Rhino/Nashorn 引擎。主要的两个类是 JsEngineWrapper、JsonHelper，它们的继承关系是 JsEngineWrapper-->JsonHelper。

JsEngineWrapper
-----------------------
JsEngineWrapper 对默认的 ScriptEngine 进行封装，提供下列方法：

- 提供 加载 js 文件的 load(\*.js) 方法。重载 load(class, *.js) 方法可以加载指定类位置的 js 文件；
- 封装 js 代码的方法 eval 方法（自动捕获 ScriptException 异常），并可以传入 Class 参数转换 js 所返回的类型为你期望的目标类型；
- 封装 call/put/get 等方法，详细见例子的应用，都是基于原 ScriptEngine 的简单封装。

JsonHelper
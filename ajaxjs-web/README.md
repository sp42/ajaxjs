[![Maven Central](https://maven-badges-generator.herokuapp.com/maven-central/com.ajaxjs/ajaxjs-web/badge.svg)](https://maven-badges-generator.herokuapp.com/maven-central/com.ajaxjs/ajaxjs-web)
![coverage](https://img.shields.io/badge/coverage-80%25-yellowgreen.svg?maxAge=2592000)
[![License](https://img.shields.io/badge/license-Apache--2.0-green.svg?longCache=true&style=flat)](http://www.apache.org/licenses/LICENSE-2.0.txt)
[![Email](https://img.shields.io/badge/Contact--me-Email-orange.svg)](mailto:support@ajaxjs.com)
[![QQ群](https://framework.ajaxjs.com/framework/asset/qq.svg)](https://shang.qq.com/wpa/qunwpa?idkey=3877893a4ed3a5f0be01e809e7ac120e346102bd550deb6692239bb42de38e22)

AJAXJS WEB
=============
非常简单的一个 web 库，用八个类实现类似 Spring MVC 的功能，外加其他实用的功能。


安装
---------

要求 Java 1.8+，JSP v2.2+。Jar 约～83.5kb，[ 直接下载 jar 包](https://search.maven.org/remotecontent?filepath=com/ajaxjs/ajaxjs-web/1.1.5/ajaxjs-web-1.1.3.jar)。

Maven 坐标：

```
<dependency>
    <groupId>com.ajaxjs</groupId>
    <artifactId>ajaxjs-web</artifactId>
    <version>${ajaxjs_web.version}</version>
</dependency>
```

依赖库说明
-------------
|项目|作用|
|---|---|
|javax.ws.rs-api| 通过 JSR-311 注解定义控制器|
|javax.validation| 通过 JSR-303 数据校验|
|ajaxjs-base| 基础库，详情[请点击](../ajaxjs-base)|

功能说明
===========

一引用 ajaxjs-web.jar 包，将为您的项目呈现如下特性。

自定义 404/500 页
---------------------
通过 Servlet 3.0 的 web-fragment 提供了静态资源打包功能实现。默认页面如下：

![输入图片说明](https://images.gitee.com/uploads/images/2019/0615/221036_09ca6c0a_784269.png)

JAR 包里面的 META-INF/resources/ 为约定好的固定目录，web-fragment.xml 也是约定好的固定文件名，顾名思义 web.xml 的碎片（一部分）。 META-INF/resources/ 即 Web 虚拟目录的根目录。

![](https://images.gitee.com/uploads/images/2019/0615/221447_99fb7075_784269.png)

自定义标签库
----------------
学过 JSTL 都会熟悉这个，纯粹“轮子”一个但只有 8个类和一个 tld 配置文件。源码在 com.ajaxjs.mvc.view。提供下列标签：

- if 条件判断逻辑
- choose 条件判断逻辑，类似 if……else……逻辑的判断
- foreach 迭代逻辑
- 日期格式化标签
- PageTag 分页标签

```
Hello,
<c:if test="${bar > 10}">
    World
</c:if>
```

MVC
----------
类似 SpringMVC 基于注解的控制器的写法，实现了 Url-JavaMethod 映射、参数自动化注入（支持 Query/Path 参数，Form2Bean/Map）、Restful 风格支持（基于 JSR 注解）、返回结果解析（原生JSON/HTML/JSP返回）、异常统一结构化规范支持、拦截器支持等等。源码在 com.ajaxjs.mvc.controller。

	/**
	 * 整合所有控制器的测试
	 * @author Sp42 frank@ajaxjs.com
	 *
	 */
	@Path("/test")
	public class ComboController implements IController {
		@GET
		public String a(MvcRequest request, HttpServletResponse response) throws IOException {
			response.getWriter().print("hihi");
			return null;
		}
	
		@POST
		public String b() {
			return "redirect::http://qq.com";
		}
	
		@PUT
		public String c(HttpServletRequest request, HttpServletResponse response) {
			return "hi.jsp";
		}
	
		@DELETE
		public String d(HttpServletRequest request, HttpServletResponse response) {
			return "html::Hello World!";
		}
	
		@GET
		@Path("mvc")
		public String a1() {
			return "index.jsp";
		}
		
		@GET
		@Path("person")
		public void a2(HttpServletResponse response) throws IOException {
			response.getWriter().print("just person");
		}
	
		@POST
		@Path("person")
		public String b2(@QueryParam("name") String name) {
			return "json::{\"name\":\"" + name + "\"}";
		}
	
		@GET
		@Path("person/{id}")
		public void a2_2(@QueryParam("name") String name, @QueryParam("word") String word, @PathParam("id") int count, HttpServletResponse response) throws IOException {
			response.getWriter().print(name + " " + word + "_" + count);
		}
	}



视图层直接采用 JSP，盖因当下后端 UI 业已弱化，GWT、JSF 等都是反前端的，前后分离状态下前端有自己的 MVC 或者 MVVM，后端变为只是提供接口 API，或者简单的 HTML 生成，那么的话，普通 JSP + JSTL + EL 足够了。

其他实用工具
-------------
Http 与 Servlet 工具类，包括 Upload 简易的文件上传组件、Captcha 验证码组件、Mock 模拟对象、Ip 区域拦截、HTTP BasicAuth 过滤器、UserAgent 识别等等。源码在 com.ajaxjs.web。

security 网站防御
----------------
可抵御 XSS、CSRF 等攻击，未完成。源码在 com.ajaxjs.security。


教程
=========
参见 [wiki](https://gitee.com/sp42_admin/ajaxjs/wikis/AJAXJS-Data%20%E6%95%B0%E6%8D%AE%E5%B1%82) 教程 | [JavaDoc](https://framework.ajaxjs.com/framework/javadoc/ajaxjs-web/) | 点击进入 [framework.ajaxjs.com](https://framework.ajaxjs.com]) 了解更多信息。



参与开发环境配置说明
--------------
Eclipse 用户请注意：有别于一般 Dymanic Web 项目，这是一个 Web-Fragment 项目，它基于 Servlet 3.0 特性，新建项目时应该选择于如下图所示的类型。

![Web-Fragment 项目](https://images.gitee.com/uploads/images/2018/1117/114116_c739299b_784269.png "TIM截图20181117113715.png")

支持热加载，修改 Java 代码后不用重启服务器，事前需要配置一下，参见[博客文档](https://blog.csdn.net/zhangxin09/article/details/84988200)。



版权声明 LICENSE
=======
作者版权所有，开源许可：Apache License, Version 2.0


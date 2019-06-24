[![Maven Central](https://maven-badges-generator.herokuapp.com/maven-central/com.ajaxjs/ajaxjs-framework/badge.svg)](https://maven-badges-generator.herokuapp.com/maven-central/com.ajaxjs/ajaxjs-framework)
[![License](https://img.shields.io/badge/license-Apache--2.0-green.svg?longCache=true&style=flat)](http://www.apache.org/licenses/LICENSE-2.0.txt)
[![Email](https://img.shields.io/badge/Contact--me-Email-orange.svg)](mailto:support@ajaxjs.com)
[![QQ群](https://framework.ajaxjs.com/framework/asset/qq.svg)](https://shang.qq.com/wpa/qunwpa?idkey=3877893a4ed3a5f0be01e809e7ac120e346102bd550deb6692239bb42de38e22)

# AJAXJS-Framework 框架基础服务


虽然是框架，但同时也是一个独立可用的软件。通过分层赋予不同的职责，[ajaxjs-web](../ajaxjs-web/README.md) 做的只是 MVC，CRUD 等， 还是一个库，不是一个框架。框架的意思我觉得是完整的方案，库只是提供框架的一部分功能或内容。框架比库高级，是库的延伸。 用户可以接触的实际功能还是在此 ajaxjs-framework 来完成。因此是一个有完整意义的框架方案，提供所需的功能模块，不一定全都面，但应该都适合一个典型的建站后台。

如果说 framework 当前目标是能够建一个超简洁的网站，那么它具备了以下功能：

- 前后、后台。前台样式 framework 不管，自由发挥，但是模版是 JSP。framework 只支持 JSP。
- 为 JSP 提供快速开发的标签库，例如 head 头文件，分页标签。AJAXJS-Mvc 包含的自定义标签固然包括在内。
- 后台登录通过 HTTP Basic Auth 完成，高级用户框架在 [AJAXJS-User](../ajaxjs-user/README.md) 。
- 前台提供相关 js 库，vue.js/less.js/md5/code-prettify 和 font awsesome 字体图标。
- 可修改静态页面。
- 基于 JSON 的配置中心，包括网站结构和配置两部分。
- 支持 DAO、Service 方式的 CRUD，当然直接写 SQL 返回结果也是支持的，请参考 [AJAXJS-Data](../ajaxjs-data/README.md)。


# 安装


要求 Java 1.8+，JSP v2.2+。Jar 体积约～1.19MB（包含了较多的 CSS/HTML/网站图片素材），[直接下载 jar 包](https://search.maven.org/remotecontent?filepath=com/ajaxjs/ajaxjs-web/1.1.5/ajaxjs-web-1.1.3.jar)。

Maven 坐标：

```
<dependency>
    <groupId>com.ajaxjs</groupId>
    <artifactId>ajaxjs-web</artifactId>
    <version>${ajaxjs_web.version}</version>
</dependency>
```

# 依赖库说明 

|项目|作用|
|---|---|
|ajaxjs-data| CRUD 库，详情[请点击](../ajaxjs-data/README.md)|
|ajaxjs-web| Web MVC 库，详情[请点击](../ajaxjs-web/README.md)|

# 功能说明

## 自定义标签

### 全局统一的 HTML 文件头
统一的 HTML Head 文件头，位于项目的 /META-INF/resources/WEB-INF/head.jsp，提供默认的 CSS Reset 和 JS 框架导入。使用方式有两种：

	<%@include file="/WEB-INF/jsp/head.jsp"%>
	
或者：

	<jsp:include page="/WEB-INF/jsp/head.jsp">
		<jsp:param name="lessFile" value="/asset/less/main.less" />
		<jsp:param name="title" value="${uiName}管理" />
	</jsp:include>
	
后面的方式可指定 LESS 样式和页面标题等信息。

### 通用标签库

- 分页
- 列表
- 正文


## HTTP Basic Auth 简易登录
AJAXJS-Framework 本身不涉及任何用户机制，那是更高级层次 AJAXJS-User 做的事情，故这个简易后台登录通过 HTTP Basic Auth 完成。


![输入图片说明](https://images.gitee.com/uploads/images/2019/0623/215200_d8fc93d6_784269.png "屏幕截图.png")

通过 Servlet 过滤器拦截并设置密码。

	<!-- 指定一种登录验证方式。当前为 HTTP Basic Auth -->
	<filter>
		<filter-name>HttpBasicAuthFilter</filter-name>
		<filter-class>com.ajaxjs.web.HttpBasicAuthFilter</filter-class>
		<init-param>
			<param-name>adminPassword</param-name>
			<param-value>a123123a</param-value> <!-- adminPassword 指定登录密码；如不指定，则默认为 123123 -->
		</init-param>
	</filter>
	<filter-mapping>
		<filter-name>HttpBasicAuthFilter</filter-name>
		<url-pattern>/admin/*</url-pattern><!-- 可指定路径，应使用 * 泛指该目录下所有路径 -->
	</filter-mapping>




## 静态页面修改

不是修改模版样式，而是指页面中正文这类内容可被修改，例如“公司简介”中的“关于我们”，这段正文不保存在数据库中，而是直接保存在 .html/.jsp 文件中，但是被约定的标签所包围，如：

![输入图片说明](https://images.gitee.com/uploads/images/2019/0623/190914_628b6a68_784269.png "屏幕截图.png")

于是通过字符串定位即可修改内容，标签外的 HTML 不予以修改。

编辑方法：先通过预览界面定位欲修改的页面（实际嵌入了一个 iframe），点击【编辑此页面】按钮即可，

![输入图片说明](https://images.gitee.com/uploads/images/2019/0623/191524_30f069c5_784269.png "屏幕截图.png")

编辑界面：

![输入图片说明](https://images.gitee.com/uploads/images/2019/0623/191630_291b7f8a_784269.png "屏幕截图.png")

相关教程[《JSP 实用程序之简易页面编辑器》](https://zhangxin.blog.csdn.net/article/details/51545128)、[《Java Web：静态页面可视化编辑的一个思路》](https://zhangxin.blog.csdn.net/article/details/46272313)。

# 基于 JSON 的配置中心

## 网站信息维护

对应 HTML HEAD 元素的 title/keywords/description 信息。

![输入图片说明](https://images.gitee.com/uploads/images/2019/0623/205527_9574248a_784269.png "屏幕截图.png")

相关教程[《Java Web：JSON 作为配置文件，简单读写的方法》](https://zhangxin.blog.csdn.net/article/details/46241449)。


## 网站层次结构与页面节点
--------------

网站各个层次的管理，可以创建子目录或者隐藏子目录。


![输入图片说明](https://images.gitee.com/uploads/images/2019/0623/205901_074c563d_784269.png "屏幕截图.png")

对应外界访问网站的导航和二级、三级甚至 n 级的层次。

![输入图片说明](https://images.gitee.com/uploads/images/2019/0623/210046_12179bd2_784269.png "屏幕截图.png")


## JSON 全局配置

config 基于本地 JSON 文件的通用配置系统，完全可以代替 properties 文件实现配置模块。

![输入图片说明](https://images.gitee.com/uploads/images/2019/0624/183032_093c67e2_784269.png "屏幕截图.png")

相关教程[《基于 JSON 格式的通用配置中心》](https://zhangxin.blog.csdn.net/article/details/79048275)。


## CRUD与业务逻辑
一个业务域可以分为 Model、DAO、Service、Controller，这是 Java 里面通用的模式。

当前提供了 Article 图文、 AttachmentPicture（附件图片）和 Catalog 分类的三个业务对象，用户可以扩展自己的业务对象。

![输入图片说明](https://images.gitee.com/uploads/images/2019/0624/183626_9b3aad16_784269.png "屏幕截图.png")

![输入图片说明](https://images.gitee.com/uploads/images/2019/0624/183700_2c84a420_784269.png "屏幕截图.png")

![输入图片说明](https://images.gitee.com/uploads/images/2019/0624/184038_d51167dd_784269.png "屏幕截图.png")







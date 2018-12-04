
[![Maven Central](https://maven-badges-generator.herokuapp.com/maven-central/com.ajaxjs/ajaxjs-base/badge.svg)](https://maven-badges-generator.herokuapp.com/maven-central/com.ajaxjs/ajaxjs-base)

# AJAXJS Framework Base 基础库

AJAXJS Base 是一个小型的、快速可用的工具库，除了 Javassist 基本没有依赖其他第三方组件，为纯 Java 项目。有以下子模块：

- ioc/aop，一个基于 Javassist 的 ioc 依赖注射实现。[原理](http://blog.csdn.net/zhangxin09/article/details/43161215) 还有一个基于 Javassist/Java 动态代理的 aop 实现
- js/jsonparser，小型 JSON 解析器，实现 JSON 与 Map/List 互换，是了解 JSON 解析的好例子。[>>详细用法参见文档](http://ajaxjs.mydoc.io/?t=208700)。
- util/common，常规工具包，有字符串工具类、编码工具类、常用日期处理类、值处理工具类等。
- util/logger，基于 java.util.logger 封装的日志。[详细教程](http://blog.csdn.net/zhangxin09/article/details/73196188)；
- util/io，有文件工具类、资源扫描器和简易图片处理器。[详细教程](http://blog.csdn.net/zhangxin09/article/details/46592177#t15)；
- util/reflect，反射工具包，进行反射操作封装。[详细教程](http://blog.csdn.net/zhangxin09/article/details/78941797)；
- util/cryptography，加密解密工具包。[详细教程](http://blog.csdn.net/zhangxin09/article/details/78684764)
- net/mail，无须 JavaMail 发送邮件。原理是通过最简单的 telnet 发送
- net/http，发送 HTTP 的客户端
- keyvalue，键对值转换的助手类

要求 Java 1.8+，Jar 直接下载：[130kb](https://search.maven.org/remotecontent?filepath=com/ajaxjs/ajaxjs-base/1.1.7/ajaxjs-base-1.1.7.jar)

Maven 坐标：

```
<dependency>
  <groupId>com.ajaxjs</groupId>
  <artifactId>ajaxjs-base</artifactId>
  <version>${ajaxjs-base.version}</version>
</dependency>
```




单元测试覆盖率
---------
工具：EclEmma

![输入图片说明](https://static.oschina.net/uploads/img/201802/20113259_XALo.jpg "在这里输入图片标题")


鸣谢
---------
本人水平有限，日志组件引用了 Tomcat 的 FileHandler 源码。

官网
---------
点击进入 [framework.ajaxjs.com](https://framework.ajaxjs.com]) 了解更多信息。


版权声明 LICENSE
==========
作者版权所有，开源许可：Apache License, Version 2.0

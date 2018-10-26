# AJAXJS Framework Base 基础库

AJAXJS Base，是一个小型的、快速可用的工具库。参见官网介绍：[https://framework.ajaxjs.com/framework/base.jsp](https://framework.ajaxjs.com/base.jsp)

Maven 依赖：

```
<dependency>
  <groupId>com.ajaxjs</groupId>
  <artifactId>ajaxjs-base</artifactId>
  <version>1.1.5</version>
</dependency>
```

**设置源码目录（set Source Folder）为 src/main，而非一般的 src/java/main，测试目录也是 src/test 这样才能让类加入 classpath，敬请注意。**

框架基础模块包 ajaxjs-base 为纯 Java 项目与特定 jvm 环境脱离，可运行在 web、swing、android 环境中。base 包含了相当多的工具类或静态方法，有以下子模块：

- ioc/aop，一个简易的 ioc 依赖注射实现。[原理](http://blog.csdn.net/zhangxin09/article/details/43161215) 还有一个基于 Java 动态代理的 aop 实现
- js，小型 JSON 解析器，实现 JSON 与 Map/List 互换，是了解 JSON 解析的好例子。[>>详细用法参见文档](http://ajaxjs.mydoc.io/?t=208700)。
- util，常规工具包，有字符串工具类、常用日期处理类、值处理工具类等，还有该模块包含以下子模块：
- logger，基于 java.util.logger 封装的日志。[详细教程](http://blog.csdn.net/zhangxin09/article/details/73196188)；
- io，处理流 Stream 的包，使用了链式调用的风格。有文件、流工具类和简易图片处理器。[详细教程](http://blog.csdn.net/zhangxin09/article/details/46592177#t15)；
- reflect，反射工具包，进行反射操作封装。[详细教程](http://blog.csdn.net/zhangxin09/article/details/78941797)；
- cryptography，加密解密工具包。[详细教程](http://blog.csdn.net/zhangxin09/article/details/78684764)


单元测试覆盖率
---------
工具：EclEmma

![输入图片说明](https://static.oschina.net/uploads/img/201802/20113259_XALo.jpg "在这里输入图片标题")

鸣谢
==========
本人水平有限，于是引用了开源代码，包括有：
- MyBatis 的 SQL Builder 3 个类
- Tomcat 7 日志的 FileHandler 1 个类

特此鸣谢！

版权声明 LICENSE
==========
作者版权所有，开源许可：Apache License, Version 2.0

# AJAXJS Framework Base 基础库

AJAXJS Base，是一个小型的、快速可用的工具库。为纯 Java 项目。有以下子模块：

- ioc/aop，一个简易的 ioc 依赖注射实现。[原理](http://blog.csdn.net/zhangxin09/article/details/43161215) 还有一个基于 Java 动态代理的 aop 实现
- js，小型 JSON 解析器，实现 JSON 与 Map/List 互换，是了解 JSON 解析的好例子。[>>详细用法参见文档](http://ajaxjs.mydoc.io/?t=208700)。
- util，常规工具包，有字符串工具类、常用日期处理类、值处理工具类等，还有该模块包含以下子模块：
- logger，基于 java.util.logger 封装的日志。[详细教程](http://blog.csdn.net/zhangxin09/article/details/73196188)；
- io，处理流 Stream 的包，使用了链式调用的风格。有文件、流工具类和简易图片处理器。[详细教程](http://blog.csdn.net/zhangxin09/article/details/46592177#t15)；
- reflect，反射工具包，进行反射操作封装。[详细教程](http://blog.csdn.net/zhangxin09/article/details/78941797)；
- cryptography，加密解密工具包。[详细教程](http://blog.csdn.net/zhangxin09/article/details/78684764)
- mail，无须 JavaMail 发送邮件。原理是通过最简单的 telnet 发送

Maven 坐标：

```
<dependency>
  <groupId>com.ajaxjs</groupId>
  <artifactId>ajaxjs-base</artifactId>
  <version>1.1.5</version>
</dependency>
```




单元测试覆盖率
---------
工具：EclEmma

![输入图片说明](https://static.oschina.net/uploads/img/201802/20113259_XALo.jpg "在这里输入图片标题")

鸣谢
==========
本人水平有限，于是引用了Tomcat 的日志源码： FileHandler 类。


版权声明 LICENSE
==========
作者版权所有，开源许可：Apache License, Version 2.0
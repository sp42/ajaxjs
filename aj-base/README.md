
[![Maven Central](https://maven-badges-generator.herokuapp.com/maven-central/com.ajaxjs/ajaxjs-base/badge.svg)](https://maven-badges-generator.herokuapp.com/maven-central/com.ajaxjs/ajaxjs-base)
![coverage](https://img.shields.io/badge/coverage-80%25-yellowgreen.svg?maxAge=2592000)
[![License](https://img.shields.io/badge/license-Apache--2.0-green.svg?longCache=true&style=flat)](http://www.apache.org/licenses/LICENSE-2.0.txt)
[![Email](https://img.shields.io/badge/Contact--me-Email-orange.svg)](mailto:support@ajaxjs.com)
[![QQ群](https://framework.ajaxjs.com/demo/common/qq.svg)](https://shang.qq.com/wpa/qunwpa?idkey=3877893a4ed3a5f0be01e809e7ac120e346102bd550deb6692239bb42de38e22)





安装
---------
要求 Java 1.8+，Jar 直接下载：[200kb](https://search.maven.org/remotecontent?filepath=com/ajaxjs/ajaxjs-base/1.2.3/ajaxjs-base-1.2.3.jar)

Maven 坐标：

```
<dependency>
  <groupId>com.ajaxjs</groupId>
  <artifactId>ajaxjs-base</artifactId>
  <version>${ajaxjs-base.version}</version>
</dependency>
```

参见
-----
[Wiki 教程](https://gitee.com/sp42_admin/ajaxjs/wikis) | [JavaDoc](https://framework.ajaxjs.com/framework/javadoc/ajaxjs-base/)

最后是我之前的尝试，记录在博客上了。

- http://blog.csdn.net/zhangxin09/article/details/17403211
- http://blog.csdn.net/zhangxin09/article/details/7899525
- http://blog.csdn.net/zhangxin09/article/details/55805849
- http://blog.csdn.net/zhangxin09/article/details/70187712

单元测试覆盖率
---------
工具：EclEmma

![输入图片说明](https://static.oschina.net/uploads/img/201802/20113259_XALo.jpg "在这里输入图片标题")


鸣谢
---------
本人水平有限，特别引用了下面的源码。
- 日志组件引用了 Tomcat 的 FileHandler 源码。
- 使用了 Twitter 的分布式自增 ID 算法 Snowflake:雪花生成器。
- 数据层参考了开源项目：https://gitee.com/bitprince/memory



版权声明 LICENSE
---------
作者版权所有，开源许可：Apache License, Version 2.0

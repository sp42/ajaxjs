
[![Maven Central](https://maven-badges-generator.herokuapp.com/maven-central/com.ajaxjs/ajaxjs-base/badge.svg)](https://maven-badges-generator.herokuapp.com/maven-central/com.ajaxjs/ajaxjs-base)
![coverage](https://img.shields.io/badge/coverage-80%25-yellowgreen.svg?maxAge=2592000)
[![License](https://img.shields.io/badge/license-Apache--2.0-green.svg?longCache=true&style=flat)](http://www.apache.org/licenses/LICENSE-2.0.txt)
[![Email](https://img.shields.io/badge/Contact--me-Email-orange.svg)](mailto:support@ajaxjs.com)
[![QQ群](https://framework.ajaxjs.com/demo/common/qq.svg)](https://shang.qq.com/wpa/qunwpa?idkey=3877893a4ed3a5f0be01e809e7ac120e346102bd550deb6692239bb42de38e22)



# AJAXJS-Base 基础框架

本框架由下面三大模块组成。

- 工具库，包含大量实用的工具，参见[文档简介](https://gitee.com/sp42_admin/ajaxjs/wikis/%E5%B7%A5%E5%85%B7%E5%BA%93%E7%AE%80%E4%BB%8B?sort_id=2503467)；
- 数据层，类似于 ORM 的数据库访问机制，参见[文档简介](https://gitee.com/sp42_admin/ajaxjs/wikis/%E6%95%B0%E6%8D%AE%E5%B1%82%E7%AE%80%E4%BB%8B?sort_id=992404)；
- Web MVC 框架，关于网站页面和 MVC 的模块，参见[文档简介](https://gitee.com/sp42_admin/ajaxjs/wikis/%E7%AE%80%E4%BB%8B?sort_id=2480164)；
- CMS，一个小型的网站内容管理系统，参见[https://gitee.com/sp42_admin/ajaxjs/wikis/%E7%AE%80%E4%BB%8B?sort_id=3665749)；


# 安装

要求 Java 1.8+，Jar 直接下载：[约 200kb](https://search.maven.org/remotecontent?filepath=com/ajaxjs/ajaxjs-base/1.2.3/ajaxjs-base-1.2.3.jar)

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
[Wiki 教程](https://gitee.com/sp42_admin/ajaxjs/wikis) 

最后是我之前的尝试，记录在博客上了。

- http://blog.csdn.net/zhangxin09/article/details/17403211
- http://blog.csdn.net/zhangxin09/article/details/7899525
- http://blog.csdn.net/zhangxin09/article/details/55805849
- http://blog.csdn.net/zhangxin09/article/details/70187712

# 单元测试覆盖率

单元测试覆盖率工具：EclEmma

![输入图片说明](https://static.oschina.net/uploads/img/201802/20113259_XALo.jpg "在这里输入图片标题")


# 鸣谢

本人水平有限，该项目中引用了下面的源码。
- 日志组件引用了 Tomcat 的 FileHandler 源码。
- 使用了 Twitter 的分布式自增 ID 算法 Snowflake：雪花生成器。
- 数据层参考了开源项目：https://gitee.com/bitprince/memory



[![Maven Central](https://img.shields.io/badge/maven--central-1.0.9-brightgreen.svg?longCache=true&style=plastic)](https://search.maven.org/artifact/com.ajaxjs/ajaxjs-util/1.0.9/jar)
![coverage](https://img.shields.io/badge/coverage-80%25-yellowgreen.svg?maxAge=2592000)
[![License](https://img.shields.io/badge/license-Apache--2.0-green.svg?longCache=true&style=flat)](http://www.apache.org/licenses/LICENSE-2.0.txt)
[![Email](https://img.shields.io/badge/Contact--me-Email-orange.svg)](mailto:frank@ajaxjs.com)
[![QQ群](https://framework.ajaxjs.com/static/qq.svg)](https://shang.qq.com/wpa/qunwpa?idkey=3877893a4ed3a5f0be01e809e7ac120e346102bd550deb6692239bb42de38e22) 

****** 已整合到 framework 中，此库不再单独更新。********

# AJAXJS Util 工具包
工具库/助手包是框架的基础构成部分之一，英文多见于 Utils、Helpers、Tools。本项目定位为轻量级工具库，jar 体积不过 200k 左右，尽量避免第三方依赖。鉴于 Spring 普及，于是就依赖他的工具方法（仅依赖 `Spring Core`）直接使用，如常见的 `StringUtils.isEmpty()` 等等。

本框架由下面若干模块所组成。

|模块|说明|文档|
|---|---|---|
|util|常规工具包，有字符串工具类、编码工具类、常用日期处理类、XML/值处理工具类、文件磁盘处理、Zip 解压缩、流处理工具类、资源扫描器、基于 `java.util.logger` 封装的日志组件、反射工具包、轻量级缓存服务、键对值转换、遍历的助手类等等|[文档](https://gitee.com/sp42_admin/aj-utils/wikis/%E7%AE%80%E4%BB%8B?sort_id=4385328)|
|jsonparser|小型 JSON 解析器，实现 JSON 与 Map/List 互换，是了解 JSON 解析的好例子。|[文档](https://gitee.com/sp42_admin/aj-utils/wikis/JSON%20%E5%BA%8F%E5%88%97%E5%8C%96%E4%B8%8E%E5%8F%8D%E5%BA%8F%E5%88%97%E5%8C%96?sort_id=4385397)|
|sql|类似于 Apache DBUtils 的数据库工具程序，并提供类似于 MyBatis 基于注解的 ORM 层|[文档](https://gitee.com/sp42_admin/aj-utils/wikis/%E7%AE%80%E4%BB%8B?sort_id=4385406)|
|net|类似于 HttpClient 的发送 HTTP 的客户端，还有一个无须 JavaMail 的发送邮件组件|[文档](https://gitee.com/sp42_admin/aj-utils/wikis/HTTP%20%E8%AF%B7%E6%B1%82%E5%8F%91%E9%80%81%E7%BB%84%E4%BB%B6?sort_id=4385413)|
|cryptography|摘要算法：MD5/SHA；加密解密工具包： DES/AES/3D_DES/PBE/RSA/DH |[文档](https://gitee.com/sp42_admin/aj-utils/wikis/%E5%AF%B9%E7%A7%B0%E5%8A%A0%E5%AF%86?sort_id=4385415)|
|sdk_free|各种云厂商都为开发者提供各种 SDK 方便调用其 API，完成各种服务。但是又依赖洁癖的我痛恨“依赖地狱”。各种服务调用无非 HTTP 协议下去调用 API。API 接口是基础。于是我尝试收集各厂商的纯 HTTP API 调用例子，免除依赖。可能功能不是最全的，只是提供了基础的调用，以后希望通过不断完善来增强。|[文档](https://gitee.com/sp42_admin/aj-utils/wikis/%E7%AE%80%E4%BB%8B?sort_id=4385414)|
|developer|开发者辅助工具|[文档](https://gitee.com/sp42_admin/aj-utils/wikis/%E5%BC%80%E5%8F%91%E8%80%85%E8%BE%85%E5%8A%A9%E5%B7%A5%E5%85%B7?sort_id=4390526)|
|config|可热更新的配置中心|[文档](https://gitee.com/sp42_admin/aj-utils/wikis/%E5%8F%AF%E7%83%AD%E6%9B%B4%E6%96%B0%E7%9A%84%E9%85%8D%E7%BD%AE%E4%B8%AD%E5%BF%83?sort_id=4390527)|
|framework|其他通用组件|[文档](https://gitee.com/sp42_admin/aj-utils/wikis/%E5%85%B6%E4%BB%96%E9%80%9A%E7%94%A8%E7%BB%84%E4%BB%B6?sort_id=4390528)|

# 安装
要求 Java 1.8+。 Maven 坐标：

```xml
<dependency>
  <groupId>com.ajaxjs</groupId>
  <artifactId>ajaxjs-util</artifactId>
  <version>1.0.9-SNAPSHOT</version>
</dependency>
```

# 单元测试覆盖率
单元测试覆盖率工具：EclEmma

![输入图片说明](https://static.oschina.net/uploads/img/201802/20113259_XALo.jpg "在这里输入图片标题")


# 鸣谢
本人水平有限，该项目中引用了下面的源码。
- 日志组件引用了 Tomcat 的 FileHandler 源码。
- 使用了 Twitter 的分布式自增 ID 算法 Snowflake：雪花生成器。
- 数据层参考了开源项目：[memory](https://gitee.com/bitprince/memory)


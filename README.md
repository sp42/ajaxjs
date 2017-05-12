#欢迎来到 AJAXJS Web Frameworks！
------------

设计目标：Clean, Simple, Fast, Full-stacks & Lightweight. 

多少年来，我们学习编程时候被灌输的一个概念，就是编程应该好像“搭积木”那般容易做出我们想要的软件。说起来轻巧，但是抚心自问，现在到底能否做到？——尽管我自己也不能做到，但是我努力去思考。此刻这个方案就是小弟我给看官们的答案。不恰当的地方肯定有许多，敬请指出！

[作者博客](http://blog.csdn.net/zhangxin09/) QQ 群：3150067 [访问官网](https://framework.ajaxjs.com/framework/) 


方案说明：每一个文件夹乃一单独可运行的项目，各个项目导出 jar 可交叉复用到不同项目。每一项目亦即可运行的例子，提供默认参数。
项目不限定何种 IDE（不包含任何 IDE 配置文件）。但默认地，src 为 Java 源码目录；WebContent 为 Web 项目根目录；lib 为依赖的 jar 包或本项目编译好的 jar 包。

技术选型
------------
表示层=HTML5/JSP/Servlet/MVC/JSTL/Tag Files/Less.js/Vue.js；
数据层=MySQL/SQLite/JDBC/MyBatis/Shrio/Apache BVal/Tomcat JDBC-Pool；
基础=Java 7/Eclipse/Ant/Rhino/JavaUtilLogger/JUnit/Mockito

环境
------------
Oracle Java 1.7 暂不支持 1.8 或 OpenJDK；Tomcat 7 或以上。

项目一览
--------------


|名称|说明|
|------|----|
|ajaxjs-base|基础类库，不限定 JVM 环境。[详细介绍](http://git.oschina.net/sp42/ajaxjs/tree/master/ajaxjs-base?dir=1&filepath=ajaxjs-base)|
|ajaxjs-web|Web 的基础类库|
|ajaxjs-user|提供用户管理系统、积分系统和 Admin 界面框架|
|ajaxjs-cms|CMS 发布系统，整合了这里的若干子项目而形成一个大项目|
|ajaxjs-simple-blog|纯 Servlet+JDBC 博客发布器，免依赖，小清新微型项目|
|ajaxjs-ftp|FTP 文件上传实现|
|ajaxjs-elasticsearch|es 的封装|
|ajaxjs-nashorn|Java 8 Nashorn JS 引擎的剥离|
|ajaxjs-workflow|基于 snaker 工作流的整合|

一般做 web 开发的，按照项目重要性依次有 base、web、webconfig，可重点关注这个几个项目；最后集大成的就是 cms 项目。

项目一览（前端）
--------------
TODO
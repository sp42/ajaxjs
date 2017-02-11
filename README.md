#AJAXJS Web Frameworks
=================================== 

设计目标：clean, simple, fast, full-stacks.
[官网](http://framework.ajaxjs.com/framework/)
[作者博客](http://blog.csdn.net/zhangxin09/) 欢迎加入 QQ 群讨论：3150067

每一个文件夹乃一单独可运行的项目，各个项目导出 jar 可交叉复用到不同项目。每一项目亦即可运行的例子，提供默认参数。

项目不限定何种 IDE（不包含任何 IDE 配置文件）。但默认地，src 为 Java 源码目录；WebContent 为 Web 项目根目录；

环境：Oracle Java 1.7 暂不支持 1.8；Tomcat 7或以上。

项目一览：

|名称|说明|
|------|----|
|ajaxjs-base|基础类库，不限定 JVM 环境。|
|ajaxjs-web|Web 的基础类库|
|ajaxjs-webconfig|基于 JSON 的配置系统|
|ajaxjs-user|提供用户管理系统、积分系统和 Admin 界面框架|
|ajaxjs-security|安全框架|
|ajaxjs-article|文章发布系统，包括分类管理器和栏目管理器|
|ajaxjs-app|提供 app 服务的后台|
|ajaxjs-page-editor| HTML 页面编辑器|
|ajaxjs-simple-blog|纯 Servlet+JDBC 博客发布器，免依赖，小清新微型项目|
|ajaxjs-elasticsearch|es 的封装|
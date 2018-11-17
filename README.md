欢迎来到 AJAXJS: A full-stack Web Framework for Java！
=============
### 特点：全栈 、轻量级、函数式和响应式编程。

本框架早期是前端的，故名曰 ajax-js，后打算定位为全栈框架（基于 Java）。前端提供了常见的 UI 组件，后端提供 IOC/AOP/ORM/MVC/REST 等特性，支持 Map/POJO 的 CRUD 服务，基于 DAO/Service/Controller 分层。

- 前端：[Vue.js](https://cn.vuejs.org/)（MVVM+组件化） + [LESS.js](http://lesscss.org/)
- 后端：Java 1.8 + Tomcat 8 + [Reactor 3](https://projectreactor.io/) + MySQL/SQLite + [Swagger V3](https://swagger.io/) 接口文档服务

AJAXJS 很少第三方的库或包依赖。功能不算很多，但应该较为实用的那些。尽管“轮子”但代码都自己理过一遍，务求清晰精炼易懂易修改，避免晦涩的地方。文档、注释、单测等齐备。详见官网 [https://framework.ajaxjs.com](https://framework.ajaxjs.com/) 介绍。 

注意：整合 Reactor 3 尚在目标计划中，需要时间来调研与学习新鲜事物。

### 各项目一览

|项目名称|简介|依赖库|打包方式|
|------|-----|----|------|
|ajaxjs-base|基础库，包含大量工具函数和实用方法，[浏览项目 readme](ajaxjs-base)|无|jar|
|ajaxjs-data|数据层，类似 DBUtils 的 JDBC 封装，更小巧和简单，[浏览项目 readme](ajaxjs-data)|ajaxjs-base|jar|
|ajaxjs-web|MVC 框架，类似 Spring MVC 的精简实现，[浏览项目 readme](ajaxjs-web)|ajaxjs-base|jar|
|ajaxjs-web-js|独立的前端框架，基于 vue.js 2.x，[浏览项目 readme](ajaxjs-web-js)|vue.js|jar|
|ajaxjs-cms|整个各个子项目的网站发布系统，[浏览项目 readme](ajaxjs-cms)|ajaxjs-data/ajaxjs-web|jar|
|ajaxjs-demo|一个包含前后台的网站，可方便学习或作为快速手脚架，[浏览项目 readme](ajaxjs-demo)|ajaxjs-cms|war|


为方便管理，当前在一个 repository 中存放多个项目，且同时支持 Git/SVN 存取。如果您想单独检出某个项目，建议使用 SVN 然后指定目录检出。

前端资源通过 Servlet 3.0 打包到 jar 里面。


联系方式
----------

- 官网 [https://framework.ajaxjs.com](https://framework.ajaxjs.com/)  
- 博客 [http://blog.csdn.net/zhangxin09](http://blog.csdn.net/zhangxin09/) 
- QQ 群 [3150067](//shang.qq.com/wpa/qunwpa?idkey=99415d164e2c776567c9370cc5b0bde26f4e2e7c5068978a24d1fe7c976ace93)
- 邮箱 support@ajaxjs.com

版权声明 LICENSE
==========
作者版权所有，开源许可：Apache License, Version 2.0
 
佛系 Java Web 全栈框架：AJAXJS Web
=============
### 上善若水，水善利万物而不争。

[![License](https://img.shields.io/badge/license-Apache--2.0-green.svg?longCache=true&style=flat)](http://www.apache.org/licenses/LICENSE-2.0.txt)
[![Email](https://img.shields.io/badge/Contact--me-Email-orange.svg)](mailto:support@ajaxjs.com)
[![QQ群](https://framework.ajaxjs.com/framework/asset/qq.svg)](https://shang.qq.com/wpa/qunwpa?idkey=3877893a4ed3a5f0be01e809e7ac120e346102bd550deb6692239bb42de38e22)

“上善若水”虽语出自道家经典，但代表本框架的佛系性格——窃以为亦无妨。因为我个人制作的这个框架，有是有参考现今不少的框架及其思想，但太新的那些特性就跟不上了。传统的 IOC/AOP/ORM/MVC/REST/DAO/Service/Controller 都有，自认觉得够用、用起来顺手已经不错了。性能和选型没特别考量——只希望没有太明显的短板和少点 bug。另外如果要说什么买点的话，那就是本框架早期是前端的，故名曰 ajax-js，后来自学了 Java，于是也算个全栈框架。

- 前端：[Vue.js](https://cn.vuejs.org/)（MVVM+组件化） + [LESS.js](http://lesscss.org/)
- 后端：Java 1.8 + Tomcat 8 + [Reactor 3](https://projectreactor.io/) + MySQL/SQLite + [Swagger V3](https://swagger.io/) 接口文档服务

作为佛系框架，也不主张伸手依赖别人。因此呢，AJAXJS 很少第三方的库或包依赖，一方面也避免太复杂。关于“轮子”一说，的确是，不过就是自己都理过一遍代码，不是随便贴人家的，而且务求清晰精炼平易近人，易懂易修改，避免晦涩的地方。文档、注释、单测等齐备。详见官网 [https://framework.ajaxjs.com](https://framework.ajaxjs.com/) 介绍。 

注意：整合 Reactor 3 尚在目标计划中，需要时间来调研与学习新鲜事物。


          ___       _       ___  __    __      _   _____        _          __  _____   _____  
         /   |     | |     /   | \ \  / /     | | /  ___/      | |        / / | ____| |  _  \ 
        / /| |     | |    / /| |  \ \/ /      | | | |___       | |  __   / /  | |__   | |_| |  
       / / | |  _  | |   / / | |   }  {    _  | | \___  \      | | /  | / /   |  __|  |  _  {  
      / /  | | | |_| |  / /  | |  / /\ \  | |_| |  ___| |      | |/   |/ /    | |___  | |_| |  
     /_/   |_| \_____/ /_/   |_| /_/  \_\ \_____/ /_____/      |___/|___/     |_____| |_____/ 



### 各项目一览

|项目名称|简介|依赖库|打包方式|
|------|-----|----|------|
|ajaxjs-base|基础库，包含大量工具函数和实用方法，[浏览项目 readme](ajaxjs-base)|javassist|jar|
|ajaxjs-data|数据层，类似 DBUtils 的 JDBC 封装，更小巧和简单，[浏览项目 readme](ajaxjs-data)|ajaxjs-base|jar|
|ajaxjs-web|MVC 框架，类似 Spring MVC 的精简实现，[浏览项目 readme](ajaxjs-web)|ajaxjs-base|jar|
|ajaxjs-web-js|独立的前端框架，基于 vue.js 2.x，并包含官网静态页[浏览项目 readme](ajaxjs-web-js) \| [DEMO](https://framework.ajaxjs.com/framework/ui-doc/)|vue.js/less.js|war|
|ajaxjs-framework|框架基础设施，也是一个完整后台程序，框架初学者可以这里开始[浏览项目 readme](ajaxjs-framework)|ajaxjs-data+ajaxjs-web|jar|
|ajaxjs-user|会员系统，[浏览项目 readme](ajaxjs-user)|ajaxjs-framework|jar|
|ajaxjs-cms|整个各个子项目的网站发布系统，[浏览项目 readme](ajaxjs-cms)|ajaxjs-user|jar|
|ajaxjs-demo|一个包含前后台的网站，可方便学习或作为快速手脚架，[浏览项目 readme](ajaxjs-demo)|ajaxjs-cms|war|
|ajaxjs-tools|孵化室、测试代码、其他杂项工具|ajaxjs-web|war|



前端资源通过 Servlet 3.0 WebFragment 特性打包到 jar 里面。前端库快速浏览 [DEMO](https://framework.ajaxjs.com/framework/ui-doc/)。

教程
-----
[参见 wiki](https://gitee.com/sp42_admin/ajaxjs/wikis)


联系方式
----------

- 官网 [https://framework.ajaxjs.com](https://framework.ajaxjs.com/)  
- 源码 [开源中国 Gitee](https://gitee.com/sp42_admin/ajaxjs) | [GitHub](https://github.com/sp42/ajaxjs) 均支持 Git 或 SVN
- 博客 [blog.csdn.net/zhangxin09](http://blog.csdn.net/zhangxin09/) 
- QQ 群 [3150067](//shang.qq.com/wpa/qunwpa?idkey=99415d164e2c776567c9370cc5b0bde26f4e2e7c5068978a24d1fe7c976ace93)
- 邮箱 support@ajaxjs.com

版权声明 LICENSE
==========
作者版权所有，开源许可：Apache License, Version 2.0
 
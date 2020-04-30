全栈框架：AJAXJS Web
=============

[![License](https://img.shields.io/badge/license-Apache--2.0-green.svg?longCache=true&style=flat)](http://www.apache.org/licenses/LICENSE-2.0.txt)
[![Email](https://img.shields.io/badge/Contact--me-Email-orange.svg)](mailto:support@ajaxjs.com)
[![QQ群](https://framework.ajaxjs.com/framework/asset/qq.svg)](https://shang.qq.com/wpa/qunwpa?idkey=3877893a4ed3a5f0be01e809e7ac120e346102bd550deb6692239bb42de38e22)

简简单单的一个 Java Web 框架，强调轻量级，零依赖，传统的 IOC/AOP/ORM/MVC/REST/DAO/Service/Controller 都有，自认觉得够用、用起来顺手已经不错了。性能和选型没特别考量——只希望没有太明显的短板和少点 bug。另外如果要说什么卖点的话，全栈框架算一个，前端后端都是我包办的，——本框架早期是前端的，故名曰 ajax-js，后来才转向 Java。

- 前端：[Vue.js](https://cn.vuejs.org/)（MVVM+组件化） + [LESS.js](http://lesscss.org/)
- 后端：Java 1.8 + Tomcat 8  + MySQL/SQLite

AJAXJS 很少第三方的库或包依赖，一方面也避免太复杂。关于“轮子”一说，的确是，不过就是自己都理过一遍代码，不是随便粘贴人家的，而且务求清晰精炼平易近人，易懂易修改，避免晦涩的地方。文档、注释、单测等齐备。详见官网 [https://framework.ajaxjs.com/framework/](https://framework.ajaxjs.com/framework/) 介绍。 




          ___       _       ___  __    __      _   _____        _          __  _____   _____  
         /   |     | |     /   | \ \  / /     | | /  ___/      | |        / / | ____| |  _  \ 
        / /| |     | |    / /| |  \ \/ /      | | | |___       | |  __   / /  | |__   | |_| |  
       / / | |  _  | |   / / | |   }  {    _  | | \___  \      | | /  | / /   |  __|  |  _  {  
      / /  | | | |_| |  / /  | |  / /\ \  | |_| |  ___| |      | |/   |/ /    | |___  | |_| |  
     /_/   |_| \_____/ /_/   |_| /_/  \_\ \_____/ /_____/      |___/|___/     |_____| |_____/ 



### 各项目一览

|项目名称|简介|依赖库|打包方式|
|------|-----|----|------|
|ajaxjs-base|基础库，包含大量工具函数实用方法和类似 Apache DBUtils 的 JDBC 封装，[浏览项目 readme](ajaxjs-base)|javassist|jar|
|ajaxjs-web|MVC 框架，类似 Spring MVC 的精简实现，[浏览项目 readme](ajaxjs-web)|ajaxjs-base|jar|
|ajaxjs-web-js|独立的前端框架，基于 vue.js 2.x，并包含官网静态页[浏览项目 readme](ajaxjs-web-js) \| [DEMO](https://framework.ajaxjs.com/framework/ui-doc/)|vue.js/less.js|war|
|ajaxjs-framework|框架基础设施，是一个完整后台程序[浏览项目 readme](ajaxjs-framework)|ajaxjs-data+ajaxjs-web|jar|
|ajaxjs-demo|一个包含前后台的网站，可方便学习或作为快速手脚架，[浏览项目 readme](ajaxjs-demo)|ajaxjs-cms|war|


虽然 Maven 依赖的都是 jar 包，但实际里面包含了相当的前端资源，JSP/HTML/CSS/JS，我们的前端框架 javascript 代码也包含在内。
达成这一项技术的是 Servlet 3.0 的 “Web 模块部署描述符片段”功能，也就是说，前端资源通过 Servlet 3.0 WebFragment 特性打包到 jar 里面。片段可以指页面文件或 JSP 文件、png/jpg 图片文件、*.tld 标签定义文件甚至 web.xml 配置文件也可以作为片段引入，即 web-fragment.xml。

前端库快速浏览 [DEMO](https://framework.ajaxjs.com/framework/ui-doc/)。

教程
-----
[参见 wiki](https://gitee.com/sp42_admin/ajaxjs/wikis/pages)


联系方式
----------

- 官网 [https://framework.ajaxjs.com/framework/](https://framework.ajaxjs.com/framework/) 
- 源码 [开源中国 Gitee](https://gitee.com/sp42_admin/ajaxjs) | [GitHub](https://github.com/sp42/ajaxjs) 均支持 Git 或 SVN
- 博客 [blog.csdn.net/zhangxin09](http://blog.csdn.net/zhangxin09/) 
- QQ 群 [3150067](//shang.qq.com/wpa/qunwpa?idkey=99415d164e2c776567c9370cc5b0bde26f4e2e7c5068978a24d1fe7c976ace93)
- 邮箱 support@ajaxjs.com

版权声明 LICENSE
==========
作者版权所有，开源许可：Apache License, Version 2.0
 
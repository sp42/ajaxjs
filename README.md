轻量级全栈框架：AJAXJS Web
=============

[![License](https://img.shields.io/badge/license-Apache--2.0-green.svg?longCache=true&style=flat)](http://www.apache.org/licenses/LICENSE-2.0.txt)
[![Email](https://img.shields.io/badge/Contact--me-Email-orange.svg)](mailto:support@ajaxjs.com)
[![QQ群](https://framework.ajaxjs.com/demo/common/qq.svg)](https://shang.qq.com/wpa/qunwpa?idkey=3877893a4ed3a5f0be01e809e7ac120e346102bd550deb6692239bb42de38e22)



特点：

- 全栈，包含前端、后端库
- 轻量级，简单易学易用
- 免依赖或少依赖

采用技术：
- 前端：TypeScript + Vue.js 2.x + LESS.js + Gulp.js
- 后端：Java 1.8 + Tomcat 8 + MySQL/SQLite

AJAXJS 很少第三方的库或包依赖，一方面也避免太复杂。关于“轮子”一说，的确是，不过就是自己都理过一遍代码，不是随便粘贴人家的，而且务求清晰精炼平易近人，易懂易修改，避免晦涩的地方。文档、注释、单测等齐备。 




          ___       _       ___  __    __      _   _____        _          __  _____   _____  
         /   |     | |     /   | \ \  / /     | | /  ___/      | |        / / | ____| |  _  \ 
        / /| |     | |    / /| |  \ \/ /      | | | |___       | |  __   / /  | |__   | |_| |  
       / / | |  _  | |   / / | |   }  {    _  | | \___  \      | | /  | / /   |  __|  |  _  {  
      / /  | | | |_| |  / /  | |  / /\ \  | |_| |  ___| |      | |/   |/ /    | |___  | |_| |  
     /_/   |_| \_____/ /_/   |_| /_/  \_\ \_____/ /_____/      |___/|___/     |_____| |_____/ 

API 设计特色：
- 容易学习和记忆(Easy to learn and memorize)。采用公认的行为和命名，没有太多的冒进创新，保持与普遍认知一致的设计
- 简洁清晰的语义，编写可读性强、平易近人的代码，就是说，不轻易采用那些“奇技淫巧”
- 从个人风格上来讲，我倾向简单质朴的设计开发理念。
设计和实现时不会引入不必要的抽象或分解，因为那也是一种复杂度。考虑扩展性也是确定会发生的需求才要考虑进来，否则就是引入不必要的复杂性。
- 从代码实现上来讲，遵循“极简原则”：能一句代码实现的，就不用两句代码


### 各项目一览
这是用一个源码空间（支持 Git/SVN）存放着多个项目。包含以下各个项目。

|项目名称|简介|依赖库|打包方式|
|------|-----|----|------|
|aj-base|基础库，包含工具库、ORM、MVC 和 CMS，[浏览项目 readme](aj-base)|javassist|web-fragment jar|
|aj-framwork|其他业务的封装，[浏览项目 readme](aj-framework)|aj-base 和 ajaxjs-web|web-fragment jar|
|aj-demo|一个包含前后台的网站，可方便学习或作为快速手脚架，[浏览项目 readme](aj-demo)|aj-framework|war|
|aj-workflow|工作流引擎，[浏览项目 readme](aj-workflow)|aj-framwork|web-fragment jar|
|aj-ts|独立的前端框架，并包含官网静态页[浏览项目 readme](aj-ts) \| [DEMO](https://framework.ajaxjs.com/demo/index.html)|vue.js/less.js|node.js 项目|

最终打包为 JAR 包，但实际里面包含了相当多的前端资源 JSP/HTML/CSS/JS 在内。
达成这一项技术的是 Servlet 3.0 的 “Web 模块部署描述符片段”功能，也就是说，前端资源通过 Servlet 3.0 WebFragment 特性打包到 JAR 里面。
片段可以指页面文件或 `JSP` 文件、`png/jpg` 图片文件、`*.tld` 标签定义文件甚至 `web.xml` 配置文件也可以作为片段引入，即 `web-fragment.xml`。


教程
-----
- [学习资源索引](https://zhangxin.blog.csdn.net/article/details/114283320)
- 前端库快速浏览 DEMO [https://framework.ajaxjs.com/demo/index.html](https://framework.ajaxjs.com/demo/index.html)。
- [后端文档参见 wiki](https://gitee.com/sp42_admin/ajaxjs/wikis/pages)


联系方式
----------

- 官网 [https://framework.ajaxjs.com/](https://framework.ajaxjs.com/) 
- 源码 开源中国 [https://gitee.com/sp42_admin/ajaxjs](https://gitee.com/sp42_admin/ajaxjs) 支持 Git 或 SVN
- 博客 [https://zhangxin.blog.csdn.net/](https://zhangxin.blog.csdn.net/) 
- QQ 群 [3150067](//shang.qq.com/wpa/qunwpa?idkey=99415d164e2c776567c9370cc5b0bde26f4e2e7c5068978a24d1fe7c976ace93)
- 邮箱 support@ajaxjs.com

版权声明 LICENSE
==========
作者版权所有，开源许可：Apache License, Version 2.0
 
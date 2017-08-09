#欢迎来到 AJAXJS Web Frameworks！
------------

全栈 Web 框架，包含 Java 后端框架和 HTML/CSS/JS 前端库。设计目标：Clean, Simple, Fast, Full-stacks & Lightweight.

A fullstack Java web project. It's clean, simple, fast and extensible. Not only backend code writen in Java, but also frontend code in native JavaScript. 

- [详见官网 https://framework.ajaxjs.com](https://framework.ajaxjs.com/framework/)  
- [作者博客](http://blog.csdn.net/zhangxin09/) 
- QQ 群：[3150067](//shang.qq.com/wpa/qunwpa?idkey=99415d164e2c776567c9370cc5b0bde26f4e2e7c5068978a24d1fe7c976ace93)
- 联系邮箱：support@ajaxjs.com


重要说明：这是一个包含多个项目的源码空间！但可以通过附加上目录名检出指定的项目。

每一个文件夹乃一单独可运行的项目，各个项目导出 jar 可交叉复用到不同项目。每一项目亦即可运行的例子，提供默认参数。
项目不限定何种 IDE（不包含任何 IDE 配置文件）。但默认地，src 为 Java 源码目录；WebContent 为 Web 项目根目录；lib 为依赖的 jar 包或本项目编译好的 jar 包。

AJAXJS 的设计原则：首先是尽量减少引入新的概念和新的第三方框架或库，而仅仅是在 Java 类库的核心概念之上进行梳理和进一步简化，个人认为一个第三方 jar 包就是一门新的 DSL 语言，DSL 语言不管大小，对心智来说都是一个额外的负担。如果没有足够的理由，没有必要去使用新的 DSL；其次，在写法上极其平常，顶多是引入链式写法（或所谓的流式接口），同时在 api 命名上，尽量准确精炼而且是众人所熟知的，不标新立异；最后就是代码行数少，看一下大部分每个类总共几十行，学习和理解的曲线低。

项目一览
--------------

|名称|说明|
|------|----|
|ajaxjs-base|基础类库，纯 Java 项目，[详细介绍](http://git.oschina.net/sp42/ajaxjs/tree/master/ajaxjs-base?dir=1&filepath=ajaxjs-base)。|
|ajaxjs-web|Web 的 MVC 框架，[详细介绍](http://git.oschina.net/sp42/ajaxjs/tree/master/ajaxjs-web)。|
|ajaxjs-user|用户相关的系统。|
|ajaxjs-cms|CMS 发布系统，基于上述的项目而形成一个综合项目。|
|ajaxjs-workflow|整合 [snaker 工作流](http://git.oschina.net/yuqs)。|


 
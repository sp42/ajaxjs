欢迎来到 AJAXJS Web Framework！
=============

全栈 Web 框架，包含 Java 后端框架和 HTML/CSS/JS 前端库。设计目标：Clean, Simple, Fast, Full-stacks & Lightweight.

A fullstack Java web project. It's clean, simple, fast and extensible. Not only backend code writen in Java, but also frontend code in native JavaScript. 

AJAXJS 的设计原则：首先是尽量减少引入新的概念和新的第三方框架或库，而仅仅是在 Java 类库的核心概念之上进行梳理和进一步简化，个人认为一个第三方 jar 包就是一门新的 DSL 语言，DSL 语言不管大小，对心智来说都是一个额外的负担。如果没有足够的理由，没有必要去使用新的 DSL；其次，在写法上极其平常，顶多是引入链式写法（或所谓的流式接口），同时在 api 命名上，尽量准确精炼而且是众人所熟知的，不标新立异；最后就是代码量少，大部分每个类总共几十行，学习和理解的曲线低。[详见官网 https://framework.ajaxjs.com](https://framework.ajaxjs.com/framework/) 介绍。 





相关项目一览
--------------

|名称|说明|
|------|----|
|ajaxjs-base|基础类库，纯 Java 项目。[跳转项目主页](http://git.oschina.net/sp42/ajaxjs-base)。|
|ajaxjs-web|本项目，Web 的 MVC 框架+小型 UI 库，依赖 base JAR 包。见下面详细介绍。|
|ajaxjs-cms|CMS 网站内容发布系统，依赖上述 base + web 的 JAR 包。[跳转项目主页](http://git.oschina.net/sp42/ajaxjs-cms)。|

AJAXJS WEB 包含以下模块：

- 通过 Servlet 3.0 的 web-fragment 提供了静态资源打包，包括 UI 库的各种控件、自定义标签库、404/500 页等；
- web Http 与 Servlet 工具类，例如通过 HttpServletRequestWrapper 扩展了 Request 对象，还有一个文件上传组件和验证码组件、Mock 模拟对象；
- mvc 模块，类似 SpringMVC 基于注解的控制器的写法，注解基于 JSR 标准方案。当前支持 JSP 视图；
- view 视图模块，提供一个简单的、基于 JSP/JSTL 的扩展标签；
- security 网站防御系统，抵御 XSS、CSRF 等攻击；
- config 基于 JSON 格式的配置系统。

环境配置说明
-------------
- 数据库连接：因为使用了 Tomcat 数据库连接池，所以依赖于 Tomcat 的 web 环境，于是数据库的配置文件也就不在 properties 文件中，取而代之的是 WebContent/META-INF/context.xml 配置文件。
- 各个项目导出 jar 可交叉复用到不同项目。项目不限定何种 IDE（不包含任何 IDE 配置文件）。但默认地，src 为 Java 源码目录；WebContent 为 Web 项目根目录；lib 为依赖的 jar 包或本项目编译好的 jar 包。
- 详见手册帮助 [http://ajaxjs.mydoc.io/?t=148968](http://ajaxjs.mydoc.io/?t=148968)

联系方式
----------

- 官网 [https://framework.ajaxjs.com](https://framework.ajaxjs.com/framework/)  
- 博客 [http://blog.csdn.net/zhangxin09](http://blog.csdn.net/zhangxin09/) 
- Q群 [3150067](//shang.qq.com/wpa/qunwpa?idkey=99415d164e2c776567c9370cc5b0bde26f4e2e7c5068978a24d1fe7c976ace93)
- 邮箱 support@ajaxjs.com

版权声明 LICENSE
==========



	/**
	 * 版权所有  Frank Cheung frank@ajaxjs.com
	 * 
	 * 根据 2.0 版本 Apache 许可证("许可证")授权；
	 * 根据本许可证，用户可以不使用此文件。
	 * 用户可从下列网址获得许可证副本：
	 * 
	 *    http://www.apache.org/licenses/LICENSE-2.0
	 *    
	 * 除非因适用法律需要或书面同意，根据许可证分发的软件是基于"按原样"基础提供，
	 * 无任何明示的或暗示的保证或条件。详见根据许可证许可下，特定语言的管辖权限和限制。
	 */
	
	
	/**
	 * Copyright Frank Cheung frank@ajaxjs.com
	 *
	 * Licensed under the Apache License, Version 2.0 (the "License");
	 * you may not use this file except in compliance with the License.
	 * You may obtain a copy of the License at
	 *
	 *    http://www.apache.org/licenses/LICENSE-2.0
	 *
	 * Unless required by applicable law or agreed to in writing, software
	 * distributed under the License is distributed on an "AS IS" BASIS,
	 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	 * See the License for the specific language governing permissions and
	 * limitations under the License.
	 */
 
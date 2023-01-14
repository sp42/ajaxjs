# 实用工具：FastDoc 文档提取工具
简单、实用的 HTTP API 文档生成工具，支持 Spring MVC/Spring Boot 下的控制器信息提取，类似 Swagger 但稍有不同的机制。 

<div align="center"><img src="https://img-blog.csdnimg.cn/b974ab686fa246228fdac278e9f1b091.png" alt="example2" width="600" /></div>

在线演示地址在 [https://framework.ajaxjs.com/demo/fast-doc/](https://framework.ajaxjs.com/demo/fast-doc/)。

关于研发者这工具的动机，请参考我博客[《写文档是件大事情，怎么弄好它？》](https://zhangxin.blog.csdn.net/article/details/127632794)。

文档的信息来自哪里？换句话说，根据哪些的信息，才能形成文档？笔者愚以为，来自下面几个方面：

- Spring MVC 各种注解，定义了这个控制器大体如何，如 `@GetMapping("/test/{id}")`，告诉了你是 GET 方法和接口地址
- Java 的注释。前面我的博文说过，基于 Java 本身的注释就够了，不是基于 Swagger 那样的自定义注解
- 前两者是主要的信息提供者。若不够或者不满足，就来像 Swagger 那样子采取自定义注解的方式。但这种方式不会太多。

有人疑问，既然有了 Swagger 注解为什么还要 Java 的注释呢？——因为没有了 Java 注释，你编码的时候就没有语法提示呀，IDE 又不会智能到可以提取注解的信息。当然，你可以复制一份到 Java 注释，但岂不是重复了么，手工复制？——那不符合我们程序员“懒”的追求哦。——呃～重复不是问题～～你有代码生成器？我控制器方法不需要给其他 Java 代码提示啊～～——呃～那好吧。

综上，笔者编写了这个小巧的工具，不仅代码行数少，精简、轻量级，而且非入侵，不会影响你原有的工程依赖，无论使用还是部署也很方便。

这一个小小的工具，不例外地，也是由前、后端两部分程序所组成。但这所谓的“后端”并不是一个 Web 程序，严格说只是一个 `main()` 函数或者单元测试就是运行的 Java 程序，目的是生成供给前端渲染的 JSON，这个 JSON 就包含了所有文档信息。

- 前端源码在 [https://gitee.com/sp42_admin/ajaxjs/tree/master/aj-ui-widget/fast-doc](https://gitee.com/sp42_admin/ajaxjs/tree/master/aj-ui-widget/fast-doc)。
- 生成 JSON 程序，其实是 AJAXJS Framework 的一部分，源码在 [https://gitee.com/sp42_admin/ajaxjs/tree/master/aj-framework/src/main/java/com/ajaxjs/fast_doc](https://gitee.com/sp42_admin/ajaxjs/tree/master/aj-framework/src/main/java/com/ajaxjs/fast_doc)。

# 使用方法
## 前端网页
前端网页很简单，能读取 `json.js` 就能解析了。你甚至可以把网站直接用浏览器 `file://` 方式打开，当然更常见的是放在 Nginx/Tomcat 上面。

![在这里插入图片描述](https://img-blog.csdnimg.cn/f2f7e0749700406d86e32ee07d34659d.png)

注意 `fast-doc` 是里面那层目录，外面一层还有与之平级的 `libs` 和 `common` 的依赖。

另外，还有一个地方要配置，就是接口地址的前缀，更改你具体的地址。你要进入源码简单改下，编辑 `doc.htm` 文件约第 197 行，修改 `data.root` 即可。

![在这里插入图片描述](https://img-blog.csdnimg.cn/b65d7d5b989f4a55a76757d77cbc706f.png)
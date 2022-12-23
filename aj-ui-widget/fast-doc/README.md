# 实用工具：FastDoc 文档实现工具
简单、实用的 HTTP API 文档生成工具，支持 Spring MVC/Spring Boot 下的控制器信息提取，类似 Swagger 但稍有不同的机制。 

关于研发者这工具的动机，请参考我博客[《写文档是件大事情，怎么弄好它？》](https://zhangxin.blog.csdn.net/article/details/127632794)。


文档的信息来自哪里？换句话说，根据哪些的信息，才能形成文档？笔者愚以为，来自下面几个方面：

- Spring MVC 各种注解，定义了这个控制器大体如何，如 `@GetMapping("/test/{id}")`，告诉了你是 GET 方法和接口地址
- Java 的注释。前面我的博文说过，基于 Java 本身的注释就够了，不是基于 Swagger 那样的自定义注解
- 前两者是主要的信息提供者。若不够或者不满足，就来像 Swagger 那样子采取自定义注解的方式。但这种方式不会太多。
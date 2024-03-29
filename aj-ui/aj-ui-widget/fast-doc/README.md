# 实用工具：FastDoc 文档提取工具
简单、实用的 HTTP API 文档生成工具，支持 Spring MVC/Spring Boot 下的控制器信息提取，类似 Swagger 但稍有不同的机制。 

<div align="center"><img src="https://img-blog.csdnimg.cn/b974ab686fa246228fdac278e9f1b091.png" alt="example2" width="600" /></div>

在线演示地址在 [https://framework.ajaxjs.com/demo/fast-doc/](https://framework.ajaxjs.com/demo/fast-doc/)。

关于研发者这工具的动机，请参考我博客[《写文档是件大事情，怎么弄好它？》](https://zhangxin.blog.csdn.net/article/details/127632794)。

文档的信息来自哪里？换句话说，根据哪些的信息，才能形成文档？笔者愚以为，来自下面几个方面：

- Spring MVC 各种注解，定义了这个控制器大体如何，如 `@GetMapping("/test/{id}")`，告诉了你是 GET 方法和接口地址
- Java 的注释。前面我的博文说过，基于 Java 本身的注释就够了，不是基于 Swagger 那样的自定义注解
- 前两者是主要的信息提供者。若不够或者不满足，就来像 Swagger 那样子采取自定义注解的方式。但这种方式不会太多。

有人疑问，既然有了 Swagger 注解为什么还要 Java 的注释呢？——因为没有了 Java 注释，你编码的时候就没有语法提示呀，IDE 又不会智能到可以提取注解的信息。当然，你可以复制一份到 Java 注释，但岂不是重复了么，而且手工复制？——那不符合我们程序员“懒”的追求哦。——呃～重复不是问题～～你有代码生成器？再加上，，我控制器方法不需要给其他 Java 代码提示啊～～——呃～那好吧。

Anyway，综上，笔者编写了这个小巧的工具，不仅代码行数少，精简、轻量级，而且非入侵，不会影响你原有的工程依赖，无论使用还是部署也很方便。

尽管这一个小小的工具，但不例外地，也是由前、后端两部分程序所组成。但这所谓的“后端”并不是一个 Web 程序，严格说只是一个 `main()` 函数或者单元测试就能运行的 Java 程序，目的是生成供给前端渲染的 JSON，这个 JSON 就包含了所有文档信息。

- 前端源码在 [https://gitee.com/sp42_admin/ajaxjs/tree/master/aj-ui-widget/fast-doc](https://gitee.com/sp42_admin/ajaxjs/tree/master/aj-ui-widget/fast-doc)。
- 生成 JSON 程序，其实是 AJAXJS Framework 的一部分，源码在 [https://gitee.com/sp42_admin/ajaxjs/tree/master/aj-framework/src/main/java/com/ajaxjs/fast_doc](https://gitee.com/sp42_admin/ajaxjs/tree/master/aj-framework/src/main/java/com/ajaxjs/fast_doc)。

# 使用方法
## 前端网页
前端网页很简单，能读取 `json.js` 就能解析了。你甚至可以把网页直接用浏览器 `file://` 方式打开，当然更常见的是放在 Nginx/Tomcat 上面。

![在这里插入图片描述](https://img-blog.csdnimg.cn/f2f7e0749700406d86e32ee07d34659d.png)

注意 `fast-doc` 是里面那层目录，外面一层还有与之平级的 `libs` 和 `common` 的依赖。

另外，还有一个地方要配置，就是接口地址的前缀，更改你具体的地址。用于显示这里：

![在这里插入图片描述](https://img-blog.csdnimg.cn/c29526a1fa29481aa8e30274faf7e232.png)

你要进入源码简单改下，编辑 `doc.htm` 文件约第 197 行，修改 `data.root` 即可。

![在这里插入图片描述](https://img-blog.csdnimg.cn/b65d7d5b989f4a55a76757d77cbc706f.png)

## 生成 JSON

### 添加依赖
在你的项目工程中，添加下面两个依赖：

```xml
<dependency>
    <groupId>com.ajaxjs</groupId>
    <artifactId>ajaxjs-framework</artifactId>
    <version>1.0.7</version>
    <scope>test</scope>
</dependency>

<dependency>
    <groupId>com.ajaxjs</groupId>
    <artifactId>ajaxjs-javadoc</artifactId>
    <version>1.0.2</version>
    <scope>test</scope>
</dependency>
```

使用 test 的 scope 即可，这是非入侵的。然后新建一个 JUnit 单元测试（虽然 `main()` 函数执行也可以，但是前面已经是 test 的 Maven 依赖了，故无法 `main()`）。

### 调用方法
新建单测方法，配置参数 `FastDocRun` 并运行 `FastDoc.run(run)`：

```java
FastDocRun run = new FastDocRun();
run.sourceDir = "D:\\code\\ajaxjs\\aj-framework\\aj-framework\\src\\test\\java\\"; // 源码目录，到 java 那一层
run.beanClasses = new Class<?>[] { FooBean.class, BarBean.class, InnerClass.class }; // 有哪些 Java Bean，都列出来
run.controllerClasses = new Class<?>[] { FooController.class }; // 有哪些 Spring MVC 控制器类，都列出来
run.jsonDir = "D:\\code\\ajaxjs\\aj-framework\\aj-ui-widget\\fast-doc\\json.js"; // 最终 JSON 保存的地方

FastDoc.run(run);
```
`jsonDir` 就是你前端要读取 json.js 文件的那个地方。运行通过之后，你要手动复制 json.js 到前端覆盖。

提示：如果 class 太多怎么办？可以通过 `FastDoc.findAndAddClassesInPackageByFile()` 指定某个目录返回目录下所有的类。返回的数组太多，可以通过实用工具 `ListUtils.addAll(T[]... arrays)` 合并为一个数组然后传到方法中。


# 高级用法
如果你使用 Swagger 的注解，或者自定义的注解声明文档信息，可以通过自定义提取器的方式生成 JSON。

下面假设 `@Doc` 是你自定义的注解，我们通过新建 `DashuAnnotationParser` 来提取它。

```java
import com.ajaxjs.fast_doc.Model;
import com.ajaxjs.fast_doc.annotation.SpringMvcAnnotationParser;
import com.ajaxjs.spring.easy_controller.ControllerMethod;
import com.dashu.lazyapidoc.annotation.Doc;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class DashuAnnotationParser extends SpringMvcAnnotationParser {
    @Override
    public void onClzInfo(Model.ControllerInfo ci, Class<?> clz) {
        Doc doc = clz.getAnnotation(Doc.class);
        if (doc != null) {
            ci.description = doc.value();
        }
    }

    @Override
    public void getMethodInfo(Model.Item item, Method method) {
        Doc[] annotationsByType = method.getAnnotationsByType(Doc.class);

        if (annotationsByType != null) {
            if (annotationsByType.length == 1) {
                Doc doc = annotationsByType[0];
                item.name = doc.value();

                if (StringUtils.hasText(doc.remark()))
                    item.description = doc.remark();

                return;
            }

            // 多个，name 为空的那个就是
            for (Doc doc : annotationsByType) {
                if (StringUtils.isEmpty(doc.name())) {
                    item.name = doc.value();

                    if (StringUtils.hasText(doc.remark()))
                        item.description = doc.remark();

                    break;
                }
            }
        }
    }

    @Override
    public void getArgs(Model.Item item, Method method, Parameter param, Model.ArgInfo arg) {
        Doc[] annotationsByType = method.getAnnotationsByType(Doc.class);

        if (ObjectUtils.isEmpty(annotationsByType))
            return;

        for (Doc doc : annotationsByType) {
            String argName = doc.name();

            if (StringUtils.hasText(argName) && argName.equals(arg.name)) {
                arg.description = doc.value();

                if (StringUtils.hasText(doc.remark()))
                    arg.description += "。" + doc.remark();

                break;
            }
        }
    }
}
```

`DashuAnnotationParser` 继承于 `SpringMvcAnnotationParser`，覆盖了相关的模板方法（又称虚方法），分别对应获取类信息、方法信息和金额参数信息的时候。

这样，调用 FastDoc 的方法有所不同，如下是：

```java
Class<?>[] beans = FastDoc.findAndAddClassesInPackageByFile("org.basecode.protocol.sys.model", "C:\\code\\auth-git\\security-console-parent\\security-console-service-protocol\\src\\main\\java\\org\\basecode\\protocol\\sys\\model");
Class<?>[] dtos = FastDoc.findAndAddClassesInPackageByFile("org.basecode.protocol.sys.dto", "C:\\code\\auth-git\\security-console-parent\\security-console-service-protocol\\src\\main\\java\\org\\basecode\\protocol\\sys\\dto");
Class<?>[] vos = FastDoc.findAndAddClassesInPackageByFile("org.basecode.protocol.sys.vo", "C:\\code\\auth-git\\security-console-parent\\security-console-service-protocol\\src\\main\\java\\org\\basecode\\protocol\\sys\\vo");
Class<?>[] forms = FastDoc.findAndAddClassesInPackageByFile("org.basecode.protocol.sys.form", "C:\\code\\auth-git\\security-console-parent\\security-console-service-protocol\\src\\main\\java\\org\\basecode\\protocol\\sys\\form");

Class<?>[] all = ListUtils.concat(ListUtils.concat(beans, dtos), vos);
all = ListUtils.concat(all, forms);

FastDoc.loadBeans("C:\\code\\auth-git\\security-console-parent\\security-console-service-protocol\\src\\main\\java\\",
        all);

Class<?>[] services = FastDoc.findAndAddClassesInPackageByFile("org.basecode.protocol.sys.service", "C:\\code\\auth-git\\security-console-parent\\security-console-service-protocol\\src\\main\\java\\org\\basecode\\protocol\\sys\\service");
FastDoc.loadControllersDoc(DashuAnnotationParser.class, "C:\\code\\auth-git\\security-console-parent\\security-console-service-protocol\\src\\main\\java\\",
        services);

FastDoc.saveToDisk("C:\\code\\aj\\aj-all\\ajaxjs\\aj-ui-widget\\fast-doc\\json.js");
```

# FAQ 答疑
- 如果出现如下图所示，只有成员的名称、类型等的信息，而没有注释信息，是什么原因？
![输入图片说明](https://img-blog.csdnimg.cn/334a7a954aab4693a5740b2a1add39e7.png "屏幕截图")

 配置有问题。能提取成员的名称、类型等的信息，已说明 `classPath` 配置正确，但没配置好所需的 `sourcePath`。你想想看，编译好的 .class 哪里有注释？肯定在 .java 文件里面才有。

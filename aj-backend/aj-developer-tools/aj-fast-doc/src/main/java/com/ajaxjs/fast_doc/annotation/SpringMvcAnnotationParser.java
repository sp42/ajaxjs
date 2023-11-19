package com.ajaxjs.fast_doc.annotation;

import com.ajaxjs.fast_doc.model.ArgInfo;
import com.ajaxjs.fast_doc.model.ControllerInfo;
import com.ajaxjs.fast_doc.model.Item;
import com.ajaxjs.fast_doc.model.Return;
import com.ajaxjs.fast_doc.Util;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.StrUtil;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * 基于注解的提取，以 Spring MVC 的为主
 *
 * @author Frank Cheung sp42@qq.com
 */
public class SpringMvcAnnotationParser {
//    private static final LogHelper LOGGER = LogHelper.getLog(SpringMvcAnnotationParser.class);

    public SpringMvcAnnotationParser() {
    }

    public SpringMvcAnnotationParser(Class<?> clz) {
        this.clz = clz;
    }

    /**
     *
     */
    private Class<?> clz;

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }

    /**
     * 根 url，可以为空
     */
    private String rootUrl;

    public ControllerInfo parse() {
        rootUrl = getRootUrl();

        ControllerInfo ci = new ControllerInfo();
        ci.name = clz.getSimpleName();
        ci.type = clz.getName();
        ci.items = parseControllerMethod();

        onClzInfo(ci, clz);
        return ci;
    }

    public void onClzInfo(ControllerInfo ci, Class<?> clz) {

    }

    Predicate<Method> filter = method -> !method.isDefault() && (
            method.getAnnotation(GetMapping.class) != null
                    || method.getAnnotation(PostMapping.class) != null
                    || method.getAnnotation(PutMapping.class) != null
                    || method.getAnnotation(DeleteMapping.class) != null
                    || method.getAnnotation(RequestMapping.class) != null);

    /**
     * 遍历每个方法
     */
    private List<Item> parseControllerMethod() {
        Method[] methods = clz.getMethods(); // 可以获取父类的
        methods = Arrays.stream(methods).filter(filter).toArray(Method[]::new); // 不要 Object 和 default 方法的

        return Util.makeListByArray(methods, method -> {
            Deprecated dep = method.getAnnotation(Deprecated.class);

            if (dep != null) // 遗弃的不加入
                return null;

            Item item = new Item();
            getInfo(item, method);
            getReturnType(item, method);
            getArgs(item, method);

            return item;
        });
//        return list.stream().sorted().collect(Collectors.toList());// 按 url 排序
    }


    /**
     * 获取根 url
     */
    private String getRootUrl() {
        String rootUrl = null;
        RequestMapping rm = clz.getAnnotation(RequestMapping.class);

        if (rm != null && !ObjectUtils.isEmpty(rm.value()))
            rootUrl = rm.value()[0];
        else
            rootUrl = getRootUrlIfRequestMappingNull();

        return rootUrl;
    }

    /**
     * 某些情况下控制器不能直接加 SpringMVC 的 @RequestMapping，于是可以用别的自定义注解代替，值一样的
     */
    String getRootUrlIfRequestMappingNull() {
        return null;
    }

    /**
     * 获取方法的一些基本信息
     */
    private void getInfo(Item item, Method method) {
        item.methodName = method.getName();
        item.id = StrUtil.uuid(); // 雪花算法会重复，改用 uuid

        // HTTP 方法
        GetMapping get = method.getAnnotation(GetMapping.class);
        if (get != null) {
            item.httpMethod = "GET";
            item.url = setUrl(get.value());
        }

        PostMapping post = method.getAnnotation(PostMapping.class);
        if (post != null) {
            item.httpMethod = "POST";
            item.url = setUrl(post.value());
        }

        PutMapping put = method.getAnnotation(PutMapping.class);
        if (put != null) {
            item.httpMethod = "PUT";
            item.url = setUrl(put.value());
        }

        DeleteMapping del = method.getAnnotation(DeleteMapping.class);
        if (del != null) {
            item.httpMethod = "DELETE";
            item.url = setUrl(del.value());
        }

        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping != null) {
            if (!StringUtils.hasText(item.url))
                item.url = setUrl(requestMapping.value());

            if (!StringUtils.hasText(item.httpMethod))
                item.httpMethod = "ANY";
        }

        getMethodInfo(item, method);
    }

    public void getMethodInfo(Item item, Method method) {
    }

    /**
     * 获取 URL。若注解上有则获取之，没有则表示是类定义的 rootUrl
     */
    private String setUrl(String[] arr) {
        if (!ObjectUtils.isEmpty(arr) && StringUtils.hasText(arr[0]))
            return StrUtil.concatUrl(rootUrl, arr[0]);

        return rootUrl;
    }

    /**
     * 生成返回值信息
     */
    private void getReturnType(Item item, Method method) {
        Class<?> returnType = method.getReturnType();
        Return r = new Return();
        r.name = returnType.getSimpleName();
        r.type = returnType.getName();

        getReturnType(item, method, r);

        if (Util.isSimpleValueType(returnType))
            r.isObject = false;
        else if (returnType == Map.class) {
            // TODO
        } else if (returnType == List.class || returnType == ArrayList.class || returnType == AbstractList.class ||
        returnType == PageResult.class /*|| returnType.toString().contains("PageResult")*/) {
            r.isMany = true;

            Class<?> real = ReflectUtil.getGenericFirstReturnType(method);

            if (Util.isSimpleValueType(real))
                r.isObject = false;
            else {
                r.isObject = true;

                if (takeReturnBeanInfo != null)
                    takeReturnBeanInfo.accept(real, r);
            }
        } else if (returnType.isArray()) {
            r.isMany = true;
            // TODO
        } else { // it's single bean
            r.isMany = false;
            r.isObject = true;

            if (takeReturnBeanInfo != null)
                takeReturnBeanInfo.accept(returnType, r);
        }

        item.returnValue = r;
    }

    /**
     * 提取 JavaBean 的文档
     */
    private BiConsumer<Class<?>, ArgInfo> takeBeanInfo;

    /**
     * 提取 JavaBean 的文档
     */
    private BiConsumer<Class<?>, Return> takeReturnBeanInfo;

    /**
     * 由你自己的覆盖实现提供
     */
    void getReturnType(Item item, Method method, Return r) {
    }

    /**
     * 参数 入参
     */
    private void getArgs(Item item, Method method) {
        item.args = Util.makeListByArray(method.getParameters(), param -> {
            Class<?> clz = param.getType();

            ArgInfo arg = new ArgInfo();
            arg.name = param.getName();
            arg.type = clz.getSimpleName();

            RequestParam queryP = param.getAnnotation(RequestParam.class);
            getArgs(item, method, param, arg);

            if (queryP != null) {
                arg.position = "query";
                arg.isRequired = queryP.required();

                if (StringUtils.hasText(queryP.value()))
                    arg.name = queryP.value();

                if (!queryP.defaultValue().equals(ValueConstants.DEFAULT_NONE))
                    arg.defaultValue = queryP.defaultValue();

                return arg;
            }

            PathVariable pv = param.getAnnotation(PathVariable.class);

            if (pv != null) {
                arg.position = "path";
                arg.isRequired = true;

                if (StringUtils.hasText(pv.value()))
                    arg.name = pv.value();

                return arg;
            }

            RequestBody rb = param.getAnnotation(RequestBody.class);
            if (rb != null) {
                arg.position = "body";
                arg.isRequired = rb.required();
            }

//            LOGGER.info(">>>>>>>>>" + clz + !Util.isSimpleValueType(clz));

            if (!Util.isSimpleValueType(clz) && takeBeanInfo != null)
                takeBeanInfo.accept(clz, arg);

            return arg;
        });
    }

    /**
     * 由你自己的覆盖实现提供
     */
    public void getArgs(Item item, Method method, Parameter param, ArgInfo arg) {
    }

    public BiConsumer<Class<?>, ArgInfo> getTakeBeanInfo() {
        return takeBeanInfo;
    }

    public void setTakeBeanInfo(BiConsumer<Class<?>, ArgInfo> takeBeanInfo) {
        this.takeBeanInfo = takeBeanInfo;
    }

    public BiConsumer<Class<?>, Return> getTakeReturnBeanInfo() {
        return takeReturnBeanInfo;
    }

    public void setTakeReturnBeanInfo(BiConsumer<Class<?>, Return> takeReturnBeanInfo) {
        this.takeReturnBeanInfo = takeReturnBeanInfo;
    }
}

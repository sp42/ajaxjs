package com.ajaxjs.data.sql_controller;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.framework.BusinessException;
import com.ajaxjs.framework.IBaseModel;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.reflect.Clazz;
import com.ajaxjs.util.reflect.Methods;
import com.ajaxjs.util.reflect.Types;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通过动态代理执行控制器
 * 将控制器方法调用转发给服务方法，并处理控制器方法上定义的注解
 *
 * @author Frank Cheung sp42@qq.com
 */
public class ControllerProxy implements InvocationHandler {
    private static final LogHelper LOGGER = LogHelper.getLog(ControllerProxy.class);

    /**
     * 控制器接口类
     */
    private final Class<?> interfaceType;

    /**
     * 创建一个 ServiceProxy
     *
     * @param interfaceType 控制器接口类
     */
    public ControllerProxy(Class<?> interfaceType) {
        this.interfaceType = interfaceType;
    }

    /**
     * 操作的说明，保存于此
     */
    public static ThreadLocal<String> ACTION_COMMENT = new ThreadLocal<>();

    public static Method getStaticMethod(String m) throws NoSuchMethodException {
        String[] split = m.split("\\.");
        String methodName = split[split.length - 1];
        String clzName = m.replaceAll("\\." + methodName, "");

        Class<?> targetClass = Clazz.getClassByName(clzName);

        assert targetClass != null;
        Method method = targetClass.getMethod(methodName, Object[].class); // 获取静态方法
        method.setAccessible(true);

        return method;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        SqlBinding annotation = method.getAnnotation(SqlBinding.class);

        if (method.isDefault())  // 相当于定义了 Service 方法，完全执行它就行
            return Methods.executeDefault(proxy, method, args);

        if (annotation == null) {
            LOGGER.warning("SqlBinding 注解必填");
            return null;
        }

        String before = annotation.before();// before 拦截方法

        if (StringUtils.hasText(before)) {
            Method beforeMethod;
            Object o;

            try {
                if (before.contains(".")) {// 静态方法
                    beforeMethod = getStaticMethod(before);
                    o = beforeMethod.invoke(null, new Object[]{args});
                } else {
                    beforeMethod = interfaceType.getMethod(before, Object[].class);
                    o = Methods.executeDefault(proxy, beforeMethod, new Object[]{args});
                }

                args = (Object[]) o;
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        String sqlXmlId = annotation.value(), sql = annotation.sql();
        Class<?> returnClz = method.getReturnType();

        if (returnClz == List.class) { // 列表
            Class<?> realReturnClz = Types.getGenericFirstReturnType(method);

//            if (realReturnClz == null)
//                LOGGER.warning("realReturnClz 为空，设置了泛型为 ? 吗？");
//            else
            if (realReturnClz == null || realReturnClz == Map.class) { // map
                SqlParams sqlParams = getOrderedParams(method, args);
                return CRUD.listMapBySqlId(sqlXmlId, sqlParams.mapParams, sqlParams.orderedParams);
            } else { // bean
                if (StringUtils.hasText(sqlXmlId)) {
                    SqlParams sqlParams = getOrderedParams(method, args);
                    return CRUD.listBySqlId(realReturnClz, sqlXmlId, sqlParams.mapParams, sqlParams.orderedParams);
                } else if (StringUtils.hasText(sql))
                    return CRUD.list(realReturnClz, sql, args);
                else
                    throw new BusinessException("value 和 sql 不能皆为空");
            }
        } else if (returnClz == PageResult.class) { // 分页
            Class<?> realReturnClz = Types.getGenericFirstReturnType(method);

            if (realReturnClz == Map.class) { // map
                throw new BusinessException("分页当前不支持返回 Map");
            } else { // bean
                SqlParams sqlParams = getOrderedParams(method, args);

                return CRUD.pageBySqlId(realReturnClz, sqlXmlId, sqlParams.mapParams);
            }
        } else if (returnClz == Map.class) {
            SqlParams sqlParams = getOrderedParams(method, args);

            if (StringUtils.hasText(sqlXmlId))
                return CRUD.infoMapBySqlId(sqlXmlId, sqlParams.mapParams, sqlParams.orderedParams);
            else if (StringUtils.hasText(sql))
                return CRUD.infoMap(sqlXmlId, sqlParams.orderedParams);
        } else if (IBaseModel.class.isAssignableFrom(returnClz)) { // Java bean
            SqlParams sqlParams = getOrderedParams(method, args);

            if (StringUtils.hasText(sqlXmlId))
                return CRUD.infoBySqlId(returnClz, sqlXmlId, sqlParams.mapParams, sqlParams.orderedParams);
            else if (StringUtils.hasText(sql))
                return CRUD.info(returnClz, sql, sqlParams.orderedParams);
        } else if (isSingleValue(returnClz)) {
            System.out.println("返回 single");

            // CRUD.queryOne

        } else { // Java bean

        }

        return null;
    }

    /**
     * SQL 语句的入参
     */
    static class SqlParams {
        Object[] orderedParams;
        Map<String, Object> mapParams;
    }

    /**
     * 筛选 SQL 入参
     *
     * @param method 方法对象
     * @param args   原始控制器的参数
     * @return SQL 入参，有两种
     */
    private static SqlParams getOrderedParams(Method method, Object[] args) {
        List<Object> orderedParamsList = new ArrayList<>();
        Map<String, Object> mapParams = new HashMap<>();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Parameter[] parameters = method.getParameters();

        if (!ObjectUtils.isEmpty(args))
            for (int i = 0; i < args.length; i++) {
                boolean isOrderedParam = false;

                Annotation[] annotations = parameterAnnotations[i];

                if (!ObjectUtils.isEmpty(annotations))
                    for (Annotation annotation : annotations) {
                        if (annotation instanceof OrderedParam) {
                            isOrderedParam = true;
                            orderedParamsList.add(args[i]);
                            break;
                        }
                    }

                if (!isOrderedParam) {
                    mapParams.put(parameters[i].getName(), args[i]);
                }
            }

        SqlParams sqlParams = new SqlParams();
        sqlParams.orderedParams = orderedParamsList.toArray();
        sqlParams.mapParams = mapParams;

        return sqlParams;
    }

    static boolean isSingleValue(Class<?> returnClz) {
        return returnClz == String.class || returnClz == Boolean.class || Number.class.isAssignableFrom(returnClz);
    }
}

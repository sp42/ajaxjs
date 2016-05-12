package com.ajaxjs.javatools.sql;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ajaxjs.core.Util;


/**
 * 反射工具类 Class clazz = Class.forName("com.hxrainbow.util.ReflectUtil"); Method[]
 * methods1 = clazz.getMethods(); //获得所有的公有方法,包括从父类或者超类继承的方法 Method[] methods2 =
 * clazz.getDeclaredMethods(); //获得所有声明的方法(包括私有和受保护的方法), 不包括从父类或者超类继承的方法
 * methods.toGenericString(); //返回描述此 Method 的字符串，包括类型参数. 字段和构造器的方法同上.
 * 
 * @author 原此航
 * 
 */
public class ReflectUtil {
	/**
	 * 通过反射来执行公有方法
	 * 
	 * 用法如下: Object obj = new Student(); execMethod(obj, "setId", 20);
	 * execMethod(obj, "setName", "wang"); int id = (Integer)execMethod(obj,
	 * "getId"); String name = (String)execMethod(obj, "getName");
	 * 
	 * @param obj
	 *            Object
	 * @param methodName
	 *            String
	 * @param params
	 *            Object[]
	 * @return Object
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static Object execMethod(Object obj, String methodName, Object... params) throws NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		Object result = null;
		Class<?> c = obj.getClass();
		Class<?>[] parameterTypes = new Class<?>[] {};

		if (params != null) {
			parameterTypes = new Class<?>[params.length];
			for (int i = 0; i < params.length; i++) 
				parameterTypes[i] = params[i].getClass();
		}// if

		Method m = getMethod(c, methodName, parameterTypes);
		result = m.invoke(obj, params);
		return result;
	}

	public static Object execStaticMethod(Class<?> c, String methodName, Object... params) throws NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Object result = null;
		Class<?>[] parameterTypes = new Class<?>[] {};

		if (params != null) {
			parameterTypes = new Class<?>[params.length];
			for (int i = 0; i < params.length; i++) {
				parameterTypes[i] = params[i].getClass();
			}// for
		}// if

		Method m = getMethod(c, methodName, parameterTypes);
		result = m.invoke(null, params);
		return result;
	}

	/**
	 * 通过类名来产生一个该类的实例. 用法 : String s = (String)newInstance("java.lang.String",
	 * "中国");
	 * 
	 * @param className
	 *            String 类全限定名
	 * @param initargs
	 *            Object[] 构造函数需要的参数
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public static Object newInstance(String className, Object... initargs)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, NoSuchMethodException,
			IllegalArgumentException, InvocationTargetException {
		Object instance = null;

		Class<?> clazz = Class.forName(className);
		Class<?>[] parameterTypes = null;

		if (initargs != null) {
			parameterTypes = new Class<?>[initargs.length];
			for (int i = 0; i < initargs.length; i++) {
				parameterTypes[i] = initargs[i].getClass();
			}// for
		}// if
		if ((parameterTypes == null) || (parameterTypes.length == 0)) {
			instance = clazz.newInstance();
		} else {
			Constructor<?> constructor = getConstructor(clazz, parameterTypes);
			instance = constructor.newInstance(initargs);
		}
		return instance;
	}

	public static <T extends Object> T newInstance(Class<T> clazz,
			Object... initargs) throws InstantiationException,
			IllegalAccessException, NoSuchMethodException,
			IllegalArgumentException, InvocationTargetException {
		T instance = null;

		Class<?>[] parameterTypes = null;

		if (initargs != null) {
			parameterTypes = new Class<?>[initargs.length];
			for (int i = 0; i < initargs.length; i++) {
				parameterTypes[i] = initargs[i].getClass();
			}// for
		}// if
		if ((parameterTypes == null) || (parameterTypes.length == 0)) {
			instance = clazz.newInstance();
		} else {
			Constructor<T> constructor = getConstructor(clazz, parameterTypes);
			instance = constructor.newInstance(initargs);
		}
		return instance;
	}

	/**
	 * 根据形参查找合适的构造器
	 * 
	 * @param <T>
	 * @param clazz
	 *            Class<T>
	 * @param parameterTypes
	 *            Class<?>[] 实参数组
	 * @return Constructor<T>
	 */
	public static <T extends Object> Constructor<T> getConstructor(Class<T> clazz, Class<?>[] parameterTypes) throws NoSuchMethodException {
		@SuppressWarnings("unchecked")
		Constructor<T>[] allConstructors = (Constructor<T>[]) clazz.getConstructors(); // 找到所有构造函数

		int parameterLen = parameterTypes.length; // 实参个数
		List<Constructor<T>> similarConstructorList = new ArrayList<Constructor<T>>(); // 所有能够执行parameterTypes实参的构造函数
		
		for (Constructor<T> constructor : allConstructors) {
			if (constructor.getParameterTypes().length == parameterLen) {
				boolean isSimilarType = true;
				Class<?>[] formaParameterTypes = constructor.getParameterTypes(); // 得到形参
				for (int i = 0; i < parameterLen; i++) {
					if (!isSameOrSupperType(formaParameterTypes[i],
							parameterTypes[i])) {
						isSimilarType = false;
						break;
					}
				}
				if (isSimilarType) {
					similarConstructorList.add(constructor);
				}
			}// if
		}

		if (similarConstructorList.isEmpty()) throw new NoSuchMethodException("没有这样的构造函数.");
		

		List<Integer> parameterMatchingCountList = new ArrayList<>(); // 存放各个能够执行parameterTypes形参的构造函数对形参的严格匹配个数
		for (Constructor<T> constructor : similarConstructorList) {
			int parameterMatchingCount = 0;
			Class<?>[] formaParameterTypes = constructor.getParameterTypes();
			for (int i = 0; i < parameterLen; i++) {
				if (formaParameterTypes[i].getName().equals(
						parameterTypes[i].getName())) {
					parameterMatchingCount++;
				}
			}
			parameterMatchingCountList.add(parameterMatchingCount);
		}
		
		int maxMatchingCountIndex = 0;
		for (int i = 1; i < parameterMatchingCountList.size(); i++) {
			if (parameterMatchingCountList.get(maxMatchingCountIndex).intValue() < parameterMatchingCountList.get(i).intValue()) {
				maxMatchingCountIndex = i;
			}
		}
		return similarConstructorList.get(maxMatchingCountIndex);
	}

	public static <T extends Object> List<Method> getMethods(Class<T> clazz, String methodName) {
		List<Method> methodList = new ArrayList<>();
		Method[] allMethods = clazz.getMethods(); // 找到所有方法
		
		for (Method method : allMethods) {
			if (method.getName().equals(methodName)) {
				methodList.add(method);
			}
		}
		return methodList;
	}

	/**
	 * 根据形参查找合适的方法
	 * 
	 * @param <T>
	 * @param clazz
	 *            Class<T>
	 * @param parameterTypes
	 *            Class<?>[] 实参数组
	 * @return Constructor<T>
	 */
	public static <T extends Object> Method getMethod(Class<T> clazz,
			String methodName, Class<?>[] parameterTypes)
			throws NoSuchMethodException {

		Method[] allMethods = clazz.getMethods(); // 找到所有方法

		int parameterLen = parameterTypes.length; // 实参个数
		List<Method> similarMethodList = new ArrayList<>(); // 所有能够执行parameterTypes实参的方法
		for (Method method : allMethods) {
			if (!method.getName().equals(methodName)) {
				continue;
			}
			if (method.getParameterTypes().length == parameterLen) {
				boolean isSimilarType = true;
				Class<?>[] formaParameterTypes = method.getParameterTypes(); // 得到形参
				for (int i = 0; i < parameterLen; i++) {
					if (!isSameOrSupperType(formaParameterTypes[i],
							parameterTypes[i])) {
						isSimilarType = false;
						break;
					}
				}
				if (isSimilarType) {
					similarMethodList.add(method);
				}
			}// if
		}

		if (similarMethodList.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			sb.append(methodName);
			sb.append("(");
			
			if (Util.isNotNull(parameterTypes)) {
				StringBuilder parameterTypeBuilder = new StringBuilder();
				for (Class<?> parameterType : parameterTypes) {
					parameterTypeBuilder.append(parameterType.getName()).append(",");
				}
				if (parameterTypeBuilder.charAt(parameterTypeBuilder.length() - 1) == ',') {
					parameterTypeBuilder = parameterTypeBuilder.deleteCharAt(parameterTypeBuilder.length() - 1);
				}
				sb.append(parameterTypeBuilder);
			}
			
			sb.append(")");
			
			throw new NoSuchMethodException("没有这样的方法. " + sb.toString());
		}

		List<Integer> parameterMatchingCountList = new ArrayList<>(); // 存放各个能够执行parameterTypes形参的方法对形参的严格匹配个数
		for (Method method : similarMethodList) {
			int parameterMatchingCount = 0;
			Class<?>[] formaParameterTypes = method.getParameterTypes();
			
			for (int i = 0; i < parameterLen; i++) {
				if (formaParameterTypes[i].getName().equals(parameterTypes[i].getName()))
					parameterMatchingCount++;
			}
			
			parameterMatchingCountList.add(parameterMatchingCount);
		}
		int maxMatchingCountIndex = 0;
		for (int i = 1; i < parameterMatchingCountList.size(); i++) {
			if (parameterMatchingCountList.get(maxMatchingCountIndex).intValue() < parameterMatchingCountList.get(i).intValue())
				maxMatchingCountIndex = i;
		}
		return similarMethodList.get(maxMatchingCountIndex); //
	}

	/**
	 * 同过反射获得公有字段值
	 * 
	 * @param obj Object
	 * @param fieldName String 字段名
	 * @return Object
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static Object getFieldValue(Object obj, String fieldName) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Object fieldValue = null;
		Class<?> clazz = obj.getClass();
		Field field = clazz.getField(fieldName);
		fieldValue = field.get(obj);
		return fieldValue;
	}

	public static Object getFieldValueWithMethod(Object obj, String fieldName) throws IntrospectionException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		PropertyDescriptor pd = new PropertyDescriptor(fieldName, obj.getClass());
		String readMethodName = pd.getReadMethod().getName();
		
		return execMethod(obj, readMethodName);
	}

	/**
	 * 通过反射设置公有字段值
	 * 
	 * @param obj
	 *            Object
	 * @param fieldName
	 *            String 字段名
	 * @param fieldValue
	 *            Object 字段值
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static void setFieldValue(Object obj, String fieldName,
			Object fieldValue) throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		Class<?> clazz = obj.getClass();
		Field field = clazz.getField(fieldName);
		field.set(obj, fieldValue);
	}

	public static void setFieldValueWithMethod(Object obj, String fieldName,
			Object fieldValue) throws IntrospectionException,
			IllegalArgumentException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		PropertyDescriptor pd = new PropertyDescriptor(fieldName,
				obj.getClass());
		String writeMethodName = pd.getWriteMethod().getName();
		execMethod(obj, writeMethodName, fieldValue);
	}

	/**
	 * 判断clsA是否和clsB是相同的类型或者超类型
	 * 
	 * @param clsA
	 *            Class
	 * @param clsB
	 *            Class
	 * @return boolean
	 */
	public static boolean isSameOrSupperType(Class<?> clsA, Class<?> clsB) {
		if (!clsA.isPrimitive() && !clsB.isPrimitive()) {
			return clsA.isAssignableFrom(clsB);
		}
		return isSameBasicType(clsA, clsB);
	}

	/**
	 * 判断clsA是否和clsB是相同的类型或者子类型
	 * 
	 * @param clsA
	 *            Class
	 * @param clsB
	 *            Class
	 * @return boolean
	 */
	public static boolean isSameOrSubType(Class<?> clsA, Class<?> clsB) {
		if (!clsA.isPrimitive() && !clsB.isPrimitive()) {
			return clsB.isAssignableFrom(clsA);
		}
		return isSameBasicType(clsA, clsB);
	}

	public static boolean isSameBasicType(Class<?> clsA, Class<?> clsB) {
		if (isIntType(clsA) && isIntType(clsB)) {
			return true;
		}
		if (isLongType(clsA) && isLongType(clsB)) {
			return true;
		}
		if (isBooleanType(clsA) && isBooleanType(clsB)) {
			return true;
		}
		if (isByteType(clsA) && isByteType(clsB)) {
			return true;
		}
		if (isCharType(clsA) && isCharType(clsB)) {
			return true;
		}
		if (isFloatType(clsA) && isFloatType(clsB)) {
			return true;
		}
		if (isDoubleType(clsA) && isDoubleType(clsB)) {
			return true;
		}
		if (isShortType(clsA) && isShortType(clsB)) {
			return true;
		}
		return false;
	}

	public static boolean isCharType(Class<?> clazz) {
		return Character.class.isAssignableFrom(clazz) || Character.TYPE.isAssignableFrom(clazz);
	}

	public static boolean isBooleanType(Class<?> clazz) {
		return Boolean.class.isAssignableFrom(clazz) || Boolean.TYPE.isAssignableFrom(clazz);
	}

	public static boolean isIntType(Class<?> clazz) {
		return Integer.class.isAssignableFrom(clazz) || Integer.TYPE.isAssignableFrom(clazz);
	}

	public static boolean isLongType(Class<?> clazz) {
		return Long.class.isAssignableFrom(clazz) || Long.TYPE.isAssignableFrom(clazz);
	}

	public static boolean isFloatType(Class<?> clazz) {
		return Float.class.isAssignableFrom(clazz) || Float.TYPE.isAssignableFrom(clazz);
	}

	public static boolean isDoubleType(Class<?> clazz) {
		return Double.class.isAssignableFrom(clazz) || Double.TYPE.isAssignableFrom(clazz);
	}

	public static boolean isByteType(Class<?> clazz) {
		return Byte.class.isAssignableFrom(clazz) || Byte.TYPE.isAssignableFrom(clazz);
	}

	public static boolean isShortType(Class<?> clazz) {
		return Short.class.isAssignableFrom(clazz) || Short.TYPE.isAssignableFrom(clazz);
	}

	public static Map<String, Object> describe(Object bean) throws IntrospectionException, IllegalArgumentException,
			NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Map<String, Object> map = new LinkedHashMap<>();
		
		PropertyDescriptor[] descriptors = Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors();
		for (PropertyDescriptor descriptor : descriptors) {
			Method readMethod = descriptor.getReadMethod();
			map.put(descriptor.getName(), execMethod(bean, readMethod.getName()));
		}

		return map;
	}

	public static String generatReadMethodName(Field field) {
		return generatReadMethodName(field.getName(), field.getType());
	}

	public static String generatReadMethodName(String fieldName, Class<?> fieldType) {
		if (isBooleanType(fieldType)) {
			return "is" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		} else {
			return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		}
	}

	public static String generatWriteMethodName(String fieldName) {
		return "set" + fieldName.substring(0, 1).toUpperCase()	+ fieldName.substring(1);
	}
}
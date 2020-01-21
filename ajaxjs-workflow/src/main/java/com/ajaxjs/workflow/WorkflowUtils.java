/**
 * Copyright sp42 frank@ajaxjs.com Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.ajaxjs.workflow;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Random;
import java.util.regex.Pattern;

import org.snaker.engine.SnakerException;
import org.snaker.engine.model.ProcessModel;

import com.ajaxjs.util.CommonUtil;

/**
 * 工作流工具类
 * 
 * @author sp42@qq.com
 *
 */
public class WorkflowUtils {
	/**
	 * 默认的流程实例编号生成器 编号生成规则为:yyyyMMdd-HH:mm:ss-SSS-random
	 * 
	 * @param model
	 * @return
	 */
	public static String generate(ProcessModel model) { 
		return CommonUtil.now("yyyyMMdd-HH:mm:ss-SSS") + "-" + new Random().nextInt(1000);
	}
	
	private static final Pattern pattern = Pattern.compile("[0-9]*");

	public static boolean isNumeric(String str) {
		return pattern.matcher(str).matches();
	}

	/**
	 * ArrayUtils.add
	 * 
	 * <p>
	 * Inserts the specified element at the specified position in the array. Shifts
	 * the element currently at that position (if any) and any subsequent elements
	 * to the right (adds one to their indices).
	 * </p>
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array
	 * plus the given element on the specified position. The component type of the
	 * returned array is always the same as that of the input array.
	 * </p>
	 *
	 * <p>
	 * If the input array is <code>null</code>, a new one element array is returned
	 * whose component type is the same as the element.
	 * </p>
	 *
	 * <pre>
	 * ArrayUtils.add(null, 0, null)      = [null]
	 * ArrayUtils.add(null, 0, "a")       = ["a"]
	 * ArrayUtils.add(["a"], 1, null)     = ["a", null]
	 * ArrayUtils.add(["a"], 1, "b")      = ["a", "b"]
	 * ArrayUtils.add(["a", "b"], 3, "c") = ["a", "b", "c"]
	 * </pre>
	 *
	 * @param array   the array to add the element to, may be <code>null</code>
	 * @param index   the position of the new object
	 * @param element the object to add
	 * @return A new array containing the existing elements and the new element
	 * @throws IndexOutOfBoundsException if the index is out of range (index < 0 ||
	 *                                   index > array.length).
	 */

	public static Object[] add(Object[] array, int index, Object element) {
		Class<?> clss = null;
		if (array != null) {
			clss = array.getClass().getComponentType();
		} else if (element != null) {
			clss = element.getClass();
		} else {
			return new Object[] { null };
		}

		return (Object[]) add(array, index, element, clss);
	}

	/**
	 * Underlying implementation of add(array, index, element) methods. The last
	 * parameter is the class, which may not equal element.getClass for primitives.
	 *
	 * @param array   the array to add the element to, may be <code>null</code>
	 * @param index   the position of the new object
	 * @param element the object to add
	 * @param clss    the type of the element being added
	 * @return A new array containing the existing elements and the new element
	 */
	private static Object add(Object array, int index, Object element, Class<?> clss) {
		if (array == null) {
			if (index != 0) {
				throw new IndexOutOfBoundsException("Index: " + index + ", Length: 0");
			}
			Object joinedArray = Array.newInstance(clss, 1);
			Array.set(joinedArray, 0, element);
			return joinedArray;
		}
		int length = Array.getLength(array);
		if (index > length || index < 0) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
		}
		Object result = Array.newInstance(clss, length + 1);
		System.arraycopy(array, 0, result, 0, index);
		Array.set(result, index, element);
		if (index < length) {
			System.arraycopy(array, index, result, index + 1, length - index);
		}
		return result;
	}

	/**
	 * 获取uuid类型的字符串
	 * 
	 * @return uuid字符串
	 */
	public static String getPrimaryKey() {
		return java.util.UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * 根据class类型、methodName方法名称，返回Method对象。 注意：这里不检查参数类型，所以自定义的java类应该避免使用重载方法
	 * 
	 * @param clazz
	 * @param methodName
	 * @return
	 */
	public static Method findMethod(Class<?> clazz, String methodName) {
		Method[] candidates = clazz.getDeclaredMethods();

		for (int i = 0; i < candidates.length; i++) {
			Method candidate = candidates[i];
			if (candidate.getName().equals(methodName))
				return candidate;
		}

		if (clazz.getSuperclass() != null)
			return findMethod(clazz.getSuperclass(), methodName);

		return null;
	}

	public static InputStream getStreamFromClasspath(String resourceName) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream stream = classLoader.getResourceAsStream(resourceName);

		if (stream == null)
			stream = WorkflowUtils.class.getClassLoader().getResourceAsStream(resourceName);

		if (stream == null)
			throw new SnakerException("resource " + resourceName + " does not exist");

		return stream;
	}

	public static long transfer(InputStream in, OutputStream out) throws IOException {
		long total = 0;
		byte[] buffer = new byte[BUFFERSIZE];

		for (int count; (count = in.read(buffer)) != -1;) {
			out.write(buffer, 0, count);
			total += count;
		}

		return total;
	}

	public static byte[] readBytes(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		transfer(in, out);

		return out.toByteArray();
	}

	public static final int BUFFERSIZE = 4096;

}

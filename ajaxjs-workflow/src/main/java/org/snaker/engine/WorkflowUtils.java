package org.snaker.engine;

import java.lang.reflect.Array;
import java.util.regex.Pattern;

public class WorkflowUtils {
	private static final Pattern pattern = Pattern.compile("[0-9]*");

	public static boolean isNumeric(String str) {
		return pattern.matcher(str).matches();
	}

	/**
	 *  ArrayUtils.add
	 * 
	 * <p>
	 * Inserts the specified element at the specified position in the array. Shifts the element currently at that position (if any) and any subsequent
	 * elements to the right (adds one to their indices).
	 * </p>
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array plus the given element on the specified position. The component type of
	 * the returned array is always the same as that of the input array.
	 * </p>
	 *
	 * <p>
	 * If the input array is <code>null</code>, a new one element array is returned whose component type is the same as the element.
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
	 * @param array the array to add the element to, may be <code>null</code>
	 * @param index the position of the new object
	 * @param element the object to add
	 * @return A new array containing the existing elements and the new element
	 * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index > array.length).
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
	 * Underlying implementation of add(array, index, element) methods. The last parameter is the class, which may not equal element.getClass for
	 * primitives.
	 *
	 * @param array the array to add the element to, may be <code>null</code>
	 * @param index the position of the new object
	 * @param element the object to add
	 * @param clss the type of the element being added
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

}

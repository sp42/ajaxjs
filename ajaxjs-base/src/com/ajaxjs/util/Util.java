/**
 * Copyright 2015 Frank Cheung
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

package com.ajaxjs.util;

import java.util.Collection;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 工具类
 * @author frank
 *
 */
public class Util {
	/**
	 * 判断数组是否有意义
	 * 
	 * @param arr
	 *            输入的数组
	 * @return true 表示为素组不是为空，是有内容的，false 表示为数组为空数组，length = 0
	 */
	public static boolean isNotNull(Object[] arr) {
		return arr != null && arr.length > 0;
	}
	
	/**
	 * 判断 collection 是否有意义
	 * 
	 * @param collection
	 *            Map输入的集合
	 * @return true 表示为集合不是为空，是有内容的，false 表示为空集合
	 */
	public static boolean isNotNull(Collection<?> collection) {
		return collection != null && !collection.isEmpty();
	}

	/**
	 * 判断 map 是否有意义
	 * 
	 * @param map
	 *            输入的
	 * @return true 表示为 map 不是为空，是有内容的，false 表示为空 map
	 */
	public static boolean isNotNull(Map<?, ?> map) {
		return map != null && !map.isEmpty();
	}
	
	/**
	 * 类似 js 的 setInterval 函数，重复做某些事
	 * 
	 * @param task
	 *            类似 new TimerTask() { @Override public void run() {....}}
	 * @param period
	 *            执行周期，如果为 null 则每十分钟执行一次
	 */
	public static void simpleSetInterval(TimerTask task, Integer period) {
		new Timer().schedule(task, 100, period == null ? 10 * 6000 : period);
	} 
}

/**
 * Copyright Sp42 frank@ajaxjs.com Licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.ajaxjs.util.cache;

/**
 * 缓存管理器接口，该接口提供具体的 cache 实现
 * 
 */
public interface CacheManager {
	/**
	 * 根据 cache 的名称获取 cache。如果不存在，默认新建并返回
	 * 
	 * @param name Cache 的名称
	 * @return Cache
	 */
	public <K, V> Cache<K, V> getCache(String name);

	/**
	 * 根据 cache 的名称获取 cache。如果不存在，默认新建并返回
	 * 
	 * 该方法指定了值的类型
	 * 
	 * @param <V>  值类型
	 * @param name Cache 的名称
	 * @param clz  值类型
	 * @return Cache
	 */
	public <V> Cache<String, V> getCache(String name, Class<V> clz);

	/**
	 * 销毁 cache
	 */
	public void destroy();
}

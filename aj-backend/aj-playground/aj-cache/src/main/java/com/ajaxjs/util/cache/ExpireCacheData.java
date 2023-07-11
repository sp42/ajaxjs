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

import java.util.Date;
import java.util.function.Supplier;

/**
 * 
 * 缓存数据。存入数据的时候把存入的实际数据增加一个外包装，顺便加上存入时间，和过期时间
 * 
 * @author sp42 frank@ajaxjs.com
 *
 * @param <T> 缓存类型，可以是任意类型
 */
public class ExpireCacheData<T> {
	/**
	 * 创建缓存数据
	 * 
	 * @param t      缓存的数据，可以是任意类型的对象
	 * @param expire 过期时间，单位是秒
	 */
	ExpireCacheData(T t, int expire) {
		this.data = t;
		this.expire = expire <= 0 ? 0 : expire * 1000L;
		this.saveTime = new Date().getTime() + this.expire;
	}

	/**
	 * 创建缓存数据
	 * 
	 * @param t      缓存的数据，可以是任意类型的对象
	 * @param expire 过期时间，单位是秒
	 * @param load   数据装载器
	 */
	ExpireCacheData(T t, int expire, Supplier<T> load) {
		this(t, expire);
		this.load = load;
	}

	/**
	 * 缓存的数据，可以是任意类型的对象
	 */
	public T data;

	/**
	 * 过期时间 小于等于0标识永久存活
	 */
	public long expire;

	/**
	 * 存活时间
	 */
	public long saveTime;

	/**
	 * 还可以增加一个数据装载器，如果缓存中没有数据或者已经过期，则调用数据装载器加载最新的数据并且加入缓存，并返回。
	 */
	public Supplier<T> load;
}
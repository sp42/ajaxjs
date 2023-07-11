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

import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于虚拟机内存的 cache 管理器
 *
 * @author Frank Cheung
 */
public class MemoryCacheManager extends ConcurrentHashMap<String, Cache<?, ?>> implements CacheManager {
    private static final long serialVersionUID = -8273827743219735439L;

    @Override
    public <K, V> Cache<K, V> getCache(String name) {
        @SuppressWarnings("unchecked")
        Cache<K, V> cache = (Cache<K, V>) get(name);

        if (cache == null) {
            cache = new MemoryCache<>();
            put(name, cache);
        }

        return cache;
    }

    @Override
    public void destroy() {
        while (!isEmpty())
            clear();
    }

    @Override
    public <V> Cache<String, V> getCache(String name, Class<V> clz) {
        @SuppressWarnings("unchecked")
        Cache<String, V> cache = (Cache<String, V>) get(name);

        if (cache == null) {
            cache = new MemoryCache<>();
            put(name, cache);
        }

        return cache;
    }
}

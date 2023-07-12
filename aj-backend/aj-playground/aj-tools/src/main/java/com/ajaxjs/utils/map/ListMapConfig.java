/**
 * Copyright sp42 frank@ajaxjs.com Licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.ajaxjs.utils.map;

import java.util.Map;
import java.util.function.Consumer;

/**
 * 配置对象
 *
 * @author sp42 frank@ajaxjs.com
 */
public class ListMapConfig {
    /**
     * Map 对象的回调函数
     *
     * @author sp42 frank@ajaxjs.com
     */
    @FunctionalInterface
    interface MapHandler {
        /**
         * 执行回调函数
         *
         * @param map      当前 Map
         * @param superMap 父级 Map
         * @param level    深度
         * @return true 表示为跳出遍历
         */
        boolean execute(Map<String, Object> map, Map<String, Object> superMap, int level);
    }

    /**
     * Map 对象身上各个 key/value 的回调函数
     *
     * @author sp42 frank@ajaxjs.com
     */
    @FunctionalInterface
    interface MapEntryHandler {
        /**
         * 执行回调函数
         *
         * @param key      键名称
         * @param obj      键值
         * @param map      当前 Map
         * @param superMap 父级 Map
         * @param level    深度
         * @return true 表示为跳出遍历
         */
        boolean execute(String key, Object obj, Map<String, Object> map, Map<String, Object> superMap, int level);
    }

    /**
     * 上下文
     *
     * @author sp42 frank@ajaxjs.com
     */
    public static class Context {
        private boolean stop;

        public boolean isStop() {
            return stop;
        }

        public void setStop(boolean stop) {
            this.stop = stop;
        }
    }

    public MapHandler mapHandler;

    public MapEntryHandler mapEntryHandler;

    public Consumer<String> newKey;

    public Consumer<String> exitKey;
}

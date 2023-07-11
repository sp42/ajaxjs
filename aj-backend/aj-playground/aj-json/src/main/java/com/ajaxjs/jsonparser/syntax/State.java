/**
 * Copyright Sp42 frank@ajaxjs.com  Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.ajaxjs.jsonparser.syntax;

import java.lang.reflect.Method;

/**
 * 状态
 */
public class State {
    /**
     * 创建一个状态对象
     *
     * @param index       状态 id
     * @param description 状态描述
     */
    public State(int index, String description) {
        this.id = index;
        this.description = description;
    }

    /**
     * 创建一个状态对象
     *
     * @param index       状态 id
     * @param description 状态描述
     * @param handler     状态转换用的处理器
     */
    public State(int index, String description, Method handler) {
        this(index, description);
        this.setHandler(handler);
    }

    /**
     * 索引
     */
    private int id;

    /**
     * 状态转换操作
     */
    private Method handler;

    /**
     * 描述
     */
    private String description;

    /**
     * 下一步期望的描述
     */
    private String expectDescription;

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the expectDescription
     */
    public String getExpectDescription() {
        return expectDescription;
    }

    /**
     * @param expectDescription the expectDescription to set
     */
    public void setExpectDescription(String expectDescription) {
        this.expectDescription = expectDescription;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the handler
     */
    public Method getHandler() {
        return handler;
    }

    /**
     * @param handler the handler to set
     */
    public void setHandler(Method handler) {
        this.handler = handler;
    }
}

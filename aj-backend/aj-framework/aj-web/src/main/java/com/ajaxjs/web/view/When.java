/**
 * Copyright 2015 Sp42 frank@ajaxjs.com
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.web.view;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

/**
 * 选择标签之 if 标签
 *
 * @author sp42 frank@ajaxjs.com
 *
 */
public class When extends SimpleTagSupport {
    /**
     * 保存是否执行的状态
     */
    private boolean test;

    /**
     * 自身为 true 才执行
     *
     * @param test true 表示为执行
     */
    public void setTest(boolean test) {
        this.test = test;
    }

    @Override
    public void doTag() throws JspException, IOException {
        Choose parent = (Choose) getParent(); // 获得父类标签对象

        if (test && !parent.isExecute()) { // 判断父类为 false 那么自身为 true 才输出
            getJspBody().invoke(null);
            parent.setExecute(true); // 把父类设置为true
        }
    }
}

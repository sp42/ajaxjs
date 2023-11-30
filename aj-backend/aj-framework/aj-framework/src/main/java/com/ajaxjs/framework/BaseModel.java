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
package com.ajaxjs.framework;

import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 基础模型类 请注意不要使用 int 而是使用 Integer
 *
 * @author sp42 frank@ajaxjs.com
 */
@Data
public abstract class BaseModel {
    /**
     * 主键
     */
    private Long id;

    /**
     * 唯一 id
     */
    private Long uid;

    /**
     * 数据字典：状态
     */
    private Integer stat;

//	private Status stat;

    private String name;

    private String content;

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 修改日期
     */
    private Date updateDate;

    /**
     * 扩展字段
     */
    public Map<String, Object> extractData;

    public void setExtractData(HashMap<String, Object> extractData /* 若为 Map 不能进行反射，即使强类型也不行 */) {
        this.extractData = extractData;
    }

    /**
     * 获取指定键对应的整数值
     *
     * @param key 指定键
     * @return 指定键对应的整数值，如果不存在则返回0
     */
    public int getExtractInt(String key) {
        Object obj = getExtractData().get(key);
        return obj != null ? (int) obj : 0;
    }

}
/**
 * Copyright 2015 Sp42 frank@ajaxjs.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations
 * under the License.
 */
package com.ajaxjs.web.mvc;

/**
 * @author sp42 frank@ajaxjs.com
 */
public interface MvcConstant {
    /**
     * MV 用的 key
     */
    String PAGE_RESULT = "PageResult";

    String START = "start";

    String LIMIT = "limit";

    String ID = "id";

    String LIST = "list";

    String INFO = "info";

    String ID_INFO = "{id}";

    String CATALOGS = "catalogs";

    String CATALOG_ID = "catalogId";

    /**
     * 搜索关键字
     */
    String KEYWORD = "keyword";

    /**
     * JSON 类型
     */
    String JSON_TYPE = "application/json";

    String USER_SESSION_ID = "X-Ajaxjs-Token";

    String USER_ID = "X-Ajaxjs-UserId";

    String USER_ID_HEADER = "X-Ajaxjs-UserId";

    String DOMAIN_CATALOG_ID = "domainCatalog_Id";

    /**
     * 全局 json 模板路径 之 WEB-INF前缀
     */
    String JSP_PERFIX_WEBINF = "/WEB-INF/jsp";

    /**
     * 操作失败，返回 msg 信息
     */
    String JSON_NOT_OK = "json::{\"isOk\": false, \"msg\" : \"%s\"}";

    /**
     * 操作成功，返回 msg 信息，可扩展字段的
     */
    String JSON_OK_EXTENSION = "json::{\"isOk\": true, \"msg\" : \"%s\", %s}";

    /**
     * 操作成功，返回 msg 信息
     */
    String JSON_OK = "json::{\"isOk\": true, \"msg\" : \"%s\"}";
}

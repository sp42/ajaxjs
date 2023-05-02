package com.ajaxjs.framework.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public interface BaseEntityConstants {
    public static final Map<Integer, String> STATE = new HashMap<Integer, String>() {
        private static final long serialVersionUID = -873485978038563365L;

        {
            put(0, "正常");
            put(1, "禁用");
            put(2, "已删除");
        }
    };

    /**
     * 正常
     */
    int STATUS_OK = 0;

    /**
     * 删除
     */
    int STATUS_DELETED = 1;

    /**
     * 草稿
     */
    int STATUS_DRAFT = -1;


    /**
     * 下架/下线/隐藏
     */
    int STATUS_OFFLINE = 2;

    public static final Date NULL_DATE = new Date(0);
}

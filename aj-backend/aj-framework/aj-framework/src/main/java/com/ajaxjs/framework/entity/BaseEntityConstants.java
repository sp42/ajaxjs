package com.ajaxjs.framework.entity;

import java.util.HashMap;
import java.util.Map;

public interface BaseEntityConstants {
    Map<Integer, String> STATE = new HashMap<Integer, String>() {
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

    interface IdType {
        /**
         * 自增
         */
        int AUTO_INC = 1;

        /**
         *
         */
        int SNOW = 2;

        /**
         * UUID
         */
        int UUID = 3;
    }

    interface CMD_TYPE {
        String SINGLE = "SINGLE";

        String CRUD = "CRUD";
    }
}

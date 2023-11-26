package com.ajaxjs.base.model;

public interface DataServiceConstant {
    /**
     * 数据库厂商
     */
    public static enum DatabaseType {
        MY_SQL, SQL_SERVER, ORACLE, POSTGRE_SQL, DB2, SQLITE, SPARK
    }

    /**
     * 主键生成策略
     */
    interface KeyGen {
        int AUTO_INCRE = 1;
        int SNOWFLAKE = 2;
        int UUID = 3;
    }

    /**
     * 分页模式
     */
    public enum GET_LIST {
        /**
         * Page:No/Size 分页方式
         */
        PAGE_NO(2, "Page:No/Size"),

        /**
         * start/limit 分页方式
         */
        START_LIMIT(1, "start/limit "),

        /**
         * 不分页
         */
        NO_PAGE(0, "不分页");

        /**
         * 常量
         */
        public int value;

        /**
         * 描述
         */
        public String desc;

        /**
         * 分页模式
         *
         * @param value 常量
         * @param desc  描述
         */
        GET_LIST(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }
    }

    /**
     * 表示增删改查的几个状态
     *
     * @author Frank Cheung
     */
    enum CRUD {
        /**
         * 读取单笔记录
         */
        INFO,

        /**
         * 读取列表记录
         */
        LIST,

        /**
         * 分页读取列表记录
         */
        PAGE_LIST,

        /**
         * 创建记录
         */
        CREATE,

        /**
         * 更新
         */
        UPDATE,

        /**
         * 删除
         */
        DELETE;

        /**
         * 是否可读（创建、更新、删除）
         *
         * @return true 表示可读
         */
        public boolean isWrite() {
            return this == CREATE || this == UPDATE || this == DELETE;
        }
    }
}

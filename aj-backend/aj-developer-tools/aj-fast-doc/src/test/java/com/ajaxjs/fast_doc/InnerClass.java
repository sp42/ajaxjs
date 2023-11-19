package com.ajaxjs.fast_doc;

public interface InnerClass {
    class HelloBean {
        /**
         * UUID
         */
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    /**
     * 不错啊
     *
     * @author Frank Cheung sp42@qq.com
     */
    class HelloWorld {
        /**
         * world
         */
        public String world;
    }
}

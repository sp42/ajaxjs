package com.ajaxjs.workflow.model;

import java.util.HashMap;

/**
 * 参数
 *
 * @author Frank Cheung sp42@qq.com
 */
public class Args extends HashMap<String, Object> {
    private static final long serialVersionUID = -3326234439640990986L;

    /**
     * 返回一个 Args 对象。如果传入的 Args 参数为空，则返回一个新的空 Args 对象，以此避免使用 null。
     *
     * @param args 传入的 Args 对象，可能为 null。
     * @return 如果 args 不为 null，则返回原 args 对象；如果为 null，则返回一个新的空 Args 对象。
     */
    public static Args getEmpty(Args args) {
        return args == null ? new Args() : args;        // 判断 args 是否为 null，为 null 时返回新实例，不为 null 时返回原实例
    }
}

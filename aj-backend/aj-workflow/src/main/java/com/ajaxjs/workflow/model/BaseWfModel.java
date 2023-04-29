package com.ajaxjs.workflow.model;

import java.io.Serializable;

import com.ajaxjs.workflow.service.handler.IHandler;
import lombok.Data;

/**
 * 模型元素基类
 */
@Data
public class BaseWfModel implements Serializable, Cloneable {
    private static final long serialVersionUID = 3082741431225739241L;

    /**
     * 元素名称
     */
    private String name;

    /**
     * 显示名称
     */
    private String displayName;

    /**
     * 将执行对象 execution 交给具体的处理器处理 TODO 或者取消？
     *
     * @param handler 具体的处理器
     * @param exec    执行对象
     */
    protected void fire(IHandler handler, Execution exec) {
        handler.handle(exec);
    }
}

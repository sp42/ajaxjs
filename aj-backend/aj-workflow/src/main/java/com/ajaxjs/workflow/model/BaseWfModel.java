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
     * 将执行对象 execution 交给具体的处理器处理。这个方法会调用处理器的 handle 方法，来处理传入的执行对象。
     * 如果处理器能够成功处理执行对象，则会根据执行对象的具体逻辑进行操作；如果无法处理或发生异常，则可能会取消执行对象的处理。
     * TODO 或者取消？
     *
     * @param handler 具体的处理器，实现了 IHandler 接口，负责具体处理执行对象
     * @param exec    执行对象，包含了需要被处理器处理的逻辑或数据
     */
    protected void fire(IHandler handler, Execution exec) {
        handler.handle(exec);
    }

    /**
     * 克隆当前的 BaseWfModel 对象。
     * 本方法通过调用超类的 clone 方法来实现对象的克隆。在克隆前后，需要确保对象的可变状态得到正确的复制，
     * 以避免克隆对象对原始对象内部状态的意外修改。
     *
     * @return BaseWfModel 克隆后的对象，该对象是当前对象的深拷贝。
     * @throws AssertionError 如果当前对象的超类不支持克隆，则抛出此异常。
     */
    @Override
    public BaseWfModel clone() {
        try {
            return (BaseWfModel) super.clone();  // 在此处复制可变状态，以确保克隆对象不会影响原始对象的内部状态
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // 如果超类不支持克隆，则抛出断言错误，因为所有 BaseWfModel 的子类都应该支持克隆
        }
    }
}

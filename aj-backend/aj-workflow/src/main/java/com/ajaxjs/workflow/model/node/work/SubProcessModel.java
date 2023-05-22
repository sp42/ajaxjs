package com.ajaxjs.workflow.model.node.work;

import com.ajaxjs.workflow.model.Execution;
import com.ajaxjs.workflow.model.ProcessModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 子流程定义 subprocess 元素
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SubProcessModel extends WorkModel {
    private static final long serialVersionUID = -3923955459202018147L;

    /**
     * 子流程名称
     */
    private String processName;

    /**
     * 子流程版本号
     */
    private Integer version;

    /**
     * 子流程定义引用
     */
    private ProcessModel subProcess;

    @Override
    protected void exec(Execution exec) {
        runOutTransition(exec);
    }
}

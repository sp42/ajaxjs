package com.ajaxjs.workflow.model.node;

import com.ajaxjs.util.reflect.NewInstance;
import com.ajaxjs.workflow.common.WfException;
import com.ajaxjs.workflow.model.BaseWfModel;
import com.ajaxjs.workflow.model.Execution;
import com.ajaxjs.workflow.model.TransitionModel;
import com.ajaxjs.workflow.service.interceptor.WorkflowInterceptor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 节点元素（存在输入输出的变迁）
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class NodeModel extends BaseWfModel {
    private static final long serialVersionUID = -2377317472320109317L;

    /**
     * 输入变迁集合
     */
    private List<TransitionModel> inputs = new ArrayList<>();

    /**
     * 输出变迁集合
     */
    private List<TransitionModel> outputs = new ArrayList<>();

    /**
     * layout
     */
    private String layout;

    /**
     * 局部前置拦截器
     */
    private String preInterceptors;

    /**
     * 局部后置拦截器
     */
    private String postInterceptors;

    /**
     * 前置局部拦截器实例集合
     */
    private List<WorkflowInterceptor> preInterceptorList = new ArrayList<>();

    /**
     * 后置局部拦截器实例集合
     */
    private List<WorkflowInterceptor> postInterceptorList = new ArrayList<>();

    /**
     * 具体节点模型需要完成的执行逻辑
     *
     * @param exec 执行对象
     */
    protected abstract void exec(Execution exec);

    /**
     * 对执行逻辑增加前置、后置拦截处理
     *
     * @param exec 执行对象
     */

    public void execute(Execution exec) {
        intercept(preInterceptorList, exec);
        exec(exec);
        intercept(postInterceptorList, exec);
    }

    /**
     * 运行变迁继续执行
     *
     * @param exec 执行对象
     */
    protected void runOutTransition(Execution exec) {
        for (TransitionModel tm : getOutputs()) {
            tm.setEnabled(true);
            tm.execute(exec);
        }
    }

    /**
     * 获取下一步的模型
     *
     * @param <T>   下一步模型
     * @param clazz 类
     * @return 下一步模型的列表
     */
    public <T> List<T> getNextModels(Class<T> clazz) {
        List<T> models = new ArrayList<>();

        for (TransitionModel tm : getOutputs())
            addNextModels(models, tm, clazz);

        return models;
    }

    /**
     * 将符合条件的下一个模型添加到模型列表中。
     * 该方法递归地检查给定转换模型的目标节点，如果目标节点是特定类的实例，则将其添加到模型列表中。
     * 如果目标节点不是该类的实例，则递归地检查目标节点的所有输出转换模型。
     *
     * @param models 模型列表，用于收集符合条件的模型。
     * @param tm 当前检查的转换模型。
     * @param clazz 需要匹配的目标类。
     * @param <T> 模型的类型参数。
     */
    @SuppressWarnings("unchecked")
    protected <T> void addNextModels(List<T> models, TransitionModel tm, Class<T> clazz) {
        NodeModel next = tm.getTarget();

        // 检查当前节点是否为目标类的实例，是则添加到模型列表中
        if (clazz.isInstance(next))
            models.add((T) next);
        else {
            // 如果不是目标类的实例，则递归检查该节点的所有输出转换模型
            for (TransitionModel tm2 : next.getOutputs())
                addNextModels(models, tm2, clazz);
        }
    }

    /**
     * 设置 前置拦截器
     *
     * @param preInterceptors 前置拦截器
     */
    public void setPreInterceptors(String preInterceptors) {
        this.preInterceptors = preInterceptors;
        addInterceptors(preInterceptors, instance -> preInterceptorList.add(instance));
    }

    /**
     * 设置后置拦截器
     *
     * @param postInterceptors 后置拦截器
     */
    public void setPostInterceptors(String postInterceptors) {
        this.postInterceptors = postInterceptors;
        addInterceptors(postInterceptors, instance -> postInterceptorList.add(instance));
    }

    /**
     * 输入拦截器字符串，通过反射创建实例加入到节点中
     *
     * @param interceptors 拦截器字符串，由 , 隔开
     * @param fn           拦截器
     */
    private static void addInterceptors(String interceptors, Consumer<WorkflowInterceptor> fn) {
        if (!StringUtils.hasText(interceptors))
            return;

        String[] arr = interceptors.split(",");

        for (String interceptor : arr) {
            WorkflowInterceptor instance = NewInstance.newInstance(WorkflowInterceptor.class, interceptor);

            if (instance != null)
                fn.accept(instance);
        }
    }

    /**
     * 拦截方法
     *
     * @param intercepts 拦截器列表
     * @param exec       执行对象
     */
    private static void intercept(List<WorkflowInterceptor> intercepts, Execution exec) {
        try {
            for (WorkflowInterceptor interceptor : intercepts)
                interceptor.intercept(exec);
        } catch (Exception e) {
            log.warn("拦截器执行失败=", e);
            throw new WfException(e);
        }
    }
}

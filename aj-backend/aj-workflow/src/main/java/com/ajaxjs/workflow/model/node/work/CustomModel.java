package com.ajaxjs.workflow.model.node.work;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.workflow.common.WfConstant;
import com.ajaxjs.workflow.common.WfConstant.TaskType;
import com.ajaxjs.workflow.common.WfException;
import com.ajaxjs.workflow.common.WfUtils;
import com.ajaxjs.workflow.model.Execution;
import com.ajaxjs.workflow.model.po.TaskHistory;
import com.ajaxjs.workflow.service.handler.IHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

/**
 * 自定义模型
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomModel extends WorkModel {
    private static final long serialVersionUID = 8796192915721758769L;

    /**
     * 需要执行的 class 类路径
     */
    private String clazz;

    /**
     * 需要执行的 class 对象的方法名称
     */
    private String methodName;

    /**
     * 执行方法时传递的参数表达式变量名称
     */
    private String args;

    /**
     * 执行的返回值变量
     */
    private String var;

    /**
     * 加载模型时初始化的对象实例
     */
    private Object invokeObject;

    @Override
    public void exec(Execution exec) {
        if (invokeObject == null)
            invokeObject = ReflectUtil.newInstance(clazz);

        if (invokeObject == null)
            throw new WfException("自定义模型[class=" + clazz + "]实例化对象失败");

        if (invokeObject instanceof IHandler) {
            IHandler handler = (IHandler) invokeObject;
            handler.handle(exec);
        } else {
            Method method = WfUtils.findMethod(invokeObject.getClass(), methodName);

            if (method == null)
                throw new WfException("自定义模型[class=" + clazz + "]无法找到方法名称:" + methodName);

            Object[] objects = getArgs(exec.getArgs(), args);
            Object returnValue = ReflectUtil.executeMethod(invokeObject, method, objects);

            if (StringUtils.hasText(var))
                exec.getArgs().put(var, returnValue);
        }

        // 记录任务历史
        TaskHistory task = new TaskHistory();
        task.setOrderId(exec.getOrder().getId());
        task.setName(getName());
        task.setFinishDate(new Date());
        task.setDisplayName(getDisplayName());
        task.setStat(WfConstant.STATE_FINISH);
        task.setTaskType(TaskType.RECORD);
        task.setParentId(exec.getTask() == null ? 0 : exec.getTask().getId());
        task.setVariable(JsonHelper.toJson(exec.getArgs()));
        CRUD.create(task);

        runOutTransition(exec);
    }

    /**
     * 根据传递的执行参数、模型的参数列表返回实际的参数对象数组
     *
     * @param execArgs 运行时传递的参数数据
     * @param args     自定义节点需要的参数
     * @return 调用自定义节点类方法的参数数组
     */
    private Object[] getArgs(Map<String, Object> execArgs, String args) {
        Object[] objects = null;

        if (StringUtils.hasText(args)) {
            String[] argArray = args.split(",");
            objects = new Object[argArray.length];

            for (int i = 0; i < argArray.length; i++)
                objects[i] = execArgs.get(argArray[i]);
        }

        return objects;
    }
}

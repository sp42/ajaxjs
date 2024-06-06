package com.ajaxjs.workflow.service.interceptor;

import com.ajaxjs.workflow.WorkflowEngine;
import com.ajaxjs.workflow.model.Execution;
import com.ajaxjs.workflow.model.po.Task;
import com.ajaxjs.workflow.service.TaskService;

/**
 * 新创建的任务通过 SurrogateInterceptor 创建委托。 查询 wf_surrogate 表获取委托代理人，并通过
 * addTaskActor() 设置为参与者(即授权人、代理人都可处理任务)；
 *
 * @author sp42 frank@ajaxjs.com
 */
public class SurrogateInterceptor implements WorkflowInterceptor {
    /**
     * 拦截执行过程中的任务，为每个任务分配代理人。
     *
     * @param exec 表示一个工作流执行实例，包含执行过程中的所有任务和相关引擎。
     */
    @Override
    public void intercept(Execution exec) {
        WorkflowEngine engine = exec.getEngine(); // 获取工作流引擎

        // 遍历执行中的所有任务
        for (Task task : exec.getTasks()) {
            if (task.getActorIds() == null)
                continue; // 如果任务没有指定执行人，则跳过该任务

            // 遍历任务的所有指定执行人
            for (Long actor : task.getActorIds()) {
                if (actor == null || actor == 0)
                    continue; // 如果执行人为空或为0，则跳过该执行人

                // 查询代理服务，获取该执行人的代理人
                Long agent = engine.surrogateService.getSurrogate(actor, exec.getProcess().getName());

                // 如果存在代理人，则为任务添加代理执行人
                if (agent != null && agent != 0)
                    TaskService.addTaskActor(task.getId(), agent);
            }
        }
    }

}

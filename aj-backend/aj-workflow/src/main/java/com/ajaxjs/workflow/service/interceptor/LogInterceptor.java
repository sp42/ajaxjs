
package com.ajaxjs.workflow.service.interceptor;

import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.workflow.model.Execution;
import com.ajaxjs.workflow.model.po.Task;
import lombok.extern.slf4j.Slf4j;


/**
 * 日志拦截器
 *
 * @author sp42 frank@ajaxjs.com
 */
@Slf4j
public class LogInterceptor implements WorkflowInterceptor {
    /**
     * 拦截产生的任务对象，打印日志
     */
    @Override
    public void intercept(Execution exec) {
        for (Task task : exec.getTasks()) {
            StringBuilder buffer = new StringBuilder(100);
            buffer.append("创建任务[标识=").append(task.getId());
            buffer.append(",名称=").append(task.getDisplayName());
            buffer.append(",创建时间=").append(task.getCreateDate());
            buffer.append(",参与者={");

            if (task.getActorIds() != null) {
                for (Long actor : task.getActorIds())
                    buffer.append(actor).append(";");
            }

            buffer.append("}]");
            log.info(buffer.toString());
        }
    }
}

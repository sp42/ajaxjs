package com.ajaxjs.workflow.model.node;

import java.util.List;

import com.ajaxjs.workflow.model.Execution;
import com.ajaxjs.workflow.model.TransitionModel;
import com.ajaxjs.workflow.model.node.work.WorkModel;
import com.ajaxjs.workflow.service.handler.AbstractMergeHandler;

/**
 * 合并定义 join 元素
 */
public class JoinModel extends NodeModel {
    private static final long serialVersionUID = 5296621319088076775L;

    /**
     * 执行合并操作的逻辑。
     * 该方法会首先尝试进行分支的合并处理，然后根据合并结果决定是否触发进一步的变迁。
     *
     * @param execution 执行上下文，包含当前执行的状态和信息。
     */
    @Override
    public void exec(Execution execution) {
        // 使用当前实例作为 JoinModel 模型
        JoinModel model = this;

        fire(new AbstractMergeHandler() {// 触发合并处理，使用一个匿名内部类实现 AbstractMergeHandler 接口
            // 查找当前活动的节点
            @Override
            protected String[] findActiveNodes() {
                StringBuilder sb = new StringBuilder(20);
                findForkTaskNames(model, sb);   // 找出所有的 fork 任务名称，并以逗号分隔

                return sb.toString().split(",");   // 将 StringBuilder 中的任务名称转换为字符串数组，每个任务名称之间以逗号分隔
            }
        }, execution);

        // 如果执行上下文中标识合并已经完成，则执行输出转换
        if (execution.isMerged())
            runOutTransition(execution);// 如果已经合并成功，则进行下一步变迁
    }

    /**
     * 对 join 节点的所有输入变迁进行递归，查找 join 至 fork 节点的所有中间 task 元素。这是一个递归的函数。
     * 该函数会遍历流程图中的节点，从给定的节点开始，递归查找并记录所有从该节点到 fork 节点之间的任务名称。
     *
     * @param node 表示当前正在处理的节点模型。函数会递归处理该节点的所有输入变迁。
     * @param sb   用于记录所有从当前节点到 fork 节点之间的任务名称的字符串构建器。任务名称以逗号分隔。
     */
    private static void findForkTaskNames(NodeModel node, StringBuilder sb) {
        if (node instanceof ForkModel)    // 如果当前节点是 fork节点，则终止递归
            return;

        // 获取当前节点的所有输入变迁
        List<TransitionModel> inputs = node.getInputs();

        // 遍历所有输入变迁，记录并递归查找来源为 WorkModel 的节点
        for (TransitionModel tm : inputs) {
            // 如果变迁的来源是 WorkModel，则记录任务名称
            if (tm.getSource() instanceof WorkModel)
                sb.append(tm.getSource().getName()).append(",");

            findForkTaskNames(tm.getSource(), sb);// 递归查找来源节点的任务名称
        }
    }

}

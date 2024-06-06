package com.ajaxjs.workflow.model.node;

import com.ajaxjs.util.reflect.NewInstance;
import com.ajaxjs.workflow.common.WfException;
import com.ajaxjs.workflow.model.Execution;
import com.ajaxjs.workflow.model.TransitionModel;
import com.ajaxjs.workflow.service.handler.DecisionHandler;
import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.el.ExpressionFactory;
import java.util.Map;
import java.util.function.Function;

/**
 * 决策定义 decision 元素
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
public class DecisionModel extends NodeModel {
    private static final long serialVersionUID = -806621814645169999L;

    /**
     * 决策选择表达式串（需要表达式引擎解析）
     */
    private String expr;

    /**
     * 决策处理类，对于复杂的分支条件，可通过 handleClass 来处理
     */
    private String handleClass;

    /**
     * 决策处理类，对于复杂的分支条件，可通过 handleLambda 来处理。 实现 lambda 需要根据执行对象做处理，并返回后置流转的 name
     * Function &lt;execution 执行对象, String 后置流转的 name&gt;
     */
    private Function<Execution, String> handleLambda;

    /**
     * 决策处理类实例
     */
    private DecisionHandler decide;

    /**
     * 表达式引擎
     */
    private ExpressionFactory factory = new ExpressionFactoryImpl();

    /**
     * 调用表达式解析器，运算表达式，返回结果
     *
     * @param <T>  返回类型泛型，指定解析表达式后的结果类型。
     * @param T    返回类型，确保与传入的表达式相关联的类型。
     * @param expr 表达式字符串，需要解析和计算的表达式。
     * @param args 参数列表，一个包含表达式中所需变量名称和其对应值的映射。
     * @return 返回计算后的结果，其类型由泛型 T 指定。
     */
    @SuppressWarnings("unchecked")
    private <T> T eval(Class<T> T, String expr, Map<String, Object> args) {
        SimpleContext cxt = new SimpleContext(); // 初始化表达式解析上下文

        // 给上下文设置变量和对应的值
        for (String key : args.keySet())
            cxt.setVariable(key, factory.createValueExpression(args.get(key), Object.class));

        return (T) factory.createValueExpression(cxt, expr, T).getValue(cxt);
    }

    /**
     * 根据给定的执行对象执行相应的逻辑。
     * 本方法首先尝试通过表达式或决定函数决定下一个步骤，然后尝试执行该步骤。
     * 如果无法决定下一个步骤，则记录警告信息。
     *
     * @param exec 执行对象，包含执行的订单信息和参数。
     */
    @Override
    public void exec(Execution exec) {
        // 记录任务运行开始的信息，包括任务ID和参数
        log.info("任务[{}]运行抉择表达式的参数是[{}]", exec.getOrder().getId(), exec.getArgs());
        String next;

        // 判断并决定下一个步骤的名称
        if (StringUtils.hasText(expr))
            next = eval(String.class, expr, exec.getArgs());
        else if (decide != null)
            next = decide.decide(exec);
        else {
            log.warn("任务[{}]不能获取下一步的步骤！", exec.getOrder().getId()); // 如果无法决定下一个步骤，记录警告信息并返回
            return;
        }

        // 记录决定表达式的执行结果
        log.info("任务[{}]运行抉择表达式[{}]的结果是[{}]", exec.getOrder().getId(), expr, next);
        boolean isFound = false;

        // 遍历输出转换，寻找与决定结果匹配的步骤并执行
        for (TransitionModel tm : getOutputs()) {
            if (!StringUtils.hasText(next)) {  // 如果没有指定名称，尝试根据条件表达式决定是否执行
                String expr = tm.getExpr();

                if (StringUtils.hasText(expr) && eval(Boolean.class, expr, exec.getArgs())) {
                    tm.setEnabled(true);
                    tm.execute(exec);
                    isFound = true;
                }
            } else if (tm.getName().equals(next)) { // 如果指定了名称，直接执行匹配的步骤
                tm.setEnabled(true);
                tm.execute(exec);
                isFound = true;
            }
        }

        if (!isFound)     // 如果没有找到可执行的步骤，抛出异常
            throw new WfException(exec.getOrder().getId() + "->decision 节点无法确定下一步执行路线");
    }

    /**
     * 设置 handleClass 属性
     *
     * @param handleClass handleClass 属性值
     */
    public void setHandleClass(String handleClass) {
        this.handleClass = handleClass;

        if (StringUtils.hasText(handleClass))
            decide = (DecisionHandler) NewInstance.newInstance(handleClass);
    }
}

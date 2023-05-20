package com.ajaxjs.workflow.model.node;

import java.util.Map;
import java.util.function.Function;

import javax.el.ExpressionFactory;

//import org.apache.el.ExpressionFactoryImpl;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.StringUtils;

import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.workflow.common.WfException;
import com.ajaxjs.workflow.model.Execution;
import com.ajaxjs.workflow.model.TransitionModel;
import com.ajaxjs.workflow.service.handler.DecisionHandler;

import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;

/**
 * 决策定义 decision 元素
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class DecisionModel extends NodeModel {
    public static final LogHelper LOGGER = LogHelper.getLog(DecisionModel.class);

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
     * Function<execution 执行对象,String 后置流转的 name>
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
     * @param <T>  返回类型
     * @param T    返回类型
     * @param expr 表达式串
     * @param args 参数列表
     * @return 返回对象
     */
    @SuppressWarnings("unchecked")
    private <T> T eval(Class<T> T, String expr, Map<String, Object> args) {
        SimpleContext cxt = new SimpleContext();

        for (String key : args.keySet())
            cxt.setVariable(key, factory.createValueExpression(args.get(key), Object.class));

        return (T) factory.createValueExpression(cxt, expr, T).getValue(cxt);
    }

    @Override
    public void exec(Execution exec) {
        LOGGER.info("任务[{0}]运行抉择表达式的参数是[{1}]", exec.getOrder().getId(), exec.getArgs());
        String next;

        if (StringUtils.hasText(expr))
            next = eval(String.class, expr, exec.getArgs());
        else if (decide != null)
            next = decide.decide(exec);
        else {
            LOGGER.warning("任务[{0}]不能获取下一步的步骤！", exec.getOrder().getId());
            return;
        }

        LOGGER.info("任务[{0}]运行抉择表达式[{1}]的结果是[{2}]", exec.getOrder().getId(), expr, next);
        boolean isFound = false;

        for (TransitionModel tm : getOutputs()) {
            if (!StringUtils.hasText(next)) {
                String expr = tm.getExpr();

                if (StringUtils.hasText(expr) && eval(Boolean.class, expr, exec.getArgs())) {
                    tm.setEnabled(true);
                    tm.execute(exec);
                    isFound = true;
                }
            } else if (tm.getName().equals(next)) {
                tm.setEnabled(true);
                tm.execute(exec);
                isFound = true;
            }
        }

        if (!isFound)
            throw new WfException(exec.getOrder().getId() + "->decision 节点无法确定下一步执行路线");
    }

    public void setHandleClass(String handleClass) {
        this.handleClass = handleClass;

        if (StringUtils.hasText(handleClass))
            decide = (DecisionHandler) ReflectUtil.newInstance(handleClass);
    }

}

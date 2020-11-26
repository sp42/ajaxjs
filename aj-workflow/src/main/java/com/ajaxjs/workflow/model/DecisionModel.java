package com.ajaxjs.workflow.model;

import java.util.Map;
import java.util.function.Function;

import javax.el.ExpressionFactory;

import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.workflow.Execution;
import com.ajaxjs.workflow.ExecutionHandler;
import com.ajaxjs.workflow.WorkflowException;

import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;

/**
 * 决策定义 decision 元素
 * 
 */
public class DecisionModel extends NodeModel {
	private static final LogHelper LOGGER = LogHelper.getLog(DecisionModel.class);

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
	 * 决策处理类，对于复杂的分支条件，可通过 handle lambda 来处理。 实现 lambda 需要根据执行对象做处理，并返回后置流转的 name
	 * 
	 * @param execution 执行对象
	 * @return String 后置流转的name
	 */
	private Function<Execution, String> handleLambda;

	/**
	 * 决策处理类实例
	 */
	private ExecutionHandler decide;

	/**
	 * 
	 */
	private ExpressionFactory factory = new ExpressionFactoryImpl();

	/**
	 * 表达式解析器
	 * 
	 * @param <T>
	 * @param T    返回类型
	 * @param expr 表达式串
	 * @param args 参数集合
	 * @return 返回对象
	 */
	@SuppressWarnings("unchecked")
	private <T> T eval(Class<T> T, String expr, Map<String, Object> args) {
		SimpleContext context = new SimpleContext();

		for (String key : args.keySet())
			context.setVariable(key, factory.createValueExpression(args.get(key), Object.class));

		return (T) factory.createValueExpression(context, expr, T).getValue(context);
	}

	@Override
	public void exec(Execution execution) {
		Map<String, Object> args = execution.getArgs();
		LOGGER.info("任务[{0}]运行抉择表达式的参数是[{1}]", execution.getActive().getId(), args);

		String next = null;
		if (!CommonUtil.isEmptyString(expr))
			next = eval(String.class, expr, args);
		else if (decide != null)
			next = decide.exec(execution).toString();

		LOGGER.info("任务[{0}]运行抉择表达式[{1}]的结果是[{2}]", execution.getActive().getId(), expr, next);
		boolean isfound = false;

		for (TransitionModel tm : getOutputs()) {
			if (CommonUtil.isEmptyString(next)) {
				String expr = tm.getExpr();

				if (!CommonUtil.isEmptyString(expr) && eval(Boolean.class, expr, args)) {
					tm.setEnabled(true);
					tm.execute(execution);
					isfound = true;
				}
			} else if (tm.getName().equals(next)) {
				tm.setEnabled(true);
				tm.execute(execution);
				isfound = true;
			}
		}

		if (!isfound)
			throw new WorkflowException(execution.getActive().getId() + "->decision 节点无法确定下一步执行路线");
	}

	public String getExpr() {
		return expr;
	}

	public void setExpr(String expr) {
		this.expr = expr;
	}

	public String getHandleClass() {
		return handleClass;
	}

	public void setHandleClass(String handleClass) {
		this.handleClass = handleClass;
		if (!CommonUtil.isEmptyString(handleClass))
			decide = (ExecutionHandler) ReflectUtil.newInstance(handleClass);
	}

	public Function<Execution, String> getHandleLambda() {
		return handleLambda;
	}

	public void setHandleLambda(Function<Execution, String> handleLambda) {
		this.handleLambda = handleLambda;
	}
}

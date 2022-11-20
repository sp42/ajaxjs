/*
 * Copyright 2013-2015 www.snakerflow.com. Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.ajaxjs.workflow.model.node;

import java.util.Map;
import java.util.function.Function;

//import org.apache.el.ExpressionFactoryImpl;
import org.springframework.util.StringUtils;

import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.workflow.common.WfException;
import com.ajaxjs.workflow.model.Execution;
import com.ajaxjs.workflow.model.TransitionModel;
import com.ajaxjs.workflow.service.handler.DecisionHandler;

import de.odysseus.el.util.SimpleContext;

/**
 * 决策定义 decision 元素
 * 
 */
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
	 * 决策处理类，对于复杂的分支条件，可通过 handle lambda 来处理。 实现 lambda 需要根据执行对象做处理，并返回后置流转的 name
	 * 
	 * @param execution 执行对象
	 * @return String 后置流转的name
	 */
	private Function<Execution, String> handleLambda;

	/**
	 * 决策处理类实例
	 */
	private DecisionHandler decide;

//	private ExpressionFactory factory = new ExpressionFactoryImpl();

	/**
	 * 表达式解析器
	 * 
	 * @param <T>
	 * @param T    返回类型
	 * @param expr 表达式串
	 * @param args 参数列表
	 * @return 返回对象
	 */
	private <T> T eval(Class<T> T, String expr, Map<String, Object> args) {
		SimpleContext context = new SimpleContext();
		System.out.println(context);

		return null;

//		for (String key : args.keySet())
//			context.setVariable(key, factory.createValueExpression(args.get(key), Object.class));
//
//		return (T) factory.createValueExpression(context, expr, T).getValue(context);
	}

	@Override
	public void exec(Execution execution) {
		LOGGER.info("任务[{0}]运行抉择表达式的参数是[{1}]", execution.getOrder().getId(), execution.getArgs());

		String next = null;

		if (StringUtils.hasText(expr))
			next = eval(String.class, expr, execution.getArgs());
		else if (decide != null)
			next = decide.decide(execution);

		LOGGER.info("任务[{0}]运行抉择表达式[{1}]的结果是[{2}]", execution.getOrder().getId(), expr, next);
		boolean isfound = false;

		for (TransitionModel tm : getOutputs()) {
			if (!StringUtils.hasText(next)) {
				String expr = tm.getExpr();

				if (StringUtils.hasText(expr) && eval(Boolean.class, expr, execution.getArgs())) {
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
			throw new WfException(execution.getOrder().getId() + "->decision 节点无法确定下一步执行路线");
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

		if (StringUtils.hasText(handleClass))
			decide = (DecisionHandler) ReflectUtil.newInstance(handleClass);
	}

	public Function<Execution, String> getHandleLambda() {
		return handleLambda;
	}

	public void setHandleLambda(Function<Execution, String> handleLambda) {
		this.handleLambda = handleLambda;
	}
}

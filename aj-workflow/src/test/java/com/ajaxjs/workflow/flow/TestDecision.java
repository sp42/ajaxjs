package com.ajaxjs.workflow.flow;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.junit.Test;

import com.ajaxjs.util.ioc.ComponentMgr;
import com.ajaxjs.workflow.BaseTest;
import com.ajaxjs.workflow.Execution;
import com.ajaxjs.workflow.ExecutionHandler;
import com.ajaxjs.workflow.WorkflowEngine;
import com.ajaxjs.workflow.process.ProcessActive;
import com.ajaxjs.workflow.task.Task;

/**
 * 决策分支流程
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class TestDecision extends BaseTest {
	/**
	 * 决策节点 decision 使用 expr 属性决定后置路线
	 */
//	@Test
	public void TestDecision1() {
//		deploy("test/flow/decision/expression.xml");

		Map<String, Object> args = new HashMap<>();
//		args.put("task1.operator", new String[]{"1","2"});
		args.put("task2.operator", new String[] { "1" });
		// args.put("task3.operator", new String[]{"1","2"});
		args.put("content", "toTask2");

		WorkflowEngine engine = ComponentMgr.get(WorkflowEngine.class);
		ProcessActive active = engine.startInstanceByName("decision1", 0, WorkflowEngine.ADMIN, args);

		for (Task task : engine.task().findByActiveId(active.getId())) {
			assertEquals("task2", task.getName());
		}
	}

	/**
	 * 使用 transition 的 expr 属性决定后置路线
	 */
//	@Test
	public void TestDecision2() {
		deploy("test/flow/decision/condition.xml");

		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", new String[] { "1" });
		args.put("task2.operator", new String[] { "1" });
		args.put("task3.operator", new String[] { "1" });
		args.put("content", 200);

		WorkflowEngine engine = ComponentMgr.get(WorkflowEngine.class);
		ProcessActive active = engine.startInstanceByName("decision2", 0, WorkflowEngine.ADMIN, args);

		for (Task task : engine.task().findByActiveId(active.getId())) {
			assertEquals("task2", task.getName());
		}
	}

	public static class DecisionHandler implements ExecutionHandler {
		@Override
		public Object exec(Execution execution) {
			return execution.getArgs().get("content");
		}
	}

	@Test
	public void TestDecision3() {
		deploy("test/flow/decision/handler.xml");

		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", new String[] { "1" });
		args.put("task2.operator", new String[] { "1" });
		args.put("task3.operator", new String[] { "1" });
		args.put("content", "toTask3");

		WorkflowEngine engine = ComponentMgr.get(WorkflowEngine.class);
		ProcessActive active = engine.startInstanceByName("decision3", 0, WorkflowEngine.ADMIN, args);
		for (Task task : engine.task().findByActiveId(active.getId())) {
			assertEquals("task3", task.getName());
		}
	}

	public static Function<Execution, String> decisionHandler = execution -> (String) execution.getArgs().get("content");

	public static void main(String[] args) {
//		String str = "com.ajaxjs.workflow.TestDecision.decisionHandler";
//		String[] arr = str.split("\\.");
//		String member = arr[arr.length - 1];
//		arr[arr.length - 1] = "";
//		String clz = String.join(".", arr);
//		clz = clz.substring(0, clz.length() - 1);
//
//		try {
//			Class<?> clazz = Class.forName(clz);
//			Field field = clazz.getField(member);
//			System.out.println(field.get(clazz));
//		} catch (ClassNotFoundException | NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
//			e.printStackTrace();
//		}
	}

}

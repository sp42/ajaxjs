package com.ajaxjs.workflow.flow;

import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;
import java.util.function.Function;

import org.junit.Test;

import com.ajaxjs.workflow.BaseTest;
import com.ajaxjs.workflow.model.Args;
import com.ajaxjs.workflow.model.Execution;
import com.ajaxjs.workflow.model.po.Order;

public class TestDecision extends BaseTest {
    // 测试决策分支流程1：决策节点 decision 使用 expr 属性决定后置路线
    @Test
    public void TestDecision1() {
//		WorlflowEngine engine = (WorlflowEngine) init("test/decision/expression.xml");

        Args args = new Args();
        args.put("task2.operator", new String[]{"1"});
        // args.put("task1.operator", new String[]{"1","2"});
        // args.put("task3.operator", new String[]{"1","2"});
        args.put("content", "toTask2");
        Order order = engine.startInstanceByName("decision1", 0, 2L, args);
        System.out.println(order);

        assertNotNull(order);
    }

    // 测试决策分支流程2：使用transition的expr属性决定后置路线
    @Test
    public void TestDecision2() {
//		WorlflowEngine engine = (WorlflowEngine) init("test/decision/condition.xml");

        Args args = new Args();
        args.put("task1.operator", new String[]{"1"});
        args.put("task2.operator", new String[]{"1"});
        args.put("task3.operator", new String[]{"1"});
        args.put("content", 200);

        Order order = engine.startInstanceByName("decision2", 0, 2L, args);
        assertNotNull(order);
        System.out.println(order);
    }

    public static Function<Execution, String> decisionHandler = execution -> (String) execution.getArgs().get("content");

    public static void main(String[] args) {
        String str = "com.ajaxjs.workflow.TestDecision.decisionHandler";
        String[] arr = str.split("\\.");
        String member = arr[arr.length - 1];
        arr[arr.length - 1] = "";
        String clz = String.join(".", arr);
        clz = clz.substring(0, clz.length() - 1);

        try {
            Class<?> clazz = Class.forName(clz);
            Field field = clazz.getField(member);
            System.out.println(field.get(clazz));
        } catch (ClassNotFoundException | NoSuchFieldException | SecurityException | IllegalArgumentException |
                 IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    // 测试决策分支流程2：使用transition的expr属性决定后置路线
    @Test
    public void TestDecision3() {
//		WorlflowEngine engine = (WorlflowEngine) init("test/decision/handler.xml");

        Args args = new Args();
        args.put("task1.operator", new String[]{"1"});
        args.put("task2.operator", new String[]{"1"});
        args.put("task3.operator", new String[]{"1"});
        args.put("content", "toTask3");
        Order order = engine.startInstanceByName("decision3", 0, 2L, args);
        System.out.println(order);
        assertNotNull(order);
    }
}

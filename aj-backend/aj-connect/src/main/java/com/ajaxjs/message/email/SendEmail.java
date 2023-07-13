//package com.ajaxjs.message.email;
//
//import com.ajaxjs.net.mail.Mail;
//import com.ajaxjs.net.mail.Sender;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.expression.MapAccessor;
//import org.springframework.expression.EvaluationContext;
//import org.springframework.expression.Expression;
//import org.springframework.expression.ExpressionParser;
//import org.springframework.expression.common.TemplateParserContext;
//import org.springframework.expression.spel.standard.SpelExpression;
//import org.springframework.expression.spel.standard.SpelExpressionParser;
//import org.springframework.expression.spel.support.StandardEvaluationContext;
//import org.springframework.stereotype.Component;
//
//import java.util.Collections;
//import java.util.Map;
//
//@Component
//public class SendEmail {
//    @Value("${email.server}")
//    private String emailServer;
//
//    @Value("${email.account}")
//    private String account;
//
//    @Value("${email.password}")
//    private String password;
//
//    /**
//     * 发送邮件
//     *
//     * @param to
//     * @param subject
//     * @param content
//     * @return
//     */
//    public boolean send(String to, String subject, String content) {
//        Mail mail = new Mail();
//        mail.setMailServer(emailServer);
//        mail.setAccount(account);
//        mail.setPassword(password);
//        mail.setFrom(account);
//        mail.setTo(to);
//        mail.setSubject(subject);
//        mail.setHTML_body(true);
//        mail.setContent(content);
//
//        return Sender.send(mail);
//    }
//
//    /**
//     * 计算表达式
//     *
//     * @param express：el 表达式
//     * @param map：el     表达式动态参数
//     * @return 表达式结果
//     */
//    public static boolean parse(String express, Map<String, Object> map) {
//        // 设置动态参数
//        StandardEvaluationContext cxt = new StandardEvaluationContext();
//        cxt.setVariables(map);
//        cxt.setPropertyAccessors(Collections.singletonList(new MapAccessor()));
//
//        // 创建一个 EL 解析器
//        ExpressionParser parser = new SpelExpressionParser();
//        SpelExpression expr = (SpelExpression) parser.parseExpression(express, new TemplateParserContext("${", "}"));
//        expr.setEvaluationContext(cxt);
//
//        return Boolean.TRUE.equals(expr.getValue(map, Boolean.class));
////		Map<String, Object> map = new HashMap<>(16);
////		map.put("exp", 4);
////
////		String result = parse("jjjj${exp>2}jkj", map);
////		System.out.println("result:" + result);
//    }
//
//    /**
//     * 编译模板
//     *
//     * @param tpl    模板
//     * @param values 值
//     * @return
//     */
//    public static String simpleTemplate(String tpl, Map<String, String> values) {
//        // 通过 evaluationContext.setVariable 可以在上下文中设定变量。
//        EvaluationContext context = new StandardEvaluationContext();
//
//        for (String key : values.keySet())
//            context.setVariable(key, values.get(key));
//
//        // 解析表达式，如果表达式是一个模板表达式，需要为解析传入模板解析器上下文。
//        Expression expression = new SpelExpressionParser().parseExpression(tpl, new TemplateParserContext());
//
//        // 使用Expression.getValue() 获取表达式的值，这里传入了 Evaluation 上下文，第二个参数是类型参数，表示返回值的类型。
//        return expression.getValue(context, String.class);
//    }
//
//    @Value("${email.template_1}") // 不能读取表达式
//    private String template;
//
//    /**
//     * 邮件模板
//     */
//    // @formatter:off
//    private final static String HTML = "用户 #{#username} 您好：<br />&nbsp;&nbsp;&nbsp;&nbsp;请点击下面的链接进行#{#desc}：<br />"
//            + "&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"#{#link}\" target=\"_blank\">#{#link}</a>。"
//            + "<br /> &nbsp;&nbsp;&nbsp;&nbsp;提示：1）请勿回复本邮件；2）本邮件超过 #{#timeout} 小时的话链接将会失效，需要重新申请#{#desc}；3）如不能打开，请复制该链接到浏览器。";
//    // @formatter:on
//
//    public String getEmailContent(Map<String, String> values) {
//        return simpleTemplate(HTML, values);
//    }
//}

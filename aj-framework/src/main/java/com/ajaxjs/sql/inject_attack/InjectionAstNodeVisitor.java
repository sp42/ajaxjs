package com.ajaxjs.sql.inject_attack;

import net.sf.jsqlparser.parser.CCJSqlParserDefaultVisitor;
import net.sf.jsqlparser.parser.SimpleNode;
import net.sf.jsqlparser.statement.select.UnionOp;

/**
 * 基于抽象语法树(AST)的注入攻击分析实现 禁用用 UNIO N语句
 * 
 * @author guyadong
 *
 */
public class InjectionAstNodeVisitor extends CCJSqlParserDefaultVisitor {
	public InjectionAstNodeVisitor() {
	}

	@Override
	public Object visit(SimpleNode node, Object data) {
		Object value = node.jjtGetValue();
		if (value instanceof UnionOp)
			throw new InjectionAttackException("DISABLE UNION");

		return super.visit(node, data);
	}
}
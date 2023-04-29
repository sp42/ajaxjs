package com.ajaxjs.sql.inject_attack;

import com.ajaxjs.sql.ParserSupport.SqlParserInfo;

//import gu.sql2java.parser.ParserSupport.SqlParserInfo;

/**
 * SQL注入攻击分析器
 * 
 * @author guyadong
 *
 */
public class SqlInjectionAnalyzer {
	private boolean injectCheckEnable = true;
	private final InjectionSyntaxObjectAnalyzer injectionChecker;
	private final InjectionAstNodeVisitor injectionVisitor;

	public SqlInjectionAnalyzer() {
		this.injectionChecker = new InjectionSyntaxObjectAnalyzer();
		this.injectionVisitor = new InjectionAstNodeVisitor();
	}

	/**
	 * 启用/关闭注入攻击检查,默认启动
	 * 
	 * @param enable
	 * @return
	 */
	public SqlInjectionAnalyzer injectCheckEnable(boolean enable) {
		injectCheckEnable = enable;
		return this;
	}

	/**
	 * 对解析后的SQL对象执行注入攻击分析，有注入攻击的危险则抛出异常{@link InjectionAttackException}
	 * 
	 * @param sqlParserInfo
	 * @throws InjectionAttackException
	 */
	public SqlParserInfo injectAnalyse(SqlParserInfo sqlParserInfo) throws InjectionAttackException {
		if (null != sqlParserInfo && injectCheckEnable) {
			/** SQL注入攻击检查 */
			sqlParserInfo.statement.accept(injectionChecker);
			sqlParserInfo.simpleNode.jjtAccept(injectionVisitor, null);
		}

		return sqlParserInfo;
	}
}

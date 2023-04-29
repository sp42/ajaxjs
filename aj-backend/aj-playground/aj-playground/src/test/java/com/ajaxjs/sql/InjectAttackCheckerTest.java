package com.ajaxjs.sql;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.sql.ParserSupport.SqlParserInfo;
import com.ajaxjs.sql.inject_attack.InjectionAttackException;
import com.ajaxjs.sql.inject_attack.SqlInjectionAnalyzer;

import net.sf.jsqlparser.JSQLParserException;

/**
 * SQL注入攻击检查测试
 * 
 * 实现基于SQL语法分析的SQL注入攻击检查 https://blog.csdn.net/10km/article/details/127767358
 * 
 * sql2java:一个古老但稳定的轻量级的ORM工具的使用说明 https://blog.csdn.net/10km/article/details/74906545
 * sql2java:一次外科手术式的bug修复过程
 * 
 * @author guyadong
 *
 */
public class InjectAttackCheckerTest {

	private static SqlInjectionAnalyzer analyser;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		analyser = new SqlInjectionAnalyzer();
	}

	private boolean injectAnalyse(String sql) throws JSQLParserException {
		SqlParserInfo sqlParserInfo = ParserSupport.parse0(sql, null, null);

		try {
			analyser.injectAnalyse(sqlParserInfo);
			return true;
		} catch (InjectionAttackException e) {
			e.printStackTrace();

			// log(e);
			return false;
		}
	}

	@Test
	public void test() throws JSQLParserException {
		assertFalse(injectAnalyse("select * from dc_device where id in (select id from other)"));
		assertFalse(injectAnalyse("select * from dc_device where 2=2.0 or 2 != 4"));
		assertFalse(injectAnalyse("select * from dc_device where 1!=2.0"));
		assertFalse(injectAnalyse("select * from dc_device where id=floor(2.0)"));
		assertFalse(injectAnalyse("select * from dc_device where not true"));
		assertFalse(injectAnalyse("select * from dc_device where 1 or id > 0"));
		assertFalse(injectAnalyse("select * from dc_device where 'tom' or id > 0"));
		assertFalse(injectAnalyse("select * from dc_device where '-2.3' "));
		assertFalse(injectAnalyse("select * from dc_device where 2 "));
		assertFalse(injectAnalyse("select * from dc_device where (3+2) "));
		assertFalse(injectAnalyse("select * from dc_device where  -1 IS TRUE"));
		assertFalse(injectAnalyse("select * from dc_device where 'hello' is null "));
		assertFalse(injectAnalyse("select * from dc_device where '2022-10-31' and id > 0"));
		assertFalse(injectAnalyse("select * from dc_device where id > 0 or 1!=2.0 "));
		assertFalse(injectAnalyse("select * from dc_device where id > 0 or 1 in (1,3,4) "));
		assertFalse(injectAnalyse("select * from dc_device  UNION select name from other"));
		assertTrue(injectAnalyse("WITH SUB1 AS (SELECT user FROM t1) SELECT * FROM T2 WHERE id > 123 "));
	}

}

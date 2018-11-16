/**
 * 版权所有 2017 Sp42 frank@ajaxjs.com
 * 
 * 根据 2.0 版本 Apache 许可证("许可证")授权；
 * 根据本许可证，用户可以不使用此文件。
 * 用户可从下列网址获得许可证副本：
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *    
 * 除非因适用法律需要或书面同意，根据许可证分发的软件是基于"按原样"基础提供，
 * 无任何明示的或暗示的保证或条件。详见根据许可证许可下，特定语言的管辖权限和限制。
 */
package com.ajaxjs.orm.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表示一个 SELECT SQL 语句
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Select {
	/**
	 * SELECT SQL 语句
	 * 
	 * @return SELECT SQL 语句
	 */
	String value() default "";

	/**
	 * 转为 Sqlite 的 SELECT SQL 语句
	 * 
	 * @return SELECT SQL 语句
	 */
	String sqliteValue() default "";

	/**
	 * 统计总行数的 SQL，以便分页
	 * 
	 * @return 统计总行数的 SQL
	 */
	String countSql() default "";

	/**
	 * 转为 Sqlite 的统计总行数的 SQL，以便分页
	 * 
	 * @return 转为 Sqlite 的统计总行数的 SQL
	 */
	String sqliteCountSql() default "";


	/**
	 * 是否分页
	 * 
	 * @return 是否分页
	 */
	boolean isPageList() default false;

	/**
	 * 是否需要条件查询
	 * 
	 * @return 是否需要条件查询
	 */
	boolean isQuerySupport() default false;
}

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
package com.ajaxjs.sql.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SQL 语句的工厂
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SqlFactory {
	/**
	 * 必须为一个静态方法的名称，该方法输入一个 String 参数，返回一个 String 结果
	 * 
	 * @return 静态方法名称
	 */
	String value() default "";

	/**
	 * 方法所在的类。若为 Object.class 则指本类身上的
	 * 
	 * @return 方法所在的类
	 */
	Class<?> clz() default Object.class;
}

/**
 * 版权所有 2017 Frank Cheung
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
package com.ajaxjs.framework.model;

import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * POJO 实体文档说明
 * 
 * @author frank
 *
 *		// if(request.getRequestURI().contains(".doc")) {
		// String[] strs = DocumentRenderer.getEntityInfo(Video.class);
		// request.setAttribute("entityInfo", strs[0]);
		//
		// if(strs[1] != null) { // 更多关于该实体的文档
		// request.setAttribute("moreDocument", strs[1]);
		// }
		//
		//// request.setAttribute("meta",
		// DocumentRenderer.getDocument(Video.class,
		// getService().getSQL_TableName()));
		// return "/WEB-INF/jsp/common/entity/showDocument.jsp";
		// }
 */
@Target(value={TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityDescription {
	/**
	 * 标记实体文档说明
	 * @return 字段说明
	 */
	public String doc() default "";
	
	/**
	 * 其他部分的文档，指定一个 jsp 路径。
	 * @return
	 */
	public String extraHTML_path() default "";
}

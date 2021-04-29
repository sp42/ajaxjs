/**
 * 版权所有 2017 Sp42 frank@ajaxjs.com 根据 2.0 版本 Apache 许可证("许可证")授权；
 * 根据本许可证，用户可以不使用此文件。 用户可从下列网址获得许可证副本：
 * http://www.apache.org/licenses/LICENSE-2.0
 * 除非因适用法律需要或书面同意，根据许可证分发的软件是基于"按原样"基础提供，
 * 无任何明示的或暗示的保证或条件。详见根据许可证许可下，特定语言的管辖权限和限制。
 */
package com.ajaxjs.framework;

import com.ajaxjs.web.mvc.ModelAndView;

public interface ViewObjectService {
	/**
	 * UI 显示列表时执行
	 * 
	 * @param mv
	 */
	public void showList(ModelAndView mv);

	/**
	 * UI 显示单个实体时执行
	 * 
	 * @param mv
	 * @param id
	 */
	public void showInfo(ModelAndView mv, Long id);
}

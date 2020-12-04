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

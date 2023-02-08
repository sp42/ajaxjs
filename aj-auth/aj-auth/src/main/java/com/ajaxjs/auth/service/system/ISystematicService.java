package com.ajaxjs.auth.service.system;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;

import com.ajaxjs.auth.model.SystemModel.Systematic;
import com.ajaxjs.auth.service.IBaseService;
import com.ajaxjs.framework.IBaseController;

/**
 * 
 */
public interface ISystematicService extends IBaseService, IBaseController<Systematic> {
	@GetMapping
	@Override
	List<Systematic> list();
}

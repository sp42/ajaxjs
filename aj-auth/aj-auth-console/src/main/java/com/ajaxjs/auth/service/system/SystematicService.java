package com.ajaxjs.auth.service.system;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ajaxjs.auth.model.SystemModel.Systematic;

@Service
public class SystematicService implements ISystematicService {
	@Override
	public List<Systematic> list() {
		return SystematicDAO.findList();
	}

}

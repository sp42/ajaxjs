package com.ajaxjs.adp;

import org.springframework.stereotype.Service;

@Service
public class DoService implements IDoService {

	@Override
	public String getSubAreaUav(Long taskId) {
		return "hii";
	}

}

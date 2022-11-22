package com.ajaxjs.adp.data_service;

import org.springframework.stereotype.Service;

import com.ajaxjs.adp.controller.ITestController;

@Service
public class TestService implements ITestController {
	public Boolean test() {
		return true;
	}
}

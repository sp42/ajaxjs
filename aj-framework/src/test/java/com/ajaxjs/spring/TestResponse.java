package com.ajaxjs.spring;

import org.junit.Test;

import com.ajaxjs.spring.response.ResponseResult;

public class TestResponse {
	@Test
	public  void main() {
		ResponseResult r = new ResponseResult();
		r.setErrorCode("121");
		String result = r.toString();
		System.out.println(result);

		r.setErrorCode(null);
		r.setData("[]");
		result = r.toString();
		System.out.println(result);
	}
}

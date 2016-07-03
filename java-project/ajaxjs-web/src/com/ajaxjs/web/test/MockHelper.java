package com.ajaxjs.web.test;

import static org.mockito.Mockito.verify;

import javax.servlet.http.HttpServletRequest;

import org.mockito.ArgumentCaptor;

public class MockHelper {

	public static String getRequestDispatcheResult(HttpServletRequest request) {
		ArgumentCaptor<String> dispatcherArgument = ArgumentCaptor.forClass(String.class);
		verify(request).getRequestDispatcher(dispatcherArgument.capture());
		return dispatcherArgument.getValue();
	}

}

package com.ajaxjs.security;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public interface SecurityInit {
	public ServletRequest initRequest(ServletRequest request);

	public ServletResponse initResponse(ServletResponse request);
}

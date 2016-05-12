package com.ajaxjs.util.websecurity;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ISecurityFilter {
	public void doFilterInvoke(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;
}

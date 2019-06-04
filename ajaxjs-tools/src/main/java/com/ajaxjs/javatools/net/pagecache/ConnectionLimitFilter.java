package com.ajaxjs.javatools.net.pagecache;

import java.io.IOException;
import java.util.concurrent.Semaphore;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class ConnectionLimitFilter implements Filter{
	private Semaphore semaphore;
	
	private boolean isReject = true;
	
	private static final int DefaultConnectLimitSize = 50;

	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {
		boolean isProcessing = false;
		boolean isOut = semaphore.tryAcquire();

		try{
			if(isOut || !isReject){
				isProcessing = true;
				try {
					semaphore.acquire();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				arg2.doFilter(arg0, arg1);
			}
			
		}finally{
			if(isProcessing) semaphore.release();	
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		String resourceLimit = arg0.getInitParameter("resourceLimit");
		int size = DefaultConnectLimitSize;
		if(resourceLimit != null && !resourceLimit.isEmpty())
			size = Integer.parseInt(resourceLimit);
		
		semaphore = new Semaphore(size);
		
		String isReject = arg0.getInitParameter("isReject");
		if(isReject != null && "false".equals(isReject))
			this.isReject = false;
	}

}

package com.ajaxjs.util.pagecache;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ajaxjs.core.Util;
import com.ajaxjs.util.pagecache.cache.CacheStore;
import com.ajaxjs.util.pagecache.config.PageCacheGlobalConfig;
import com.ajaxjs.util.pagecache.config.UrlKeyCreator;

public class PageCacheFilter implements Filter {
	private CacheStore cacheStore;

	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpServletResponse httpResponse = (HttpServletResponse) response;

			String method = httpRequest.getMethod();
			if("GET".equalsIgnoreCase(method) || "POST".equalsIgnoreCase(method)){
				String requestUri = httpRequest.getRequestURI();
				String urlPattern = UrlKeyCreator.getMatchUrlPattern(requestUri);
				
				if(urlPattern != null){
					String  urlKey = UrlKeyCreator.getUrlKey(requestUri, urlPattern, httpRequest.getParameterMap()), 
							pageContent = cacheStore.get(urlKey);
					
					if(pageContent != null){
						httpResponse.getWriter().write(pageContent);
						return;
					}
					
					PageCacheHttpServletResponse pageCacheResponse = new PageCacheHttpServletResponse(httpResponse);
					filterChain.doFilter(request, pageCacheResponse);
					cacheStore.put(urlKey, pageCacheResponse.getPageContent(), urlPattern);
					return;
				}
			}
		}
		filterChain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		String  cacheStoreName = arg0.getInitParameter("cacheStore"),
				cacheStoreParams = arg0.getInitParameter("cacheStoreParams");
		
		try {
			Class<?> cacheStoreClass = Class.forName(cacheStoreName);
			cacheStore = (CacheStore) cacheStoreClass.newInstance();
			cacheStore.init(getInitParams(cacheStoreParams));
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			throw new ServletException(e);
		}
		
		String urlPattern = arg0.getInitParameter("urlPattern");
		String cacheExpireTime = arg0.getInitParameter("cacheExpireTime");
		String includeParams = arg0.getInitParameter("includeParams");
		String excludeParams = arg0.getInitParameter("excludeParams");
		initGrobleConfig(urlPattern, cacheExpireTime, includeParams, excludeParams);
	}

	private Map<String, String> getInitParams(String cacheStoreParams) {
		Map<String, String> initParams = new HashMap<>();
		String[] keyValues = cacheStoreParams.split(";");
		
		if (Util.isNotNull(keyValues)) {
			for (String kv : keyValues) {
				String[] keyValue = kv.split(":");
				if (keyValue != null && keyValue.length == 2) 
					initParams.put(keyValue[0], keyValue[1]);
			}
		}
		return initParams;
	}

	private void initGrobleConfig(String urlPattern, String cacheExpireTime, String includeParams, String excludeParams) {
		String[] urlPatterns = urlPattern.split(","), 
				 cacheExpireTimes = cacheExpireTime.split(",");
		
		Map<String, Integer> urlCacheTime = new HashMap<>();
		for (int i = 0; i < urlPatterns.length; i++) 
			urlCacheTime.put(urlPatterns[i], Integer.valueOf(cacheExpireTimes[i]));
	
		PageCacheGlobalConfig.setUrlPattern(Arrays.asList(urlPatterns));
		PageCacheGlobalConfig.setUrlCacheTime(urlCacheTime);
		PageCacheGlobalConfig.setUrlIncludeParams(initUrlParams(urlPatterns, includeParams));
		PageCacheGlobalConfig.setUrlExcludeParams(initUrlParams(urlPatterns, excludeParams));
	}
	
	private Map<String, List<String>> initUrlParams(String[] urlPatterns, String params){
		if(Util.isEmptyString(params)) return null;
		
		String[] paramList = params.split(";");
//		Map<String, Integer> urlCacheTime = new HashMap<>();
		Map<String, List<String>> urlParams = new HashMap<>();
		
		for (int i = 0; i < urlPatterns.length; i++) 
			urlParams.put(urlPatterns[i], Arrays.asList(paramList[i].split(",")));
		
		return urlParams;
	}

}

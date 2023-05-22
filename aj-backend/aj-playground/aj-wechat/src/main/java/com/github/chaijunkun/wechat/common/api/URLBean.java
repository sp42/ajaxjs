package com.github.chaijunkun.wechat.common.api;

/**
 * URL封装对象
 * @author chaijunkun
 * @since 2016年9月5日
 */
public class URLBean {
	
	/** 绝对地址 */
	private String absoluteURL;
	
	/**
	 * 构建URL封装对象
	 * @param useTls 是否使用安全传输协议(https)
	 * @param domain 域名
	 * @param relativeURL 域名下的相对地址
	 */
	public URLBean(final boolean useTls, final String domain, final String relativeURL) {
		absoluteURL = "";
		if (useTls){
			absoluteURL = absoluteURL.concat("https://");
		}else{
			absoluteURL = absoluteURL.concat("http://");
		}
		//这里不使用String.format是考虑到有可能以后相对URL中存在%s通配符
		absoluteURL = absoluteURL.concat(domain).concat(relativeURL);
	}

	/**
	 * 获取绝对地址 
	 * @return 绝对地址
	 */
	public String getAbsoluteURL() {
		return absoluteURL;
	}

}

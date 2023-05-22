package com.github.chaijunkun.wechat.common.api;

/**
 * 微信抽象URL工厂<br/>
 * 所有子类可控制域名的属性都必须为private static volatile修饰
 * @author chaijunkun
 * @since 2016年9月5日
 */
public abstract class AbstractURLFactory {

	/** 公共API域名 */
	protected static final String COMMON_API_DOMAIN = "api.weixin.qq.com";
	
	/** 商户平台API域名 */
	protected static final String MERCHANT_API_DOMAIN = "api.mch.weixin.qq.com";

}

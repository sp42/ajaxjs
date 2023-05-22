package com.github.chaijunkun.wechat.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * API最基本的无需签名的公共参数定义
 * @author chaijunkun
 * @since 2015年4月15日
 */
@JsonInclude(Include.NON_NULL)
public abstract class WeChatAPIParam {
	
}

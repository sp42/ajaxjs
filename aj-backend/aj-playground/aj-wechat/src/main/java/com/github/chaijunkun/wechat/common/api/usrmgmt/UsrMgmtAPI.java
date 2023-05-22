package com.github.chaijunkun.wechat.common.api.usrmgmt;

import com.github.chaijunkun.wechat.common.api.WeChatAPI;
import com.github.chaijunkun.wechat.common.api.WeChatAPIException;

/**
 * 用户管理接口
 * @author chaijunkun
 * @since 2016年9月6日
 */
public class UsrMgmtAPI extends WeChatAPI<UsrMgmtURLFactory> {

	@Override
	public UsrMgmtURLFactory getUrlFactory() {
		return urlFactory;
	}

	@Override
	public void setUrlFactory(UsrMgmtURLFactory urlFactory) {
		this.urlFactory = urlFactory;
	}

	public UserInfoResult userInfo(UserInfoParam param) throws WeChatAPIException {
		UserInfoResult result = super.doGetAPI(urlFactory.getUserInfo(), param, UserInfoResult.class);
		if (null == result){
			throw new IllegalStateException("获取到的用户信息为空");
		}
		if (null != result.getErrcode()){
			throw new WeChatAPIException(result.getErrcode(), result.getErrmsg());
		}
		return result;
	}

}

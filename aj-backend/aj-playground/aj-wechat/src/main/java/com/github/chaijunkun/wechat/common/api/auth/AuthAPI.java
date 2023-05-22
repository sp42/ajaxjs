package com.github.chaijunkun.wechat.common.api.auth;

import com.github.chaijunkun.wechat.common.api.WeChatAPI;
import com.github.chaijunkun.wechat.common.api.WeChatAPIException;
import com.github.chaijunkun.wechat.common.api.auth.AccessTokenParam.AccessTokenGrantType;

/**
 * 页面用户授权接口
 * @author chaijunkun
 * @since 2016年9月6日
 */
public class AuthAPI extends WeChatAPI<AuthURLFactory> {

	@Override
	public AuthURLFactory getUrlFactory() {
		return urlFactory;
	}

	@Override
	public void setUrlFactory(AuthURLFactory urlFactory) {
		this.urlFactory = urlFactory;
	}

	public AccessTokenResult accessToken(String code) throws WeChatAPIException {
		AccessTokenParam param = new AccessTokenParam();
		param.setCode(code);
		param.setAppId(getConfig().getAppId());
		param.setSecret(getConfig().getSecret());
		param.setGrantType(AccessTokenGrantType.authorization_code.getType());
		AccessTokenResult result = super.doGetAPI(urlFactory.getAccessToken(), param, AccessTokenResult.class);
		if (null == result){
			throw new IllegalStateException("获取到的用户access_token为空");
		}
		if (null != result.getErrcode()){
			throw new WeChatAPIException(result.getErrcode(), result.getErrmsg());
		}
		return result;
	}

}

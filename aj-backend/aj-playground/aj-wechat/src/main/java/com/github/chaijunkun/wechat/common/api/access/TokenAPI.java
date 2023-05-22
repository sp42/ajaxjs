package com.github.chaijunkun.wechat.common.api.access;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.github.chaijunkun.wechat.common.util.JSONUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.github.chaijunkun.wechat.common.api.WeChatAPI;
import com.github.chaijunkun.wechat.common.api.WeChatAPIException;
import com.github.chaijunkun.wechat.common.api.WeChatAPIException.APIErrEnum;
import com.github.chaijunkun.wechat.common.api.access.TokenParam.GrantType;

/**
 * 后端接入微信接口
 * @author chaijunkun
 * @since 2016年8月26日
 */
public class TokenAPI extends WeChatAPI<TokenAPIURLFactory> {

	/** 缓存模板 */
	private StringRedisTemplate redisTmpl;

	/**
	 * 获取缓存模板
	 * @return 缓存模板
	 */
	public StringRedisTemplate getRedisTmpl() {
		return redisTmpl;
	}

	/**
	 * 设置缓存模板
	 * @param redisTmpl 缓存模板
	 */
	public void setRedisTmpl(StringRedisTemplate redisTmpl) {
		this.redisTmpl = redisTmpl;
	}

	private String getAccessTokenCacheKey(TokenParam param){
		return String.format("wechat_access_token_%s", param.getAppId());
	}

	private TokenResult getTokenFromAPI(TokenParam param) throws WeChatAPIException {
		TokenResult token = super.doGetAPI(urlFactory.getToken(), param, TokenResult.class);
		if (null == token){
			throw new IllegalStateException("获取到的token为空");
		}
		if (!token.isSuccess()){
			throw new WeChatAPIException(token.getErrcode(), token.getErrmsg());
		}
		try {
			redisTmpl.opsForValue().set(this.getAccessTokenCacheKey(param), JSONUtil.toJSON(token), token.getExpiresIn(), TimeUnit.SECONDS);
			return token;
		} catch (IOException e) {
			throw new WeChatAPIException(APIErrEnum.SysErr, e);
		}
	}

	private TokenResult getToken() throws WeChatAPIException {
		TokenParam param = new TokenParam();
		param.setAppId(getConfig().getAppId());
		param.setSecret(getConfig().getSecret());
		param.setGrantType(GrantType.clientCredential.getType());
		String json = redisTmpl.opsForValue().get(this.getAccessTokenCacheKey(param));
		if (StringUtils.isNotBlank(json)){
			try {
				TokenResult result = JSONUtil.fromJSON(json, TokenResult.class);
				return (null == result)? this.getTokenFromAPI(param) : result;
			} catch (IOException e) {
				throw new WeChatAPIException(APIErrEnum.SysErr, e);
			}
		}else{
			return this.getTokenFromAPI(param);
		}
	}

	@Override
	public TokenAPIURLFactory getUrlFactory() {
		return urlFactory;
	}

	@Override
	public void setUrlFactory(TokenAPIURLFactory urlFactory) {
		this.urlFactory = urlFactory;
	}

	public TokenResult token() throws WeChatAPIException{
		return this.getToken();
	}

}

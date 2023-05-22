package com.github.chaijunkun.wechat.common.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import com.github.chaijunkun.wechat.common.util.JSONUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;

import com.github.chaijunkun.wechat.common.WeChatAPIConfig;
import com.github.chaijunkun.wechat.common.util.HttpUtil;

/**
 * 微信API公共定义
 * 
 * @author chaijunkun
 * @since 2016年8月29日
 */
public abstract class WeChatAPI<T extends AbstractURLFactory> {

	protected static final Logger logger = LoggerFactory.getLogger(WeChatAPI.class);

	/** 子业务封装具体接口URL的工厂 */
	protected T urlFactory;

	/** 业务接入配置 */
	private WeChatAPIConfig config;

	/**
	 * 获取子业务封装具体接口URL的工厂
	 * 
	 * @return 子业务封装具体接口URL的工厂
	 */
	public abstract T getUrlFactory();

	/**
	 * 设置子业务封装具体接口URL的工厂
	 * 
	 * @param urlFactory
	 *            子业务封装具体接口URL的工厂
	 */
	public abstract void setUrlFactory(T urlFactory);

	/**
	 * 获取业务接入配置
	 * 
	 * @return 业务接入配置
	 */
	public WeChatAPIConfig getConfig() {
		return config;
	}

	/**
	 * 设置业务接入配置
	 * 
	 * @param config
	 *            业务接入配置
	 */
	public void setConfig(WeChatAPIConfig config) {
		this.config = config;
	}

	/**
	 * 进行接口GET调用
	 * 
	 * @param url
	 *            接口请求地址
	 * @param param
	 *            业务参数
	 * @return 调用后返回的结果.该流需要<b>手动关闭</b>
	 * @throws WeChatAPIException
	 */
	protected static InputStream doGetAPI(final String url, WeChatAPIParam param) throws WeChatAPIException {
		try {
			APIParamDescriber describer = JSONUtil.convertValue(param, APIParamDescriber.class);
			return HttpUtil.getInstance().doGet(url, describer);
		} catch (URISyntaxException | IOException e) {
			throw new WeChatAPIException(WeChatAPIException.APIErrEnum.SysErr, e);
		}
	}

	/**
	 * 进行接口GET调用
	 * 
	 * @param url
	 *            接口请求地址
	 * @param param
	 *            业务参数
	 * @param clazz
	 *            结果返回类型(该类型不能含有泛型定义)
	 * @return 调用后返回的结果对象
	 * @throws WeChatAPIException
	 */
	@SuppressWarnings("unchecked")
	protected static <T> T doGetAPI(final String url, WeChatAPIParam param, Class<? extends WeChatAPIRet> clazz) throws WeChatAPIException {
		InputStream in = null;
		try {
			in = doGetAPI(url, param);
			T t = (T) JSONUtil.fromJSON(in, clazz);
			WeChatAPIRet ret = (WeChatAPIRet) t;
			if (ret.isSuccess()) {
				return t;
			} else {
				throw new WeChatAPIException(ret.getErrcode(), ret.getErrmsg());
			}
		} catch (IOException e) {
			throw new WeChatAPIException(WeChatAPIException.APIErrEnum.SysErr, e);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	/**
	 * 进行接口GET调用
	 * 
	 * @param url
	 *            接口请求地址
	 * @param param
	 *            业务参数
	 * @param valueTypeRef
	 *            结果返回类型(该类型可以含有泛型定义)
	 * @return 调用后返回的结果对象
	 * @throws WeChatAPIException
	 */
	@SuppressWarnings("unchecked")
	protected static <T> T doGetAPI(final String url, WeChatAPIParam param, TypeReference<? extends WeChatAPIRet> valueTypeRef) throws WeChatAPIException {
		InputStream in = null;
		try {
			in = doGetAPI(url, param);
			T t = (T) JSONUtil.fromJSON(in, valueTypeRef);
			WeChatAPIRet ret = (WeChatAPIRet) t;
			if (ret.isSuccess()) {
				return t;
			} else {
				throw new WeChatAPIException(ret.getErrcode(), ret.getErrmsg());
			}
		} catch (IOException e) {
			throw new WeChatAPIException(WeChatAPIException.APIErrEnum.SysErr, e);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	/**
	 * 进行接口POST调用
	 * 
	 * @param url
	 *            接口请求地址
	 * @param headerDescriber
	 *            业务请求头查询参数
	 * @param bodyDescriber
	 *            业务请求体参数
	 * @return 调用后返回的结果.该流需要<b>手动关闭</b>
	 * @throws WeChatAPIException
	 */
	protected static InputStream doPostAPI(final String url, APIParamDescriber headerDescriber, APIParamDescriber bodyDescriber) throws WeChatAPIException {
		try {
			return HttpUtil.getInstance().doPost(url, headerDescriber, bodyDescriber);
		} catch (URISyntaxException | IOException e) {
			throw new WeChatAPIException(WeChatAPIException.APIErrEnum.SysErr, e);
		}
	}

	/**
	 * 进行接口POST调用
	 * 
	 * @param url
	 *            接口请求地址
	 * @param headerDescriber
	 *            业务请求头查询参数
	 * @param content
	 *            content post内容
	 * @param contentType
	 *            内容类型
	 * @return 调用后返回的结果.该流需要<b>手动关闭</b>
	 * @throws WeChatAPIException
	 */
	protected static InputStream doPostAPI(final String url, APIParamDescriber headerDescriber, String content, ContentType contentType) throws WeChatAPIException {
		try {
			return HttpUtil.getInstance().doPost(url, headerDescriber, content, contentType);
		} catch (URISyntaxException | IOException e) {
			throw new WeChatAPIException(WeChatAPIException.APIErrEnum.SysErr, e);
		}
	}

	/**
	 * 进行接口POST调用
	 * 
	 * @param url
	 *            接口请求地址
	 * @param headerDescriber
	 *            业务请求头查询参数
	 * @param param
	 *            业务参数
	 * @return 调用后返回的结果.该流需要<b>手动关闭</b>
	 * @throws WeChatAPIException
	 */
	protected static InputStream doPostAPI(final String url, APIParamDescriber headerDescriber, WeChatAPIParam param) throws WeChatAPIException {
		APIParamDescriber bodyDescriber = JSONUtil.convertValue(param, APIParamDescriber.class);
		return doPostAPI(url, headerDescriber, bodyDescriber);
	}

	/**
	 * 进行接口POST调用
	 * 
	 * @param url
	 *            接口请求地址
	 * @param param
	 *            业务参数
	 * @param clazz
	 *            结果返回类型(该类型不能含有泛型定义)
	 * @return 调用后返回的结果对象
	 * @throws WeChatAPIException
	 */
	@SuppressWarnings("unchecked")
	protected static <T> T doPostAPI(final String url, WeChatAPIParam param, Class<? extends WeChatAPIRet> clazz) throws WeChatAPIException {
		InputStream in = null;
		try {
			in = doPostAPI(url, null, param);
			T t = (T) JSONUtil.fromJSON(in, clazz);
			WeChatAPIRet ret = (WeChatAPIRet) t;
			if (ret.isSuccess()) {
				return t;
			} else {
				throw new WeChatAPIException(ret.getErrcode(), ret.getErrmsg());
			}
		} catch (IOException e) {
			throw new WeChatAPIException(WeChatAPIException.APIErrEnum.SysErr, e);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	/**
	 * 进行接口POST调用
	 * 
	 * @param url
	 *            接口请求地址
	 * @param param
	 *            业务参数
	 * @param valueTypeRef
	 *            结果返回类型(该类型可以含有泛型定义)
	 * @return 调用后返回的结果对象
	 * @throws WeChatAPIException
	 */
	@SuppressWarnings("unchecked")
	protected static <T> T doPostAPI(final String url, WeChatAPIParam param, TypeReference<? extends WeChatAPIRet> valueTypeRef) throws WeChatAPIException {
		InputStream in = null;
		try {
			in = doPostAPI(url, null, param);
			T t = (T) JSONUtil.fromJSON(in, valueTypeRef);
			WeChatAPIRet ret = (WeChatAPIRet) t;
			if (ret.isSuccess()) {
				return t;
			} else {
				throw new WeChatAPIException(ret.getErrcode(), ret.getErrmsg());
			}
		} catch (IOException e) {
			throw new WeChatAPIException(WeChatAPIException.APIErrEnum.SysErr, e);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	/**
	 * 进行接口POST调用
	 * 
	 * @param url
	 *            接口请求地址
	 * @param param
	 *            需要access_token的业务参数
	 * @param clazz
	 *            结果返回类型(该类型不能含有泛型定义)
	 * @return 调用后返回的结果对象
	 * @throws WeChatAPIException
	 */
	@SuppressWarnings("unchecked")
	protected static <T> T doPostAPIWithAccessToken(final String url, WeChatAPIParamWithToken param, Class<? extends WeChatAPIRet> clazz) throws WeChatAPIException {
		InputStream in = null;
		try {
			APIParamDescriber headerDescriber = new APIParamDescriber();
			headerDescriber.put(WeChatAPIParamWithToken.ACCESS_TOKEN_FIELD, param.getAccessToken());
			in = doPostAPI(url, headerDescriber, JSONUtil.toJSON(param), ContentType.APPLICATION_JSON);
			T t = (T) JSONUtil.fromJSON(in, clazz);
			WeChatAPIRet ret = (WeChatAPIRet) t;
			if (ret.isSuccess()) {
				return t;
			} else {
				throw new WeChatAPIException(ret.getErrcode(), ret.getErrmsg());
			}
		} catch (IOException e) {
			throw new WeChatAPIException(WeChatAPIException.APIErrEnum.SysErr, e);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	/**
	 * 进行接口POST调用
	 * 
	 * @param url
	 *            接口请求地址
	 * @param param
	 *            需要access_token的业务参数
	 * @param valueTypeRef
	 *            结果返回类型(该类型可以含有泛型定义)
	 * @return 调用后返回的结果对象
	 * @throws WeChatAPIException
	 */
	@SuppressWarnings("unchecked")
	protected static <T> T doPostAPIWithAccessToken(final String url, WeChatAPIParamWithToken param, TypeReference<? extends WeChatAPIRet> valueTypeRef) throws WeChatAPIException {
		InputStream in = null;
		try {
			APIParamDescriber headerDescriber = new APIParamDescriber();
			headerDescriber.put(WeChatAPIParamWithToken.ACCESS_TOKEN_FIELD, param.getAccessToken());
			APIParamDescriber bodyDescriber = JSONUtil.convertValue(param, APIParamDescriber.class);
			bodyDescriber.remove(WeChatAPIParamWithToken.ACCESS_TOKEN_FIELD);
			in = doPostAPI(url, headerDescriber, JSONUtil.toJSON(bodyDescriber), ContentType.APPLICATION_JSON);
			T t = (T) JSONUtil.fromJSON(in, valueTypeRef);
			WeChatAPIRet ret = (WeChatAPIRet) t;
			if (ret.isSuccess()) {
				return t;
			} else {
				throw new WeChatAPIException(ret.getErrcode(), ret.getErrmsg());
			}
		} catch (IOException e) {
			throw new WeChatAPIException(WeChatAPIException.APIErrEnum.SysErr, e);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

}

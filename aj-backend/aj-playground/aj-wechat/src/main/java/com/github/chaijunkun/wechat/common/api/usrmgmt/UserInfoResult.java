package com.github.chaijunkun.wechat.common.api.usrmgmt;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.github.chaijunkun.wechat.common.api.WeChatAPIRet;

/**
 * 用户详细信息查询结果
 * @author chaijunkun
 * @since 2016年9月1日
 */
public class UserInfoResult extends WeChatAPIRet {
	
	private static final long serialVersionUID = -5066917634877049872L;

	/**
	 * 用户是否订阅该公众号标识
	 * 值为0时，代表此用户没有关注该公众号，拉取不到其余信息
	 */
	@JsonProperty(value = "subscribe")
	private Integer subscribe;
	
	/** 用户的标识，对当前公众号唯一 */
	@JsonProperty(value = "openid")
	private String openId;
	
	/** 用户的昵称 */
	@JsonProperty(value = "nickname")
	private String nickname;
	
	/** 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知 */
	@JsonProperty(value = "sex")
	private Integer sex;
	
	/** 用户的语言，简体中文为zh_CN */
	@JsonProperty(value = "language")
	private String language;
	
	/** 用户所在城市 */
	@JsonProperty(value = "city")
	private String city;
	
	/** 用户所在省份 */
	@JsonProperty(value = "province")
	private String province;
	
	/** 用户所在国家 */
	@JsonProperty(value = "country")
	private String country;
	
	/** 
	 * 用户头像
	 * 最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），
	 * 用户没有头像时该项为空。
	 * 若用户更换头像，原有头像URL将失效
	 */
	@JsonProperty(value = "headimgurl")
	private String headImgUrl;
	
	/** 用户关注时间，为时间戳。单位：秒。如果用户曾多次关注，则取最后关注时间 */
	@JsonProperty(value = "subscribe_time")
	private Long subscribeTime;
	
	/** 
	 * 统一用户标识
	 * 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段
	 */
	@JsonProperty(value = "unionid")
	private String unionId;
	
	/** 公众号运营者对粉丝的备注，公众号运营者可在微信公众平台用户管理界面对粉丝添加备注 */
	@JsonProperty(value = "remark")
	private String remark;
	
	/** 用户所在的分组ID（兼容旧的用户分组接口） */
	@JsonProperty(value = "groupid")
	private Integer groupId;
	
	/** 用户被打上的标签ID列表 */
	@JsonProperty(value = "tagid_list")
	private List<Integer> tagIdList;

	/**
	 * 获取用户是否订阅该公众号标识
	 * @return 用户是否订阅该公众号标识
	 */
	public Integer getSubscribe() {
		return subscribe;
	}

	/**
	 * 设置用户是否订阅该公众号标识
	 * @param subscribe 用户是否订阅该公众号标识
	 */
	public void setSubscribe(Integer subscribe) {
		this.subscribe = subscribe;
	}

	/**
	 * 获取用户的标识
	 * @return 用户的标识
	 */
	public String getOpenId() {
		return openId;
	}

	/**
	 * 设置用户的标识
	 * @param openId 用户的标识
	 */
	public void setOpenId(String openId) {
		this.openId = openId;
	}

	/**
	 * 获取用户的昵称
	 * @return 用户的昵称
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * 设置用户的昵称
	 * @param nickname 用户的昵称
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * 获取用户的性别
	 * @return 用户的性别
	 */
	public Integer getSex() {
		return sex;
	}

	/**
	 * 设置用户的性别
	 * @param sex 用户的性别
	 */
	public void setSex(Integer sex) {
		this.sex = sex;
	}

	/**
	 * 获取用户的语言
	 * @return 用户的语言
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * 设置用户的语言
	 * @param language 用户的语言
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * 获取用户所在城市
	 * @return 用户所在城市
	 */
	public String getCity() {
		return city;
	}

	/**
	 * 设置用户所在城市
	 * @param city 用户所在城市
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * 获取用户所在省份
	 * @return 用户所在省份
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * 设置用户所在省份
	 * @param province 用户所在省份
	 */
	public void setProvince(String province) {
		this.province = province;
	}

	/**
	 * 获取用户所在国家
	 * @return 用户所在国家
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * 设置用户所在国家
	 * @param country 用户所在国家
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * 获取用户头像
	 * @return 用户头像
	 */
	public String getHeadImgUrl() {
		return headImgUrl;
	}

	/**
	 * 设置用户头像
	 * @param headImgUrl 用户头像
	 */
	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	/**
	 * 获取用户关注时间
	 * @return 用户关注时间
	 */
	public Long getSubscribeTime() {
		return subscribeTime;
	}

	/**
	 * 设置用户关注时间
	 * @param subscribeTime 用户关注时间
	 */
	public void setSubscribeTime(Long subscribeTime) {
		this.subscribeTime = subscribeTime;
	}

	/**
	 * 获取统一用户标识
	 * @return 统一用户标识
	 */
	public String getUnionId() {
		return unionId;
	}

	/**
	 * 设置统一用户标识
	 * @param unionId 统一用户标识
	 */
	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}

	/**
	 * 获取公众号运营者对粉丝的备注
	 * @return 公众号运营者对粉丝的备注
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 设置公众号运营者对粉丝的备注
	 * @param remark 公众号运营者对粉丝的备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 获取用户所在的分组ID
	 * @return 用户所在的分组ID
	 */
	public Integer getGroupId() {
		return groupId;
	}

	/**
	 * 设置用户所在的分组ID
	 * @param groupId 用户所在的分组ID
	 */
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	/**
	 * 获取用户被打上的标签ID列表
	 * @return 用户被打上的标签ID列表
	 */
	public List<Integer> getTagIdList() {
		return tagIdList;
	}

	/**
	 * 设置用户被打上的标签ID列表
	 * @param tagIdList 用户被打上的标签ID列表
	 */
	public void setTagIdList(List<Integer> tagIdList) {
		this.tagIdList = tagIdList;
	}
	
}

package com.github.chaijunkun.wechat.common.callback;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 微信服务器回调接口参数
 * @author chaijunkun
 * @since 2016年8月29日
 */
public class WeChatCallbackQueryParam {
	
	/** 回调签名字段 */
	public static final String SIGNATURE_FIELD = "signature";
	
	/** 时间戳字段 */
	public static final String TIMESTAMP_FIELD = "timestamp";
	
	/** 随机数字段 */
	public static final String NONCE_FIELD = "nonce";
	
	/** 随机数字段 */
	public static final String  ECHOSTR_FIELD = "echostr";
	
	/** 消息发送方的openId字段 */
	public static final String OPEN_ID_FIELD = "openid";
	
	/** 消息加解密方式字段 */
	public static final String ENCRYPT_TYPE_FIELD = "encrypt_type";
	
	/** 消息签名字段 */
	public static final String MSG_SIGNATURE_FIELD = "msg_signature";
	
	public static enum EncryptType {
		/** 使用AES加密 */
		AES("aes"),
		/** 没有加密或兼容模式 */
		NONE("none");
		/** 加密方式*/
		private String value;
		
		private EncryptType(String value){
			this.value = value;
		}
		
		/**
		 * 获取加密方式
		 * @return 加密方式
		 */
		public String getValue() {
			return value;
		}
		
		/**
		 * 根据加密类型值判断采用何种加密手段
		 * @param val
		 * @return
		 */
		public static EncryptType getEncrypTypeByValue(String val) {
			EncryptType[] values = EncryptType.values();
			for (EncryptType value : values) {
				if (value.getValue().equals(val)){
					return value;
				}
			}
			return EncryptType.NONE;
		}
		
	}
	
	/** 微信回调签名 */
	@JsonProperty(value = SIGNATURE_FIELD)
	private String signature;
	
	/**
	 * 时间戳
	 * <p>这里不使用long型是因为该字段反复参与签名,为了避免频繁类型转换,直接使用string类型</p>
	 */
	@JsonProperty(value = TIMESTAMP_FIELD)
	private String timestamp;
	
	/** 随机数 */
	@JsonProperty(value = NONCE_FIELD)
	private String nonce;
	
	/** 随机字符串 */
	@JsonProperty(value = ECHOSTR_FIELD)
	private String echostr;
	
	/** 消息发送方的openId */
	@JsonProperty(value = OPEN_ID_FIELD)
	private String openId;
	
	/** 消息加解密方式 */
	@JsonProperty(value = ENCRYPT_TYPE_FIELD)
	private String encryptType;
	
	/** 消息签名 */
	@JsonProperty(value = MSG_SIGNATURE_FIELD)
	private String msgSignature;

	/**
	 * 获取微信回调签名
	 * @return 微信回调签名
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * 设置微信回调签名
	 * @param signature 微信回调签名
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}

	/**
	 * 获取时间戳
	 * @return 时间戳
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * 设置时间戳
	 * @param timestamp 时间戳
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * 获取随机数
	 * @return 随机数
	 */
	public String getNonce() {
		return nonce;
	}

	/**
	 * 设置随机数
	 * @param nonce 随机数
	 */
	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	/**
	 * 获取随机字符串
	 * @return 随机字符串
	 */
	public String getEchostr() {
		return echostr;
	}

	/**
	 * 设置随机字符串
	 * @param echostr 随机字符串
	 */
	public void setEchostr(String echostr) {
		this.echostr = echostr;
	}

	/**
	 * 获取消息发送方的openId
	 * @return 消息发送方的openId
	 */
	public String getOpenId() {
		return openId;
	}

	/**
	 * 设置消息发送方的openId
	 * @param openId 消息发送方的openId
	 */
	public void setOpenId(String openId) {
		this.openId = openId;
	}

	/**
	 * 获取消息加解密方式
	 * @return 消息加解密方式
	 */
	public String getEncryptType() {
		return encryptType;
	}

	/**
	 * 设置消息加解密方式
	 * @param encryptType 消息加解密方式
	 */
	public void setEncryptType(String encryptType) {
		this.encryptType = encryptType;
	}

	/**
	 * 获取消息签名
	 * @return 消息签名
	 */
	public String getMsgSignature() {
		return msgSignature;
	}

	/**
	 * 设置消息签名
	 * @param msgSignature 消息签名
	 */
	public void setMsgSignature(String msgSignature) {
		this.msgSignature = msgSignature;
	}
	
}

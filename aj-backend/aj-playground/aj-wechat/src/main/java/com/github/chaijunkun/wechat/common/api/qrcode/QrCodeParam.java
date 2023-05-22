package com.github.chaijunkun.wechat.common.api.qrcode;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.github.chaijunkun.wechat.common.api.WeChatAPIParamWithToken;

public class QrCodeParam extends WeChatAPIParamWithToken {

	public enum QrCodeAction {
		/**
		 * 临时
		 */
		QR_SCENE,
		/**
		 * 永久
		 */
		QR_LIMIT_SCENE,
		/**
		 * 永久的字符串参数值
		 */
		QR_LIMIT_STR_SCENE,
	}

	public static class ActionInfo {
		private Scene scene;
		
		public ActionInfo(Scene scene) {
			super();
			this.scene = scene;
		}

		public void setScene(Scene scene) {
			this.scene = scene;
		}

		public Scene getScene() {
			return this.scene;
		}

	}

	public static class Scene {
		/**
		 * 场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）
		 */
		@JsonProperty(value = "scene_id")
		private Integer sceneId;
		
		public Scene(Integer sceneId) {
			super();
			this.sceneId = sceneId;
		}

		public void setSceneId(int sceneId) {
			this.sceneId = sceneId;
		}

		public int getSceneId() {
			return this.sceneId;
		}

	}

	/**
	 * 该二维码有效时间，以秒为单位。 最大不超过604800（即7天）。
	 */
	@JsonProperty(value = "expire_seconds")
	private Integer expireSeconds;
	/**
	 * 二维码类型，QR_SCENE为临时,QR_LIMIT_SCENE为永久,QR_LIMIT_STR_SCENE为永久的字符串参数值
	 */
	@JsonProperty(value = "action_name")
	private String actionName;
	/**
	 * 二维码详细信息
	 */
	@JsonProperty(value = "action_info")
	private ActionInfo actionInfo;
	
	public Integer getExpireSeconds() {
		return expireSeconds;
	}

	public void setExpireSeconds(Integer expireSeconds) {
		this.expireSeconds = expireSeconds;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public ActionInfo getActionInfo() {
		return actionInfo;
	}

	public void setActionInfo(ActionInfo actionInfo) {
		this.actionInfo = actionInfo;
	}

}

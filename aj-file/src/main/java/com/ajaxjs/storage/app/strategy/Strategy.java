package com.ajaxjs.storage.app.strategy;

/**
 * @TableName("ufs_storage_strategy")
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class Strategy {
	/**
	 * 策略 ID"
	 */
	private Long id;

	/**
	 * 策略名称
	 */
	private String name;

	/**
	 * 是否默认
	 */
	private boolean isDefault;

	/**
	 * 类型
	 */
	private String type;

	/**
	 * 策略配置
	 */
	private Settings settings;

	/**
	 * 
	 * @author Frank Cheung<sp42@qq.com>
	 *
	 */
	public static class Settings {
		private StorageAreaSettings storageAreaSettings;

		public static class StorageAreaSettings {
			public String getMatchType() {
				return matchType;
			}

			public void setMatchType(String matchType) {
				this.matchType = matchType;
			}

			public String getPublicField() {
				return publicField;
			}

			public void setPublicField(String publicField) {
				this.publicField = publicField;
			}

			public String getIndexServer() {
				return indexServer;
			}

			public void setIndexServer(String indexServer) {
				this.indexServer = indexServer;
			}

			private String matchType;

			private String publicField;

			private String indexServer;
		}

		public StorageAreaSettings getStorageAreaSettings() {
			return storageAreaSettings;
		}

		public void setStorageAreaSettings(StorageAreaSettings storageAreaSettings) {
			this.storageAreaSettings = storageAreaSettings;
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

}

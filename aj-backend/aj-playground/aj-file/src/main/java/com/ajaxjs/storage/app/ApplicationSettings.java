package com.ajaxjs.storage.app;

import java.util.List;
import java.util.Map;

public class ApplicationSettings {
	private Map<String, StorageSettings> storages;
	private IndexSettings index;
	private List<ConvertRule> convertRules;
	private Constraints constraints = new Constraints();
	private WatermarkSetting watermarkSetting;

	public static class WatermarkSetting {
		private TextConfig textConfig;
		private PictureConfig pictureConfig;

		public static class TextConfig {
			private Boolean watermarkEnable;
			private Integer rotate;
			private String font;
			private Integer size;
			private String color;
			private Boolean newLine;
			private Integer transparency;
			private String display;
			private List<Content> contents;
		}

		public static class PictureConfig {
			private Boolean watermarkEnable;
			private String ufsid;
			private Integer widthZoom;
			private Integer heightZoom;
			private Integer transparency;
			private String display;
		}

		public static class Content {
			private String text;
			private Boolean translate;
		}
	}

	public static class StorageSettings {
		private String kind;
		private S3StorageSettings s3;
		private DiskStorageSettings disk;
		private Boolean isDefault;
	}

	public static class S3StorageSettings {
		public static final String DEFAULT_BUCKET_NAME = "ufs";

		private Long fileServerId;
		private String url;
		private String bucketName = DEFAULT_BUCKET_NAME;
		private String accessKey;
		private String secretKey;
		private String singerType = "V4";
		private ShardingSettings sharding;

		public static class ShardingSettings {
			public static final String DEFAULT_BUCKET_NAME_FORMAT = "ufs-%s";

			private String bucketNameFormat = DEFAULT_BUCKET_NAME_FORMAT;
			private Integer totalCount;
		}
	}

	public static class DiskStorageSettings {
		private String folder;
		private String domain;
		private String username;
		private String password;
		private String secretKey;
		private DiskStorageSettings.ShardingSettings sharding;

		public static class ShardingSettings {
			public static final String DEFAULT_BUCKET_NAME_FORMAT = "ufs-%s";
			private Integer totalCount;
		}
	}

	public static class IndexSettings {
		public static final String DEFAULT_INDEX_NAME_FORMAT = "${appId}-file-${date}";
		public static final String BUILD_INDEX_AUTO = "auto";
		public static final String BUILD_INDEX_MANUAL = "manual";

		private Long id;
		private String[] hosts;
		private String username;
		private String password;
		private String indexNameFormat = DEFAULT_INDEX_NAME_FORMAT;
		private String indexTemplate;
		private String buildIndex = BUILD_INDEX_AUTO;
		private Boolean isDefault;
		private String pathPrefix;
	}

	public static class ConvertRule {
		private List<String> mediaTypes;
		private String kind;
		private String name;
		private String params;
	}

	public static class Constraints {
		/**
		 * 是否把原始文件名归档，默认为 TRUE
		 */
		private boolean archiveFilename = false;

		/**
		 * 是否把文件创建人归档，默认为 TRUE
		 */
		private boolean archiveCreatedBy = false;
	}
}

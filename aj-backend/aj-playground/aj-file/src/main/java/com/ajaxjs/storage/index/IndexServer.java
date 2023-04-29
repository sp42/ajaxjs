package com.ajaxjs.storage.index;

import com.ajaxjs.storage.app.ApplicationSettings;

/**
 * @TableName("ufs_index_server")
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class IndexServer {
	public static final String KIND_PUBLIC = "public";

	public static final String KIND_PRIVATE = "private";

	/**
	 * 服务器ID
	 */
	private Long id;

	/**
	 * 服务器名称
	 */
	private String name;

	/**
	 * 集群名称
	 */
	private String cluster;

	/**
	 * 集群配置
	 */
	private String clusterSettings;

	private ApplicationSettings.IndexSettings settings = new ApplicationSettings.IndexSettings();

	private String[] seeds;

	private String kind;

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

	public String getCluster() {
		return cluster;
	}

	public void setCluster(String cluster) {
		this.cluster = cluster;
	}

	public ApplicationSettings.IndexSettings getSettings() {
		return settings;
	}

	public void setSettings(ApplicationSettings.IndexSettings settings) {
		this.settings = settings;
	}

	public String getClusterSettings() {
		return clusterSettings;
	}

	public void setClusterSettings(String clusterSettings) {
		this.clusterSettings = clusterSettings;
	}

	public String[] getSeeds() {
		return seeds;
	}

	public void setSeeds(String[] seeds) {
		this.seeds = seeds;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}
}

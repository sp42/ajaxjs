package com.ajaxjs.util.config2;

import org.springframework.core.env.PropertySource;
import java.nio.file.Path;

public class PropertySourceMeta {
	private PropertySource<?> propertySource;

	private Path filePath;

	private long lastModifyTime;

	public PropertySourceMeta(PropertySource<?> ps, Path path, long l) {
		this.propertySource = ps;
		this.filePath = path;
		this.lastModifyTime = l;
	}

	public PropertySource<?> getPropertySource() {
		return propertySource;
	}

	public void setPropertySource(PropertySource<?> propertySource) {
		this.propertySource = propertySource;
	}

	public Path getFilePath() {
		return filePath;
	}

	public void setFilePath(Path filePath) {
		this.filePath = filePath;
	}

	public long getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(long lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

}

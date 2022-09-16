//package com.ajaxjs.util.config2;
//
//import java.nio.file.Path;
//import java.util.ArrayList;
//import java.util.List;
//import static com.ajaxjs.util.config2.ConfigurationUtils.CONFIG_FILE_PREFIX;
//import static com.ajaxjs.util.config2.ConfigurationUtils.trimRelativePathAndReplaceBackSlash;
//
//public class FileSystemWatchTarget {
//	private WatchTargetType type;
//
//	private String normalizedDir;
//
//	private List<String> filterFiles;
//
//	private Path rootDir;
//
//	FileSystemWatchTarget(WatchTargetType type, String originalPath) {
//		this.type = type;
//
//		if (originalPath.startsWith(CONFIG_FILE_PREFIX))
//			originalPath = trimRelativePathAndReplaceBackSlash(originalPath.substring(CONFIG_FILE_PREFIX.length()));
//		else
//			originalPath = trimRelativePathAndReplaceBackSlash(originalPath);
//
//		if (type == WatchTargetType.CONFIG_LOCATION)
//			this.normalizedDir = originalPath;
//		else if (type == WatchTargetType.CONFIG_IMPORT_FILE) {
//			int idx = originalPath.lastIndexOf("/");
//			this.normalizedDir = originalPath.substring(0, idx);
//			this.filterFiles = new ArrayList<>(2);
//			this.filterFiles.add(originalPath.substring(idx + 1));
//		} else if (type == WatchTargetType.CONFIG_IMPORT_TREE)
//			this.normalizedDir = originalPath;
//	}
//
//	/**
//	 * The watch target type, from spring.config.location or import:configtree /
//	 * file
//	 */
//	public enum WatchTargetType {
//
//		/**
//		 * When using spring.config.location
//		 */
//		CONFIG_LOCATION,
//
//		/**
//		 * When using spring.config.import=file:
//		 */
//		CONFIG_IMPORT_FILE,
//
//		/**
//		 * When using spring.config.import=configtree:
//		 */
//		CONFIG_IMPORT_TREE
//	}
//
//	public WatchTargetType getType() {
//		return type;
//	}
//
//	public void setType(WatchTargetType type) {
//		this.type = type;
//	}
//
//	public String getNormalizedDir() {
//		return normalizedDir;
//	}
//
//	public void setNormalizedDir(String normalizedDir) {
//		this.normalizedDir = normalizedDir;
//	}
//
//	public List<String> getFilterFiles() {
//		return filterFiles;
//	}
//
//	public void setFilterFiles(List<String> filterFiles) {
//		this.filterFiles = filterFiles;
//	}
//
//	public Path getRootDir() {
//		return rootDir;
//	}
//
//	public void setRootDir(Path rootDir) {
//		this.rootDir = rootDir;
//	}
//
//}

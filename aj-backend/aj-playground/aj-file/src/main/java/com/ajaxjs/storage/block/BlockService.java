package com.ajaxjs.storage.block;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.ajaxjs.storage.ErrorMessages;
import com.ajaxjs.storage.app.AppRepository;
import com.ajaxjs.storage.app.ApplicationSettings;
import com.ajaxjs.storage.app.model.Application;
import com.ajaxjs.storage.block.model.ContentBlock;

/**
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class BlockService {
	private final Map<String, StorageFactory> registry = new HashMap<>();

	@Autowired
	private AppRepository appRepository;

	@Autowired
	private BlockRepository repository;

	@Autowired
	private FileServerRepository fileServerRepository;

	@Autowired(required = false)
	private Storage defaultStorage;

	public void registry(String kind, StorageFactory factory) {
		if (registry.containsKey(kind))
			throw new IllegalArgumentException(ErrorMessages.INVALID_STORAGE_KIND);

		registry.put(kind, factory);
	}

	public Storage getDefaultStorage() {
		if (defaultStorage == null)
			throw new IllegalStateException(ErrorMessages.NO_DEFAULT_STORAGE);

		return defaultStorage;
	}

	public Storage getStorage(String appId, String name) {
		if (StringUtils.startsWith(name, "fs:")) {
			String fileServerId = StringUtils.substringAfter(name, "fs:");
			FileServer fileServer = fileServerRepository.selectById(Long.valueOf(fileServerId));
			return registry.get(fileServer.getKind()).getStorage(fileServer.getSettings());
		}

		Application app = appRepository.selectById(appId);

		if (app == null)
			throw new IllegalArgumentException(ErrorMessages.APPLICATION_NOT_FOUND);

		if (app.getSettings() == null || app.getSettings().getStorages() == null)
			throw new IllegalArgumentException(ErrorMessages.STORAGE_NOT_FOUND);

		ApplicationSettings.StorageSettings storageSettings = app.getSettings().getStorages().get(name);
		if (storageSettings == null)
			throw new IllegalArgumentException(ErrorMessages.STORAGE_NOT_FOUND);

		StorageFactory factory = registry.get(storageSettings.getKind());
		if (factory == null)
			throw new IllegalArgumentException(ErrorMessages.STORAGE_KIND_NOT_FOUND);

		return factory.getStorage(storageSettings);
	}

	public void insert(ContentBlock block) {
		repository.insert(block);
	}

	public ContentBlock getById(long blockId) {
		return repository.selectById(blockId);
	}

	public ContentBlock getByMd5(String md5) {
		return repository.selectByMd5(md5);
	}

	public ContentBlock getByMd5(String appId, String storage, String md5) {
		return repository.selectByMd5(appId, storage, md5);
	}

	public boolean retain(long blockId) {
		return repository.retain(blockId);
	}

	public void release(long blockId) {
		repository.release(blockId);
	}
}
package com.ajaxjs.storage.block;


import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.sl.usermodel.ObjectMetaData.Application;

import com.ajaxjs.storage.app.AppRepository;
import com.ajaxjs.storage.app.ApplicationSettings;
import com.ajaxjs.storage.block.model.ContentBlock;


public class BlockServiceImpl implements BlockService {
    private final Map<String, StorageFactory> registry = new HashMap<>();

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private BlockRepository repository;

    @Autowired
    private FileServerRepository fileServerRepository;


    @Autowired(required = false)
    private Storage defaultStorage;

    public void registry(@NotNull() String kind, @NotNull() StorageFactory factory) {
        if (registry.containsKey(kind)) 
			throw new IllegalArgumentException(ErrorMessages.INVALID_STORAGE_KIND);
        

        registry.put(kind, factory);
    }

    @Override
    public Storage getDefaultStorage() {
        if (defaultStorage == null) 
            throw new IllegalStateException(ErrorMessages.NO_DEFAULT_STORAGE);
        

        return defaultStorage;
    }

    @Override
    public Storage getStorage(@NotNull() String appId, @NotNull()String name) {

        if(StringUtils.startsWith(name,"fs:")) {
            String fileServerId = StringUtils.substringAfter(name,"fs:");
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
            throw new IllegalArgumentException(STORAGE_NOT_FOUND);
        

        StorageFactory factory = registry.get(storageSettings.getKind());
        if (factory == null) 
            throw new IllegalArgumentException(STORAGE_KIND_NOT_FOUND);
        
        return factory.getStorage(storageSettings);
    }

    @Override
    public void insert(ContentBlock block) {
        repository.insert(block);
    }

    @Override
    public ContentBlock getById(long blockId) {
        return repository.selectById(blockId);
    }

    @Override
    public ContentBlock getByMd5(String md5) {
        return repository.selectByMd5(md5);
    }

    @Override
    public ContentBlock getByMd5(String appId, String storage, String md5) {
        return repository.selectByMd5(appId, storage, md5);
    }

    @Override
    public boolean retain(long blockId) {
        return repository.retain(blockId);
    }

    @Override
    public void release(long blockId) {
        repository.release(blockId);
    }
}

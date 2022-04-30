package com.ajaxjs.storage.block;

import com.ajaxjs.storage.app.ApplicationSettings;

/**
 * @author Helfen
 */
public interface StorageFactory {
    Storage getStorage(ApplicationSettings.StorageSettings settings);
}

package com.ajaxjs.storage.block;

import com.ajaxjs.storage.block.model.ContentBlock;
import com.google.common.base.Strings;

/**
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public interface BlockService {
	Storage getDefaultStorage();

	Storage getStorage(String appId, String name);

	default Storage getStorage(ContentBlock block) {
		if (Strings.isNullOrEmpty(block.getAppId()) || Strings.isNullOrEmpty(block.getStorage()))
			return getDefaultStorage();
		else
			return getStorage(block.getAppId(), block.getStorage());
	}

	void insert(ContentBlock block);

	ContentBlock getById(long blockId);

	ContentBlock getByMd5(String md5);

	ContentBlock getByMd5(String appId, String storage, String md5);

	boolean retain(long blockId);

	void release(long blockId);
}

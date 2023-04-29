package com.ajaxjs.storage.app;

import java.io.Serializable;

/**
 * @author Helfen
 */
public interface AppRepository {
//    @Select("select count(1) from ufs_storage_app where id = #{id} limit 1")
	boolean exists(Serializable id);
}

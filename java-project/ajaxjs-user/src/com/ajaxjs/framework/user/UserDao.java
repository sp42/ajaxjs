package com.ajaxjs.framework.user;

import java.util.Date;

import org.apache.ibatis.annotations.Update;

import com.ajaxjs.framework.dao.DAO;

public interface UserDao extends DAO<User> {
	@Update("UPDATE SET user loginDate = ${loginDate}, ip = ${ip} WHERE id = ${id}")
	int updateLoginInfo(long id, Date loginDate, String ip);
}

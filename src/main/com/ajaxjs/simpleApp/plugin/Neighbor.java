package com.ajaxjs.simpleApp.plugin;

import java.io.Serializable;
import java.util.Map;

import com.ajaxjs.jdbc.Helper;
import com.ajaxjs.jdbc.JdbcConnection;
import com.ajaxjs.mvc.ModelAndView;

/**
 * 相邻记录
 * 
 * @author admin
 *
 */
public class Neighbor {
	/**
	 * 
	 * @param model
	 * @param tableName
	 * @param id
	 */
	public static void init(ModelAndView model, String tableName, Serializable id) {
		Map<String, Object> perv, next;
		perv = Helper.query(JdbcConnection.getConnection(), "SELECT id, name FROM " + tableName + " WHERE status = 1 AND id < ? ORDER BY id DESC LIMIT 1", id);
		next = Helper.query(JdbcConnection.getConnection(), "SELECT id, name FROM " + tableName + " WHERE status = 1 AND id > ? LIMIT 1", id);

		model.put("neighbor_pervInfo", perv);
		model.put("neighbor_nextInfo", next);
	}
}

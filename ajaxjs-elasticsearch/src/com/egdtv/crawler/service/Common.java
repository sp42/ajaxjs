package com.egdtv.crawler.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.ajaxjs.app.MyBatis;
import com.ajaxjs.framework.exception.ServiceException;
import com.ajaxjs.framework.model.JspModel;
import com.ajaxjs.jdbc.Helper;
import com.ajaxjs.util.SimpleCache;
import com.egdtv.crawler.Constant;
import com.egdtv.crawler.model.NetItem;


public class Common {

	/**
	 * 添加爬虫类型的分类
	 * 
	 * @param model
	 *            Spring MVC 模型
	 */
	public static void prepareData(Model model) {
		model.addAttribute("crawlerTypes", Constant.crawlerTypes);
	}

	/**
	 * 
	 * @param bean
	 * @param model
	 * @throws ServiceException
	 */
	public static void getLinkDataForInfo(NetItem bean, Model model) throws ServiceException {
		// 分类
		int catalogId = bean.getCatalog();
		model.addAttribute("catalog", new CatalogService().getById(catalogId));
		// 门户 
		int protalId = bean.getPortalId();
		model.addAttribute("portal", new PortalService().getById(protalId));
		// 专辑信息
		Integer albumId =bean.getAlbumId();
		if(albumId != null)
			model.addAttribute("album", new AlbumService().getById(albumId));
	}
	
	/**
	 * 缓存
	 */
	public static SimpleCache<String, List<Map<String, Object>>> cache = new SimpleCache<>(20);
	
	static {
		// 写缓存
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				try(Connection conn = MyBatis.getConnection();){
					List<Map<String, Object>> all_portals_video, all_portals_audio, 
											  all_catalogs_video, all_catalogs_audio,
											  all_portals_album, all_catalogs_album;
					
					String portalSql = "SELECT REPTILE_PORTAL.id, REPTILE_PORTAL.name, (SELECT COUNT(*) FROM %s WHERE portalId = REPTILE_PORTAL.id) AS totalRecordCount FROM REPTILE_PORTAL";
					System.out.println(String.format(portalSql, "video"));
					all_portals_video = Helper.queryList(conn, String.format(portalSql, "video"));
					all_portals_audio = Helper.queryList(conn, String.format(portalSql, "audio"));
					all_portals_album = Helper.queryList(conn, String.format(portalSql, "album"));
					
					String catalogSql = "SELECT catalog.id, catalog.categoryName AS name, catalog.parentId, (SELECT COUNT(*) FROM %s WHERE catalog = catalog.id) AS totalRecordCount FROM TOPIC_CATEGORY_INFO catalog";
					
					System.out.println(String.format(catalogSql, "video"));
					all_catalogs_video = Helper.queryList(conn, String.format(catalogSql, "video"));
					all_catalogs_audio = Helper.queryList(conn, String.format(catalogSql, "audio"));
					all_catalogs_album = Helper.queryList(conn, String.format(catalogSql, "album"));
					
					cache.put("all_portals_video",	all_portals_video);
					cache.put("all_portals_audio",	all_portals_audio);
					cache.put("all_catalogs_video", all_catalogs_video);
					cache.put("all_catalogs_audio", all_catalogs_audio);
					cache.put("all_portals_album",  all_portals_album);
					cache.put("all_catalogs_album", all_catalogs_album);
					
					System.out.println("已设置缓存！");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		};
		
		Timer timer = new Timer();
		timer.schedule(task, 500, 10 * 6000);
	}

	/**
	 * 从缓存里面读取门户和分类数据
	 * 
	 * @param request
	 *            请求
	 * @param tableName
	 *            表名
	 */
	public static void initFilters(HttpServletRequest request, String tableName) {
		String all_portals_key  = "all_portals_"  + tableName, 
			   all_catalogs_key = "all_catalogs_" + tableName;
		
		List<Map<String, Object>> all_portals = cache.get(all_portals_key);
		// 门户
		request.setAttribute(all_portals_key, all_portals);
		request.setAttribute(all_portals_key + "_map", JspModel.list2map_id_as_key(all_portals));
	
		// 分类
		List<Map<String, Object>> all_catalogs = (List<Map<String, Object>>) cache.get(all_catalogs_key);
		request.setAttribute(all_catalogs_key, all_catalogs);
		request.setAttribute(all_catalogs_key + "_map", JspModel.list2map_id_as_key(all_catalogs));
	}
	
	public static void initFilters(Model model) {
		// 门户
		List<Map<String, Object>> all_portals = cache.get("all_portals_video");
		model.addAttribute("all_portals", all_portals);
		model.addAttribute("all_portals_map", JspModel.list2map_id_as_key(all_portals));
	
		// 分类
		List<Map<String, Object>> all_catalogs = (List<Map<String, Object>>) cache.get("all_catalogs_video");
		model.addAttribute("all_catalogs", all_catalogs);
		model.addAttribute("all_catalogs_map", JspModel.list2map_id_as_key(all_catalogs));
	}

}

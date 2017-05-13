package com.ajaxjs.web.config;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ajaxjs.js.JsonHelper;
import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.io.FileUtil;
import com.ajaxjs.util.map.MapData;
import com.ajaxjs.web.HtmlHead;

/**
 * 配置管理后台
 */
public class AdminController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/asset/jsp/config/config.jsp").forward(request, response);
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MapData mapHandler = new MapData();
		mapHandler.setParameterMapRaw(request.getParameterMap());
		
		Map<String, String> hash =  mapHandler.toMap().getParameterMap_String();
		
		JsonConfig updater = load(request, hash);

		// 可以json.str() 的 变量名
		String topVarName = hash.get("topVarName");
		if (StringUtil.isEmptyString(topVarName))
			throw new IllegalArgumentException("没有  topVarName 参数！");
		hash.remove("topVarName");

		updater.save(hash);

		try {
			String JSON_as_String = new JsonHelper(InitConfig.allConfig.getEngine()).stringify(topVarName);
			
			if (JSON_as_String != null) {
				String fileBody = topVarName + " = " + JSON_as_String + ";";
				// LOGGER.info(fileBody);
				// LOGGER.info("::::::::::::::::::2:"+jsonFileFullPath);
				// 保存文件
				new FileUtil().setFilePath(updater.getJsonPath()).setContent(fileBody).save();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("更新配置失败，不能序列化配置！");
		}
		
	}

	/**
	 * 解析 JSON 文件的磁盘绝对路径， 并加载 JSON 移除 jsonFile 项 返回磁盘绝对路径
	 * 
	 * @param request
	 * @param hash
	 * @return
	 */
	private JsonConfig load(HttpServletRequest request, Map<String, String> hash) {
		String jsonFile = hash.get("jsonFile"), jsonFileFullPath;

		if (StringUtil.isEmptyString(jsonFile))
			throw new NullPointerException("没有 json File 参数！");
		
		jsonFileFullPath = HtmlHead.Mappath(request, jsonFile);
		
		JsonConfig update = new JsonConfig(InitConfig.allConfig.getEngine());
		update.setJsonPath(jsonFileFullPath);
		update.load(jsonFileFullPath);
		
		hash.remove("jsonFile");// 不需要路径

		return update;
	}

}

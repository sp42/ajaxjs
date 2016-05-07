package com.ajaxjs.cms.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ajaxjs.util.LogHelper;
import com.ajaxjs.util.map.MapHelper;
import com.ajaxjs.web.Requester;
import com.ajaxjs.web.Responser;

@org.springframework.stereotype.Controller
@RequestMapping(value = "/cms/config")
public class ConfigController {
	private static final LogHelper LOGGER = LogHelper.getLog(ConfigController.class);
	
	private Service service = new Service();
	
	/**
	 * 系统配置界面
	 * 配置已经在内存中，所有无须重新读取。
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String doGet(HttpServletRequest request) {
		LOGGER.info("系统配置界面");
		return "common/misc/config";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		Requester requester = new Requester(request);
		Responser responser  = new Responser(response);
		
		String jsonFileFullPath = requester.Mappath(requester.getParameter("jsonFile")); // 解析 JSON 文件的磁盘绝对路径
		
		try{
			service.save(jsonFileFullPath, MapHelper.toMap(request.getParameterMap()));
			request.setAttribute("output", new Object(){  
				@SuppressWarnings("unused")
				public String msg = "修改成功！";
			});
		}catch(final Exception e){
			request.setAttribute("output", new Object(){  
				@SuppressWarnings("unused")
				public String msg = "修改失败！" + e.getMessage();
			});
		}
		
		responser.setRequest(request);
		responser.outputJSON();
	}
}

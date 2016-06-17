package com.egdtv.crawler.controller;


import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ajaxjs.framework.controller.AbstractController;
import com.ajaxjs.framework.exception.ServiceException;
import com.ajaxjs.json.Json;
import com.ajaxjs.util.LogHelper;
import com.ajaxjs.web.Responser;
import com.egdtv.crawler.Constant;
import com.egdtv.crawler.model.Video;
import com.egdtv.crawler.remote_call.IService;
import com.egdtv.crawler.service.Common;
import com.egdtv.crawler.service.VideoService;
 

@Controller
@RequestMapping(value = "/show_video")
public class VideoController extends AbstractController<Video> {
	private static final LogHelper LOGGER = LogHelper.getLog(VideoController.class);

	public VideoController() {
		setService(new VideoService());
		setEnableDefaultWrite(false);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String redirect() {
		return "redirect:show_video/list";
	}
	
	@RequestMapping(value = {"/list"}, method = RequestMethod.GET)
	@Override
	public String list(
			@RequestParam(value = "start", required = false, defaultValue = "0") int start,
			@RequestParam(value = "limit", required = false, defaultValue = "8") int limit,
			HttpServletRequest request, Model model
		) {
		super.list(start, limit, request, model);
		Common.initFilters(request, "video");
		
		return formatDectect(request, 0);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@Override
	public String getById(@PathVariable("id") long id, Model model, HttpServletRequest request) {
		super.getById(id, model, request);
		
		return formatDectect(request, 1);
	}
	
	@Override
	public void prepareData(HttpServletRequest request, Model model) {
		Common.prepareData(model);
	}
	
	/**
	 * 获取实时源
	 * @param id
	 * @param site
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/liveSourceById", method = RequestMethod.GET)
	public void getLiveSourceById (
		@RequestParam(value = "site", 	required = true) String site, 
		@RequestParam(value = "miscId", required = true) String miscId, 
		HttpServletResponse response
	) throws IOException {
		Map<String, String> returnSource = null;
		
		try {
			// 在RMI服务注册表中查找名称为RHello的对象，并调用其上的方法
			IService video = (IService) Naming.lookup(Constant.rmiService);
			String parameter = site + "#" + miscId;
			
			returnSource = video.getVideo(parameter);
		} catch (NotBoundException | MalformedURLException | RemoteException e) {
			LOGGER.warning(e);
		} 
		

		
		new Responser(response).outputJSON(Json.stringify(returnSource));
//		new Responser(response).outputJSON("{\"result\": \"" + returnSource + "\"}");
	}
	
	/**
	 * 获取实时源
	 * @param entityId
	 * @param site
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/{entityId}/liveSource", method = RequestMethod.GET)
	public void getLiveSource(
		@PathVariable("entityId") long entityId, 
		@RequestParam(value = "site", required = false) String site, 
		HttpServletResponse response
	) throws IOException {
		try {
			Video video = (Video)getService().getById(entityId);
			getLiveSourceById(site, video.getMisc(), response);
		} catch (ServiceException e) {
			LOGGER.warning(e);
		}
	}
}

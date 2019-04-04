package com.ajaxjs.cms.app.nativeapp;

import java.io.IOException;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.web.UploadFile;
import com.ajaxjs.web.UploadFileInfo;

@Bean
@Path("/admin/appUpdate")
public class AppUpdateAdminController extends BaseController<AppUpdate> {
	@Resource("AppUpdateService")
	private AppUpdateService service;
	
	@GET
	@Path("/list")
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam(start) int start, @QueryParam(limit) int limit, ModelAndView mv) {
		listPaged(start, limit, mv);
		return adminList();
	}

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("/{id}")
	@Override
	public String editUI(@PathParam("id") Long id, ModelAndView mv) {
		super.editUI(id, mv);
		return editUI();
	}
	
	@GET
	@Override
	public String createUI(ModelAndView mv) {
		super.createUI(mv);
		return editUI();
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(AppUpdate entity) {
		return super.create(entity);
	}

	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam("id") Long id, AppUpdate entity) {
		return super.update(id, entity);
	}

	@DELETE
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam("id") Long id) {
		return delete(id, new AppUpdate());
	}

	@POST
	@Path("/upload")
	@Produces(MediaType.APPLICATION_JSON)
	public String upload(MvcRequest request) throws IOException {
		UploadFileInfo uploadFileInfo = new UploadFileInfo();
		uploadFileInfo.maxSingleFileSize = 1024 * 50000; // 50 MB;
		uploadFileInfo.allowExtFilenames = new String[] { "apk" };
		uploadFileInfo.isFileOverwrite = true;
		uploadFileInfo.saveFolder = request.mappath("/app/");

		new UploadFile(request, uploadFileInfo).upload();

		uploadFileInfo.path = "/app/" + uploadFileInfo.saveFileName;
		uploadFileInfo.visitPath = request.getBasePath() + uploadFileInfo.path;

		return "err";
//		return String.format(json_ok_extension, "上传成功", "\"visitPath\" : \"" + uploadFileInfo.visitPath + "\"");
	}

	@Override
	public IBaseService<AppUpdate> getService() {
		return service;
	}
}

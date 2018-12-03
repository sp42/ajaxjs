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

import com.ajaxjs.cms.controller.CommonController;
import com.ajaxjs.cms.controller.CommonEntryAdminController;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.web.UploadFile;
import com.ajaxjs.web.UploadFileInfo;

@Path("/admin/appUpdate")
@Bean("AppUpdateAdminController")
public class AppUpdateAdminController extends CommonController<AppUpdate, Long> implements CommonEntryAdminController<AppUpdate, Long> {

	@Resource("AppUpdateService")
	private AppUpdateService service;

	@GET
	@Path("/list")
	@MvcFilter(filters = DataBaseFilter.class)
	@Override
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView model) {
		prepareData(model);
		model.put("PageResult", service.findPagedList(start, limit));
		return jsp_perfix + "/common-entity/app-update-list";
	}

	@GET
	@Override
	public String createUI(ModelAndView model) {
		super.createUI(model);
		return jsp_perfix + "/common-entity/app-update";
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	public String create(AppUpdate entity, ModelAndView model) {
		return create(entity, model, _entity -> service.create(_entity));
	}

	@DELETE
	@Path("/{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String delete(@PathParam("id") Long id, ModelAndView model) {
		return delete(id, new AppUpdate(), model, entity -> service.delete(entity));
	}

	@GET
	@Path("/{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	@Override
	public String editUI(@PathParam("id") Long id, ModelAndView model) {
//		info(id, model);
		super.editUI(model);
		return jsp_perfix + "/common-entity/app-update";
	}

	@PUT
	@Path("/{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam("id") Long id, AppUpdate app, ModelAndView model) {
		return update(id, app, model, _entity -> service.update(_entity));
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
}

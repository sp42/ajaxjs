package com.ajaxjs.app.attachment;

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
import com.ajaxjs.framework.CommonConstant;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.sql.orm.IBaseService;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.web.UploadFileInfo;

/**
 * 
 * 控制器
 */
@Bean
@Path("/admin/attachment")
public class AttachmentController extends BaseController<Attachment> {
	private static final LogHelper LOGGER = LogHelper.getLog(AttachmentController.class);

	@Resource("AttachmentService")
	private AttachmentService service;

	@GET
	@MvcFilter(filters = { DataBaseFilter.class })
	public String list(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, @QueryParam(CATALOG_ID) int catalogId,
			ModelAndView mv) {
		LOGGER.info("附件列表-前台");
		prepareData(mv);
		
		return page(mv, service.findPagedList(catalogId, start, limit, CommonConstant.ON_LINE, true));
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(Attachment entity) {
		return super.create(entity);
	}

	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam(ID) Long id, Attachment entity) {
		return super.update(id, entity);
	}

	@Override
	public IBaseService<Attachment> getService() {
		return service;
	}

	// TODO owenerId 判定是否本人，而不是其他人
	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("upload/{id}/")
	@Produces(MediaType.APPLICATION_JSON)
	public String upload(MvcRequest request, @PathParam(ID) Long owenerId, @QueryParam(CATALOG_ID) int catalogId)
			throws IOException {
		LOGGER.info("上传附件");
		// TODO 文件类型限制
		final UploadFileInfo info = uploadByConfig(request);

		if (info.isOk) {
			Attachment pic = new Attachment();
			pic.setOwner(owenerId);
			pic.setName(info.saveFileName);
			pic.setFileSize((int) (info.contentLength / 1024));

			if (catalogId != 0)
				pic.setCatalogId(catalogId);

			final Long _newlyId = service.create(pic);

			return "json::" + JsonHelper.toJson(new Object() {
				@SuppressWarnings("unused")
				public Boolean isOk = true;
				@SuppressWarnings("unused")
				public String msg = "上传成功！";
				@SuppressWarnings("unused")
				public String imgUrl = info.path.replaceFirst("^/", "");
				@SuppressWarnings("unused")
				public Long newlyId = _newlyId;
			});

		} else
			return jsonNoOk("上传失败！");
	}
	
	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("getListByOwnerUid/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getListByOwnerUid(@PathParam(ID) Long uid) {
		return toJson(service.findByOwner(uid));
	}

	@DELETE
	@Path(ID_INFO)
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(ID) Long id, MvcRequest request) {
		Attachment attachment = service.findById(id);
		// TODO 删除物理文件
//		attachment.setPath(request.mappath(attachment.getPath())); // 转换为绝对地址

		return delete(id, attachment);
	}
}

package com.ajaxjs.cms.app.attachment;

import java.io.IOException;
import java.util.Map;

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
import com.ajaxjs.util.io.ImageHelper;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.web.UploadFileInfo;

/**
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Bean
@Path("/admin/attachmentPicture")
public class Attachment_pictureController extends BaseController<Attachment_picture> {
	private static final LogHelper LOGGER = LogHelper.getLog(Attachment_pictureController.class);
	
	@Resource("Attachment_pictureService")
	private Attachment_pictureService service;

	public void setService(Attachment_pictureService service) {
		this.service = service;
	}

	@GET
	@Path(list)
	@MvcFilter(filters = { DataBaseFilter.class })
//	@Authority(filter = DataBaseFilter.class, value = 1)
	public String list(@QueryParam(start) int start, @QueryParam(limit) int limit, @QueryParam(catalogId) int catalogId, ModelAndView mv) {
		page(mv, service.findPagedList(start, limit, null));
		mv.put("DICT", Attachment_pictureService.DICT);

		return jsp("admin/" + service.getShortName() + "-list");
	}

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("getListByOwnerUid/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getListByOwnerUid(@PathParam(id) Long owenrUid) {
		return toJson(service.findByOwner(owenrUid));
	}

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("getAttachmentPictureByOwner/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAttachmentPictureByOwner(@PathParam(id) Long owenrUid) {
		return toJson(service.findAttachmentPictureByOwner(owenrUid));
	}

//	@POST
//	@MvcFilter(filters = DataBaseFilter.class)
//	@Path("/uploadMultiple/{id}/")
//	@Produces(MediaType.APPLICATION_JSON)
//	public String imgUpload(MvcRequest request, @PathParam(id) Long owenerId, @QueryParam("catelog") int catelogId, @FormParam("imgUrls") String imgUrls) throws IOException {
//		
//	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("upload/{id}/")
	@Produces(MediaType.APPLICATION_JSON)
	public String imgUpload(MvcRequest request, @PathParam(id) Long owenerId, @QueryParam(catalogId) int catalogId) throws IOException {
		LOGGER.info("上传图片");
		final UploadFileInfo info = uploadByConfig(request);

		if (info.isOk) {
			// 获取图片信息
			ImageHelper imgHelper = new ImageHelper(info.fullPath);

			Attachment_picture pic = new Attachment_picture();
			pic.setOwner(owenerId);
			pic.setName(info.saveFileName);
			pic.setPath(info.path);
			pic.setPicWidth(imgHelper.width);
			pic.setPicHeight(imgHelper.height);
			pic.setFileSize((int) (imgHelper.file.length() / 1024));

			if (catalogId != 0)
				pic.setCatalog(catalogId);

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

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("upload/staticPageUsedImg/")
	@Produces(MediaType.APPLICATION_JSON)
	public String imgUpload(MvcRequest request) throws IOException {
		final UploadFileInfo info = uploadByConfig(request);

		if (info.isOk) {
			// 获取图片信息
			ImageHelper imgHelper = new ImageHelper(info.fullPath);

			Attachment_picture picture = new Attachment_picture();
			picture.setName(info.saveFileName);
			picture.setPath(info.path);
			picture.setPicWidth(imgHelper.width);
			picture.setPicHeight(imgHelper.height);
			picture.setFileSize((int) (imgHelper.file.length() / 1024));
			picture.setCatalog(1);

			final Long _newlyId = service.create(picture);
			System.out.println(info.path);

			return "json::" + JsonHelper.toJson(new Object() {
				@SuppressWarnings("unused")
				public Boolean isOk = true;
				@SuppressWarnings("unused")
				public String msg = "上传成功！";
				@SuppressWarnings("unused")
				public String imgUrl = info.path;
				@SuppressWarnings("unused")
				public Long newlyId = _newlyId;
			});

		} else
			return jsonNoOk("上传失败！");
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("saveImgIndex")
	@Produces(MediaType.APPLICATION_JSON)
	public String saveImgIndex(Map<String, Object> map) {
		if (service.saveImgIndex(map)) {
			return jsonNoOk("修改图片索引成功！");
		}
		return jsonNoOk("修改图片索引失败！");
	}

	@GET
	@Override
	public String createUI(ModelAndView model) {
		return show405;
	}
	
	@POST
	@Override
	public String create(Attachment_picture entity) {
		return show405;
	}

	@PUT
	@Path(idInfo)
	@Override
	public String update(@PathParam(id) Long id, Attachment_picture entity) {
		return show405;
	}

	@DELETE
	@Path(idInfo)
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(id) Long id, MvcRequest request) {
		Attachment_picture pic = service.findById(id);
		pic.setPath(request.mappath(pic.getPath())); // 转换为绝对地址

		return delete(id, pic);
	}

	@Override
	public IBaseService<Attachment_picture> getService() {
		return service;
	}

}
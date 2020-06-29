package com.ajaxjs.app.attachment;

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
import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.object_storage.NosUploadFile;
import com.ajaxjs.sql.SnowflakeIdWorker;
import com.ajaxjs.sql.orm.IBaseService;
import com.ajaxjs.util.CommonUtil;
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
	@Path(LIST)
	@MvcFilter(filters = { DataBaseFilter.class })
//	@Authority(filter = DataBaseFilter.class, value = 1)
	public String list(@QueryParam(START) int start, @QueryParam(LIMIT) int limit,
			@QueryParam(CATALOG_ID) int catalogId, ModelAndView mv) {
		page(mv, service.findPagedList(start, limit, null));
		mv.put("DICT", Attachment_pictureService.DICT);

		return jsp("admin/" + service.getShortName() + "-list");
	}

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("getListByOwnerUid/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getListByOwnerUid(@PathParam(ID) Long owenrUid) {
		return toJson(service.findByOwner(owenrUid));
	}

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("getAttachmentPictureByOwner/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAttachmentPictureByOwner(@PathParam(ID) Long owenrUid) {
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
	public String imgUpload(MvcRequest request, @PathParam(ID) Long owenerId, @QueryParam(CATALOG_ID) int catalogId)
			throws IOException {
		LOGGER.info("上传图片");

		UploadFileInfo info = new UploadFileInfo();
		info.saveFileName = SnowflakeIdWorker.getId() + "";
		NosUploadFile u = new NosUploadFile(request, info);
		u.upload();
		info.fullPath = ConfigService.getValueAsString("uploadFile.imgPerfix") + info.saveFileName;

		if (info.isOk) {
			// 获取图片信息
			ImageHelper imgHelper = new ImageHelper(u.uploadBytes);

			Attachment_picture pic = new Attachment_picture();
			pic.setOwner(owenerId);
			pic.setName(info.saveFileName);
			pic.setPath(info.saveFileName);
			pic.setPicWidth(imgHelper.width);
			pic.setPicHeight(imgHelper.height);
			pic.setFileSize(u.uploadBytes.length);

			if (catalogId != 0)
				pic.setCatalogId(catalogId);

			final Long _newlyId = service.create(pic);

			return "json::" + JsonHelper.toJson(new Object() {
				@SuppressWarnings("unused")
				public Boolean isOk = true;
				@SuppressWarnings("unused")
				public String msg = "上传成功！";
				@SuppressWarnings("unused")
				public String imgUrl = info.saveFileName;
				@SuppressWarnings("unused")
				public String fullUrl = info.fullPath;
				@SuppressWarnings("unused")
				public Long newlyId = _newlyId;
			});

		} else
			return jsonNoOk("上传失败！");
	}

	public String imgUpload_Old(MvcRequest request, @PathParam(ID) Long owenerId, @QueryParam(CATALOG_ID) int catalogId)
			throws IOException {
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

	@POST
	@Path("upload/staticPageUsedImg/")
	@Produces(MediaType.APPLICATION_JSON)
	public String imgUpload(MvcRequest request, @QueryParam("url") String url) throws IOException {
		final UploadFileInfo info = uploadByConfig(request);

		url = url.replaceAll("^/|/$", "");
		int folderDeep = url.split("/").length;
		final String _imgUrl = CommonUtil.repeatStr("../", "", folderDeep) + info.path.replaceAll("^/", "");

		if (info.isOk) {
			// 获取图片信息
			return "json::" + JsonHelper.toJson(new Object() {
				@SuppressWarnings("unused")
				public Boolean isOk = true;
				@SuppressWarnings("unused")
				public String msg = "上传成功！";
				@SuppressWarnings("unused")
				public String imgUrl = _imgUrl;
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
		return SHOW_405;
	}

	@POST
	@Override
	public String create(Attachment_picture entity) {
		return SHOW_405;
	}

	@PUT
	@Path(ID_INFO)
	@Override
	public String update(@PathParam(ID) Long id, Attachment_picture entity) {
		return SHOW_405;
	}

	@DELETE
	@Path(ID_INFO)
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(ID) Long id, MvcRequest request) {
		Attachment_picture pic = service.findById(id);
		pic.setPath(request.mappath(pic.getPath())); // 转换为绝对地址

		return delete(id, pic);
	}

	@Override
	public IBaseService<Attachment_picture> getService() {
		return service;
	}

}

package com.ajaxjs.cms.app.attachment;

import java.io.IOException;
import java.util.HashMap;
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

import com.ajaxjs.cms.controller.CommonController;
import com.ajaxjs.cms.controller.CommonEntryAdminController;
import com.ajaxjs.cms.model.Attachment_picture;
import com.ajaxjs.cms.service.Attachment_pictureService;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.keyvalue.MappingJson;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.util.io.image.ImageUtil;
import com.ajaxjs.web.UploadFileInfo;

/**
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Path("/admin/attachmentPicture")
@Bean("Attachment_pictureController")
public class Attachment_pictureController extends CommonController<Attachment_picture, Long> implements CommonEntryAdminController<Attachment_picture, Long> {
	@Resource("Attachment_pictureService")
	private Attachment_pictureService service;

	static Map<Integer, String> catelogMap = new HashMap<>();
	static {
		catelogMap.put(0, "普通图片");
		catelogMap.put(1, "普通图片");
		catelogMap.put(2, "头像/封面图片");
		catelogMap.put(3, "相册图片");
	}

	@GET
	@Path("list")
	@MvcFilter(filters = DataBaseFilter.class)
	@Override
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView model) {
		model.put("imgRelativePath", ConfigService.getValueAsString("uploadFile" + ".relativePath"));

		model.put("catelogMap", catelogMap);
		list(start, limit, model, (_start, _limit) -> service.findPagedList(_start, _limit));

		return jsp_perfix + "/attachment/pic-list";
	}

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("getListByOwnerUid/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getListByOwnerUid(@PathParam("id") Long owenrUid, ModelAndView model) {
		return outputJson(service.findByOwner(owenrUid), model);
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("/upload/{id}/")
	@Produces(MediaType.APPLICATION_JSON)
	public String imgUpload(MvcRequest request, @PathParam("id") Long owenerId, @QueryParam("catelog") int catelogId) throws IOException, ServiceException {
		final UploadFileInfo info = uploadByConfig(request);

		if (info.isOk) {
			// 获取图片信息
			ImageUtil img = new ImageUtil().setFilePath(info.fullPath).getSize();

			Attachment_picture picture = new Attachment_picture();
			picture.setOwner(owenerId);
			picture.setName(info.saveFileName);
			picture.setPath(info.path);
			picture.setPicWidth(img.getWidth());
			picture.setPicHeight(img.getHeight());
			picture.setFileSize((int) (img.getFile().length() / 1024));

			if (catelogId != 0)
				picture.setCatelog(catelogId);

			final Long _newlyId = service.create(picture);

			return "json::" + MappingJson.stringifySimpleObject(new Object() {
				@SuppressWarnings("unused")
				public Boolean isOk = true;
				@SuppressWarnings("unused")
				public String msg = "上传成功！";
				@SuppressWarnings("unused")
				public String imgUrl = info.visitPath;
				@SuppressWarnings("unused")
				public Long newlyId = _newlyId;
			});

		} else
			return jsonNoOk("上传失败！");
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("/upload/staticPageUsedImg/")
	@Produces(MediaType.APPLICATION_JSON)
	public String imgUpload(MvcRequest request) throws IOException, ServiceException {
		final UploadFileInfo info = uploadByConfig(request);

		if (info.isOk) {
			// 获取图片信息
			ImageUtil img = new ImageUtil().setFilePath(info.fullPath).getSize();

			Attachment_picture picture = new Attachment_picture();
			picture.setName(info.saveFileName);
			picture.setPath(info.path);
			picture.setPicWidth(img.getWidth());
			picture.setPicHeight(img.getHeight());
			picture.setFileSize((int) (img.getFile().length() / 1024));
			picture.setCatelog(1);

			final Long _newlyId = service.create(picture);

			return "json::" + MappingJson.stringifySimpleObject(new Object() {
				@SuppressWarnings("unused")
				public Boolean isOk = true;
				@SuppressWarnings("unused")
				public String msg = "上传成功！";
				@SuppressWarnings("unused")
				public String imgUrl = info.visitPath;
				@SuppressWarnings("unused")
				public Long newlyId = _newlyId;
			});

		} else
			return jsonNoOk("上传失败！");
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("/saveImgIndex")
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

	@Override
	public String editUI(Long id, ModelAndView model) {
		return show405;
	}

	@POST
	@Override
	public String create(Attachment_picture entity, ModelAndView model) {
		return show405;
	}

	@PUT
	@Path("{id}")
	@Override
	public String update(@PathParam("id") Long id, Attachment_picture entity, ModelAndView model) {
		return show405;
	}

	@DELETE
	@Path("{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam("id") Long id, ModelAndView model, MvcRequest request) {
		Attachment_picture pic = service.findById(id);
		pic.setPath(request.mappath(pic.getPath())); // 转换为绝对地址

		return delete(id, pic, model, _id -> service.delete(_id));
	}

	@Override
	public String delete(Long id, ModelAndView model) {
		return null;
	}
}

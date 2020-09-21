package com.ajaxjs.app;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

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
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.CommonConstant;
import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.sql.SnowflakeIdWorker;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.orm.IBaseService;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.util.Encode;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.ioc.ComponentMgr;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.web.UploadFileInfo;
import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.controller.MvcRequest;
import com.ajaxjs.web.mvc.filter.MvcFilter;

/**
 * 
 * 控制器
 */
@Component
@Path("/admin/attachment")
public class AttachmentController extends BaseController<Attachment> {
	private static final LogHelper LOGGER = LogHelper.getLog(AttachmentController.class);

	@TableName(value = "attachment", beanClass = Attachment.class)
	public static interface AttachmentDao extends IBaseDao<Attachment> {
	}

	public static AttachmentDao dao = new Repository().bind(AttachmentDao.class);

	public static class AttachmentService extends BaseService<Attachment> {
		{
			setUiName("通用附件");
			setShortName("attachment");
			setDao(dao);
		}

		/**
		 * 根据实体 uid 找到其所拥有的图片
		 * 
		 * @param owner 实体 uid
		 * @return 图片列表
		 */
		public List<Attachment> findByOwner(Long uid) {
			return dao.findList(by("owner", uid));
		}
	}

	private AttachmentService service = new AttachmentService();

	@GET
	@MvcFilter(filters = { DataBaseFilter.class })
	public String list(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, @QueryParam(CATALOG_ID) int catalogId, ModelAndView mv) {
		LOGGER.info("附件列表-前台");
		return output(mv, service.findPagedList(catalogId, start, limit, CommonConstant.ON_LINE, true), "jsp::app/attachment-admin-list");
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
	@SuppressWarnings("unused")
	public String upload(MvcRequest req, @PathParam(ID) Long owenerId, @QueryParam(CATALOG_ID) int catalogId) throws IOException {
		LOGGER.info("上传附件");

		UploadFileInfo info = new UploadFileInfo();
		// 约束上传
		info.maxSingleFileSize = 1024 * 10000; // 10m
		info.allowExtFilenames = null;

		ThirdPartyService storageService = ComponentMgr.get(ThirdPartyService.class);
		storageService.uploadFile(req, info);

		final Long _newlyId;
		if (info.isOk) {

			info.fullPath = ConfigService.get("uploadFile.imgPerfix") + info.saveFileName;

			Attachment pic = new Attachment();
			pic.setOwner(owenerId);
			pic.setName(info.saveFileName);
			pic.setFileSize((int) (info.contentLength / 1024));

			if (catalogId != 0)
				pic.setCatalogId(catalogId);

			_newlyId = service.create(pic);
		} else
			_newlyId = 0L;

		return info.isOk ? toJson(new Object() {
			public Boolean isOk = true;
			public String msg = "上传附件成功！";
			public String imgUrl = info.saveFileName;
			public String fullUrl = info.fullPath;
			public Long newlyId = _newlyId;
		}) : jsonNoOk("上传失败！");
	}

	public String classUpload(MvcRequest request, @PathParam(ID) Long owenerId, @QueryParam(CATALOG_ID) int catalogId) throws IOException {
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

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("imgUpload")
	@Produces(MediaType.APPLICATION_JSON)
	public String imgUpload(MvcRequest request, @PathParam(ID) long owenerId) {
		LOGGER.info("上传图片");

		UploadFileInfo info = new UploadFileInfo();
		info.saveFileName = SnowflakeIdWorker.getId() + "";
		
		// TODO 删除原图片

		ThirdPartyService storageService = ComponentMgr.get(ThirdPartyService.class);
		storageService.uploadFile(request, info);
		info.fullPath = ConfigService.get("uploadFile.imgPerfix") + info.saveFileName;

		return "json::" + JsonHelper.toJson(new Object() {
			@SuppressWarnings("unused")
			public Boolean isOk = true;
			@SuppressWarnings("unused")
			public String msg = "上传成功！";
			@SuppressWarnings("unused")
			public String imgUrl = info.saveFileName;
			@SuppressWarnings("unused")
			public String fullUrl = info.fullPath;
		});
	}

	@DELETE
	@Path(ID_INFO)
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(ID) Long id, MvcRequest request) {
		Attachment attachment = service.findById(id);

		// 删除物理文件
		String objectKey = attachment.getName();
		ThirdPartyService storageService = ComponentMgr.get(ThirdPartyService.class);
		storageService.deleteFile(objectKey);

		return delete(id, attachment);
	}

	/**
	 * 调用文件上传
	 * 
	 * @param req
	 * @param saveFilename
	 * @param info
	 * @param filename
	 * @return 返回 JSON 结果
	 */
	@SuppressWarnings("unused")
	public static String upload(MvcRequest req, Consumer<String> saveFilename, UploadFileInfo info, String filename) {
		info.saveFileName = Encode.urlEncode(filename); // 适应 OSS 的上传路径，文件夹路径要转移 / -> 2%F

		ThirdPartyService storageService = ComponentMgr.get(ThirdPartyService.class);
		storageService.uploadFile(req, info);

		filename = Encode.urlDecode(info.saveFileName);
		info.fullPath = ConfigService.get("uploadFile.imgPerfix") + filename;

		if (info.isOk && saveFilename != null)
			saveFilename.accept(filename);

		return info.isOk ? toJson(new Object() {
			public Boolean isOk = true;
			public String msg = "上传成功！";
			public String imgUrl = Encode.urlDecode(info.saveFileName);
			public String fullUrl = info.fullPath;
		}) : jsonNoOk("上传失败！");
	}
}

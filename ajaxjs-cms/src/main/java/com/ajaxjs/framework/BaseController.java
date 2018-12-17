package com.ajaxjs.framework;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import com.ajaxjs.keyvalue.MappingHelper;
import com.ajaxjs.mvc.Constant;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.util.logger.LogHelper;

public abstract class BaseController<T extends IBaseBean> implements IController, Constant {
	private static final LogHelper LOGGER = LogHelper.getLog(BaseController.class);

	public abstract IBaseService<T> getService();
	
	private IBaseService<T> service;

	/**
	 * 指向新建记录的页面
	 * 
	 * @param model 页面 Model 模型
	 * @return 新建记录 UI JSP 模版路径
	 */
	public String createUI(ModelAndView mv) {
		return ui(mv, true, "新建");
	}

	/**
	 * 指向编辑记录的页面
	 * 
	 * @param mv 页面 Model 模型
	 * @return 编辑记录 UI JSP 模版路径
	 */
	public String editUI(Long id, ModelAndView mv) {
//		info(id, model, _id -> getService().findById(_id));
		return ui(mv, false, "修改");
	}

	private String ui(ModelAndView mv, boolean isCreate, String text) {
		LOGGER.info(text + "记录 UI");

		prepareData(mv);
		mv.put("isCreate", isCreate); // 因为新建/编辑（update）为同一套 jsp 模版，所以用 isCreate = true 标识为创建，以便与 update 区分开来。
		mv.put("actionName", text);

		return editUI();
	}

	/**
	 * 创建实体
	 * 
	 * @param entity 实体
	 * @return JSON 响应
	 */
	public String create(T entity, Function<T, Long> createAction) {
		LOGGER.info("创建 name:{0}，数据库将执行 INSERT 操作", entity);

		Long newlyId = createAction.apply(entity);
		if (newlyId == null)
			throw new Error("创建失败！");

		return String.format(MappingHelper.json_ok_extension, "创建实体成功", "\"newlyId\":" + newlyId);
	}

	public String create(T entry) {
		return create(entry, e -> service.create(entry));
	}

	/**
	 * 修改实体
	 * 
	 * @param id 实体 Long
	 * @param entity 实体
	 * @return JSON 响应
	 */
	public String update(Long id, /* @Valid */T entity, Consumer<T> updateAction) {
		LOGGER.info("修改 name:{0}，数据库将执行 UPDATE 操作", entity);
		entity.setId(id);
		updateAction.accept(entity);

		return jsonOk("修改成功");
	}

	public String update(Long id, T entity) {
		return update(id, entity, e -> getService().update(e));
	}

	/**
	 * 因为范型的缘故，不能实例化 bean 对象。应该在子类实例化 bean，再调用本类的 delete()
	 * 
	 * @param entity 实体
	 * @param model 页面 Model 模型
	 * @return JSON 响应
	 */
	public String delete(T entity, Predicate<T> deleteAction) {
		LOGGER.info("删除 id:{0}，数据库将执行 DELETE 操作", entity);

		if (!deleteAction.test(entity))
			throw new Error("删除失败！");

		return jsonOk("删除成功");
	}

	/**
	 * 根据 id 删除实体
	 * 
	 * @param id 实体 id
	 * @param model 页面 Model 模型
	 * @return JSON 响应
	 */
	public String delete(Long id, T entity, Predicate<T> deleteAction) {
		entity.setId(id);
		return delete(entity, deleteAction);
	}

	public String delete(Long id, T entity) {
		return delete(id, entity, e -> getService().delete(e));
	}

	/**
	 * 可覆盖的模版方法，用于装备其他数据，如分类这些外联的表。
	 * 
	 * @param mv 模型
	 */
	public void prepareData(ModelAndView mv) {
		mv.put("uiName", getService().getUiName());
		mv.put("shortName", getService().getShortName());
		mv.put("tableName", getService().getTableName());
	}

	public static String jsonOk(String msg) {
		return MappingHelper.jsonOk(msg);
	}

	public static String jsonNoOk(String msg) {
		return MappingHelper.jsonNoOk(msg);
	}

	public String adminList_CMS() {
		return String.format(jsp_perfix + "/common-entity/%s-list", getService().getShortName());
	}

	public String adminList() {
		return String.format(jsp_perfix_webinf + "/entry/%s-admin-list", getService().getShortName());
	}

	public String infoUI_CMS() {
		return jsp_perfix + "/common-entity/" + getService().getShortName();
	}

	public String editUI() {
		return String.format(jsp_perfix_webinf + "/entry/%s-admin", getService().getShortName());
	}

	public String editUI_CMS() {
		return String.format(jsp_perfix_webinf + "/entry/%s-admin", getService().getShortName());
	}
}

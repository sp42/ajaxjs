package com.ajaxjs.app.catalog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.util.CommonUtil;

@Bean("CatalogService")
public class CatalogService extends BaseService<Catalog> {
	public static CatalogDao dao = new Repository().bind(CatalogDao.class);
 
	/**
	 * IN 查询用，多用于分页统计总数
	 * 用于 catelogId 查询的，通常放在 LEFT JOIN 后面还需要，WHERE e.catelog = c.id。 还需要预留一个
	 * catelogId 的参数 另外也可以用 IN 查询
	 */
	public final static String CATALOG_FIND = "e.catalogId IN ( " + CatalogDao.PATH_LIKE_MYSQL_ID + ")";

	{
		setUiName("分类");
		setShortName("catalog");
		setDao(dao);
	}

	@Override
	public PageResult<Catalog> findPagedList(int start, int limit) {
		/*
		 * 父id 必须在子id之前，不然下面 findParent() 找不到后面的父节点，故先排序. 前端排序的话 chrom 有稳定排序的问题，故放在后端排序
		 */
		return dao.findPagedList(start, limit, orderBy("pid"));
	}

	/**
	 * 根据父 id 获取下一层的子分类列表，获取直接一层的分类。
	 * 
	 * @param pid 父 id
	 * @return 子分类列表
	 */
	public List<Catalog> findByParentId(int pid) {
		return dao.findList(by("pid", pid));
	}

	/**
	 * 根据父 id 获取所有的子id列表（可以不包含父节点），不管多少层
	 * 
	 * @param pid        父 id
	 * @param withParent 是否需要连同父节点一起返回
	 * @return 所有的子 id 列表
	 */
	public List<Catalog> findAllListByParentId(int pid, boolean withParent) {
		List<Catalog> list = dao.getAllListByParentId(pid);

		if (!withParent && list != null) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getId() == new Integer(pid).longValue()) {
					list.remove(i);// 不要父节点
					break;
				}
			}
		}

		return list;
	}

	/**
	 * 根据父 id 获取所有的子id列表（包含父节点），不管多少层
	 * 
	 * @param pId 父 id
	 * @return 所有的子 id 列表
	 */
	public List<Catalog> findAllListByParentId(int pId) {
		return findAllListByParentId(pId, true);
	}

	/**
	 * 获取下一级和下下一级，一共只获取这两级
	 * 
	 * @param pId 父 id
	 * @return
	 */
	public List<Map<String, Object>> findListAndSubByParentId(int pId) {
		return dao.getListAndSubByParentId(pId);
	}

	@Override
	public Long create(Catalog bean) {
		if (bean.getPid() != -1) { // 非根节点
			Catalog parent = findById(bean.getPid().longValue()); // 保存路径信息

			String path = "";

			if (parent.getPid() != -1) 
				path += parent.getPath();

			path += "/" + parent.getId() + "/";
			bean.setPath(path);
		}

		Long newlyId = super.create(bean);

		if (newlyId != null) { // 需要创建了之后才有自己的 id
			Catalog updatePath = new Catalog();
			updatePath.setId(bean.getId());
			updatePath.setPath((bean.getPid() == -1 ? "/" : bean.getPath()) + bean.getId());

			update(updatePath);
		}

		return newlyId;
	}

	@Override
	public boolean delete(Catalog bean) {
		return dao.deleteAll(bean.getId().intValue());
	}

	@Override
	public String getTableName() {
		return "general_catalog";
	}

	/**
	 * 直接查询 catalogId，不进行递归
	 * 
	 * @param catalogId
	 * @return
	 */
	public static Function<String, String> byCatalogId(int catalogId) {
		return by("catalogId", catalogId);
	}

	/**
	 * 
	 * @param catalogId
	 * @param service
	 * @return
	 */
	public static Function<String, String> setCatalog(int catalogId, int domainCatalogId) {
		if (catalogId == 0)
			catalogId = domainCatalogId;

		return setCatalog(catalogId);
	}

	public static Function<String, String> setCatalog(int catalogId) {
		return setWhere(catalogId == 0 ? null : String.format(CATALOG_FIND, catalogId));
	}

	public static Function<String, String> setCatalog() {
		Object v = getValue("catalogId", int.class);

		return v == null ? setWhere(null) : setCatalog((int) v);
	}

	/**
	 * 把列表（BaseModel 结构）转换为 map，以 id 作为键值。key 本来是 long，为照顾 el 转换为 int
	 * 
	 * @param bean 实体列表
	 * @return 以 id 作为键值的 map
	 */
	public static Map<Long, BaseModel> list_bean2map_id_as_key(List<? extends BaseModel> bean) {
		if (CommonUtil.isNull(bean))
			return null;

		Map<Long, BaseModel> map = new HashMap<>();

		for (BaseModel item : bean) {
//			map.put(new Long(item.getId()).intValue(), item);
			map.put(item.getId(), item);
		}

		return map;
	}

	/**
	 * 把列表（Map结构）转换为 map，以 id 作为键值。key 本来是 long，为照顾 el 转换为 int
	 * 
	 * @param list 实体列表
	 * @return 以 id 作为键值的 map
	 */
	public static Map<Integer, Object> list2map_id_as_key(List<Map<String, Object>> list) {
		if (CommonUtil.isNull(list))
			return null;

		Map<Integer, Object> map = new HashMap<>();

		for (Map<String, Object> item : list)
			map.put(new Long(item.get("id").toString()).intValue(), item);

		return map;
	}

}
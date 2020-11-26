package com.ajaxjs.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.sql.annotation.Delete;
import com.ajaxjs.sql.annotation.Select;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.ioc.Component;

@Component
public class TreeLikeService extends BaseService<Catalog> {
	@TableName(value = "general_catalog", beanClass = Catalog.class)
	public static interface TreeLikeoDao extends IBaseDao<Catalog> {
		/**
		 * 左连接分类表，实体简写必须为 e
		 */
		public final static String LEFT_JOIN_CATALOG = " LEFT JOIN general_catalog gc ON gc.id = e.catalogId ";

		/**
		 * 关联分类表以获取分类名称，即增加了 catalogName 字段。另外如果前台不需要显示的话，只是后台的话，可以用 map 显示
		 */
		public final static String SELECT_CATALOGNAME = "SELECT e.*, gc.name catalogName FROM ${tableName} e" + LEFT_JOIN_CATALOG + WHERE_REMARK_ORDER;

		/**
		 * 获取 pid 下面的所有子节点，无论下面有多少级
		 * 
		 * @param pid
		 * @return
		 */
		@Select("SELECT * FROM general_catalog WHERE `path` LIKE (CONCAT ((SELECT `path` FROM general_catalog WHERE id = ?) , '/%'))")
		List<Catalog> getAllChildren(int pid);

		/**
		 * 删除所有，包括子分类 如果子查询的 from 子句和更新、删除对象使用同一张表，会出现错误。
		 * 
		 * @param id
		 * @return
		 */
		@Delete("DELETE FROM ${tableName} WHERE id in ( SELECT n.id FROM ("
				+ "(SELECT id FROM ${tableName} WHERE `path` LIKE ( CONCAT ( (SELECT `path` FROM general_catalog WHERE id = ?) , '%')))) AS n)")
		public boolean deleteAll(int id);
	}

	public static TreeLikeoDao dao = new Repository().bind(TreeLikeoDao.class);

	{
		setUiName("分类");
		setShortName("catalog");
		setDao(dao);
	}

	/**
	 * 获取 pid 下面直接一级的子节点，
	 * 
	 * @param pid
	 * @return
	 */
	public List<Catalog> getDirectChildren(int pid) {
		return dao.findList(by("pid", pid));
	}

	/**
	 * 获取全部的分类
	 * 
	 * @return
	 */
	public List<Catalog> getAllChildren() {
		return dao.findList(null);
	}

	/**
	 * 获取 pid 下面的所有子节点，无论下面有多少级
	 * 
	 * @param pid
	 * @return
	 */
	public List<Catalog> getAllChildren(int pid) {
		return dao.getAllChildren(pid);
	}

	/**
	 * 转换为适合前端显示的 map
	 * 
	 * @param pid
	 * @return
	 */
	public Map<Long, BaseModel> getAllChildrenAsMap(int pid) {
		return idAskey(getAllChildren(pid));
	}

	/**
	 * 把列表（BaseModel 结构）转换为 map，以 id 作为键值。key 本来是 long，为照顾 el 转换为 int
	 * 
	 * @param bean 实体列表
	 * @return 以 id 作为键值的 map
	 */
	public static Map<Long, BaseModel> idAskey(List<? extends BaseModel> list) {
		if (CommonUtil.isNull(list))
			return null;

		Map<Long, BaseModel> map = new HashMap<>();
		list.forEach(item -> map.put(item.getId(), item));

		return map;
	}

	/**
	 * 把列表（Map结构）转换为 map，以 id 作为键值。key 本来是 long，为照顾 el 转换为 int
	 * 
	 * @param list 实体列表
	 * @return 以 id 作为键值的 map
	 */
	public static Map<Integer, Object> idAsKey(List<Map<String, Object>> list) {
		if (CommonUtil.isNull(list))
			return null;

		Map<Integer, Object> map = new HashMap<>();
		list.forEach(item -> map.put(new Integer(item.get("id").toString()), item));

		return map;
	}

	@Override
	public Long create(Catalog bean) {
		if (bean.getPid() != -1) { // 非根节点
			Catalog parent = findById(bean.getPid().longValue()); // 保存路径信息

			String path = "";

			// if (parent.getPid() != -1)
			path += parent.getPath();

			path += "/";
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

	/**
	 * 供其它实体关联时候用，可以获取下级所有子分类
	 */
	public final static String PATH_LIKE_MYSQL_ID = "SELECT id FROM general_catalog WHERE `path` LIKE ( CONCAT (( SELECT `path` FROM general_catalog WHERE id = %d ) , '%%'))";

	/**
	 * IN 查询用，多用于分页统计总数 用于 catelogId 查询的，通常放在 LEFT JOIN 后面还需要，WHERE e.catelog =
	 * c.id。 还需要预留一个 catelogId 的参数 另外也可以用 IN 查询
	 */
	public final static String CATALOG_FIND = "e.catalogId IN ( " + PATH_LIKE_MYSQL_ID + ")";

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

}

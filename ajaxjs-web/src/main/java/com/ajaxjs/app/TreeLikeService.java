package com.ajaxjs.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ajaxjs.app.catalog.Catalog;
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
	public interface TreeLikeoDao extends IBaseDao<Catalog> {
		/**
		 * 获取 pid 下面的所有子节点，无论下面有多少级
		 * 
		 * @param pid
		 * @return
		 */
		@Select("SELECT * FROM general_catalog WHERE `path` LIKE (CONCAT ((SELECT `path` FROM general_catalog WHERE id = ?) , '%'))")
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
}

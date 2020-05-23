package com.ajaxjs.app.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.util.CommonUtil;

/**
 * 栏目
 * 
 * 对于可以放到栏目的实体，要求这些字段：id、name、cover，并且在 entityProfile 有登记注册类型的实体
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Bean
public class ColumnService extends BaseService<Column> {

	@TableName(value = "column_list", beanClass = Column.class)
	public interface ColumnDao extends IBaseDao<Column> {
		@Select("SELECT id FROM ${tableName} WHERE entityUid = ? AND typeId = ?")
		public boolean checkIfExist(long entityUid, int typeId);

		@Select("SELECT id, uid, name, cover FROM #tableName# WHERE uid in (?)")
		public List<Map<String, Object>> queryAnyTable(Function<String, String> sqlHandler);
	}

	public static ColumnDao dao = new Repository().bind(ColumnDao.class);

	{
		setDao(dao);
	}

	public static class Section {
		private List<Long> uids = new ArrayList<>();
		// 该实体的表名
		private String tableName;

		public Section(int typeId) {
			switch (typeId) {
			case 1:
				tableName = "entity_article";
				break;
			case 2:
				tableName = "shop_goods";
				break;
			case 3:
				tableName = "entity_article";
				break;
			case 4:
				tableName = "entity_article";
				break;
			}
		}

		public List<Long> getUids() {
			return uids;
		}

		public void setUids(List<Long> uids) {
			this.uids = uids;
		}

		public List<Map<String, Object>> query() {
			String _uids = "";
			for (Long uid : uids)
				_uids += uid + ",";

			String uidsSql = _uids.substring(0, _uids.length() - 1);
			List<Map<String, Object>> list = dao
					.queryAnyTable(sql -> sql.replace("#tableName#", tableName).replace("?", uidsSql));

			return list;
		}
	}

	/**
	 * 
	 * @author sp42 frank@ajaxjs.com
	 *
	 */
	static class ColumnData extends HashMap<Integer, Section> {
		private static final long serialVersionUID = 1164227271752733138L;

		/**
		 * 
		 * @param cols 本身的栏目数据
		 */
		public ColumnData(List<Column> cols) {
			this.cols = cols;
		}

		// 本身的栏目数据
		private List<Column> cols;

		// 从各个表查询回来的数据组合在一起
		private List<Map<String, Object>> union = new ArrayList<>();

		private void getUids() {
			for (Column col : cols) {
				Integer typeId = col.getTypeId();
				Section section;

				if (!containsKey(typeId)) {
					section = new Section(typeId);
					put(typeId, section);
				} else {
					section = get(typeId);
				}

				section.getUids().add(col.getEntityUid());
			}
		}

		private Map<String, Object> findByUid(long uid) {
			for (Map<String, Object> entity : union) {
				if (uid == (long) entity.get("uid"))
					return entity;
			}

			return null;
		}

		public List<Column> getListByCatalogId() {
			getUids();

			for (Integer typeId : keySet()) {
				union.addAll(get(typeId).query());
			}

			if (union.size() != cols.size())
				throw new RuntimeException("栏目记录数与实体总数不匹配！");

			for (Column col : cols) {
				Map<String, Object> entity = findByUid(col.getEntityUid());
				Object id = entity.get("id");
				col.setEntityId((long) id);

				if (CommonUtil.isEmptyString(col.getName()))
					col.setName(entity.get("name").toString());

				if (CommonUtil.isEmptyString(col.getCover()))
					col.setCover(entity.get("cover").toString());
			}

			return cols;
		}
	}

	/**
	 * 获取中间表中匹配的数据
	 * 
	 * @param catalogId 类别 id
	 * @return 栏目的实体集合
	 */
	public List<Column> getListByCatalogId(int catalogId) {
		ColumnData map = new ColumnData(dao.findList(by("catalogId", catalogId)));
		return map.getListByCatalogId();
	}

	/**
	 * 获取中间表中匹配的数据，可分页的
	 * 
	 * @param start     分页 start
	 * @param limit     分页 limit
	 * @param catalogId 类别 id
	 * @return 可分页的栏目的实体集合
	 */
	public PageResult<Column> getListByCatalogId(int start, int limit, int catalogId) {
		return dao.findPagedList(start, limit, catalogId == 0 ? null : by("catalogId", catalogId));
	}

}

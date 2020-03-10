package com.ajaxjs.cms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.util.CommonUtil;

/**
 * 涉及不同的业务表，所以需要在子类继承该基类。 虽然有多个不同的页面，但它们的表结构是相同的，都是多对多关系。故放在 section_list
 * 表里面保存，这张表可能很长。不同类型的业务通过 catalogId 区分，例如收藏的、栏目的。
 * section 是支持混合类型的，多个不同类型的实体可以在同一个业务中（即 catalogId），他们通过 entityTypeId 实体类型 id 区分。
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public abstract class SectionListService extends BaseService<SectionList> {

	@TableName(value = "section_list", beanClass = SectionList.class)
	public interface SectionListDao extends IBaseDao<SectionList> {
		@Select("SELECT id FROM ${tableName} WHERE entryId = ? AND entryTypeId = ? AND userId = ?")
		public Long checkIfExist(long entryId, int entryType, long userId);

		@Select("")
		public List<SectionList> unionAll(Function<String, String> doSql);
	}

	public static SectionListDao dao = new Repository().bind(SectionListDao.class);

	{
		setDao(dao);
	}

	/**
	 * 获取中间表中匹配的数据
	 * 
	 * @param catalogId 类别 id
	 * @return 栏目的实体集合
	 */
	public List<SectionList> getListByCatalogId(int catalogId) {
		return dao.findList(by("catelogId", catalogId));
	}

	/**
	 * 获取中间表中匹配的数据，可分页的
	 * 
	 * @param start
	 * @param limit
	 * @param catalogId 类别 id
	 * @return 可分页的栏目的实体集合
	 */
	public PageResult<SectionList> getListByCatalogId(int start, int limit, int catalogId) {
		return dao.findPagedList(start, limit, catalogId == 0 ? null : by("catelogId", catalogId));
	}

	public int getDomainCatalogId() {
		return ConfigService.getValueAsInt("data.adsCatalog_Id");
	}

	/**
	 * 到具体的实体表中取出 id、名称的信息。
	 */
	public static final String SELECT = "SELECT entry.id AS entryId, entry.name,  %s AS entryTypeId, %s FROM %s entry WHERE id in (%s)\n";

	@FunctionalInterface
	public static interface ScanTable {
		/**
		 * 
		 * @param sqls
		 * @param entityTypeId 实体类型 id
		 * @param entityIds    实体 id
		 * @param caseSql
		 */
		public void addSql(List<String> sqls, int entityTypeId, String entityIds, String caseSql);
	}

	/**
	 * 
	 * @param list 这个 List 是原始的 SectionList 表得到的数据
	 * @param fn
	 * @return
	 */
	public List<SectionList> union(List<SectionList> list, ScanTable fn) {
		if (CommonUtil.isNull(list))
			return null;

		Map<Integer, List<String>> map = new HashMap<>();

		// 按照实体类型不同进行分组
		list.forEach(b -> {
			int entryTypeId = b.getEntryTypeId();

			if (!map.containsKey(entryTypeId))
				map.put(entryTypeId, new ArrayList<>());

			// 保存相关信息，这是一个 List 集合
			map.get(entryTypeId).add(b.getEntryId() + "_" + b.getId() + "_" + b.getUserId() + "_" + b.getCreateDate());
		});

		List<String> sqls = new ArrayList<>();
		String sqlCase = " WHEN entry.id = %s THEN %s ", sqlCaseStr = " WHEN entry.id = %s THEN '%s' ";

		for (Integer entityTypeId : map.keySet()) {
			List<String> l = map.get(entityTypeId);
			List<String> entityIds = new ArrayList<>(), cases = new ArrayList<>(), userIds = new ArrayList<>(),
					dates = new ArrayList<>();

			for (String i : l) {
				String[] arr = i.split("_");
				String entityId = arr[0], sectionId = arr[1], userId = arr[2], date = arr[3];

				entityIds.add(entityId);
				cases.add(String.format(sqlCase, entityId, sectionId));
				userIds.add(String.format(sqlCase, entityId, userId));
				dates.add(String.format(sqlCaseStr, entityId, date));
			}

			String caseSql = String.format("(CASE %s END) AS id", String.join(" ", cases));
			caseSql += String.format(", (CASE %s END) AS userId", String.join(" ", userIds));
			caseSql += String.format(", (CASE %s END) AS createDate", String.join(" ", dates));

			fn.addSql(sqls, entityTypeId, String.join(", ", entityIds), caseSql);
		}

//			System.out.println(String.join("UNION \n", sqls));
		List<SectionList> bs = dao.unionAll(sql -> String.join("UNION \n", sqls));

		if (list instanceof PageResult) {
			// 分页要处理一下
			PageResult<SectionList> p = (PageResult<SectionList>) list;
			p.clear();
			p.addAll(bs);

			return (List<SectionList>) p;
		}

		return bs;
	}

	/**
	 *
	 * 
	 * @param sectionId
	 * @param fn
	 * @return
	 */
	public List<SectionList> findSectionListBySectionId(int sectionId, ScanTable fn) {
		return union(getListByCatalogId(sectionId), fn);
	}

	/**
	 * 可分页的
	 * 
	 * @param start
	 * @param limit
	 * @param sectionId
	 * @return
	 */
	public PageResult<SectionList> findSectionListBySectionId(int start, int limit, int sectionId, ScanTable fn) {
		return (PageResult<SectionList>) union(getListByCatalogId(start, limit, sectionId), fn);
	}
}
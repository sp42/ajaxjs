package com.ajaxjs.app.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.ajaxjs.app.ArticleService;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.shop.service.GoodsService;
import com.ajaxjs.util.CommonUtil;

/**
 * 涉及不同的业务表，所以需要在子类继承该基类。 虽然有多个不同的页面，但它们的表结构是相同的，都是多对多关系。故放在 section_list
 * 表里面保存，这张表可能很长。不同类型的业务通过 catalogId 区分，例如收藏的、栏目的。 section
 * 是支持混合类型的，多个不同类型的实体可以在同一个业务中（即 catalogId），他们通过 entityTypeId 实体类型 id 区分。
 * 
 * 工作流：1、先在 section-list 里面查找栏目元素。
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
	 * @param start     分页 start
	 * @param limit     分页 limit
	 * @param catalogId 类别 id
	 * @return 可分页的栏目的实体集合
	 */
	public PageResult<SectionList> getListByCatalogId(int start, int limit, int catalogId) {
		return dao.findPagedList(start, limit, catalogId == 0 ? null : by("catalogId", catalogId));
	}

	/**
	 * 到具体的实体表中取出 id、名称的信息。
	 */
	public static final String SELECT = "SELECT e.id AS entryId, e.name,  %s AS entryTypeId, %s FROM %s e WHERE id in (%s)\n";

	@FunctionalInterface
	public static interface ScanTable {
		/**
		 * 
		 * @param sqls         收集各种类型实体要查询的 SQL 语句
		 * @param entityTypeId 实体类型 id
		 * @param entityIds    匹配的那些实体 id，接着要查询这些实体的详情
		 * @param caseSql
		 */
		public void addSql(List<String> sqls, int entityTypeId, String entityIds, String caseSql);
	}
	
	
	abstract ScanTable getScanTable();

	/**
	 * 
	 * @param list 原始的 SectionList 表得到的数据
	 * @param fn
	 * @return
	 */
	public List<SectionList> union(List<SectionList> list) {
		if (CommonUtil.isNull(list))
			return null;

		Map<Integer, List<String>> map = new HashMap<>();

		// 按照实体类型不同进行分组
		list.forEach(b -> {
			int typeId = b.getEntryTypeId();

			if (!map.containsKey(typeId)) // 有新的类型添加
				map.put(typeId, new ArrayList<>());

			// 保存相关信息，这是一个 List 集合
			map.get(typeId).add(b.getEntryId() + "_" + b.getId() + "_" + b.getUserId() + "_" + b.getCreateDate());
		});

		List<String> sqls = new ArrayList<>();
		String sqlCase = " WHEN e.id = %s THEN %s ", sqlCaseStr = " WHEN e.id = %s THEN '%s' ";
		
		// map 类似于
		// {52=[17_2_null_2020-04-07 16:22:48.0, 10_1_null_2020-04-06 19:19:21.0], 53=[77_3_null_2020-04-07 16:22:48.0]}
		// System.out.println(map);

		// 拼凑 SQL。查找各自的实体表，但用 section list 字段 override 实体的，于是用 CASE WHEN 覆盖
		for (Integer typeId : map.keySet()) {
			List<String> l = map.get(typeId);
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

			getScanTable().addSql(sqls, typeId, String.join(", ", entityIds), caseSql);
		}

		// UNION 拼接一起
		// System.out.println(String.join("UNION \n", sqls));
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
	 * 根据栏目 id 查找数据
	 * 
	 * @param sectionId 栏目 id
	 * @param fn
	 * @return 改栏目下的所有数据
	 */
	public List<SectionList> findListBySectionId(int sectionId) {
		return union(getListByCatalogId(sectionId));
	}

	/**
	 * 根据栏目 id 查找数据，可分页的
	 * 
	 * @param start     分页 start
	 * @param limit     分页 limit
	 * @param sectionId 栏目 id
	 * @param fn
	 * @return 改栏目下的分页数据
	 */
	public PageResult<SectionList> findListBySectionId(int start, int limit, int sectionId) {
		return (PageResult<SectionList>) union(getListByCatalogId(start, limit, sectionId));
	}

	/**
	 * 实体在系统中的类型
	 */
	public static final int TYPE_ARTICLE = 52;
	public static final int TYPE_GOODS = 53;
	public static final int TYPE_TOPIC = 54;
	public static final int TYPE_ADS = 55;

	public static final Map<Integer, String> TYPE_NAME = new HashMap<Integer, String>() {
		private static final long serialVersionUID = -1L;

		{
			put(TYPE_ARTICLE, new ArticleService().getUiName());
			put(TYPE_GOODS, new GoodsService().getUiName());
			put(TYPE_ADS, new AdsService().getUiName());
		}
	};
}
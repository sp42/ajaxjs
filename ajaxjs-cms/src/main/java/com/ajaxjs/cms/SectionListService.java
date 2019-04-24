package com.ajaxjs.cms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.ajaxjs.cms.app.catalog.Catalogable;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.util.CommonUtil;

/**
 * 涉及不同的业务表，所以需要在子类继承该基类
 * 
 * @author Frank Cheung
 *
 */
public abstract class SectionListService extends BaseService<SectionList> implements Catalogable<SectionList> {

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

	@FunctionalInterface
	public static interface ScanTable {
		public void addSql(List<String> sqls, int entryTypeId, String entryIds, String caseSql);
	}

	public static final String select = "SELECT entry.id AS entryId, entry.name,  %s AS entryTypeId, %s, " + IBaseDao.selectCover + " AS cover FROM %s entry WHERE id in (%s)\n";
	
	/**
	 *
	 * 
	 * @param sectionId
	 * @param fn
	 * @return
	 */
	public List<SectionList> findSectionListBySectionId(int sectionId, ScanTable fn) {
		return union(getListByCatelogId(sectionId), fn);
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
		return (PageResult<SectionList>) union(getListByCatelogId(start, limit, sectionId), fn);
	}

	/**
	 * 获取中间表中匹配的数据
	 * 
	 * @param catelogId
	 * @return
	 */
	public List<SectionList> getListByCatelogId(int catelogId) {
		return dao.findList(sql -> sql + " WHERE catelogId = " + catelogId);
	}

	/**
	 * 获取中间表中匹配的数据，可分页的
	 * 
	 * @param start
	 * @param limit
	 * @param catelogId
	 * @return
	 */
	public PageResult<SectionList> getListByCatelogId(int start, int limit, int catelogId) {
		return dao.findPagedList(start, limit, sql -> sql.replaceAll(" 1 = 1", " catelogId = " + catelogId));
	}

	/**
	 * 
	 * @param list 这个 List 是原始的 SectionList 表得到的数据
	 * @param fn
	 * @return
	 */
	public List<SectionList> union(List<SectionList> list, ScanTable fn) {
		if (CommonUtil.isNull(list)) {
			return null;
		} else {
			Map<Integer, List<String>> map = new HashMap<>();
			// 分组
			list.forEach(b -> {
				int entryTypeId = b.getEntryTypeId();
				if (!map.containsKey(entryTypeId)) {
					map.put(entryTypeId, new ArrayList<>());
				}
				map.get(entryTypeId).add(b.getEntryId() + "_" + b.getId() + "_" + b.getUserId() + "_" + b.getCreateDate());
			});

			List<String> sqls = new ArrayList<>();
			String sqlCase = " WHEN entry.id = %s THEN %s ", sqlCaseStr = " WHEN entry.id = %s THEN '%s' ";

			for (Integer entryTypeId : map.keySet()) {
				List<String> l = map.get(entryTypeId);
				List<String> entryIds = new ArrayList<>(), cases = new ArrayList<>(), userIds = new ArrayList<>(), dates = new ArrayList<>();
				
				for(String i : l) {
					String[] arr = i.split("_");
					String entryId = arr[0], sectionId = arr[1], userId = arr[2], date = arr[3];
					
					entryIds.add(entryId);
					cases.add(String.format(sqlCase, entryId, sectionId));
					userIds.add(String.format(sqlCase, entryId, userId));
					dates.add(String.format(sqlCaseStr, entryId, date));
				}
				
				String caseSql = String.format("(CASE %s END) AS id", String.join(" ", cases));
				caseSql += String.format(", (CASE %s END) AS userId", String.join(" ", userIds));
				caseSql += String.format(", (CASE %s END) AS createDate", String.join(" ", dates));
				
				fn.addSql(sqls, entryTypeId, String.join(", ", entryIds), caseSql);
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
	}

	@Override
	public SectionList findById(Long id) {
		return dao.findById_cover(id);
	}

	@Override
	public PageResult<SectionList> findPagedListByCatelogId(int catelogId, int start, int limit) {
		if (catelogId == 0)
			catelogId = getDomainCatelogId();
		return dao.findPagedListByCatelogId_Cover(catelogId, start, limit, null);
	}

	@Override
	public int getDomainCatelogId() {
		return ConfigService.getValueAsInt("data.adsCatalog_Id");
	}

	@Override
	public List<SectionList> findListByCatelogId(int catelogId) {
		return dao.findList_Cover(BaseService.addWhere("catelogId" + catelogId));
	}
}
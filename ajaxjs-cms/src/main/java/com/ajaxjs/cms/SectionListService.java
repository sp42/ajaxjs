package com.ajaxjs.cms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.ajaxjs.cms.app.catelog.Catelogable;
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
public abstract class SectionListService extends BaseService<SectionList> implements Catelogable<SectionList> {

	@TableName(value = "section_list", beanClass = SectionList.class)
	public interface BookmarkDao extends IBaseDao<SectionList> {
		@Select("SELECT id FROM ${tableName} WHERE entryId = ? AND entryTypeId = ?")
		public Long checkIfExist(long entryId, int entryType);

		@Select("")
		public List<SectionList> unionAll(Function<String, String> doSql);
	}

	public static BookmarkDao dao = new Repository().bind(BookmarkDao.class);

	{
		setDao(dao);
	}
	
	@FunctionalInterface
	public static interface ScanTable {
	    public void addSql(Map<Integer, List<String>> map, List<String> sqls, int entryTypeId);
	}
	
	public static final String select = "SELECT entry.id AS entryId, entry.name,  %s AS entryTypeId, " + IBaseDao.selectCover + " AS cover FROM %s entry WHERE id in (%s)\n";

	public List<SectionList> findSectionListBySectionId(int sectionId, ScanTable fn) {
		return union(getListByCatelogId(sectionId), fn);
	}
	
	public List<SectionList> getListByCatelogId(int catelogId) {
		return dao.findList(sql -> sql + " WHERE catelogId = " + catelogId);
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
				map.get(entryTypeId).add(b.getEntryId() + "");
			});

			List<String> sqls = new ArrayList<>();

			for (Integer entryTypeId : map.keySet()) {
				fn.addSql(map, sqls, entryTypeId);
			}

//			System.out.println(String.join("UNION \n", sqls));
			List<SectionList> bs = dao.unionAll(sql -> String.join("UNION \n", sqls));
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
		return dao.findPagedListByCatelogId_Cover(catelogId, start, limit);
	}

	@Override
	public int getDomainCatelogId() {
		return ConfigService.getValueAsInt("data.adsCatalog_Id");
	}

	@Override
	public List<SectionList> findListByCatelogId(int catelogId) {
		return dao.findListByCatelog_Cover(catelogId);
	}
}
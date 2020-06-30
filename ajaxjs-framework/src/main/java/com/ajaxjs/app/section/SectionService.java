package com.ajaxjs.app.section;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.sql.annotation.Select;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.orm.PageResult;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.util.ioc.Bean;

/**
 * 栏目
 * 
 * 对于可以放到栏目的实体，要求这些字段：id、name、cover，并且在 entityProfile 有登记注册类型的实体
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Bean
public class SectionService extends BaseService<Section> {
	@TableName(value = "column_list", beanClass = Section.class)
	public interface ColumnDao extends IBaseDao<Section> {
		@Select("SELECT id FROM ${tableName} WHERE entityUid = ? AND typeId = ?")
		public boolean checkIfExist(long entityUid, int typeId);

		@Select("SELECT id, uid, name, cover FROM #tableName# WHERE uid in (?)")
		public List<Map<String, Object>> queryAnyTable(Function<String, String> sqlHandler);
	}

	public static ColumnDao dao = new Repository().bind(ColumnDao.class);

	{
		setDao(dao);
	}

	/**
	 * 获取中间表中匹配的数据
	 * 
	 * @param catalogId 类别 id
	 * @return 栏目的实体集合
	 */
	public List<Section> getListByCatalogId(int catalogId) {
		SectionData map = new SectionData(dao.findList(by("catalogId", catalogId)));
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
	public PageResult<Section> getListByCatalogId(int start, int limit, int catalogId) {
		return dao.findPagedList(start, limit, catalogId == 0 ? null : by("catalogId", catalogId));
	}

}

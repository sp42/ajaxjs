package com.ajaxjs.cms.app.catalog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.util.CommonUtil;

@Bean("CatalogService")
public class CatalogServiceImpl extends BaseService<Catalog> implements CatalogService {
	public static CatalogDao dao = new Repository().bind(CatalogDao.class);
	
	{
		setUiName("分类");
		setShortName("catelog");
		setDao(dao);
	}

	@Override
	public List<Catalog> findByParentId(int id) {
		return dao.getListByParentId(id);
	}

	@Override
	public Catalog findById(Long id) {
		return dao.findById(id);
	}

	@Override
	public Long create(Catalog bean) {
		if (bean.getPid() != -1) { // 非根节点
			Catalog parent = findById(bean.getPid().longValue()); // 保存路径信息

			String path = "";

			if (parent.getPid() != -1) {
				path += parent.getPath();
			}

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
	public int update(Catalog bean) {
		return dao.update(bean);
	}

	@Override
	public boolean delete(Catalog bean) {
		return dao.deleteAll(bean.getId().intValue());
	}

	@Override
	public PageResult<Catalog> findPagedList(int start, int limit) {
		return dao.findPagedList(start, limit);
	}

	@Override
	public PageResult<Catalog> findList() {
		return null;
	}

	@Override
	public String getTableName() {
		return "general_catelog";
	}

	@Override
	public List<Catalog> findAll() {
		return dao.findPagedList(0, 999999);
	}

	@Override
	public List<Catalog> findAllListByParentId(int parentId, boolean isWithParent) {
		List<Catalog> list = dao.getAllListByParentId(parentId);
		
		if (!isWithParent && list != null) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getId() == new Integer(parentId).longValue()) {
					list.remove(i);// 不要父节点
					break;
				}
			}
		}

		return list;
	}

	@Override
	public List<Catalog> findAllListByParentId(int parentId) {
		return findAllListByParentId(parentId, true);
	}

	@Override
	public List<Map<String, Object>> findListAndSubByParentId(int parentId) {
		return dao.getListAndSubByParentId(parentId);
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
package com.ajaxjs.cms.app.catelog;

import java.util.List;
import java.util.Map;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;

@Bean("CatelogService")
public class CatelogServiceImpl extends BaseService<Catelog> implements CatelogService {
	CatelogDao dao = new Repository().bind(CatelogDao.class);
	
	{
		setUiName("分类");
		setShortName("catelog");
		setDao(dao);
	}


	@Override
	public List<Catelog> findByParentId(int id) {
		return dao.getListByParentId(id);
	}

	@Override
	public Catelog findById(Long id) {
		return dao.findById(id);
	}

	@Override
	public Long create(Catelog bean) {
		if (bean.getPid() != -1) { // 非根节点
			Catelog parent = findById(bean.getPid().longValue()); // 保存路径信息

			String path = "";

			if (parent.getPid() != -1) {
				path += parent.getPath();
			}

			path += "/" + parent.getId() + "/";
			bean.setPath(path);
		}

		Long newlyId = super.create(bean);

		if (newlyId != null) { // 需要创建了之后才有自己的 id
			Catelog updatePath = new Catelog();
			updatePath.setId(bean.getId());
			updatePath.setPath((bean.getPid() == -1 ? "/" : bean.getPath()) + bean.getId());

			update(updatePath);
		}

		return newlyId;
	}

	@Override
	public int update(Catelog bean) {
		return dao.update(bean);
	}

	@Override
	public boolean delete(Catelog bean) {
		return dao.deleteAll(bean.getId().intValue());
	}

	@Override
	public PageResult<Catelog> findPagedList(int start, int limit) {
		return dao.findPagedList(start, limit);
	}

	@Override
	public PageResult<Catelog> findList() {
		return null;
	}

	@Override
	public String getTableName() {
		return CatelogDao.tableName;
	}

	@Override
	public List<Catelog> findAll() {
		return dao.findPagedList(0, 999999);
	}

	@Override
	public List<Catelog> getAllListByParentId(int parentId, boolean isWithParent) {
		List<Catelog> list = dao.getAllListByParentId(parentId);
		
		if (!isWithParent && list != null) {
			for (int i = 0; i < list.size(); i++) {
				System.out.println(list.get(i).getName() +"::" + parentId+ " id:::" + list.get(i).getId());
				if (list.get(i).getId() == new Integer(parentId).longValue()) {
					list.remove(i);// 不要父节点
					break;
				}
			}

			
		}

		return list;
	}

	@Override
	public List<Catelog> getAllListByParentId(int parentId) {
		return getAllListByParentId(parentId, true);
	}

	@Override
	public List<Map<String, Object>> getListAndSubByParentId(int parentId) {
		return dao.getListAndSubByParentId(parentId);
	}

}
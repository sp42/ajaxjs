package com.ajaxjs.cms.app.catelog;

import java.util.List;
import java.util.Map;

import com.ajaxjs.cms.dao.CatalogDao;
import com.ajaxjs.cms.service.aop.CommonService;
import com.ajaxjs.cms.service.aop.GlobalLogAop;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.dao.DaoHandler;
import com.ajaxjs.orm.dao.PageResult;

@Bean(value = "CatelogService", aop = { CommonService.class, GlobalLogAop.class })
public class CatelogServiceImpl implements CatelogService {
	CatalogDao dao = new DaoHandler().bind(CatalogDao.class);

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

		Long newlyId = dao.create(bean);

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
	public String getName() {
		return "分类";
	}

	@Override
	public String getTableName() {
		return CatalogDao.tableName;
	}

	@Override
	public List<Catelog> findAll() {
		return dao.findPagedList(0, 999999);
	}

	@Override
	public List<Catelog> getAllListByParentId(int parentId, boolean isWithParent) {
		List<Catelog> list = dao.getAllListByParentId(parentId);

		if (!isWithParent) {
			int j = 0;
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getId() == new Integer(parentId).longValue()) {
					j = i;
					break;
				}
			}

			list.remove(j);// 不要父节点
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
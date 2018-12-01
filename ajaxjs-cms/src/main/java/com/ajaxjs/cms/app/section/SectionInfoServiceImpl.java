package com.ajaxjs.cms.app.section;

import java.util.List;
import java.util.Map;

import com.ajaxjs.cms.service.aop.CommonService;
import com.ajaxjs.cms.service.aop.GlobalLogAop;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.dao.DaoHandler;
import com.ajaxjs.orm.dao.PageResult;

@Bean(value = "SectionInfoService", aop = { CommonService.class, GlobalLogAop.class })
public class SectionInfoServiceImpl implements SectionInfoService {
	public static SectionInfoDao dao = new DaoHandler().bind(SectionInfoDao.class);

	@Override
	public List<SectionInfo> findByParentId(int id) {
		return dao.getListByParentId(id);
	}

	@Override
	public SectionInfo findById(Long id) {
		return dao.findById(id);
	}

	@Override
	public Long create(SectionInfo bean) {
		if (bean.getPid() != -1) { // 非根节点
			SectionInfo parent = findById(bean.getPid().longValue()); // 保存路径信息

			String path = "";

			if (parent.getPid() != -1) {
				path += parent.getPath();
			}

			path += "/" + parent.getId() + "/";
			bean.setPath(path);
		}

		Long newlyId = dao.create(bean);

		if (newlyId != null) { // 需要创建了之后才有自己的 id
			SectionInfo updatePath = new SectionInfo();
			updatePath.setId(bean.getId());
			updatePath.setPath((bean.getPid() == -1 ? "/" : bean.getPath()) + bean.getId());

			update(updatePath);
		}

		return newlyId;
	}

	@Override
	public int update(SectionInfo bean) {
		return dao.update(bean);
	}

	@Override
	public boolean delete(SectionInfo bean) {
		return dao.deleteAll(bean.getId().intValue());
	}

	@Override
	public PageResult<SectionInfo> findPagedList(int start, int limit) {
		return dao.findPagedList(start, limit);
	}

	@Override
	public PageResult<SectionInfo> findList() {
		return null;
	}

	@Override
	public String getName() {
		return "栏目";
	}

	@Override
	public String getTableName() {
		return SectionInfoDao.tableName;
	}

	@Override
	public List<SectionInfo> findAll() {
		return dao.findPagedList(0, 999999);
	}

	@Override
	public List<SectionInfo> getAllListByParentId(int parentId, boolean isWithParent) {
		List<SectionInfo> list = dao.getAllListByParentId(parentId);

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
	public List<SectionInfo> getAllListByParentId(int parentId) {
		return getAllListByParentId(parentId, true);
	}

	@Override
	public List<Map<String, Object>> getListAndSubByParentId(int parentId) {
		return dao.getListAndSubByParentId(parentId);
	}

}
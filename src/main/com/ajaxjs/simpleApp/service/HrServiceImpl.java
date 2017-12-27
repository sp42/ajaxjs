package com.ajaxjs.simpleApp.service;

import java.util.List;
import java.util.Map;

import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.framework.dao.DaoHandler;
import com.ajaxjs.framework.dao.QueryParams;
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.jdbc.PageResult;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.simpleApp.dao.HrDao;
import com.ajaxjs.simpleApp.model.Catalog;

public class HrServiceImpl implements HrService {
	HrDao dao = new DaoHandler<HrDao>().bind(HrDao.class);

	@Override
	public Map<String, Object> findById(Long id) throws ServiceException {
		return dao.findById(id);
	}

	@Override
	public Long create(Map<String, Object> bean) throws ServiceException {
		return dao.create(bean);
	}

	@Override
	public int update(Map<String, Object> bean) throws ServiceException {
		return dao.update(bean);
	}

	@Override
	public boolean delete(Map<String, Object> bean) throws ServiceException {
		return dao.delete(bean);
	}

	@Override
	public PageResult<Map<String, Object>> findPagedList(QueryParams params) throws ServiceException {
		PageResult<Map<String, Object>> result = dao.findPagedList(params);

		Map<Integer, BaseModel> catalogMap = ModelAndView.list_bean2map_id_as_key(getHrCatalog());
		for (Map<String, Object> map : result.getRows()) {
			BaseModel catalogBean = catalogMap.get((int) map.get("catalog"));
			if (catalogBean == null) {
				System.out.println("找不到对应的分类，实体分类id 为：" + map.get("catalog"));
			} else {
				map.put("catalogName", catalogBean.getName());
			}
		}
		return result;
	}

	@Override
	public PageResult<Map<String, Object>> findPagedList(int start, int limit) throws ServiceException {
		return dao.findPagedList(new QueryParams(start, limit));
	}

	@Override
	public String getName() {
		return "招聘";
	}

	@Override
	public String getTableName() {
		return "hr";
	}

	@Override
	public List<Map<String, Object>> getTop5() {
		return dao.getTop5();
	}

	private List<Catalog> catalog;

	@Override
	public List<Catalog> getHrCatalog() {
		if (catalog == null)
			catalog = dao.getHrCatalog();
		return catalog;
	}

}

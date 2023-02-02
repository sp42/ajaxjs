package com.ajaxjs.workflow.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.ajaxjs.util.cache.Cache;
import com.ajaxjs.util.cache.CacheManager;
import com.ajaxjs.util.cache.CacheManagerAware;
import com.ajaxjs.util.cache.MemoryCacheManager;
import com.ajaxjs.workflow.common.WfConstant;
import com.ajaxjs.workflow.model.ProcessModel;
import com.ajaxjs.workflow.model.po.ProcessPO;
import com.ajaxjs.workflow.service.parser.ProcessModelParser;

/**
 * 流程处理
 */
@Service
public class ProcessService extends BaseWfService implements CacheManagerAware {
	private static final String SEPARATOR = ".";

	public ProcessPO findById(Long id) {
		ProcessPO p;
		String name = nameCache.get(id);

		if (name != null && beanCache.get(name) != null) // 先通过 cache 获取，如果返回空，就从数据库读取并 put
			p = beanCache.get(name);
		else {
			p = ProcessDAO.findById(id);
			p.setModel(ProcessModelParser.parse(p.getContent()));
			saveCache(id, p);
		}

		// TODO: Model Null？
		return p;
	}

	private void saveCache(Long id, ProcessPO p) {
		String name = p.getName() + SEPARATOR + p.getVersion();
		nameCache.put(id, name);
		beanCache.put(name, p);
	}

	/**
	 * 新建流程不叫 create，而是 deploy 部署
	 * 
	 * @param processXml 流程定义文件内容，该 XML 包含了流程定义的所有信息
	 * @param creator    创建人
	 * @return 新建流程 id
	 */
	public long deploy(String processXml, Long creator) {
		ProcessModel model = ProcessModelParser.parse(processXml);
		ProcessPO bean = new ProcessPO();

		bean.setName(model.getName());
		bean.setDisplayName(model.getDisplayName());
		bean.setContent(processXml);
		bean.setStat(WfConstant.STATE_ACTIVE);
		bean.setCreator(creator);

		Integer ver = ProcessDAO.getLatestProcessVersion(model.getName()); // 同名的，设置不同的版本号
		bean.setVersion(ver == null || ver < 0 ? 0 : ver + 1);

		long newlyId = (long) ProcessDAO.create(bean);

		bean.setModel(model);
		saveCache(newlyId, bean);

		lastDeployProcessId = newlyId;

		return newlyId;
	}

	/**
	 * 根据 processId 卸载流程
	 * 
	 * @param id 流程 id
	 */
	public void undeploy(long id) {
		ProcessPO bean = new ProcessPO();
		bean.setId(id);
		bean.setStat(WfConstant.STATE_FINISH);

		ProcessDAO.update(bean);

		String name = nameCache.get(id);

		if (name != null) {
			ProcessPO p = beanCache.get(nameCache.get(id));

			if (p != null)
				p.setStat(WfConstant.STATE_FINISH); // 只是修改了 状态
		}
	}

	/**
	 * 根据流程名称查找流程
	 * 
	 * @param name 流程名称
	 * @return 流程列表
	 */
	public ProcessPO findByName(String name) {
		return findByVersion(name, null);
	}

	/**
	 * 根据流程名称、流程版本号查找流程
	 * 
	 * @param name 流程名称
	 * @param name 流程版本号
	 * @return 流程列表
	 */
	public ProcessPO findByVersion(String name, Integer version) {
		Map<String, Object> params = new HashMap<>();
		params.put("name", name);
		if (version != null)
			params.put("version", version);

		List<ProcessPO> list = ProcessDAO.setWhereQuery(params).findList();

		if (ObjectUtils.isEmpty(list))
			return null;
		else {
			ProcessPO p = list.get(0);
			p.setModel(ProcessModelParser.parse(p.getContent()));
			saveCache(p.getId(), p);

			return p;
		}
	}

	/**
	 * 获取所有流程定义的名称（不重复 name）
	 * 
	 * @return 流程定义的名称列表
	 */
	public List<String> getAllProcessNames() {
		List<ProcessPO> list = ProcessDAO.findList();
		List<String> names = new ArrayList<>();

		for (ProcessPO entity : list) {
			if (names.contains(entity.getName()))
				continue;
			else
				names.add(entity.getName());
		}

		return names;
	}

	// 上次加载的流程 id，测试用
	public long lastDeployProcessId;

	private Cache<Long, String> nameCache;

	private Cache<String, ProcessPO> beanCache;

	{
		setCacheManager(new MemoryCacheManager());
	}

	@Override
	public void setCacheManager(CacheManager cacheManager) {
		nameCache = cacheManager.getCache("process-id-name");
		beanCache = cacheManager.getCache("process-name-bean", ProcessPO.class);
	}
}

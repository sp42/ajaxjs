package com.ajaxjs.workflow.service;

import java.util.List;
import java.util.function.Function;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.util.cache.Cache;
import com.ajaxjs.util.cache.CacheManager;
import com.ajaxjs.util.cache.CacheManagerAware;
import com.ajaxjs.workflow.WorkflowConstant;
import com.ajaxjs.workflow.dao.ProcessDao;
import com.ajaxjs.workflow.model.ProcessModel;
import com.ajaxjs.workflow.model.entity.Process;
import com.ajaxjs.workflow.model.parser.ProcessModelParser;

@Component
public class ProcessService extends BaseService<Process> implements CacheManagerAware {
	{

		setDao(dao);
	}
	
	public static ProcessDao dao = new Repository().bind(ProcessDao.class);

	private static final String SEPARATOR = ".";

	@Override
	public Process findById(Long id) {
		Process p;
		String name = nameCache.get(id);

		if (name != null && beanCache.get(name) != null) // 先通过 cache 获取，如果返回空，就从数据库读取并 put
			p = beanCache.get(name);
		 else {
			p = super.findById(id);
			p.setModel(ProcessModelParser.parse(p.getContent()));
			saveCache(id, p);
		}

		// TODO: Model Null？
		return p;
	}

	private void saveCache(Long id, Process p) {
		String name = p.getName() + SEPARATOR + p.getVersion();
		nameCache.put(id, name);
		beanCache.put(name, p);
	}

	public PageResult<Process> list(int start, int limit) {
		return dao.findPagedList(start, limit, null);
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
		Process bean = new Process();

		bean.setName(model.getName());
		bean.setDisplayName(model.getDisplayName());
		bean.setContent(processXml);
		bean.setStat(WorkflowConstant.STATE_ACTIVE);
		bean.setCreator(creator);

		Integer ver = dao.getLatestProcessVersion(model.getName());
		bean.setVersion(ver == null || ver < 0 ? 0 : ver + 1);

		long newlyId = create(bean);

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
		Process bean = new Process();
		bean.setId(id);
		bean.setStat(WorkflowConstant.STATE_FINISH);

		update(bean);

		Process p = beanCache.get(nameCache.get(id));

		if (p != null)
			p.setStat(WorkflowConstant.STATE_FINISH); // 只是修改了 状态
	}

	/**
	 * 根据流程名称查找流程
	 * 
	 * @param name 流程名称
	 * @return 流程列表
	 */
	public Process findByName(String name) {
		return findByVersion(name, null);
	}

	/**
	 * 根据流程名称、流程版本号查找流程
	 * 
	 * @param name 流程名称
	 * @param name 流程版本号
	 * @return 流程列表
	 */
	public Process findByVersion(String name, Integer version) {
		Function<String, String> fn = by("name", name);

		if (version != null)
			fn.andThen(by("version", version));

		List<Process> list = findList(fn);

		if (ObjectUtils.isEmpty(list))
			return null;
		else {
			Process p = list.get(0);
			p.setModel(ProcessModelParser.parse(p.getContent()));
			saveCache(p.getId(), p);

			return p;
		}
	}

	// 上次加载的流程 id，测试用
	public long lastDeployProcessId;

	private Cache<Long, String> nameCache;

	/**
	 * 
	 */
	private Cache<String, Process> beanCache;

	@Override
	public void setCacheManager(CacheManager cacheManager) {
		nameCache = cacheManager.getCache("process-id-name");
		beanCache = cacheManager.getCache("process-name-bean", Process.class);
	}
}

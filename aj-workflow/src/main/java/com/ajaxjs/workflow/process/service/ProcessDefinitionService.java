package com.ajaxjs.workflow.process.service;

import java.util.List;
import java.util.function.Function;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.sql.orm.PageResult;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.cache.Cache;
import com.ajaxjs.util.cache.CacheManager;
import com.ajaxjs.util.cache.CacheManagerAware;
import com.ajaxjs.util.cache.MemoryCacheManager;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.workflow.model.ProcessModel;
import com.ajaxjs.workflow.parser.ProcessModelParser;
import com.ajaxjs.workflow.process.ProcessDefinition;

@Component
public class ProcessDefinitionService extends BaseService<ProcessDefinition> implements CacheManagerAware {
	{
		setUiName("流程定义");
		setShortName("process-def");
		setDao(DAO);
	}

	public static ProcessDefinitonDao DAO = new Repository().bind(ProcessDefinitonDao.class);

	private static final String SEPARATOR = ".";

	@Override
	public ProcessDefinition findById(Long id) {
		ProcessDefinition def;
		String name = nameCache.get(id);

		// 先通过 cache 获取，如果返回空，就从数据库读取并 put
		if (name != null && beanCache.get(name) != null) {
			def = beanCache.get(name);
		} else {
			def = super.findById(id);
			def.setModel(ProcessModelParser.parse(def.getContent()));
			saveCache(id, def);
		}

		// TODO: Model Null？
		return def;
	}

	private void saveCache(Long id, ProcessDefinition def) {
		String name = def.getName() + SEPARATOR + def.getVersion();
		nameCache.put(id, name);
		beanCache.put(name, def);
	}

	public PageResult<ProcessDefinition> list(int start, int limit) {
		return DAO.findPagedList(start, limit, null);
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
		ProcessDefinition bean = new ProcessDefinition();

		bean.setName(model.getName());
		bean.setDisplayName(model.getDisplayName());
		bean.setContent(processXml);
		bean.setStat(ProcessModel.STATE_ACTIVE);
		bean.setCreator(creator);

		Integer ver = DAO.getLatestProcessVersion(model.getName());
		bean.setVersion(ver == null || ver < 0 ? 0 : ver + 1);

		long newlyId = create(bean);

		bean.setModel(model);
		saveCache(newlyId, bean);

		return newlyId;
	}

	/**
	 * 根据 processId 卸载流程
	 * 
	 * @param id 流程 id
	 */
	public void undeploy(long id) {
		ProcessDefinition bean = new ProcessDefinition();
		bean.setId(id);
		bean.setStat(ProcessModel.STATE_FINISH);

		update(bean);

		ProcessDefinition def = beanCache.get(nameCache.get(id));

		if (def != null)
			def.setStat(ProcessModel.STATE_FINISH); // 只是修改了 状态
	}

	/**
	 * 根据流程名称查找流程
	 * 
	 * @param name 流程名称
	 * @return 流程列表
	 */
	public ProcessDefinition findByName(String name) {
		return findByVersion(name, null);
	}

	/**
	 * 根据流程名称、流程版本号查找流程
	 * 
	 * @param name 流程名称
	 * @param name 流程版本号，可选，若为 null，则选择数据库匹配的第一行记录
	 * @return 流程列表
	 */
	public ProcessDefinition findByVersion(String name, Integer version) {
		Function<String, String> fn = by("name", name);

		if (version != null)
			fn.andThen(by("version", version));

		List<ProcessDefinition> list = findList(fn);

		if (CommonUtil.isNull(list))
			return null;
		else {
			ProcessDefinition def = list.get(0);
			def.setModel(ProcessModelParser.parse(def.getContent()));
			saveCache(def.getId(), def);

			return def;
		}
	}

	private Cache<Long, String> nameCache;

	/**
	 * 
	 */
	private Cache<String, ProcessDefinition> beanCache;

	/**
	 * 如果没有缓存，初始化一个默认的
	 */
	public void initCache() {
		if (nameCache == null || beanCache == null)
			setCacheManager(new MemoryCacheManager());
	}

	@Override
	public void setCacheManager(CacheManager cacheManager) {
		nameCache = cacheManager.getCache("process-id-name");
		beanCache = cacheManager.getCache("process-name-bean", ProcessDefinition.class);
	}
}

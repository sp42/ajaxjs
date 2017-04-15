/**
 * 版权所有 2017 Frank Cheung
 * 
 * 根据 2.0 版本 Apache 许可证("许可证")授权；
 * 根据本许可证，用户可以不使用此文件。
 * 用户可从下列网址获得许可证副本：
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *    
 * 除非因适用法律需要或书面同意，根据许可证分发的软件是基于"按原样"基础提供，
 * 无任何明示的或暗示的保证或条件。详见根据许可证许可下，特定语言的管辖权限和限制。
 */
package com.ajaxjs.framework.service;

import java.io.Serializable;
import java.util.List;

import com.ajaxjs.framework.dao.IDao;
import com.ajaxjs.framework.dao.QueryParams;
import com.ajaxjs.framework.dao.DaoHandler;
import com.ajaxjs.framework.model.ModelAndView;
import com.ajaxjs.jdbc.ConnectionMgr;

/**
 * 业务基类
 * @author xinzhang
 *
 * @param <T>
 * @param <ID>
 */
public abstract class BaseDaoService<T, ID extends Serializable, D extends IDao<T, ID>> implements IService<T, ID> {
	/**
	 * 数据访问对象
	 */
	private D dao;

	/**
	 * 业务名称（英文）
	 */
	private String name;
	
	/**
	 * 业务名称（中文）
	 */
	private String uiName;
	
	/**
	 * 携带的数据，可用于 UI 显示或者其他数据
	 */
	private ModelAndView mv;

	/**
	 * 实例化 DAO。因为是 class 所以不能注入，于是一般在 Service 构造器里面调用该方法
	 * @param clz
	 */
	public void initDao(Class<D> clz) {
		if(ConnectionMgr.getConnection() == null)
			throw new RuntimeException("请先创建 connection 再创建 service！");
		
		D dao = new DaoHandler<D>().setConn(ConnectionMgr.getConnection()).bind(clz);
		setDao(dao);
	}
	
	@Override
	public void prepareData(ModelAndView model) {
		// 每次 servlet 都会执行的。记录时间
		model.put("requestTimeRecorder", System.currentTimeMillis());

		// 设置实体 id 和 现实名称 。
		model.put("uiName", getUiName());
		model.put("tableName", getName());
	}
	
	/**
	 * 获取全部数据（不分页）
	 * @return
	 * @throws ServiceException
	 */
	public List<T> getAll() throws ServiceException{
		return findPagedList(new QueryParams(0, 999999)).getRows();
	}
	
	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public D getDao() {
		return dao;
	}

	public void setDao(D dao) {
		this.dao = dao;
	}

	public ModelAndView getMv() {
		return mv;
	}

	public void setMv(ModelAndView mv) {
		this.mv = mv;
	}

	public String getUiName() {
		return uiName;
	}

	public void setUiName(String uiName) {
		this.uiName = uiName;
	}
}

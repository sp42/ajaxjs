/**
 * 版权所有 2017 Sp42 frank@ajaxjs.com
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
package com.ajaxjs.framework.service.aop;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.bval.jsr.ApacheValidationProvider;

import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.framework.service.IService;
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.ioc.Aop;
import com.ajaxjs.jdbc.SnowflakeIdWorker;

/**
 * 常见的数据服务
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 * @param <T>
 *            实体类型，可以是 POJO 或 Map
 * @param <ID>
 *            序号类型，可以是 INTEGER/LONG/String
 * @param <S>
 *            Service 类型
 */
public class CommonTestService<T, ID extends Serializable, S extends IService<T, ID>> extends Aop<S> {
	
	@SuppressWarnings("unchecked")
	@Override
	public Object before(S obj, Method method, String methodName, Object[] args) throws Throwable {
		if ("create".equals(methodName)) { // 新建
			
			if (args[0] instanceof BaseModel) {
				BaseModel model = (BaseModel) args[0];
				onCreate(model);
				validate(model);
			}
			 
			if (args[0] instanceof Map) {
				onCreate((Map<String, Object>) args[0]);
			}
		}

		if ("update".equals(methodName)) { // 修改
			if (args[0] instanceof BaseModel) {
				onUpdate((BaseModel) args[0]);
			}
		}
		
		return null;
	}

	private final static ValidatorFactory avf = Validation.byProvider(ApacheValidationProvider.class).configure().buildValidatorFactory();  

	/**
	 * 数据验证
	 * @param model
	 * @throws ServiceException
	 */
	private static void validate(BaseModel model) throws ServiceException {
		Validator validator = avf.getValidator();
		Set<ConstraintViolation<BaseModel>> result = validator.validate(model);

		if (!result.isEmpty()) {
			String err = "";
			for (ConstraintViolation<BaseModel> r : result) {
				err += r.getPropertyPath() + ":" + r.getMessage();
			}
			
			throw new ServiceException("数据验证不通过！" + err);
		}
	}

	@Override
	public void after(S obj, Method method, String methodName, Object[] args, Object returnObj) {
		if ("create".equals(methodName)) { // 新建
			BaseModel model = null; //asset there is a basemodel

			if (args[0] instanceof BaseModel) {
				model = (BaseModel) args[0];// 保存生成的 id
				model.setId((long) returnObj);
			}
		} 
	}
	

	public static void onCreate(BaseModel model) {
		SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
		model.setUid(idWorker.nextId());
		Date now = new Date();

		if (model.getCreateDate() == null)
			model.setCreateDate(now);
		if (model.getUpdateDate() == null)
			model.setUpdateDate(now);
	}
	
	public static void onUpdate(BaseModel model) {
		Date now = new Date();
		if (model.getUpdateDate() == null)
			model.setUpdateDate(now);
	}

	public static void onCreate(Map<String, Object> map) {
		SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
		map.put("uid", idWorker.nextId());
		Date now = new Date();
		
		map.put("createDate", now);
		map.put("updateDate", now);
	}

	public static void onUpdate(Map<String, Object> map) {
		Date now = new Date();

		map.put("updateDate", now);
	}
}
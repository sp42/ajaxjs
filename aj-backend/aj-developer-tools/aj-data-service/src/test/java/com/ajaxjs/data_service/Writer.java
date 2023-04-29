package com.ajaxjs.data_service;

import com.ajaxjs.data_service.model.ServiceContext;
import com.ajaxjs.data_service.plugin.IPlugin;
import com.ajaxjs.framework.spring.DiContextUtil;
import com.ajaxjs.user.gateway.Passport;
import com.ajaxjs.user.gateway.PassportFilter;

import java.util.Map;

/**
 * 保存租户信息
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
public class Writer implements IPlugin {
	@Override
	public boolean before(DataServiceConstant.CRUD type, ServiceContext ctx) {

		if (type == DataServiceConstant.CRUD.CREATE ) {// 非 null 表明 passportFilter 已激活
			PassportFilter passportFilter = DiContextUtil.getBean(PassportFilter.class);
			if(passportFilter != null){
				
			Passport passport = passportFilter.getPassportByClientId(ctx.getRequest());

			Map<String, Object> params = ctx.getRequestParams();
			params.put("tenantId", passport.getTenantId());

			if (passport.getPortalId() != null) // 门户 id 可选写入
				params.put("portalId", passport.getTenantId());
			}
		}

		return true;
	}
}

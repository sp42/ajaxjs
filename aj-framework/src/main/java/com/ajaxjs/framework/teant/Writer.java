package com.ajaxjs.framework.teant;

import com.ajaxjs.data_service.model.ServiceContext;

import java.util.Map;

import com.ajaxjs.data_service.model.DataServiceConstant.CRUD;
import com.ajaxjs.data_service.plugin.IPlugin;
import com.ajaxjs.gateway.Passport;
import com.ajaxjs.gateway.PassportFilter;
import com.ajaxjs.spring.DiContextUtil;

/**
 * 保存租户信息
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class Writer implements IPlugin {
	@Override
	public boolean before(CRUD type, ServiceContext ctx) {

		if (type == CRUD.CREATE ) {// 非 null 表明 passportFilter 已激活
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

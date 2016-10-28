/**
 * Copyright 2015 Frank Cheung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.user.auth;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.subject.PrincipalCollection;

//import com.ajaxjs.framework.user.Service;
//import com.ajaxjs.framework.user.User;
import com.ajaxjs.util.LogHelper;

public class ShiroDbRealm extends AuthenticatingRealm {
	private static final LogHelper LOGGER = LogHelper.getLog(ShiroDbRealm.class);
	// private Service service = new Service();

	/**
	 * 认证回调函数,登录时调用
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
			throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;

		String userName = token.getUsername();
		LOGGER.info(userName);

		// User user = service.findByUserName(userName);

		return new SimpleAuthenticationInfo(userName, token.getPassword(), getName());
		// if (user != null) {
		// return new SimpleAuthenticationInfo(userName, token.getPassword(),
		// getName());
		// } else {
		// return null;
		// }
	}

	/**
	 * 
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 * 
	 */
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String loginName = (String) principals.fromRealm(getName()).iterator().next();

		LOGGER.info("doGetAuthorizationInfo:" + loginName);
		// Object user = "";
		//
		// if (user != null) {
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.addStringPermission("common-user");
		return info;
		// } else {
		// return null;
		// }
	}

}

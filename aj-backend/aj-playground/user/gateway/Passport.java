package com.ajaxjs.user.gateway;

import lombok.Data;

/**
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
@Data
public class Passport {
	private String clientId;

	private String token;

	private Long tenantId;

	private Long portalId;

}

package com.ajaxjs.model;

import lombok.Data;

/**
 * 
 * @author Frank Cheung
 *
 */
@Data
public class ClientUser {
	private Long id;

	private Long authClientId;

	private Long userId;

	private Long authScopeId;
}
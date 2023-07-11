package com.ajaxjs.sso.service;

import java.util.HashMap;
import java.util.Map;

import com.ajaxjs.data_service.sdk.Caller;
import com.ajaxjs.data_service.sdk.IDataService;
import com.ajaxjs.sso.model.AccessToken;
import com.ajaxjs.sso.model.ClientDetails;
import com.ajaxjs.sso.model.ClientUser;
import com.ajaxjs.sso.model.RefreshToken;
import com.ajaxjs.sso.model.Scope;

/**
 * 
 * @author Frank Cheung
 *
 */
public interface SsoDAO {
	static interface AccessTokenDAO extends IDataService<AccessToken> {
	}

	static interface ClientDetailDAO extends IDataService<ClientDetails> {
	}

	static interface RefreshTokenDAO extends IDataService<RefreshToken> {
	}

	static interface ScopeDAO extends IDataService<Scope> {
	}

	static interface ClientUserDAO extends IDataService<ClientUser> {
		ClientUser findByClientId(long clientId, long userId, long scopeId);
	}

	public final static AccessTokenDAO AcessTokenDAO = new Caller("cms", "sso_access_token").bind(AccessTokenDAO.class, AccessToken.class);

	public final static ClientDetailDAO ClientDetailDAO = new Caller("cms", "sso_client_details").bind(ClientDetailDAO.class, ClientDetails.class);

	public final static RefreshTokenDAO RefreshTokenDAO = new Caller("cms", "sso_refresh_token").bind(RefreshTokenDAO.class, RefreshToken.class);

	public final static ScopeDAO ScopeDAO = new Caller("cms", "sso_scope").bind(ScopeDAO.class, Scope.class);

	public final static ClientUserDAO ClientUserDAO = new Caller("cms", "sso_scope").bind(ClientUserDAO.class, ClientUser.class);

	/**
	 * 
	 * @param accessToken
	 * @return
	 */
	default AccessToken findByAccessToken(String accessToken) {
		return AcessTokenDAO.setWhereQuery("accessToken", accessToken).findOne();
	}

	/**
	 * 
	 * @param clientId
	 * @return
	 */
	default ClientDetails findClientDetailsByClientId(String clientId) {
		return ClientDetailDAO.setWhereQuery("clientId", clientId).findOne();
	}

	default AccessToken findTokenByUserIdClientIdScope(long userId, long clientId, String scope) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("clientId", clientId);
		params.put("scope", scope);

		return AcessTokenDAO.setWhereQuery(params).findOne();
	}
}
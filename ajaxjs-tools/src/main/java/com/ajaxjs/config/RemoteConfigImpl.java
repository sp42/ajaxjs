package com.ajaxjs.config;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteConfigImpl extends UnicastRemoteObject implements RemoteConfig {
	private static final long serialVersionUID = 4322905628650075193L;

	protected RemoteConfigImpl() throws RemoteException {
		super();
	}

	@Override
	public String getStru() throws RemoteException {
		return "foo";
	}

}

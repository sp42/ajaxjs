package com.ajaxjs.config;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteConfig extends Remote {
	public String getStru() throws RemoteException;
}

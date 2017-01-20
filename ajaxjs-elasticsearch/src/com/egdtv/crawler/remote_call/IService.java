package com.egdtv.crawler.remote_call;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface IService extends Remote {

	/**
	 * 得到参数对应的视频链接
	 * 
	 * @return 返回“视频链接"字样
	 * @throws java.rmi.RemoteException
	 */
	public Map<String, String> getVideo(String str) throws RemoteException;

	/**
	 * 控制抓取的开关 0为开，1为关；
	 * 
	 * @throws java.rmi.RemoteException
	 */
	public String threadSwitch(String str) throws RemoteException;

	/**
	 * 检查当前抓取状态
	 * 
	 * @return
	 * @throws RemoteException
	 */
	public String CheckSwitch() throws RemoteException;
}
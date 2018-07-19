package com.ajaxjs.config;

import java.rmi.Naming;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Application Lifecycle Listener implementation class Server
 *
 */
//@javax.servlet.annotation.WebListener
public class RemoteConfigServerListener implements ServletContextListener {

	/**
	 * Default constructor.
	 */
	public RemoteConfigServerListener() {
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		try {
			RemoteConfig c = new RemoteConfigImpl();
			LocateRegistry.createRegistry(8888);
			Naming.bind("//localhost:8888/RHello", c);
			System.out.println("RMI ok!");
		} catch (RemoteException e) {
			System.out.println("创建远程对象发生异常！");
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			System.out.println("发生重复绑定对象异常！");
			e.printStackTrace();
		} catch (MalformedURLException e) {
			System.out.println("发生URL畸形异常！");
			e.printStackTrace();
		}
	}

}

package com.ajaxjs.monitor;

import java.util.ArrayList;
import java.util.Set;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

//import org.apache.catalina.util.ServerInfo;

/**
 * 获取 Tomcat 监控信息
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class TomcatInfo {
//	/**
//	 * 获取版本，如 Apache Tomcat/8.5.50
//	 * 
//	 * @return
//	 */
//	public static String getVersion() {
//		return ServerInfo.getServerInfo();
//	}

	/**
	 * 获取 tomcat 运行端口
	 * 
	 * @return
	 */
	public static int getTomcatPort() {
		int port = 0;

		MBeanServer mBeanServer = null;
		ArrayList<MBeanServer> mBeanServers = MBeanServerFactory.findMBeanServer(null);

		if (mBeanServers.size() > 0)
			mBeanServer = mBeanServers.get(0);

		if (mBeanServer == null)
//			throw new IllegalStateException("没有发现JVM中关联的MBeanServer.");
			return port;

		try {
			Set<ObjectName> objectNames = mBeanServer.queryNames(new ObjectName("Catalina:type=Connector,*"), null);

			if (objectNames == null || objectNames.size() <= 0)
				throw new IllegalStateException("没有发现JVM中关联的MBeanServer : " + mBeanServer.getDefaultDomain() + " 中的对象名称.");

			for (ObjectName objectName : objectNames) {
				String protocol = (String) mBeanServer.getAttribute(objectName, "protocol");

				if (protocol.equals("HTTP/1.1"))
					port = (Integer) mBeanServer.getAttribute(objectName, "port");
			}
		} catch (MalformedObjectNameException | MBeanException | AttributeNotFoundException | ReflectionException | InstanceNotFoundException e) {
			e.printStackTrace();
		}

		return port;
	}
}

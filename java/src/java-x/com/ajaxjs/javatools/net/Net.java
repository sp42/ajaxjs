package com.ajaxjs.javatools.net;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.Query;
import javax.management.ReflectionException;

import com.ajaxjs.core.LogFactory;
import com.ajaxjs.core.io.Image;
import com.ajaxjs.framework.web.util.IP;

public class Net {
	private final static Logger LOGGER = LogFactory.getLog(Net.class);
	
    /**
	 * 打开URL对应的网页并保存为图片
	 * 程序运行时用户不能有其它操作，否则可能保存错误截屏。 这里假设加载一个网页时间最长为8秒.
	 * @param svaefile
	 */
	public static void webscreenCut(String svaefile) {
		if (!Desktop.isDesktopSupported()) {
			System.err.println("Desktop is not supported (fatal)");
			return;
		}

		Desktop desktop = Desktop.getDesktop();
		if (!desktop.isSupported(Desktop.Action.BROWSE)) {
			System.err.println("Desktop doesn't support the browse action (fatal)");
			return;
		}

		try {
			desktop.browse(URI.create("http://www.csdn.net"));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		try {
			Thread.sleep(8000); // 8 seconds is enough to load the any page.
		} catch (InterruptedException e) {
			e.printStackTrace();
			return;
		} 
		
		// Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize() );
		BufferedImage image = null;
		
		try {
			image = new Robot().createScreenCapture(new Rectangle(300, 90, 1000, 720));
		} catch (AWTException e) {
			e.printStackTrace();
			return;
		}
		
		if(image != null) Image.saveImgfile(svaefile, image, "jpg");
	}
	
	/**
	 * linux 没有 localhost 要在host文件中加入 127.0.0.1 localhost 
	 * @return
	 * @throws MalformedObjectNameException
	 * @throws NullPointerException
	 * @throws UnknownHostException
	 * @throws AttributeNotFoundException
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 */
	public static List<String> getEndPoints() throws MalformedObjectNameException, NullPointerException, UnknownHostException,
		AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException {
		ArrayList<String> endPoints = new ArrayList<>();
		
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		Set<ObjectName> objs = mbs.queryNames(new ObjectName("*:type=Connector,*"), Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
		InetAddress[] addresses = InetAddress.getAllByName(IP.getLocalHostName());
		
		for (Iterator<ObjectName> i = objs.iterator(); i.hasNext();) {
			ObjectName obj = i.next();
			String  scheme = mbs.getAttribute(obj, "scheme").toString(),
					port = obj.getKeyProperty("port");
			
//			if("80".equals(port))isDebug = false; // 如果发现 tomcata 有提供 80 端口，那么表示是 正式环境
			for (InetAddress addr : addresses){
				endPoints.add(scheme + "://" + addr.getHostAddress() + ":" + port);
			}
		}
		
		return endPoints;
	}
	
	/**
	 * 模拟ping ping("192.168.0.113");
	 * 
	 * @param ip
	 */
	public static void ping(String ip) {
		try {
			InetAddress address = InetAddress.getByName(ip);
			LOGGER.info("" + address.isReachable(5000)); // 设定超时时间，返回结果表示是否连上
		} catch (UnknownHostException e) {
			if (com.ajaxjs.core.Util.isEnableConsoleLog) e.printStackTrace();
		} catch (IOException e) {
			if (com.ajaxjs.core.Util.isEnableConsoleLog) e.printStackTrace();
		}
	}
	
	/**
	 * 模拟 telnet telnet("192.168.0.201", 8899);
	 * 
	 * @param ip
	 * @param port
	 */
	public static void telnet(String ip, int port) {
		Socket server = null;

		try {
			server = new Socket();
			InetSocketAddress address = new InetSocketAddress(ip, port);
			server.connect(address, 5000);
		} catch (UnknownHostException e) {
			LOGGER.warning("未知主机");
		} catch (IOException e) {
			LOGGER.warning("telnet失败");
		} finally {
			if (server != null)
				try {
					server.close();
				} catch (IOException e) {}
		}
	}
}

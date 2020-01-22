/*
 * Copyright 2013-2015 www.snakerflow.com. Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package org.snaker.engine;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.snaker.engine.access.transaction.TransactionInterceptor;
import org.snaker.engine.core.ServiceContext;
import org.snaker.engine.impl.SimpleContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ajaxjs.Version;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.XMLHelper;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 只允许应用程序存在一个Configuration实例 初始化服务上下文，查找流程引擎实现类并初始化依赖的服务
 * 
 * @author yuqs
 * @since 1.0
 */
public class Configuration {
	public static final LogHelper LOGGER = LogHelper.getLog(Configuration.class);

	private static final String BASE_CONFIG_FILE = "base.config.xml";
	private final static String EXT_CONFIG_FILE = "ext.config.xml";
	private final static String USER_CONFIG_FILE = "snaker.xml";

	/**
	 * 访问数据库的对象，根据使用的orm框架进行设置。如果未提供此项设置，则按照默认orm加载方式初始化 jdbc:DataSource
	 * hibernate:SessionFactory mybatis:SqlSessionFactory
	 */
	private Object accessDBObject;

	/**
	 * 事务拦截器抽象类
	 */
	private TransactionInterceptor interceptor = null;

	/**
	 * 需要事务管理的class类型
	 */
	private Map<String, Class<?>> txClass = new HashMap<>();

	/**
	 * 无参构造方法，创建简单的Context实现类，并调用{@link Configuration#Configuration(Context)}
	 */
	public Configuration() {
		this(new SimpleContext());
	}

	/**
	 * 根据服务查找实现类构造配置对象
	 * 
	 * @param context 上下文实现
	 */
	public Configuration(Context context) {
		ServiceContext.setContext(context);
	}

	/**
	 * 构造SnakerEngine对象，用于api集成 通过SpringHelper调用
	 * 
	 * @return SnakerEngine
	 * @throws SnakerException
	 */
	public SnakerEngine buildSnakerEngine() throws SnakerException {
		LOGGER.info("Service parsing start......");
		// 依次解析框架固定的配置及用户自定义的配置 固定配置文件:base.config.xml 扩展配置文件:ext.config.xml
		// 用户自定义配置文件:snaker.xml
		parser(USER_CONFIG_FILE);
		parser(BASE_CONFIG_FILE);

		if (!isCMB()) {
			parser(EXT_CONFIG_FILE);

			for (String key : txClass.keySet()) {
				Class<?> clz = txClass.get(key);
				Object instance = interceptor == null ? clz : interceptor.getProxy(clz);
				ServiceContext.put(key, instance);
			}
		}
		
		LOGGER.info("Service parsing finish......");
		// 由服务上下文返回流程引擎
		SnakerEngine configEngine = ServiceContext.getEngine();

		if (configEngine == null)
			throw new SnakerException("配置无法发现SnakerEngine的实现类");

		LOGGER.info("SnakerEngine be found:" + configEngine.getClass());

		return configEngine.configure(this);
	}

	/**
	 * 解析给定resource配置，并注册到ServiceContext上下文中
	 * 
	 * @param resource 资源
	 */
	private void parser(String resource) {
		XMLHelper.xPath(Version.srcFolder + File.separator + resource, "config", configNode -> {
			NodeList nodeList = configNode.getChildNodes();

			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;

					String name = element.getAttribute("name");
					String className = element.getAttribute("class");
					String proxy = element.getAttribute("proxy");

					if (CommonUtil.isEmptyString(name))
						name = className;

					if (ServiceContext.exist(name)) {
						LOGGER.warning("Duplicate name is:" + name);
						continue;
					}

					Class<?> clazz = ReflectUtil.getClassByName(className);

					if (TransactionInterceptor.class.isAssignableFrom(clazz)) {
						interceptor = (TransactionInterceptor) ReflectUtil.newInstance(clazz);
						ServiceContext.put(name, interceptor);
						continue;
					}

					if (proxy != null && proxy.equalsIgnoreCase("transaction")) {
						txClass.put(name, clazz);
					} else {
						ServiceContext.put(name, clazz);
					}

				}
			}
		});
	}

	/**
	 * 初始化DBAccess的数据库访问对象
	 * 
	 * @param dbObject 数据访问对象
	 * @return Configuration
	 */
	public Configuration initAccessDBObject(Object dbObject) {
		this.accessDBObject = dbObject;
		return this;
	}

	/**
	 * 返回DBAccess的数据库访问对象
	 * 
	 * @return 数据访问对象
	 */
	public Object getAccessDBObject() {
		return accessDBObject;
	}

	/**
	 * 返回是否容器托管的bean
	 * 
	 * @return 是否容器托管
	 */
	public boolean isCMB() {
		return false;
	}
}
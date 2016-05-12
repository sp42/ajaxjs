作者
https://github.com/zhwj184/pagecache
http://blog.csdn.net/zhongweijian/article/details/7625289
http://blog.csdn.net/zhongweijian/article/details/7625291
基于servlet的页面级缓存框架的基本用法：
在web.xml里面配置

urlPattern：需要缓存的页面url的正则表达式列表，多个不同url的正则用，分隔；
cacheExpireTime：对应urlPattern的每个url的缓存时间，单位秒，用，分隔；
includeParams：对应urlPattern的每个url需要考虑的参数列表，每个url的参数用；分隔，每个url的多个参数用，号分隔；需要配置了该参数，则不考虑excludeParams的参数配置；
excludeParams：对应urlPattern的每个url需要排除的参数列表，每个url的参数用；分隔，每个url的多个参数用，号分隔；
cacheStore：缓存策略，这里提供基本本地的缓存LRU实现SimpleLRUCacheStore，用类名，可以通过实现org.pagecache.cache.CacheStore接口来实现自己缓存存储策略，常用的可以用memcache，后续提供

cacheStoreParams：对应cacheStore的缓存策略类参数列表，参数之间用；号分隔，参数名和参数值用：分隔，在init参数中可以根据参数做一些初始化工作；



	<filter>
	<filter-name>pageCacheFilter</filter-name>
	<filter-class>org.pagecache.servlet.PageCacheFilter</filter-class>
	<init-param>
		<param-name>urlPattern</param-name>
		<param-value>/pageTest/plugin/[a-zA-z0-9]+.htm[lL],/pageTest/plugin3/[a-zA-z0-9]+.htm[lL]</param-value>
	</init-param>
	<init-param>
		<param-name>cacheExpireTime</param-name>
		<param-value>50,40</param-value>
	</init-param>
	<init-param>
		<param-name>includeParams</param-name>
		<param-value>id,name;brandId,brandName</param-value>
	</init-param>
	<init-param>
		<param-name>excludeParams</param-name>
		<param-value>tracelog;tracelog</param-value>
	</init-param>
	<init-param>
		<param-name>cacheStore</param-name>
		<param-value>org.pagecache.cache.SimpleLRUCacheStore</param-value>
	</init-param>	
			<init-param>
		<param-name>cacheStoreParams</param-name>
		<param-value>cachesize:5</param-value>
	</init-param>			
	</filter>
	<filter-mapping>
		<filter-name>pageCacheFilter</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>



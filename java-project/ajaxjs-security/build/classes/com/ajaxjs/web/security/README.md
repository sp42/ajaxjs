webSecurity
http://blog.csdn.net/zhongweijian/article/details/8680737
https://github.com/zhwj184/webSecurity
===========

web安全框架,主要用servlet filter方式覆盖httpServletRequest和HttpServletResponse方式增加一些输入输出的过滤，

1. XSS过滤（获取用户输入参数和参数值进行XSS过滤，对Header和cookie value值进行XSS过滤（转码Script标签的< > 符号），

2. 对Response的setStatus(int sc, String sm)方法 sm错误信息进行XSS过滤；

3. 对Header的CLRF进行过滤；

4. 对cookie大小和cookie的白名单进行验证；

 

7. CSRF攻击 tokenID防御支持；

8. SESSION通过加密存储到cookie支持；



	redirectWhiteList：是配置重定向白名单url参数；



	<filter>
		<filter-name>HttpSessionCookitStoreFilter</filter-name>
		<filter-class>org.websecurity.filter.HttpSessionCookitStoreFilter</filter-class>
		<init-param>	
			<param-name>encryKey</param-name>
			<param-value>1234567887654321</param-value>
		</init-param>
	</filter>

	<filter>
		<filter-name>DefaultBaseSecurityFilter</filter-name>
		<filter-class>org.websecurity.DefaultBaseSecurityFilter</filter-class>
		<init-param>
			<param-name>securityFilterList</param-name><!-- ,org.websecurity.filter.CsrfTokenCkeckFilter -->
			<param-value>org.websecurity.filter.CookieWhiteListFilter,org.websecurity.filter.FormPostPermitCheckFilter</param-value>
		</init-param>
		<init-param>
			<param-name>cookieWhiteList</param-name>
			<param-value>id,JESSIONID,name,clrf</param-value>
		</init-param>
		<init-param>
			<param-name>onlyPostUrlList</param-name>
			<param-value>/d/sssecurity, /user/aaa/name*</param-value><!-- 支持正则匹配 -->
		</init-param>
		<init-param>
			<param-name>whitefilePostFixList</param-name>
			<param-value>jpg,png,doc,xls</param-value>
		</init-param>
		<init-param>
			<param-name>encryKey</param-name>
			<param-value>1234567887654321</param-value>
		</init-param>
		<init-param>
			<param-name>redirectWhiteList</param-name>
			<param-value>http://localhost:8080/[0-9A-Za-z]*,http://www.taobao.com/[0-9A-Za-z]*</param-value>
		</init-param>
	</filter>

	<filter-mapping>
		<filter-name>HttpSessionCookitStoreFilter</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>

	<filter-mapping>
		<filter-name>DefaultBaseSecurityFilter</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>

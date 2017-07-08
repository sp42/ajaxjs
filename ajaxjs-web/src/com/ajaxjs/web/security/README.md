基于 Fliter 的安全与防御框架
=================================== 

|作用|对应类名|加载方式| init-param|
|----|-----|-----|----|
|XSS过滤  |  com.ajaxjs.web.security.wrapper.XSS_Request/XSS_Response |wrapper|enableXSSFilter|
|Header CLRF 过滤  |  com.ajaxjs.web.security.wrapper.CLRF_Response |wrapper|enableCLRF_Filter|
|Cookies Key 验证和大小验证   |  com.ajaxjs.web.security.wrapper.CookieRequest/CookieResponse |wrapper|cookieWhiteList（配置白名单）|
|文件上传后缀验证   |  com.ajaxjs.web.security.wrapper.UploadRequest |wrapper|uploadfileWhiteList（配置白名单）|
|CSRF 攻击     |  com.ajaxjs.web.security.filter.CSRF |filter|encryCookieKey（配置 key）|
|Session 通过加密存储到 cookie     |  com.ajaxjs.web.security.filter.EncrySessionInCookie |filter|encryCookieKey（配置 key）|
|POST 白名单／黑名单机制验证     |  com.ajaxjs.web.security.filter.Post |filter|postWhiteList/postBlackList（配置白名单/黑名单）|
|Referer 来路检测     |  com.ajaxjs.web.security.filter.RefererFilter |filter|RefererFilter（配置 key）|

详情参见《Java Web：主动和被动方式检测安全的框架》 http://blog.csdn.net/zhangxin09/article/details/54881190

web.xml 增加下面过滤器

	<!-- 防御 -->
	<filter>
		<filter-name>SecurityFilter</filter-name>
		<filter-class>com.ajaxjs.web.security.ConfigLoader</filter-class>
		
		<!-- 是否启动 XSS 过滤 -->
		<init-param>
			<param-name>enableXSSFilter</param-name>
			<param-value>true</param-value>
		</init-param>
		
		<!-- 是否启动 CLRF 过滤 -->
		<init-param>
			<param-name>enableCLRF_Filter</param-name>
			<param-value>true</param-value>
		</init-param>
		
		<!-- SESSION 通过加密存储到 cookie -->
		<init-param>
			<param-name>encryCookieKey</param-name>
			<param-value>1234567887654321</param-value>
		</init-param>
		
		<!-- Cookies 白名单机制验证和大小验证 -->
		<init-param>
			<param-name>cookieWhiteList</param-name>
			<param-value>id,JESSIONID,name,clrf</param-value>
		</init-param>
		
		<!-- 文件上传后缀白名单 过滤 -->
		<init-param>
			<param-name>uploadfileWhiteList</param-name>
			<param-value>jpg,png,doc,xls</param-value>
		</init-param>
	
		<!-- CSRF 攻击 过滤 -->
		<init-param>
			<param-name>CSRF_Filter</param-name>
			<param-value>true</param-value>
		</init-param>
	
		<!-- POST 白名单／黑名单机制验证（支持正则匹配） -->
		<init-param>
			<param-name>postWhiteList</param-name>
			<param-value>/d/sssecurity, /user/aaa/name*</param-value>
		</init-param>
		<init-param>
			<param-name>postBlackList</param-name>
			<param-value>true</param-value>
		</init-param>
		
		<!-- 配置 Security 异常发生后跳转 url 参数 -->
		<init-param>
			<param-name>redirectUrl</param-name>
			<param-value>/showErr.jsp?msg=</param-value>
		</init-param>
	</filter>
	
	<filter-mapping>
		<filter-name>SecurityFilter</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>
	<!-- // -->
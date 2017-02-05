基于 Fliter 的安全框架
=================================== 
- XSS过滤（获取用户输入参数和参数值进行XSS过滤，对Header和cookie value值进行XSS过滤（转码Script标签的< > 符号）
- CSRF 攻击：采用 tokenID 防御支持
- 
- 

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
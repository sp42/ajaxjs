基于 Fliter 的安全框架
=================================== 
- XSS过滤（获取用户输入参数和参数值进行XSS过滤，对Header和cookie value值进行XSS过滤（转码Script标签的< > 符号）
- CSRF 攻击：采用 tokenID 防御支持
- POST 白名单／黑名单机制验证
- Cookies 白名单机制验证和大小验证
- Referer 检测

|作用|对应类名|加载方式| init-param|
|----|-----|-----|----|
|XSS过滤  |  com.ajaxjs.web.security.filter.XSS_Request/XSS_Response |wrapper|enableXSSFilter|
|SESSION 通过加密存储到 cookie     |  com.ajaxjs.web.security.filter.EncrySessionInCookie |filter|encryCookieKey（配置 key）|
|Cookies Key 验证和大小验证   |  com.ajaxjs.web.security.filter.CookieRequest/CookieResponse |wrapper|cookieWhiteList（配置白名单）|
|文件上传后缀验证   |  com.ajaxjs.web.security.filter.UploadRequest |wrapper|uploadfileWhiteList（配置白名单）|
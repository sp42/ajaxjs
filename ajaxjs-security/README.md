基于 Fliter 的安全框架
=================================== 
- CSRF 攻击：采用 tokenID 防御支持
- POST 白名单／黑名单机制验证
- Cookies 白名单机制验证和大小验证
- Referer 检测
- 对文件上传后缀白名单进行验证

|作业|对应类名|加载方式|
|----|-----|-----|
|SESSION 通过加密存储到 cookie 支持   |  com.ajaxjs.web.security.filter.EncrySessionInCookie |filter|
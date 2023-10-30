




| 工程/模块                 | 说明                                              | 对应 OAuth/OIDC 角色                                              |
|-----------------------|-------------------------------------------------|---------------------------------------------------------------|
| aj-iam-client         | OAuth  的 客户端 Client                | OAuth.Client   <br/>及<br/>OIDC.Relying Party（RP）              |
| aj-iam-resource-server | OAuth  的 ResourceServer 资源服务器的 SDK，主要提供一个拦截器保护资源 | OAuth.ResourceServer（RS）                                      |
| aj-iam-server | OAuth  的 Authentication Server，授权服务器 | OAuth.Authentication Server（AS）<br/>及<br/>OIDC.OpenID Provider（OP） |


- 多数时候，client 与 resource-server 可以合二为一。
- `aj-iam-client`、`aj-iam-resource-server` 代码量比较少，或者不用 SDK 整合，直接拷贝代码到你的工程即可


# Aj-SSO 单点登录用户系统

参见我博客：

- [SSO 轻量级实现指南（原生 Java 实现）：SSO Server 部分](https://zhangxin.blog.csdn.net/article/details/123292897)
- [SSO 轻量级实现指南（原生 Java 实现）：SSO Client 部分](https://zhangxin.blog.csdn.net/article/details/124401580)

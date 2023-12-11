




| 工程/模块                 | 说明                                              | 对应 OAuth/OIDC 角色                                              |
|-----------------------|-------------------------------------------------|---------------------------------------------------------------|
| aj-iam-client         | OAuth  的 客户端 Client                | OAuth.Client   <br/>及<br/>OIDC.Relying Party（RP）              |
| aj-iam-resource-server | OAuth  的 ResourceServer 资源服务器的 SDK，主要提供一个拦截器保护资源 | OAuth.ResourceServer（RS）                                      |
| aj-iam-server | OAuth  的 Authentication Server，授权服务器 | OAuth.Authentication Server（AS）<br/>及<br/>OIDC.OpenID Provider（OP） |


- 多数时候，client 与 resource-server 可以合二为一。
- `aj-iam-client`、`aj-iam-resource-server` 代码量比较少，或者不用 SDK 整合，直接拷贝代码到你的工程即可



# 可配置
强调系统的鲁棒性，从最简单的组件到复杂的组件，均可支持，例如，缓存组件，简单点的你可以使用本地的 Servlet Session，高级一点的可以采用 Redis。

# 部署
目标是部署简单，且灵活多种方式。下面是 SSO 中心的部署方式。
||单独部署|整合部署|
|---|---|---|
|部署形态| WAR/JAR 包，单独进程运行|JAR 包引入依赖，或源码整合|
|优点|独立维护|占资源更低|

除了 SSO 中心的部署，还有客户端以 JAR 包的形式提供。






更高阶的产品 IAM（Identity and Access Management），即“身份识别与访问管理”，具有单点登录、强大的认证管理、基于策略的集中式授权和审计、动态授权、企业可管理性等功能。参考开源项目 [Keycloak](https://github.com/keycloak)。


# Aj-SSO 单点登录用户系统

参见我博客：

- [SSO 轻量级实现指南（原生 Java 实现）：SSO Server 部分](https://zhangxin.blog.csdn.net/article/details/123292897)
- [SSO 轻量级实现指南（原生 Java 实现）：SSO Client 部分](https://zhangxin.blog.csdn.net/article/details/124401580)

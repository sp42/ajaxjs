
```java
@Value("${auth.excludes: }")
private String excludes;

/**
 * 加入认证拦截器
 */
@Override
public void addInterceptors(InterceptorRegistry registry) {
    LogHelper.p("初始化 SSO 拦截器");
    InterceptorRegistration interceptorRegistration = registry.addInterceptor(authInterceptor());
    interceptorRegistration.addPathPatterns("/**"); // 拦截所有

    // 不需要的拦截路径
    if (StringUtils.hasText(excludes)) {
        String[] arr = excludes.split("\\|");
        interceptorRegistration.excludePathPatterns(arr);
    }
}
```
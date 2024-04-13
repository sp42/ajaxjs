# S3 存储 Java 客户端

安装

```xml
<dependency>
    <groupId>com.ajaxjs</groupId>
    <artifactId>aj-s3client</artifactId>
    <version>1.0</version>
</dependency>
```

详见[教程](https://blog.csdn.net/zhangxin09/article/details/137671230)。

单元测试的配置文件application.yml没有随着 VCS 提交，其内容如下：

```yaml
S3Storage:
  Nso:
    accessKey: xx
    accessSecret: xx
    api: nos-eastchina1.126.net
    bucket: xx
  Oss:
    accessKeyId: xx
    secretAccessKey: xx
    endpoint: oss-cn-beijing.aliyuncs.com
    bucket: xx
  LocalStorage: # 本地保存
    absoluteSavePath: c:\temp\ # 若有此值，保存这个绝对路径上
```
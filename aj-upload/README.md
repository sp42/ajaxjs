# 统一文件上传服务

提供文件上传接口。可支持下面两种存储方案：

- 有云存储的，如 S3、OSS 的。先上传到当前服务器，再上传到云存储
- 没有云存储的，上传到本地服务器的磁盘即可，并提供一个静态服务访问资源

云存储当前支持：

- 阿里 OSS
- 网易云对象存储

TODO：

- 当期只限图片上传，未来提供附件上传（任意文件）
- 客户端管理，客户端的认证

# 用法

该服务只有一个接口： `/upload` （你可以修改 Controller 的路径），客户端调用如下数据：

```html
 <form method="POST" action="/upload/upload" enctype="multipart/form-data">
    File to upload: <input type="file" name="file">
    <input type="submit" value="Upload"> Press here to upload the file!
 </form>
```
	 
	 
上传成功返回：

```json
{
    "isOk": true,
    "msg": "上传成功",
    "filename": "9274656743690240.webp",
    "url": "https://xxxxx/9274656743690240.webp"
}
```

在 Classpath 目录下新建一个 `application.yml` 配置文件，内容如下：

```yaml
# 文件上传
FileUpload:
  maxSingleFileSize: 1 # 单位 mb
  allowExtFilenames: jpeg, jpg, png, gif, bmp, webp
  isRename: true
  FILE_URL_ROOT: https://leidong.nos-eastchina1.126.net
  saveFolder: attachment

# 云存储，对象存储
S3Storage:
 Nso: # 网易云
  accessKey: 4c2396f706744a74ba6b8319e1099b60
  accessSecret: 67f070468939410491c54d6979614980
  api: https://leidong.nos-eastchina1.126.net/
  bucket: leidong
 Oss: # 阿里 OSS
  accessKeyId: LTAI4FtWXD5P4cCAxPcb7apP
  secretAccessKey: i6HPqz4AlTZUPkVQSj7Pr74yQsK69Q
  endpoint: oss-cn-beijing.aliyuncs.com
  bucket: leyou-cxk
 LocalStorage: # 本地保存
  absoluteSavePath: c:\temp\ # 若有此值，保存这个绝对路径上
```
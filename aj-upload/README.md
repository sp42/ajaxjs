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
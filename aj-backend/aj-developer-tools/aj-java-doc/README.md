# 裁剪自 JDK tools.jar 的关于 JavaDoc 源码

tools.jar 是 JDK 自带的，但要你额外引入到工程中，没有 Maven 公共库，自己导入也麻烦。于是自己从源码裁剪出来，只是 JavaDoc 部分，其他不需要了。

- v1.0.2
    
    加入 zh-CN 资源文件
- v1.0.3

    IDEA 下不能加载 zh-CN 资源文件。明明是有的，Eclipse 下也正常。
现在把抛出异常的语句去掉。其实这些日志提示信息无关紧要，不影响主逻辑。

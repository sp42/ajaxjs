# 数据库简易帮助包

DB helper 只是举行轻量级简单的封装，特性有：

 - 提供增改查三种静态函数，可以传值参数。新增记录返回主键；
 - 一律采用 PreparedStatment，以防止 SQL 注入及自动值类型转换；
 - 打印运行时出错的SQL语句，其可以直接拷贝到数据库客户端上进行调试；
 - 提供简单的分页功能； 支持 JDBC 4 以及 Java 7
 - autoClose 自动关闭资源；
 - 格式化 SQL 工具函数

详细参见教程：http://blog.csdn.net/zhangxin09/article/details/55805849
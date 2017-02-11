#AJAXJS Base
------------
框架基础包，纯 Java 项目（不限定 Web/Android/Swing 环境），包括以下内容：

- util 工具包（链式风格文件处理包、Map 结构工具类、反射工具包、日志工具类、简易 ioc 依赖注射器、基于 Java Rhino 的 JSON 解析器）
- jdbc JDBC 数据工具包
- net 网络通讯包（http 链式风格请求包、mail 基于 telnet 的简易邮件发送器）
- framework 数据层服务，泛型封装 Service/DAO/Model 提供快速增删改查服务，包含分页，查询条件，整合 Validator 后端验证

如果发现缺少 HttpServletRequest 等的类，请手动把 javax-servlet.jar 加入到 classpath 中。
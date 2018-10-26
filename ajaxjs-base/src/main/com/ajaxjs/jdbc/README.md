
JDBC 封装
===============

此乃围绕数据库而设的一些工具包，大体上分为两种模块，除了一种基于 JDBC 的简单 CRUD 封装方法外，还有一种类似于 MyBatis 注解的半 ORM 方案。无论 CRUD 还是 ORM 都需要先产生一个数据库连接。

获取数据库连接对象
------------------------
试观察 com.ajaxjs.jdbc.JdbcConnection 类，提供如下方法：

	Connection getConnection(String jdbcUrl);
	Connection getConnection(String jdbcUrl, Properties props);

其中 jdbcUrl 参数形如 jdbc:sqlite:c:\project\foo\work\work.sqlite，无须传入数据库驱动字符串 Driver 名称（如“org.sqlite.JDBC”），那是采用了 JDBC4 新特性的缘故。

数据库连接池方面我们推荐 Tomcat 自带 Pool。通过下面方法可返回由连接池产生的数据库连接对象。

	 Connection conn = JdbcConnection .getDataSource("jdbc/sqlite").getConnection(); 

下面介绍简单 CRUD 模块。

简单 CRUD
----------------
简单的增删改查方法实际是基于 JDBC 浅浅的封装，仍沿用 PreparedStatment 方法传参数至 SQL。简单起见，数据格式一律使用 Map/List。主要的类是 com.ajaxjs.jdbc.Helper。

查询单个记录，将其转换为 map。支持带参数的 SQL，如：

	Map<String, Object> info = Helper.query(conn, "SELECT * FROM news WHERE id = ?", 1);

查询多个记录，将其转换为 List。
	
	List<Map<String, Object>> list = Helper.queryList(conn, "SELECT * FROM news WHERE type = 1");
	// 或
	List<Map<String, Object>> list = Helper.queryList(conn, "SELECT * FROM news WHERE type = ?", 1);
	
新增、修改和删除例子如下：

	// 新增的主键，返回为 java.io.serializable 类型，兼容 int/long/string 的情况
	Serializable newlyId = Helper.create(conn, "INSERT INTO news (name) VALUES (?)", "test2");
	
	int id = (int) newlyId;
	assertNotNull(id);
	
	int updated;// 影响的行数
	updated = Helper.update(conn, "UPDATE news SET name = ? WHERE id = ?", "Hi", id);
	assertEquals(1, updated);
	
	assertTrue(Helper.delete(conn, "news", id)); // 删除方法是本类唯一封装了 SQL 语句的方法，故需要传入表名 tableName
除 Connection 外，涉及的 JDBC 对象如 statement、resultset 对象均采用 Java 7 autoClose 自动关闭资源，无须啰嗦的手动 close()。

SimpleORM
----------------------
实体类型的数据格式若不想用 Map，可以用简单 ORM（com.ajaxjs.jdbc.SimpleORM），它类似于 [DBUtils](http://commons.apache.org/proper/commons-dbutils/)，支持 Java Bean。SimpleORM 是 Helper 的子类。
当然你仍然可以用 Map，不过那样用法和 Helper 的没什么两样：

	SimpleORM<Map> simpleORM = new SimpleORM<>(conn, Map.class);
	Map<String, Object> map = simpleORM.query("SELECT * FROM news WHERE id = ?", 1);
	List<Map> list = simpleORM.queryList("SELECT * FROM news");
支持 Bean，这才是重点。假设 News 是一个有 getter/setter 的标准 POJO，那么：

	SimpleORM<News> simpleORM = new SimpleORM<>(conn, News.class);
	News news = simpleORM.query("SELECT * FROM news WHERE id = ?", 23);
	List<News> allNews = simpleORM.queryList("SELECT * FROM news");
	// POJO
	News news = new News();
	news.setName("标题一");
	int newlyId = (int)simpleORM.create(news, "news");// 新建
	
	news.setName("标题二");
	news.setId(new Long(newlyId)); // 修改刚刚生成的记录
	int effectRows = (int)simpleORM.update(news, "news");

分页
------------------
自动生成求记录总数的 SQL，让开发者写的 sql 里面包含分页的操作，该类自动处理。如果 > 0 则允许进行分页；可指定起始数、每页记录行数。
	
	// 列表
	SimplePager sp = new SimplePager(conn, sql, request.getParameter("start"));
	
	// 每页显示数量
	if(request.getParameter("limit") != null) {
	  sp.setPageSize(Integer.parseInt(request.getParameter("limit")));
	}
	PageResult<Map<String, Object>> pr = sp.getResult();
	
	request.setAttribute("PageResult", pr);
	request.getRequestDispatcher("/asset/jsp/admin/admin_table.jsp").include(request, response);

返回 PageResult 对象供 页面 渲染。PageResult 是一个 Java Bean。

打印最终 SQL
--------------------
这是 AJAXJS-Data 的一个特性，以方便开发者直观观察执行了什么 SQL 语句，可以将其直接拷贝到数据库客户端上进行调试，并提供格式化 SQL 工具函数，打印日志时更美观。

![这里写图片描述](https://images.gitee.com/uploads/images/2018/0722/090613_b7b1a4d3_784269.png)

Java 动态 SQL
--------------------
提炼了 MyBatis 的 SQL Builder 包，可以通过 Java 动态拼接 SQL，详见 com.ajaxjs.jdbc.sqlbuilder 包。


支持的数据库
------------
当前支持 MySQL 和 SQLite。

framework
-----------
为什么叫 framework？因为说明这是为更高级的数据业务层框架所服务的，不仅仅是简单的 CRUD，而是要考虑更多情况的了。framework 提供泛型封装 Service/DAO/Model 提供快速增删改查服务，包含分页，查询条件，整合 Validator 后端验证。该模块包含以下子模块：

- dao，Data Access Object 数据访问对象
- model，数据模型
- service，业务服务层

讨论 framework 起来篇幅比较大，我们择日再讲。

鸣谢开源项目 

https://gitee.com/bitprince/memory

文档参考了不少，它的思路跟我的差不多的。另外下面是我之前的尝试：

- http://blog.csdn.net/zhangxin09/article/details/17403211
- http://blog.csdn.net/zhangxin09/article/details/7899525
- http://blog.csdn.net/zhangxin09/article/details/55805849
- http://blog.csdn.net/zhangxin09/article/details/70187712
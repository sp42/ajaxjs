# 数据库简易帮助包

为数据库连接、查询和分页提供了简单的方法。

数据库连接 JdbcConnection
--------------

**获取数据源 DataSource**

数据源提供了一种简单获取数据库连接的方式，并能在内部通过一个池的机制来复用数据库连接，这样就大大减少创建数据库连接的次数，提高了系统性能。另外由于使用了 datasource，就不需要手动关闭连接——如果使用 connection，就需要自己关闭连接。对于数据源的应用，一般都选择实用开源的数据源或数据库连接池来使用，当前我们这个框架使用了 Tomcat 自带 Pool。

	// 简写方式
	javax.sql.DataSource ds = (javax.sql.DataSource)new InitialContext().lookup("java:/comp/env/jdbc/derby");
	// 通过数据源对象获得数据库连接对象
	Connection connection ds..getConnection();
	
**经典方式连接数据库**

相当于传统方式连接数据库，提供如下方法：

	Connection getConnection(String jdbcUrl);
	Connection getConnection(String jdbcUrl, Properties props);

其中 jdbcUrl 参数形如 jdbc:sqlite:c:\\project\\foo\\work\\work.sqlite，无须传入数据库驱动字符串 Driver 名称（如 "org.sqlite.JDBC"），这是采用了 JDBC4 新特性的缘故，如果你的 JDK 不支持 JDBC4，可以退回旧写法：

	Connection conn = null;
	try {
		if (props == null)
			conn = DriverManager.getConnection(jdbcUrl);
		else
			conn = DriverManager.getConnection(jdbcUrl, props);
		LOGGER.info("数据库连接成功： " + conn.getMetaData().getURL());
	} catch (SQLException e) {
		LOGGER.warning("数据库连接失败！", e);
		try { // jdbc 4的新 写法可不用 Class.forName，如果不支持，退回旧写法
			Class.forName(driver);
		} catch (ClassNotFoundException e1) {
			LOGGER.warning("创建数据库连接失败，请检查是否安装对应的 Driver:{0}。", driver);
		}
	}

数据库数据的增删改查
----------------------
**查询**

先连接数据库：

	Connection conn = getConnection("jdbc:sqlite:c:\\project\\foo\\work\\work.sqlite");
查询单个记录，将其转换为 map。

	Map<String, Object> info = Helper.queryMap(conn, "SELECT * FROM news WHERE id = 1");

查询多个记录，将其转换为 List<map>。

	List<Map<String, Object>> list = Helper.queryList(conn, "SELECT * FROM news WHERE");
	
均采用 Java 7 autoClose 自动关闭资源，无须手动 close()。

**创建**

为防止 SQL 注入，推荐使用 PreparedStatement。

	String createSql = "INSERT INTO myblog (name, content, uid, brief, catalog, createDate) values(?, ?, ?, ?, ?, datetime('now'))"; 
	try (PreparedStatement ps = conn.prepareStatement(createSql);) {
		ps.setString(1, request.getParameter("name"));
		ps.setString(2, request.getParameter("content"));
		ps.setString(3, Util.getUUID());
		ps.setString(4, request.getParameter("brief") == null ? "" : request.getParameter("brief"));
		ps.setInt(5, Integer.parseInt(request.getParameter("catalog")));
		
		if (ps.executeUpdate() == 0) {
			request.setAttribute("errMsg", "创建失败！");
		} else {
			request.setAttribute("okMsg", "创建成功！");
		}
	}

更新和删除如创建那样子，并没有进行封装，仍需要开发者自己写出。

分页
----------------	
自动生成求记录总数的 SQL，如果 > 0 则允许进行分页；可指定起始数、每页记录行数。

	SimplePager sp = new SimplePager(conn, sql, request.getParameter("start"));
	PageResult<Map<String, Object>> pr = sp.getResult();
	
返回 PageResult 对象供 页面 渲染。PageResult 是一个 Java Bean。
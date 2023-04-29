
public class Connection {
    /**
     * 根据 JNDI 获取数据源。这是配套 Tomcat 自带 Pool 的数据库连接池服务。
     *
     * @param jndi JNDI 的路径，参阅 META-INF/context.xml
     * @return 数据源对象
     */
    public static DataSource getDataSourceByJNDI(String jndi) {
        try {
            Object obj = new InitialContext().lookup("java:/comp/env");
            Objects.requireNonNull(obj, "没有该节点 java:/comp/env");

            Context context = (Context) obj; // 环境变量
            Object result = context.lookup(jndi);
            return (DataSource) result;
        } catch (NamingException e) {
            String msg = "读取数据源的配置文件失败，请检查 Tomcat 连接池配置！ path: " + jndi;
            msg += " 提示：没发现数据库 /WebRoot/META-INF/context.xml 下的 XML 配置文件，该文件位置一般不可移动，请参阅 TomcatPool 数据库连接池的相关文档。";
            LOGGER.warning(msg, e);

            return null;
        }
    }

    /**
     * 根据 JNDI 路径获得数据库连接对象。这是配套 Tomcat 自带 Pool 的数据库连接池服务。
     *
     * @param jndi JNDI 的路径，参阅 META-INF/context.xml
     * @return 数据库连接对象
     */
    public static Connection getConnectionByJNDI(String jndi) {
        return getConnection(Objects.requireNonNull(getDataSourceByJNDI(jndi)));
    }

    /**
     * 初始化数据库连接并保存到 ThreadLocal 中。这是框架内最主要的调用数据库连接方法，带有池化的服务。
     *
     * @param jndi JNDI 的路径，参阅 META-INF/context.xml
     */
    public static void initDbByJNDI(String jndi) {
        Objects.requireNonNull(jndi, "缺少 jndiPath 参数！");
        LOGGER.info("启动[{0}]数据库 JNDI 连接", jndi);

        try {
            if (getConnection() == null || getConnection().isClosed()) {
                Connection conn = getConnectionByJNDI(jndi);
                setConnection(conn);
                LOGGER.info("成功连接数据库[{0}]", conn);
            }
        } catch (SQLException e) {
            LOGGER.warning(e);
        }
    }

    /**
     * 创建 SQLite 数据库连接对象（测试用）
     *
     * @return 数据库连接对象
     */
    public static Connection getTestSqliteConnection() {
        return JdbcConnection.getSqliteConnection(Resources.getResourcesFromClasspath("com\\ajaxjs\\sql\\test_used_database.sqlite"));
    }

    @Test
    public void testGetSqliteConnection() throws SQLException {
        Connection conn = getTestSqliteConnection();
        assertNotNull(conn);
        conn.close();
    }
    //	@Test
    public void testThreadLocal() {
        Connection conn = getTestSqliteConnection();
        setConnection(conn);
        assertNotNull(getConnection());
        JdbcConnection.closeDb();
        addSql("SELECT * FROM news");
    }
}
package myblog;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ajaxjs.framework.model.PageResult;
import com.ajaxjs.jdbc.Helper;
import com.ajaxjs.jdbc.SimplePager;

/**
 * Servlet implementation class Index
 */
@WebServlet(urlPatterns = { "/index", "/detail" })
public class Index extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	final static boolean isDebug = true;

	public static String db_context_path = isDebug ? "jdbc/sqlite_mac" : "jdbc/sqlite_deploy";
	
	 @Override
	 public void init(ServletConfig config) throws ServletException {
		 config.getServletContext().setAttribute("isDebug", isDebug);
	 }
	
	static String sql = "SELECT myblog.*, catalog.name AS catalogName FROM myblog INNER JOIN catalog ON myblog.catalog = catalog.id WHERE 1 = 1 ORDER BY createDate DESC";

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(final HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try(Connection conn = Helper.getConnection(Helper.getDataSource(db_context_path));) {
			request.setAttribute("catalogs", Helper.queryList(conn, "SELECT * FROM catalog"));
			if (request.getRequestURI().indexOf("index") != -1) { // 列表
				String sql = Index.sql;
				
				// 指定分类
				String catalogId = request.getParameter("catalogId");
				if(catalogId != null) {
					sql = sql.replace(" WHERE 1 = 1", " WHERE catalog = " + catalogId);
				}
				
				SimplePager sp = new SimplePager(conn, sql, request.getParameter("start"));
				PageResult<Map<String, Object>> pr = sp.getResult();
				
				request.setAttribute("pageInfo", pr);
				request.setAttribute("list", pr.getRows());
			} else if (request.getRequestURI().indexOf("detail") != -1) { // 详情		
				Map<String, Object> info = Helper.queryMap(conn, sql.replace("WHERE 1 = 1", "WHERE myblog.id = " + request.getParameter("id")));
				request.setAttribute("info", info);
				request.setAttribute("neighbor", Helper.getNeighbor(conn, "myblog", strToDate(info.get("createDate").toString())));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.getRequestDispatcher("/asset/jsp/index.jsp").forward(request, response);
	}
	
	/**
	 * 字符串转换成日期
	 * 
	 * @param str
	 * @return date
	 */
	public static String strToDate(String str) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return format.format(date);
	}
}

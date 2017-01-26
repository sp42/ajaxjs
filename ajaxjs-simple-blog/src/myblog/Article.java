package myblog;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ajaxjs.framework.model.PageResult;
import com.ajaxjs.jdbc.Helper;
import com.ajaxjs.jdbc.SimplePager;
import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.Util;

/**
 * Servlet implementation class Article
 */
@WebServlet("/Article/*")
public class Article extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 验证用户是否已经登录，如果登录了便进行下一步操作。
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public static boolean checkAuth(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");

		if (Admin.checkAuth(request)) {
			return true;
		} else {
			response.getWriter().append("Served at: ").append(request.getContextPath());
			return false;
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			if (checkAuth(request, response)) {
				String uri = request.getRequestURI();
				request.setAttribute("uiName", "文章");

				try (Connection conn = Helper.getConnection(Helper.getDataSource(Index.db_context_path));) {
					request.setAttribute("catalogs", getCatalogs(conn));
					
					if (uri.contains("create")) {
						// 创建
						request.setAttribute("isCreate", true);
						request.getRequestDispatcher("/WEB-INF/jsp/admin/edit.jsp").include(request, response);
					} else if (uri.contains("edit/")) {
						// 编辑
						request.setAttribute("isCreate", false);
						String id = StringUtil.regMatch("\\d+$", uri);
						Map<String, Object> info = Helper.queryMap(conn, "SELECT * FROM myblog WHERE id = " + id);
						request.setAttribute("info", info);
						request.getRequestDispatcher("/WEB-INF/jsp/admin/edit.jsp").include(request, response);
					} else {
						String sql = Index.sql;
						
						// 指定分类
						String catalogId = request.getParameter("catalogId");
						if(catalogId != null) {
							sql = sql.replace(" WHERE 1 = 1", " WHERE 1 = 1 AND catalog = " + catalogId);
						}
						
						// 搜索
						String keyword = request.getParameter("keyword");
						if(keyword != null) {
							String search = " WHERE 1 = 1 AND (myblog.name LIKE '%" + keyword + "%' OR myblog.content LIKE '%" + keyword + "%')";
//							System.out.println(String.format(search, keyword, keyword));
						
							sql = sql.replace(" WHERE 1 = 1", search);
							System.out.println(sql);
						}
						
						// 列表
						SimplePager sp = new SimplePager(conn, sql, request.getParameter("start"));
						
						// 每页显示数量
						if(request.getParameter("limit") != null) {
							sp.setPageSize(Integer.parseInt(request.getParameter("limit")));
						}
						PageResult<Map<String, Object>> pr = sp.getResult();

						request.setAttribute("PageResult", pr);
						request.getRequestDispatcher("/WEB-INF/jsp/admin/admin_table.jsp").include(request, response);
					}
				}
			}
		} catch (Exception e) {
			response.getWriter().append("Error: " + e);
			e.printStackTrace();
		}
	}

	private List<Map<String, Object>> getCatalogs(Connection conn) {
		return Helper.queryList(conn, String.format("SELECT * FROM catalog"));
	}

	private final static String createSql = "INSERT INTO myblog (name, content, uid, brief, catalog, createDate) values(?, ?, ?, ?, ?, datetime('now'))";

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("创建实体");

		action(request, response, new Action() {
			@Override
			public void doAction(HttpServletRequest request, HttpServletResponse response, Long id, Connection conn) throws Exception {
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
			};
		});
	}

	private final static String updateSql = "UPDATE myblog SET name = ?, content = ?, brief = ?, catalog = ? WHERE id = ?";

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("修改实体");

		action(request, response, new Action() {
			@Override
			public void doAction(HttpServletRequest request, HttpServletResponse response, Long id, Connection conn) throws Exception {
				Map<String, Object> data = Utils.getPutRequestData(request);
				try (PreparedStatement ps = conn.prepareStatement(updateSql);) {
					ps.setString(1, data.get("name").toString());
					ps.setString(2, data.get("content").toString());
					ps.setString(3, data.get("brief") == null ? "" : data.get("brief").toString());
					ps.setInt(4, (int)data.get("catalog"));
					ps.setLong(5, id);

					if (ps.executeUpdate() == 0) {
						request.setAttribute("errMsg", "创建失败！");
					} else {
						request.setAttribute("okMsg", "创建成功！");
					}
				}
			};
		});
	}

	private final static String deleteSql = "DELETE FROM myblog WHERE id = ?";

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("删除实体");
		
		action(request, response, new Action() {
			@Override
			public void doAction(HttpServletRequest request, HttpServletResponse response, Long id, Connection conn) throws Exception {
				try (PreparedStatement ps = conn.prepareStatement(deleteSql);) {
					ps.setLong(1, id);

					if (ps.executeUpdate() == 0) {
						request.setAttribute("errMsg", "删除失败！");
					} else {
						request.setAttribute("okMsg", "删除成功！");
					}
				}
			}
		});
	}
	
	public static interface Action {
		public void doAction(HttpServletRequest request, HttpServletResponse response, Long id, Connection conn) throws Exception;
	};

	public static void action(HttpServletRequest request, HttpServletResponse response, Action action) throws ServletException, IOException {
		try {
			if (checkAuth(request, response)) {
				String id = StringUtil.regMatch("\\d+$", request.getRequestURI());

				try (Connection conn = Helper.getConnection(Helper.getDataSource(Index.db_context_path));) {
					action.doAction(request, response, id != null ? Long.parseLong(id) : null, conn);
				}
			}
		} catch (Exception e) {
			request.setAttribute("errMsg", "异常信息：" + e);
			e.printStackTrace();
		}

		request.getRequestDispatcher("/WEB-INF/jsp/admin/json-msg.jsp").include(request, response);
	}

}

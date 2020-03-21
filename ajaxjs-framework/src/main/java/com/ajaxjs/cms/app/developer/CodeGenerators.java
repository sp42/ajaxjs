package com.ajaxjs.cms.app.developer;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.ajaxjs.cms.utils.codegenerators.Utils;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 代码生成器
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Path("/admin/CodeGenerators")
public class CodeGenerators implements IController {
	private static final LogHelper LOGGER = LogHelper.getLog(CodeGenerators.class);

	static private String saveFolder = "C:\\temp";
	static private String packageName = "com.ajaxjs.user.role";
	static private String dbUrl = "jdbc:mysql://127.0.0.1/zyjf";
	static private String dbUser = "";
	static private String dbPassword = "";
	static private String zipSave = "/download/code.zip";

	@POST
	public String doGet(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException {
		if (request.getParameter("saveFolder") != null)
			saveFolder = request.getParameter("saveFolder");
		if (request.getParameter("packageName") != null)
			packageName = request.getParameter("packageName");
		if (request.getParameter("dbUrl") != null)
			dbUrl = request.getParameter("dbUrl");
		if (request.getParameter("dbUser") != null)
			dbUser = request.getParameter("dbUser");
		if (request.getParameter("dbPassword") != null)
			dbPassword = request.getParameter("dbPassword");

		String pojoSave = saveFolder + "\\model\\%s.java", daoSave = saveFolder + "\\dao\\%s.java",
				serviceSave = saveFolder + "\\service\\%s.java", controllerSave = saveFolder + "\\controller\\%s.java";
		Utils.mkdir(new String[] { saveFolder + "\\model", saveFolder + "\\dao", saveFolder + "\\service",
				saveFolder + "\\controller" });

		ServletContext sc = request.getServletContext();
		request.setAttribute("packageName", packageName);

		Connection conn = Utils.getMySqlDataSource(dbUrl, dbUser, dbPassword);

		// 是否生成model，默认为
		boolean isMap = "true".equals(request.getParameter("isMap"));

		if (request.getParameter("getTable") != null) {
			String tableName = request.getParameter("getTable");
			List<Map<String, String>> columnComments = DataBaseStruController.getColumnCommentByTableName(conn,
					tableName);
			String beanName = request.getParameter("beanName") == null ? Utils.firstLetterUpper(tableName)
					: request.getParameter("beanName"),
					tablesComment = DataBaseStruController.getCommentByTableName(conn, tableName);

			request.setAttribute("fields", columnComments);
			request.setAttribute("tableName", tableName);
			request.setAttribute("beanName", beanName);
			request.setAttribute("tablesComment", tablesComment);
			request.setAttribute("tablesCommentChinese", Utils.getName(tablesComment));

			render(isMap, pojoSave, daoSave, serviceSave, controllerSave, beanName, request, response, sc);

		} else {
			List<String> tables = DataBaseStruController.getAllTableName(conn);

			for (String tableName : tables) {
				String beanName = Utils.firstLetterUpper(tableName);
				Map<String, String> tablesComment = DataBaseStruController.getCommentByTableName(conn, tables);
				Map<String, List<Map<String, String>>> infos = DataBaseStruController.getColumnCommentByTableName(conn,
						tables);

				request.setAttribute("fields", infos.get(tableName));
				request.setAttribute("tableName", tableName);
				request.setAttribute("beanName", beanName);
				request.setAttribute("tablesComment", tablesComment);

				render(isMap, pojoSave, daoSave, serviceSave, controllerSave, beanName, request, response, sc);
			}
		}

		try {
			conn.close();
		} catch (SQLException e) {
			LOGGER.warning(e);
		}

		return "html::Done!<a href=\"" + request.getContextPath() + zipSave + "\" download>download</a>";
	}

	static final String tplSave = BaseController.admin("code-generator");

	/**
	 * 替换为实际内容
	 * 
	 * @param isMap
	 * @param pojoSave
	 * @param daoSave
	 * @param serviceSave
	 * @param controllerSave
	 * @param beanName
	 * @param request
	 * @param response
	 * @param sc
	 * @throws FileNotFoundException
	 */
	static void render(boolean isMap, String pojoSave, String daoSave, String serviceSave, String controllerSave,
			String beanName, HttpServletRequest request, HttpServletResponse response, ServletContext sc)
			throws FileNotFoundException {
		String dao = tplSave + "/dao.jsp", service = tplSave + "/service.jsp",
				serviceImp = tplSave + "/serviceImpl.jsp", controller = tplSave + "/controller.jsp";

		if (!isMap)
			render(tplSave + "/pojo.jsp", String.format(pojoSave, beanName), sc, request, response);
		else {
			dao = tplSave + "/dao_map.jsp";
			service = tplSave + "/service_map.jsp";
			serviceImp = tplSave + "/serviceImpl_map.jsp";
		}

		render(dao, String.format(daoSave, beanName + "Dao"), sc, request, response);
		render(service, String.format(serviceSave, beanName + "Service"), sc, request, response);
		render(serviceImp, String.format(serviceSave, beanName + "ServiceImpl"), sc, request, response);
		render(controller, String.format(controllerSave, beanName + "Controller"), sc, request, response);

//		MvcRequest r = new MvcRequest(request);
//		ZipHelper.toZip(saveFolder, r.mappath(zipSave));
	}

	/**
	 * 
	 * @author sp42 frank@ajaxjs.com
	 *
	 */
	static class sO extends ServletOutputStream {
		private ByteArrayOutputStream os;

		public sO(ByteArrayOutputStream os) {
			this.os = os;
		}

		@Override
		public void write(byte[] data, int offset, int length) {
			os.write(data, offset, length);
		}

		@Override
		public void write(int b) throws IOException {
			os.write(b);
		}

		@Override
		public boolean isReady() {
			return false;
		}

		@Override
		public void setWriteListener(WriteListener arg0) {
		}
	}

	/**
	 * 
	 * @param url      请求页面地址，如 /sqlDoc.jsp
	 * @param save     保存地址，如 c:\\sp42\\d.htm
	 * @param sc
	 * @param request
	 * @param response
	 */
	private static void render(String url, String save, ServletContext sc, HttpServletRequest request,
			HttpServletResponse response) {
		RequestDispatcher rd = sc.getRequestDispatcher(url);

		try (ByteArrayOutputStream os = new ByteArrayOutputStream();
				ServletOutputStream stream = new sO(os);
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(os, "UTF-8"));
				OutputStream out = new FileOutputStream(save);) {
			HttpServletResponse rep = new HttpServletResponseWrapper(response) {
				@Override
				public ServletOutputStream getOutputStream() {
					return stream;
				}

				@Override
				public PrintWriter getWriter() {
					return pw;
				}
			};

			rep.setCharacterEncoding("utf-8");// response的编码为gbk防乱码
			rd.include(request, rep);
			pw.flush();

			os.writeTo(out);
		} catch (IOException | ServletException e) {
			LOGGER.warning(e);
		}
	}
}

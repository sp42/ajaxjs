package com.ajaxjs.app.developer;

import java.io.File;
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
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.sql.JdbcConnection;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.ByteArrayServletOutputStream;
import com.ajaxjs.web.mvc.controller.IController;
import com.ajaxjs.web.mvc.controller.MvcRequest;

/**
 * 代码生成器
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Path("/admin/CodeGenerators")
public class CodeGenerators implements IController {
	private static final LogHelper LOGGER = LogHelper.getLog(CodeGenerators.class);

	/**
	 * 打包文件保存位置
	 */
	private final static String ZIP_SAVE = "/download/code.zip";

	@POST
	public String doGet(MvcRequest req, HttpServletResponse resp) {
		req.setAttribute("packageName", req.getParameter("packageName"));
		Connection conn = JdbcConnection.getMySqlConnection(req.getParameter("dbUrl"), req.getParameter("dbUser", "root"), req.getParameter("dbPassword"));

		if (req.getParameter("getTable") != null) {
			CodeGeneratorsInfo info = new CodeGeneratorsInfo(req.getParameter("getTable"), req.getParameter("saveFolder", "C:\\temp"));

			if (req.getParameter("beanName") != null)
				info.setBeanName(req.getParameter("beanName"));

			pareperRender(info, DataBaseStruController.getColumnComment(conn, info.getTableName()), DataBaseStruController.getTableComment(conn, info.getTableName()), req, resp);
		} else {
			List<String> tables = DataBaseStruController.getAllTableName(conn);
			Map<String, String> tablesComment = DataBaseStruController.getTableComment(conn, tables);
			Map<String, List<Map<String, String>>> infos = DataBaseStruController.getColumnComment(conn, tables);

			for (String tableName : tables) {
				CodeGeneratorsInfo info = new CodeGeneratorsInfo(tableName, req.getParameter("saveFolder", "C:\\temp"));
				pareperRender(info, infos.get(tableName), tablesComment.get(tableName), req, resp);
			}
		}

		try {
			conn.close();
		} catch (SQLException e) {
			LOGGER.warning(e);
		}

		return "html::Done!<a href=\"" + req.getContextPath() + ZIP_SAVE + "\" download>download</a>";
	}

	/**
	 * 
	 * @param info
	 * @param fields
	 * @param tableComment
	 * @param req
	 * @param resp
	 */
	private static void pareperRender(CodeGeneratorsInfo info, List<Map<String, String>> fields, String tableComment, MvcRequest req, HttpServletResponse resp) {
		req.setAttribute("fields", fields);
		req.setAttribute("tableName", info.getTableName());
		req.setAttribute("beanName", info.getBeanName());
		req.setAttribute("tablesComment", tableComment);
		req.setAttribute("tablesCommentShortName", getName(tableComment));

		// 是否生成 model
		if (!req.hasParameter("isMap"))
			render(info.setType("pojo"), req, resp);

		render(info.setType("dao"), req, resp);
		render(info.setType("service"), req, resp);
		render(info.setType("serviceImpl"), req, resp);
		render(info.setType("controller"), req, resp);
	}

	/**
	 * 替换为实际内容
	 * 
	 * @param info 请求页面地址，如 /sqlDoc.jsp 保存地址，如 c:\\sp42\\d.htm
	 * @param req
	 * @param resp
	 */
	private static void render(CodeGeneratorsInfo info, HttpServletRequest req, HttpServletResponse resp) {
		File save = new File(info.getSaveTarget());
		mkdir(save);
		RequestDispatcher rd = req.getServletContext().getRequestDispatcher(info.getJsp());

		try (ByteArrayServletOutputStream stream = new ByteArrayServletOutputStream();
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(stream.getOut(), "UTF-8"));
				OutputStream out = new FileOutputStream(save);) {
			rd.include(req, new HttpServletResponseWrapper(resp) {
				@Override
				public ServletOutputStream getOutputStream() {
					return stream;
				}

				@Override
				public PrintWriter getWriter() {
					return pw;
				}
			});
			pw.flush();

			stream.writeTo(out);
		} catch (IOException | ServletException e) {
			LOGGER.warning(e);
		}
	}
	
	/**
	 * 代码生成器信息
	 * 
	 * @author sp42 frank@ajaxjs.com
	 *
	 */
	static class CodeGeneratorsInfo {

		public CodeGeneratorsInfo(String tableName, String saveFolder) {
			this.setTableName(tableName);
			this.saveFolder = saveFolder;
		}

		/**
		 * 模板保存位置
		 */
		private static final String TPL_SAVE = BaseController.admin("developer/code-generator");

		private String type;

		private String tableName;

		private String beanName;

		private String saveFolder;

		/**
		 * 请求页面地址，如 /sqlDoc.jsp
		 */
		public String getJsp() {
			return TPL_SAVE + "/" + type + ".jsp";
		}

		/**
		 * 保存地址，如 c:\\sp42\\d.htm
		 */
		public String getSaveTarget() {
			return saveFolder + "/" + type + "/" + getBeanName() + ReflectUtil.firstLetterUpper(type) + ".java";
		}

		public String getTableName() {
			return tableName;
		}

		public void setTableName(String tableName) {
			this.tableName = tableName;
		}

		public String getBeanName() {
			return beanName == null ? ReflectUtil.firstLetterUpper(tableName) : beanName;
		}

		public void setBeanName(String beanName) {
			this.beanName = beanName;
		}

		public String getType() {
			return type;
		}

		public CodeGeneratorsInfo setType(String type) {
			this.type = type;
			return this;
		}
	}

	/**
	 * 先检查目录是否存在，若不存在先建立
	 * 
	 * @param save
	 */
	private static void mkdir(File save) {
		File dir = new File(save.getParent());

		if (!dir.exists())
			dir.mkdirs();
	}

	public static String getName(String sqlType) {
		String[] arr = sqlType.split("，|,|\\.|。");

		return arr[0];
	}
}

package com.ajaxjs.cms.app.developer;

import java.io.File;
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
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.orm.JdbcConnection;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.ByteArrayServletOutputStream;

/**
 * 代码生成器
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Path("/admin/CodeGenerators")
public class CodeGenerators implements IController {
	private static final LogHelper LOGGER = LogHelper.getLog(CodeGenerators.class);

	static private String zipSave = "/download/code.zip";
	
	@POST
	public String doGet(MvcRequest request, HttpServletResponse response) throws FileNotFoundException {
		request.setAttribute("packageName", request.getParameter("packageName"));
	
		Connection conn = JdbcConnection.getMySqlConnection(request.getParameter("dbUrl"),
				request.getParameter("dbUser", "root"), request.getParameter("dbPassword"));
	
		if (request.getParameter("getTable") != null) {
			Info info = new Info(request.getParameter("getTable"), request.getParameter("saveFolder", "C:\\temp"));
	
			if (request.getParameter("beanName") != null)
				info.setBeanName(request.getParameter("beanName"));
	
			pareperRender(info, DataBaseStruController.getColumnComment(conn, info.getTableName()),
					DataBaseStruController.getTableComment(conn, info.getTableName()), request, response);
		} else {
			List<String> tables = DataBaseStruController.getAllTableName(conn);
			Map<String, String> tablesComment = DataBaseStruController.getTableComment(conn, tables);
			Map<String, List<Map<String, String>>> infos = DataBaseStruController.getColumnComment(conn,
					tables);
	
			for (String tableName : tables) {
				Info info = new Info(tableName, request.getParameter("saveFolder", "C:\\temp"));
	
				pareperRender(info, infos.get(tableName), tablesComment.get(tableName), request, response);
			}
		}
	
		try {
			conn.close();
		} catch (SQLException e) {
			LOGGER.warning(e);
		}
	
		return "html::Done!<a href=\"" + request.getContextPath() + zipSave + "\" download>download</a>";
	}

	private static void pareperRender(Info info, List<Map<String, String>> fields, String tableComment, MvcRequest request,
			HttpServletResponse response) {
		request.setAttribute("fields", fields);
		request.setAttribute("tableName", info.getTableName());
		request.setAttribute("beanName", info.getBeanName());
		request.setAttribute("tablesComment", tableComment);
		request.setAttribute("tablesCommentShortName", getName(tableComment));
	
		// 是否生成 model
		if (!request.hasParameter("isMap"))
			render(info.setType("pojo"), request, response);
	
		render(info.setType("dao"), request, response);
		render(info.setType("service"), request, response);
		render(info.setType("serviceImpl"), request, response);
		render(info.setType("controller"), request, response);
	}
	
	/**
	 * 替换为实际内容
	 * 
	 * @param info      请求页面地址，如 /sqlDoc.jsp  保存地址，如 c:\\sp42\\d.htm
	 * @param request
	 * @param response
	 */
	private static void render(Info info, HttpServletRequest request, HttpServletResponse response) {
	//		MvcRequest r = new MvcRequest(request);
	//		ZipHelper.toZip(saveFolder, r.mappath(zipSave));
	
		File save = new File(info.getSaveTarget());
		mkdir(save);
		RequestDispatcher rd = request.getServletContext().getRequestDispatcher(info.getJsp());
	
		try (ByteArrayServletOutputStream stream = new ByteArrayServletOutputStream();
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(stream.getOut(), "UTF-8"));
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
	
			rd.include(request, rep);
			pw.flush();
	
			stream.writeTo(out);
		} catch (IOException | ServletException e) {
			LOGGER.warning(e);
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

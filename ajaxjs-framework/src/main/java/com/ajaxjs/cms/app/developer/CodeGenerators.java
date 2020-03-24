package com.ajaxjs.cms.app.developer;

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

import com.ajaxjs.cms.utils.codegenerators.Utils;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.orm.JdbcConnection;
import com.ajaxjs.util.ReflectUtil;
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

	/**
	 * 模板保存位置
	 */
	static final String tplSave = BaseController.admin("developer/code-generator");

	@POST
	public String doGet(MvcRequest request, HttpServletResponse response) throws FileNotFoundException {
		String saveFolder = request.getParameter("saveFolder", "C:\\temp");
		String pojoSave = saveFolder + "\\model\\%s.java", daoSave = saveFolder + "\\dao\\%s.java",
				serviceSave = saveFolder + "\\service\\%s.java", controllerSave = saveFolder + "\\controller\\%s.java";

		Utils.mkdir(new String[] { saveFolder + "\\model", saveFolder + "\\dao", saveFolder + "\\service",
				saveFolder + "\\controller" });

		request.setAttribute("packageName", request.getParameter("packageName"));

		Connection conn = JdbcConnection.getMySqlConnection(request.getParameter("dbUrl"),
				request.getParameter("dbUser", "root"), request.getParameter("dbPassword"));

		// 是否生成model，默认为
		boolean isMap = "true".equals(request.getParameter("isMap"));

		if (request.getParameter("getTable") != null) {
			String tableName = request.getParameter("getTable");
			Info info = new Info(request.getParameter("getTable"));
			
			
			List<Map<String, String>> columnComments = DataBaseStruController.getColumnCommentByTableName(conn,
					info.getTableName());
			String beanName = request.getParameter("beanName") == null ? ReflectUtil.firstLetterUpper(tableName)
					: request.getParameter("beanName");
			String tablesComment = DataBaseStruController.getCommentByTableName(conn, tableName);

			
			request.setAttribute("fields", columnComments);
			request.setAttribute("tableName", tableName);
			request.setAttribute("beanName", beanName);
			request.setAttribute("tablesComment", tablesComment);
			request.setAttribute("tablesCommentChinese", getName(tablesComment));

			render(isMap, pojoSave, daoSave, serviceSave, controllerSave, beanName, request, response);

		} else {
			List<String> tables = DataBaseStruController.getAllTableName(conn);

			for (String tableName : tables) {
				String beanName = ReflectUtil.firstLetterUpper(tableName);
				Map<String, String> tablesComment = DataBaseStruController.getCommentByTableName(conn, tables);
				Map<String, List<Map<String, String>>> infos = DataBaseStruController.getColumnCommentByTableName(conn,
						tables);

				request.setAttribute("fields", infos.get(tableName));
				request.setAttribute("tableName", tableName);
				request.setAttribute("beanName", beanName);
				request.setAttribute("tablesComment", tablesComment);

				render(isMap, pojoSave, daoSave, serviceSave, controllerSave, beanName, request, response);
			}
		}

		try {
			conn.close();
		} catch (SQLException e) {
			LOGGER.warning(e);
		}

		return "html::Done!<a href=\"" + request.getContextPath() + zipSave + "\" download>download</a>";
	}

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
	private static void render(boolean isMap, String pojoSave, String daoSave, String serviceSave,
			String controllerSave, String beanName, HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException {
		String dao = tplSave + "/dao.jsp", service = tplSave + "/service.jsp",
				serviceImp = tplSave + "/serviceImpl.jsp", controller = tplSave + "/controller.jsp";

		if (!isMap)
			render(tplSave + "/pojo.jsp", String.format(pojoSave, beanName), request, response);
		else {
			dao = tplSave + "/dao_map.jsp";
			service = tplSave + "/service_map.jsp";
			serviceImp = tplSave + "/serviceImpl_map.jsp";
		}

		render(dao, String.format(daoSave, beanName + "Dao"), request, response);
		render(service, String.format(serviceSave, beanName + "Service"), request, response);
		render(serviceImp, String.format(serviceSave, beanName + "ServiceImpl"), request, response);
		render(controller, String.format(controllerSave, beanName + "Controller"), request, response);

//		MvcRequest r = new MvcRequest(request);
//		ZipHelper.toZip(saveFolder, r.mappath(zipSave));
	}

	/**
	 * 
	 * @param url      请求页面地址，如 /sqlDoc.jsp
	 * @param save     保存地址，如 c:\\sp42\\d.htm
	 * @param sc
	 * @param request
	 * @param response
	 */
	private static void render(Info info, HttpServletRequest request, HttpServletResponse response) {
		RequestDispatcher rd = request.getServletContext().getRequestDispatcher(info.getJsp());

		try (ByteArrayServletOutputStream stream = new ByteArrayServletOutputStream();
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(stream.getOut(), "UTF-8"));
				OutputStream out = new FileOutputStream(info.getSaveTarget());) {
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

	public static String getName(String sqlType) {
		String[] arr = sqlType.split("，|,|\\.|。");

		return arr[0];
	}
}

package com.ajaxjs.web;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 打包 js
 */
@WebServlet("/JsController")
public class JsController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * 收集所有的 js
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		if (request.getParameter("saveFolder") != null) {
			String js = "// build date:" + new Date() + "\n";

			response.getWriter().append(js);
		} else {
			String js = "";

			js += read(mappath(request, "js/ajaxjs-base.js")) + "\n";
			js += action(mappath(request, "js/widgets/")) + "\n";

			response.getWriter().append(js);
		}
	}

	/**
	 * 压缩 CSS 并将其保存到一个地方
	 * 
	 * @throws IOException
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String output = "", saveFolder = request.getParameter("saveFolder");

		String js = request.getParameter("js");
		if (js != null) {
			try {
				mkDir(saveFolder + "\\WebContent\\asset\\js\\");
				save(saveFolder + "\\WebContent\\asset\\js\\all.js", js);
				copyHTML(saveFolder + "\\WebContent\\asset\\js\\");

				output = "{\"isOk\":true}";
			} catch (Throwable e) {
				e.printStackTrace();
				output = "{\"isOk\":false}";
			}
		} else {

			String css = request.getParameter("css"), file = request.getParameter("file") == null ? "main" : request.getParameter("file");

			Logger.getGlobal().info(request.getParameter("saveFolder"));

			try {
				mkDir(saveFolder);
				save(saveFolder + "\\" + file, css);

				output = "{\"isOk\":true}";
			} catch (Throwable e) {
				e.printStackTrace();
				output = "{\"isOk\":false}";
			}
		}

		response.getWriter().append(output);
	}

	private final static String source = "C:\\project\\ajaxjs-js\\WebContent\\html\\";

	private static void copyHTML(String dest) {
		copy(source + "form.html", dest + "form.html", true);
		copy(source + "grid.html", dest + "grid.html", true);
		copy(source + "list.html", dest + "list.html", true);
	}

	public static boolean copy(String target, String dest, boolean isREPLACE_EXISTING) {
		try {
			if (isREPLACE_EXISTING)
				Files.copy(Paths.get(target), Paths.get(dest), StandardCopyOption.REPLACE_EXISTING);
			else
				Files.copy(Paths.get(target), Paths.get(dest));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * 打包某个目录下所有的 js
	 * 
	 * @param _folder
	 * @param isCompress
	 * @return
	 */
	public static String action(String _folder) {
		StringBuilder sb = new StringBuilder();
		File folder = new File(_folder);
		File[] files = folder.listFiles();

		if (files != null)
			for (File file : files) {
				if (file.isFile()) {
					sb.append("\n");
					sb.append(read(file.toPath().toString()));
				}
			}

		return sb.toString();
	}

	/**
	 * 获取磁盘真實地址
	 * 
	 * @param cxt          Web 上下文
	 * @param relativePath 相对地址
	 * @return 绝对地址
	 */
	public static String mappath(HttpServletRequest request, String relativePath) {
		String absolute = request.getServletContext().getRealPath(relativePath);

		if (absolute != null)
			absolute = absolute.replace('\\', '/');
		return absolute;
	}

	static String read(String filePath) {
		Path path = Paths.get(filePath);

		try {
			// 此方法不适合读取很大的文件，因为可能存在内存空间不足的问题。
			StringBuilder sb = new StringBuilder();
			Files.lines(path, StandardCharsets.UTF_8).forEach(str -> sb.append(str + "\n"));

			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void save(String fullpath, String content) throws IOException {
		Path path = Paths.get(fullpath);

		if (Files.isDirectory(path))
			throw new IOException("参数 fullpath：" + fullpath + " 不能是目录，请指定文件");

		if (!Files.exists(path))
			Files.createFile(path);

		System.out.println(content);
		Logger.getGlobal().info(path.toString());
		Files.write(path, content.getBytes());
	}

	/**
	 * 创建目录
	 * 
	 * @param folder 目录字符串
	 */
	public static void mkDir(String folder) {
		mkDir(new File(folder));
	}

	/**
	 * 创建目录
	 * 
	 * @param folder 目录对象
	 */
	public static void mkDir(File folder) {
		if (!folder.exists())// 先检查目录是否存在，若不存在建立
			folder.mkdirs();
	}
}

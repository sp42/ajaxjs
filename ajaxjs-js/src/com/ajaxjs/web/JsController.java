package com.ajaxjs.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Objects;
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

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String js = "// build date:" + new Date() + "\n";
		js += JavaScriptCompressor.compress(read(mappath(request, "js/ajaxjs-base.js"))) + "\n";
		js += JavaScriptCompressor.compress(read(mappath(request, "js/ajaxjs-list.js"))) + "\n";
		js += action(mappath(request, "js/widgets/"), true) + "\n";

		String output = request.getParameter("output"); // 保存位置
		Objects.requireNonNull(output, "必填参数");
		save(output + "\\WebContent\\asset\\js\\all.js", js);
		response.getWriter().append("Pack js Okay.");
	}

	static String frontEnd = "C:\\project\\wstsq\\WebContent\\asset\\css";

	/**
	 * 压缩 CSS 并将其保存到一个地方
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String css = request.getParameter("css"),
				file = request.getParameter("file") == null ? "main" : request.getParameter("file");
		String output = "";
		String saveFolder = request.getParameter("saveFolder") == null ? frontEnd : request.getParameter("saveFolder");

		Logger.getGlobal().info(request.getParameter("saveFolder"));

		try {
			save(saveFolder + "\\" + file, css);

			output = "{\"isOk\":true}";
		} catch (Throwable e) {
			e.printStackTrace();
			output = "{\"isOk\":false}";
		}

		response.getWriter().append(output);
	}

	/**
	 * 打包某个目录下所有的 js
	 * 
	 * @param _folder
	 * @param isCompress
	 * @return
	 */
	public static String action(String _folder, boolean isCompress) {
		StringBuilder sb = new StringBuilder();
		File folder = new File(_folder);
		File[] files = folder.listFiles();

		if (files != null)
			for (File file : files) {
				if (file.isFile()) {
					String jsCode = null;
					try {
						jsCode = read(file.toPath());
					} catch (IOException e) {
						e.printStackTrace();
					}
					sb.append("\n");
					sb.append(isCompress ? JavaScriptCompressor.compress(jsCode) : jsCode);
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

	public static String read(Path path, Charset encode) throws IOException {
		if (Files.isDirectory(path))
			throw new IOException("参数 fullpath：" + path.toString() + " 不能是目录，请指定文件");

		if (!Files.exists(path))
			throw new IOException(path.toString() + "　不存在");

		return new String(Files.readAllBytes(path), encode);
	}

	public static String read(String fullpath, Charset encode) throws IOException {
		Path path = Paths.get(fullpath);
		return read(path, encode);
	}

	public static String read(Path path) throws IOException {
		return read(path, StandardCharsets.UTF_8);
	}

	public static String read(String fullpath) throws IOException {
		return read(fullpath, StandardCharsets.UTF_8);
	}

	public static void saveClassic(String fullpath, String content) throws IOException {
		File file = new File(fullpath);
		if (file.isDirectory())
			throw new IOException("参数 fullpath：" + fullpath + " 不能是目录，请指定文件");

		try (FileOutputStream fop = new FileOutputStream(file)) {
			if (!file.exists())
				file.createNewFile();

			fop.write(content.getBytes());
			fop.flush();
		}
	}

	public void test() throws IOException {
		String content = read("c://temp//newfile.txt");
		save("c://temp//newfile2.txt", content);
	}

	public static void save(String fullpath, String content) throws IOException {
		Path path = Paths.get(fullpath);

		if (Files.isDirectory(path))
			throw new IOException("参数 fullpath：" + fullpath + " 不能是目录，请指定文件");

		if (!Files.exists(path))
			Files.createFile(path);

		Logger.getGlobal().info(path.toString());
		Files.write(path, content.getBytes());

	}
}

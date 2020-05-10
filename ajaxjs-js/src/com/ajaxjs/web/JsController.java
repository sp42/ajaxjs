package com.ajaxjs.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceFile;

/**
 * 打包 js
 */
@WebServlet("/JsController")
public class JsController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String js = "// build date:" + new Date() + "\n";

		js += compileJs(mappath(request, "js/ajaxjs-base.js")) + "\n";
		js += compileJs(mappath(request, "js/ajaxjs-list.js")) + "\n";
		js += action(mappath(request, "js/widgets/")) + "\n";

		String output = request.getParameter("output"); // 保存位置
		Objects.requireNonNull(output, "必填参数");
		save(output + "\\WebContent\\asset\\js\\all.js", js.replaceAll("'use strict';", ""));
		response.getWriter().append("Pack js Okay.");
	}

	/**
	 * 校验js语法、压缩js
	 * 
	 * @param code
	 * @return
	 */
	public static String compileJs(String code) {
		CompilerOptions options = new CompilerOptions();
		// Simple mode is used here, but additional options could be set, too.
		CompilationLevel.WHITESPACE_ONLY.setOptionsForCompilationLevel(options);

		// To get the complete set of externs, the logic in
		// CompilerRunner.getDefaultExterns() should be used here.
		SourceFile extern = SourceFile.fromCode("externs.js", "function alert(x) {}");

		// The dummy input name "input.js" is used here so that any warnings or
		// errors will cite line numbers in terms of input.js.
//		SourceFile input = SourceFile.fromCode("input.js", code);

		SourceFile jsFile = SourceFile.fromFile(code);

		Compiler compiler = new Compiler();
		compiler.compile(extern, jsFile, options);

		// The compiler is responsible for generating the compiled code; it is not
		// accessible via the Result.
		if (compiler.getErrorCount() > 0) {
			StringBuilder sb = new StringBuilder();
			for (JSError jsError : compiler.getErrors()) {
				sb.append(jsError.toString());
			}

			// System.out.println(sb.toString());
		}

		return compiler.toSource();
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
	public static String action(String _folder) {
		StringBuilder sb = new StringBuilder();
		File folder = new File(_folder);
		File[] files = folder.listFiles();

		if (files != null)
			for (File file : files) {
				if (file.isFile()) {
					sb.append("\n");
					sb.append(compileJs(file.toPath().toString()));
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

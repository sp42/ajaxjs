package com.ajaxjs.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class JsController
 */
@WebServlet("/JsController")
public class JsController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 * response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * 压缩 CSS 并将其保存到一个地方
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 * response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String css = request.getParameter("css"), type = request.getParameter("type"), fullpath = "";
//				fullpath = request.mappath("/asset/css/" + type + ".css");
		String output = "";

		try {
			save(fullpath, css);

			output = "json::{\"isOk\":true}";
		} catch (Throwable e) {
			e.printStackTrace();
			output = "json::{\"isOk\":false}";
		}

		response.getWriter().append(output).append(request.getContextPath());
	}

	public static String read(String fullpath, Charset encode) throws IOException {
		Path path = Paths.get(fullpath);

		if (Files.isDirectory(path))
			throw new IOException("参数 fullpath：" + fullpath + " 不能是目录，请指定文件");

		if (!Files.exists(path))
			throw new IOException(fullpath + "　不存在");

		return new String(Files.readAllBytes(path), encode);
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
		System.out.println(content);
		save("c://temp//newfile2.txt", content);
	}

	public static void save(String fullpath, String content) throws IOException {
		Path path = Paths.get(fullpath);

		if (Files.isDirectory(path))
			throw new IOException("参数 fullpath：" + fullpath + " 不能是目录，请指定文件");

		if (!Files.exists(path))
			Files.createFile(path);

		Files.write(path, content.getBytes());
		String[]  f = new String[] {"aa", "l434"};
		System.out.println(String.join(",", f));
	
	}
	
	

}

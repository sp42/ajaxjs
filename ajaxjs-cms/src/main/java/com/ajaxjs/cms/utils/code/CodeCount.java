package com.ajaxjs.cms.utils.code;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/CodeCount")
public class CodeCount  extends HttpServlet {
	private static final long serialVersionUID = 1154027927197924156L;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		recursionFolder(new File(request.getParameter("folder")));
		response.getWriter().append("Code line count: " + count);
	}

	static int count = 0;

	static ArrayList<String> ignore_folder = new ArrayList<>();
	static ArrayList<String> ignore_ext = new ArrayList<>();

	static {
		//忽略的文件后缀
		ignore_ext.add(".jar");
		ignore_ext.add(".class");
		ignore_ext.add(".classpath");
		ignore_ext.add(".project");

		//忽略的文件夹
		ignore_folder.add("resources");
		ignore_folder.add("classes");
		ignore_folder.add(".settings");
		ignore_folder.add("META-INF");
	}

	static void readLineCount(File _f) {
		if (_f != null) {
			String ext = _f.getPath();

			//文件后缀
			for (String _ext : ignore_ext) {
				if (ext.endsWith(_ext)) {
					//System.out.println("文件后缀:"+ext);
					return;
				}
			}
			//文件夹
			for (String _folder : ignore_folder) {
				if (ext.indexOf(_folder) > -1) {
					//System.out.println("文件夹:"+ext);
					return;
				}
			}
		}
		//System.out.println(_f.getPath());

		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(_f));) {
			while (bufferedReader.readLine() != null)
				count++;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return;
	}

	static void recursionFolder(File f) {
		if (f != null && !f.exists()) {
			System.out.println("文件夹不存在:" + f.getPath());
			return;

		}
		for (File _f : f.listFiles()) {
			if (_f.isFile()) 
				readLineCount(_f);
			
			if (_f.isDirectory()) 
				recursionFolder(_f);
		}
	}

	public static void main(String[] args) {
		try (Scanner s = new Scanner(System.in);) {
			System.out.println("请输入要统计代码的文件夹:");
			String pfolder = s.nextLine();
			System.out.println(pfolder);
			File folder = new File(pfolder);
			recursionFolder(folder);
			System.out.println("代码总行数:" + count);
		}
	}
}
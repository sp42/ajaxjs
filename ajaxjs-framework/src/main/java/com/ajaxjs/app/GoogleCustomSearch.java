package com.ajaxjs.app;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
 
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import com.google.gson.Gson;
 
/**
 * Author:wangjian
 */
public class GoogleCustomSearch extends HttpServlet {
 

	private static final long serialVersionUID = -2609053092204535091L;
	
	// 最大返回结果数
	private static final int RESULT_OK = 200;
	// 8KB
	private static final int BUF_SIZE = 1024 * 8;
	
	// 自定义搜索引擎的ID 
	private static final String ID = "010589607068358538435:p8ot8fmvira"; 
																			
 
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		handle(request, response);
	}
 
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		handle(request, response);
	}
 
 
	private void handle(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		int start = Integer.parseInt(request.getParameter("start"));
		int num = Integer.parseInt(request.getParameter("num"));
		String queryExpression = request.getParameter("queryexpression");
		String strRequest = "https://www.google.com:443/cse/publicurl?cx="
				+ ID
				+ "&q=%queryExpression%&searchType=image&start=%start%&num=%num%";
		strRequest = strRequest.replace("%queryExpression%", queryExpression)
				.replace("%start%", String.valueOf(start))
				.replace("%num%", Integer.toString(num));
 
		HttpURLConnection conn = null;
		String queryResult = null;
		try {
			URL url = new URL(strRequest);
			conn = (HttpURLConnection) url.openConnection();
			// 使用GET方法
			conn.setRequestMethod("GET");
			int resultCode = conn.getResponseCode();
			if (resultCode == RESULT_OK) {
				InputStream is = conn.getInputStream();
				queryResult = readAsString(is);
				is.close();
			} else {
				System.out.println("Fault on getting http result, code: "
						+ resultCode);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			conn.disconnect();
		}
 
		if ((queryResult != null) && (queryResult.length() != 0)) {
			String respStr = parseResult(queryResult);
			System.out.println(respStr);
 
			// 设置返回JSON内容
			response.setContentType("application/json; charset=utf-8");
			// 设置不缓存内容
			response.setHeader("pragma", "no-cache");
			response.setHeader("cache-control", "no-cache");
			PrintWriter writer = response.getWriter();
			writer.write(respStr);
			writer.flush();
			writer.close();
		}
	}
 
	/**
	 * 将搜索结果字符串转化为需要的对象列表
	 * 
	 * @param queryResult
	 * @return
	 */
	private String parseResult(String queryResult) {
		Gson gson = new Gson();
		SearchResult e = gson.fromJson(queryResult, SearchResult.class);
		System.out.println(queryResult);
		SearchResultItem items[] = e.getItems();
		Image images[] = new Image[items.length];
		for (int i = 0; i < items.length; i++) {
			images[i] = new Image(items[i].title, items[i].link,
					items[i].displayLink);
		}
		return gson.toJson(images);
	}
 
	/**
	 * 将输出内容转换为字符串形式
	 * 
	 * @param ins
	 * @return
	 * @throws IOException
	 */
	public static String readAsString(InputStream ins) throws IOException {
		ByteArrayOutputStream outs = new ByteArrayOutputStream();
		byte[] buffer = new byte[BUF_SIZE];
		int len = -1;
		try {
			while ((len = ins.read(buffer)) != -1) {
				outs.write(buffer, 0, len);
			}
		} finally {
			outs.flush();
			outs.close();
		}
		return outs.toString();
	}
 
}
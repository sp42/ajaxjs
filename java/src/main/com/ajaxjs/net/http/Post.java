package com.ajaxjs.net.http;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import com.ajaxjs.util.IO;
import com.ajaxjs.util.StringUtil;

public class Post {
	/**
	 * 
	 * @param url
	 * @param data 表单数据 KeyValue的请求数据，注意要进行 ? & 编码，使用 URLEncoder.encode()
	 * @return
	 */
	public static Request POST(String url, Map<String, Object> data) {
		Request request = new Request();
		request.setMethod("POST");
		request.setUrl(url);
		if (data != null && data.size() > 0) {
			byte[] b = join(data).getBytes();
			request.setWriteData(b);
		}
		
	
		RequestClient client = new RequestClient(request);
		client.getConnection().setDoOutput(true); // for conn.getOutputStream().write(someBytes);
		client.getConnection().setDoInput(true);
		client.getConnection().setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		try {
			client.connect();
		} catch (ConnectException e) {
			e.printStackTrace();
			return null;
		}
		
		request.setDone(true);
		return request;
	}

	/**
	 * 
	 * @param map
	 * @return
	 */
	public static String join(Map<String, Object> map) {
		String[] pairs = new String[map.size()];

		int i = 0;
		try {
			for (String key : map.keySet())
				pairs[i++] = key + "=" + URLEncoder.encode(map.get(key).toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return StringUtil.stringJoin(pairs, "&");
	}
	
	
	/**
	 * request 头和上传文件内容之间的分隔符  
	 */
	private static final String BOUNDARY = "---------------------------123821742118716"; 
	
	/**
	 * 多段 POST 的分隔
	 */
	private static final String DIV = "\r\n--%s\r\nContent-Disposition: form-data; name=\"%s\"\r\n\r\n%s";
	
	/**
	 * 多段上传
	 * 
	 * @param urlStr
	 * @param text
	 * @param fileMap 二进制数据
	 * @return
	 */
	public static Request MultiPOST(String url, Map<String, Object> text, Map<String, String> fileMap) {  
		Request request = new Request();
		request.setMethod("POST");
		request.setUrl(url);
		
		
		byte[] data = null;
		if (text != null && text.size() > 0) {
			StringBuilder strs = new StringBuilder();
			
			for (String name : text.keySet()) {
				String value = text.get(name).toString();
				if (StringUtil.isEmptyString(value))
					continue;
				strs.append(String.format(DIV, BOUNDARY, name, value));
				
//					strs.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
//					strs.append("Content-Disposition: form-data; name=\"" + name + "\"\r\n\r\n");
//					strs.append(value);
			}
			
			data = strs.toString().getBytes();
		}
		
        if (fileMap != null && fileMap.size() > 0) {  
			for (String name : fileMap.keySet()) {
				String value = fileMap.get(name);
				if (StringUtil.isEmptyString(value))
					continue;

				File file = new File(value);
				String filename = file.getName(), contentType = IO.getMime(file);

				String str = String.format(DIV, BOUNDARY, name, filename) + "Content-Type:" + contentType + "\r\n\r\n";
				
				concat(data, str.getBytes());
//				StringBuilder strBuf = new StringBuilder();
//				strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
//				strBuf.append(
//						"Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + filename + "\"\r\n");
//				strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
//
//				out.write(strBuf.toString().getBytes());
//
//				DataInputStream in = new DataInputStream(new FileInputStream(file));
//				int bytes = 0;
//				byte[] bufferOut = new byte[1024];
//				while ((bytes = in.read(bufferOut)) != -1)
//					out.write(bufferOut, 0, bytes);
//				in.close();
			}
        }  
        
        byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();  
        concat(data, endData);
		request.setWriteData(data);
	
		RequestClient client = new RequestClient(request);
		client.getConnection().setDoOutput(true); // for conn.getOutputStream().write(someBytes);
		client.getConnection().setDoInput(true);
		client.getConnection().setRequestProperty("Content-type", "multipart/form-data; boundary=" + BOUNDARY);
		
		try {
			client.connect();
		} catch (ConnectException e) {
			e.printStackTrace();
		}
		
		request.setDone(true);
		return request;

//        try (OutputStream out = new DataOutputStream(client.getConnection().getOutputStream());) {  
    } 
	
	static byte[] concat(byte[] a, byte[] b) {
		byte[] c = new byte[a.length + b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		return c;
	}
}

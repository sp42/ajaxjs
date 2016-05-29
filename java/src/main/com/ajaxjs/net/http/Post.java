package com.ajaxjs.net.http;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;

import com.ajaxjs.util.StringUtil;

public class Post extends RequestClient {
	public Post(Request req) {
		super(req);
		req.setMethod("POST");
	}

	private String FORM_POST_ContentType = "application/x-www-form-urlencoded;charset=utf-8";

	private String requestData;

	@Override
	public boolean connect() {
		if (getRequestData() != null) { // 写入 POST 数据
			try (OutputStream os = getConnection().getOutputStream()) {
				os.write(getRequestData().getBytes());
				os.flush();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		getConnection().setDoOutput(true); // for conn.getOutputStream().write(someBytes);
		getConnection().setDoInput(true);
		getConnection().setRequestProperty("Content-type", getFORM_POST_ContentType());
		
		try {
			return super.connect();
		} catch (ConnectException e) {
			e.printStackTrace();
			return false;
		}

	}

	public String getRequestData() {
		return requestData;
	}

	/**
	 * 设置表单数据
	 * 
	 * @param requestData
	 *            KeyValue的请求数据，注意要进行 ? & 编码，使用 URLEncoder.encode()
	 */
	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}

	public String getFORM_POST_ContentType() {
		return FORM_POST_ContentType;
	}

	public void setFORM_POST_ContentType(String c) {
		FORM_POST_ContentType = c;
	}
	
	static String BOUNDARY = "---------------------------123821742118716"; //boundary就是request头和上传文件内容的分隔符  
//	private String FORM_POST_ContentType = "multipart/form-data; boundary=" + BOUNDARY; 
	
	/**
	 * post方式提交表单实现图片上传
	 * 
	 * @param urlStr
	 * @param textMap
	 * @param fileMap
	 * @return
	 */
	public String formUpload(Map<String, String> textMap, Map<String, String> fileMap) {  
        String res = "";  
        
        try {  
            OutputStream out = new DataOutputStream(getConnection().getOutputStream());  
            // text  
            if (textMap != null) {  
                StringBuilder strs = new StringBuilder();  
                
                for(String name : textMap.keySet()){
                	String value = textMap.get(name);
                	if(StringUtil.isEmptyString(value)) continue;

                	strs.append("\r\n").append("--").append(BOUNDARY).append("\r\n");  
                	strs.append("Content-Disposition: form-data; name=\"" + name + "\"\r\n\r\n");  
                	strs.append(value);  
                }
                  
                out.write(strs.toString().getBytes());  
            }  
  
            // file  
            if (fileMap != null) {  
                for(String name : fileMap.keySet()){
                	String value = fileMap.get(name);
                	if(StringUtil.isEmptyString(value)) continue;
                	
                	File file = new File(value); 
                	String filename = file.getName(), contentType = new MimetypesFileTypeMap().getContentType(file);
                	
                	if (filename.endsWith(".png")) contentType = "image/png";  
                	if (StringUtil.isEmptyString(contentType)) contentType = "application/octet-stream";  

                	StringBuilder strBuf = new StringBuilder();  
                	strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");  
                	strBuf.append("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + filename + "\"\r\n");  
                	strBuf.append("Content-Type:" + contentType + "\r\n\r\n");  
                	
                	out.write(strBuf.toString().getBytes());  
                	
                	DataInputStream in = new DataInputStream(new FileInputStream(file));  
                	int bytes = 0;  
                	byte[] bufferOut = new byte[1024];  
                	while ((bytes = in.read(bufferOut)) != -1) out.write(bufferOut, 0, bytes);  
                	in.close();  
                }
            }  
  
            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();  
            out.write(endData);  
            out.flush();  
            out.close();  
  
            // TODO 读取返回数据  

        } catch (Exception e) {  
            e.printStackTrace();  
        } 
        return res;  
    } 
}

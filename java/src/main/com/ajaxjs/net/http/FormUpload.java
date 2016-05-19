/**
 * Copyright 2015 Frank Cheung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.net.http;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Map;
import javax.activation.MimetypesFileTypeMap;

import com.ajaxjs.util.StringUtil;


/**
 * post方式提交表单实现图片上传 
 * http://blog.csdn.net/wangpeng047/article/details/38303865
 * http://blog.csdn.net/5iasp/article/details/8669644
 * @author frank
 * TODO
 *
 */
public class FormUpload extends Post{
	public FormUpload(String url) {
		super(url);
	}
	
	static String BOUNDARY = "---------------------------123821742118716"; //boundary就是request头和上传文件内容的分隔符  
	private String FORM_POST_ContentType = "multipart/form-data; boundary=" + BOUNDARY; 
	
	/**
	 * 在之前的基础上配置 POST 的参数
	 */
	@Override
	public void initConn(HttpURLConnection conn){
		super.initConn(conn);
	}
	
	private void writeData(HttpURLConnection conn) {
		
	}
	
	public static String formUpload(String urlStr, Map<String, String> textMap, Map<String, String> fileMap) {  
        String res = "";  
        HttpURLConnection conn = null;  
        
        try {  
            OutputStream out = new DataOutputStream(conn.getOutputStream());  
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
  
            // 读取返回数据  
//            StringBuffer strBuf = new StringBuffer();  
//            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));  
//            String line = null;  
//            while ((line = reader.readLine()) != null) {  
//                strBuf.append(line).append("\n");  
//            }  
//            res = strBuf.toString();  
//            reader.close();  
//            reader = null;  
        } catch (Exception e) {  
            System.out.println("发送POST请求出错。" + urlStr);  
            e.printStackTrace();  
        } finally {  
            if (conn != null) {  
                conn.disconnect();  
                conn = null;  
            }  
        }  
        return res;  
    } 
}

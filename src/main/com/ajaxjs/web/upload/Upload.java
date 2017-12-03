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
package com.ajaxjs.web.upload;

import java.io.*;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.util.Encode;
import com.ajaxjs.util.io.StreamUtil;


/**
 * 上传类
 * @author frank
 *
 */
public class Upload {
	
	/**
	 * 原始的字符串
	 */
	private String rawStr;
	
	/**
	 * 保存上传的文件数据（文件二进制数据）
	 */
	private byte dateBytes[];
	
	/**
	 * 接受上传
	 * 
	 * @param uRequest
	 *            上传 POJO
	 * @return
	 * @throws UploadException
	 */
	public UploadRequest upload(UploadRequest uRequest) throws UploadException {
		HttpServletRequest req = uRequest.getRequest();
		
		// 取得客户端上传的数据类型
		String contentType = req.getContentType();

		if(!req.getMethod().equals("POST")){
			throw new UploadException("必须 POST 请求");
		}
		
		if (contentType.indexOf("multipart/form-data") == -1) {
			throw new UploadException("未设置表单  multipart/form-data");
		}
		
		int formDataLength = req.getContentLength();
		
		if (formDataLength > uRequest.getMaxFileSize()) { // 是否超大
			throw new UploadException("文件大小超过系统限制！");
		}
		
		try {
			dateBytes = new StreamUtil().setIn(req.getInputStream()).inputStream2Byte().close().getData();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
			
		rawStr = Encode.byte2String(dateBytes);
		
		// 取得数据分割字符串，数据分割线开始位置boundary=---------------------------
		String boundary = contentType.substring(contentType.lastIndexOf("=") + 1, contentType.length());

		// 计算开头数据头占用的长度
		int startPos = getStartPos();
		// 边界位置
		int endPos = byteIndexOf(dateBytes, boundary.getBytes(), (dateBytes.length - startPos)) - 4;
		
		if(startPos == endPos) {
			throw new UploadException("上传表单中没有二进制数据，上传文件为空！");
		}

		// 创建文件
		String fileName = uRequest.getUpload_save_folder() + getFileName(uRequest.isNewName());
		uRequest.setUploaded_save_filePath(fileName);
		File checkedFile = initFile(uRequest);

		// 写入文件
		try(FileOutputStream fileOut = new FileOutputStream(checkedFile);){
			fileOut.write(dateBytes, startPos, endPos - startPos);
			fileOut.flush();
			
			uRequest.setUploaded_save_fileName(checkedFile.getName());
			uRequest.setOk(true);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new UploadException(e.toString());
		} catch (IOException e) {
			e.printStackTrace();
			throw new UploadException(e.toString());
		} 
		
		return uRequest;
	}

	/**
	 * 获取开头数据头占用的长度
	 * 
	 * @param dateBytes
	 *            文件二进制数据
	 * @return
	 * @throws UploadException 
	 */
	private int getStartPos() throws UploadException {
		int start;
		
		try{
			start = byteIndexOf(dateBytes, "filename=\"".getBytes(), 0);
			start = byteIndexOf(dateBytes, "\n".getBytes(), start) + 1; // 遍历掉3个换行符到数据块
			start = byteIndexOf(dateBytes, "\n".getBytes(), start) + 1;
			start = byteIndexOf(dateBytes, "\n".getBytes(), start) + 1;
			
//			System.out.println(rawStr);
			
			int contentLength_index = rawStr.indexOf("Content-Length:");
			
			if(contentLength_index != -1) {
				String contentLength;
				contentLength = rawStr.substring(contentLength_index);
				contentLength = contentLength.substring(0, contentLength.indexOf("\n"));
				System.out.println(contentLength);	
				
				start = byteIndexOf(dateBytes, contentLength.getBytes(), start) + contentLength.length();
				start = byteIndexOf(dateBytes, "\n".getBytes(), start) + 1; // 遍历掉3个换行符到数据块
				start = byteIndexOf(dateBytes, "\n".getBytes(), start) + 1; 
			}
 
		}catch (ArrayIndexOutOfBoundsException e) {
			throw new UploadException("你的表单中没有设置一个 name，不能获取字段");
		}
		
		return start;
	}

	/**
	 * 在字节数组里查找某个字节数组，找到返回>=0，未找到返回-1
	 * @param data
	 * @param search
	 * @param start
	 * @return
	 */
	private static int byteIndexOf(byte[] data, byte[] search, int start) {
		int index = -1, len = search.length;
		
		for (int i = start, j = 0; i < data.length; i++) {
			int temp = i;
			j = 0;
			
			while (data[temp] == search[j]) {
				// System.out.println((j+1)+",值："+data[temp]+","+search[j]);
				// 计数
				j++;
				temp++;
				if (j == len) {
					index = i;
					return index;
				}
			}
		}
		
		return index;
	}
	
	/**
	 * 如果没有指定目录则创建；检测是否可以覆盖文件
	 * 
	 * @param uRequest
	 *            上传 POJO
	 * @return
	 * @throws UploadException
	 */
	private static File initFile(UploadRequest uRequest) throws UploadException {
		File dir = new File(uRequest.getUpload_save_folder());
		if (!dir.exists())
			dir.mkdirs();
		
		File checkFile = new File(uRequest.getUploaded_save_filePath());
		
		if (!uRequest.isFileOverwrite() && checkFile.exists()) {
			throw new UploadException("文件已经存在，禁止覆盖！");
		}
		
		return checkFile;
	}
	
	/**
	 * 获取 POST Body 中的文件名
	 * 
	 * @param dateBytes
	 *            文件二进制数据
	 * @param isAutoName
	 *            是否自定命名，true = 时间戳文件名
	 * @return
	 */
	private String getFileName(boolean isAutoName) {
		String saveFile = null;
		
		if(isAutoName){
			saveFile = "" + System.currentTimeMillis();
		} else {
			// 取得上传的文件名
			saveFile = rawStr.substring(rawStr.indexOf("filename=\"") + 10);
			saveFile = saveFile.substring(0, saveFile.indexOf("\n"));
			saveFile = saveFile.substring(saveFile.lastIndexOf("\\") + 1, saveFile.indexOf("\""));
		}
		return saveFile;
	}
}

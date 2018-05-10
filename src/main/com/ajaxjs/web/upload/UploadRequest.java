/**
 * Copyright Sp42 frank@ajaxjs.com
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

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.ajaxjs.util.Encode;
import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.io.FileUtil;
import com.ajaxjs.util.io.StreamUtil;

/**
 * 文件上传
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 */
public class UploadRequest extends HttpServletRequestWrapper {
	/**
	 * 创建一个上传请求对象
	 * 
	 * @param request
	 *            请求对象
	 */
	public UploadRequest(HttpServletRequest request) {
		super(request);
	}

	/**
	 * 配置对象
	 */
	private UploadConfig config = new UploadConfigImpl();

	/**
	 * 原始的字符串
	 */
	private String dataStr;

	/**
	 * 保存上传的文件数据（文件二进制数据）
	 */
	private byte dataBytes[];

	/**
	 * 预检查
	 * 
	 * @throws IOException
	 */
	private void check() throws IOException {
		if (!getMethod().equals("POST"))
			throw new IllegalArgumentException("必须 POST 请求");

		if (getContentLength() > config.getMaxTotalFileSize()) // 是否超大
			throw new IOException("文件大小超过系统限制！");

		if (getContentType().indexOf("multipart/form-data") == -1)// 取得客户端上传的数据类型
			throw new IllegalArgumentException("未设置表单  multipart/form-data");
	}

	/**
	 * 取得数据分割字符串，数据分割线开始位置boundary=---------------------------
	 * 
	 * @return 分割符
	 */
	private byte[] getBoundary() {
		String boundary = StringUtil.regMatch("boundary=((?:-|\\w)+)$", getContentType(), 1);
		return boundary.getBytes();
	}

	/**
	 * 执行上传
	 * 
	 * @return 上传结果
	 * @throws IOException
	 */
	public UploadResult upload() throws IOException {
		check();
		ServletInputStream in = null;

		try {
			in = getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}

		dataBytes = new StreamUtil().setIn(in).inputStream2Byte().close().getData();
		dataStr = Encode.byte2String(dataBytes);
		//		System.out.println(dataStr);

		MetaData meta = new MetaData();
		meta.parseMeta(dataStr);

		int offset = MetaData.get(dataBytes), length = getLength(offset);
		return save(offset, length, meta);
	}

	/**
	 * 获取上传文件的长度
	 * 
	 * @param start
	 *            开始位置
	 * @return 文件长度
	 */
	private int getLength(int start) {
		int endPos = StreamUtil.byteIndexOf(dataBytes, getBoundary(), start) - 4;
		if (start == endPos)
			throw new IllegalArgumentException("上传表单中没有二进制数据，上传文件为空！");

		int length = endPos - start;
		return length;
	}

	/**
	 * 获取开头数据头占用的长度
	 * 
	 * @deprecated
	 * @return 开头数据头占用的长度
	 * @throws UploadException
	 */
	public int getStartPos() {
		int start = 0;
		int contentLength_index = dataStr.indexOf("Content-Length:");

		try {
			if (contentLength_index != -1) { // 如果有 Content-Length 字段
				String contentLength = dataStr.substring(contentLength_index);
				contentLength = contentLength.substring(0, contentLength.indexOf("\n"));
				start = StreamUtil.byteIndexOf(dataBytes, contentLength.getBytes(), start) + contentLength.length();
				start = StreamUtil.byteIndexOf(dataBytes, "\n".getBytes(), start) + 1;
				start = StreamUtil.byteIndexOf(dataBytes, "\n".getBytes(), start) + 1;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new IllegalArgumentException("你的表单中没有设置一个 name，不能获取字段");
		}

		return start;
	}

	/**
	 * 保存文件
	 * 
	 * @param offset
	 * @param length
	 * @param meta
	 * @return 上传结果
	 */
	private UploadResult save(int offset, int length, MetaData meta) {
		String fullPath = config.getSaveFolder() + config.getFileName(meta);
		File file = null;
		UploadResult result = new UploadResult();

		try {
			file = FileUtil.createFile(fullPath, config.isFileOverwrite());
			// 写入文件
			new FileUtil().setData(dataBytes).setFile(file).save(offset, length).close();
			result.fullPath = fullPath;
			result.fileName = file.getName();
			result.isOk = true;
		} catch (IOException e) {
			result.errMsg = e.getMessage();
			e.printStackTrace();
		}

		return result;
	}

	public UploadConfig getConfig() {
		return config;
	}

	public void setConfig(UploadConfig config) {
		this.config = config;
	}
}

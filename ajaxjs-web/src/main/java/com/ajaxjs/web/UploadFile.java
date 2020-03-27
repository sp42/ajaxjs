/**
 * Copyright Sp42 frank@ajaxjs.com Licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.ajaxjs.web;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.Encode;
import com.ajaxjs.util.io.FileHelper;
import com.ajaxjs.util.io.IoHelper;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 文件上传
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class UploadFile extends HttpServletRequestWrapper {
	private static final LogHelper LOGGER = LogHelper.getLog(UploadFile.class);

	/**
	 * 创建一个上传请求对象
	 * 
	 * @param request        请求对象
	 * @param uploadFileInfo 上传的配置信息
	 */
	public UploadFile(HttpServletRequest request, UploadFileInfo uploadFileInfo) {
		super(request);
		setUploadFileInfo(uploadFileInfo);
	}

	/**
	 * 原始的字符串
	 */
	private String dataStr;

	/**
	 * 保存上传的文件数据（文件二进制数据）
	 */
	private byte dataBytes[];

	/**
	 * 配置信息
	 */
	private UploadFileInfo uploadFileInfo;

	/**
	 * 预检查
	 * 
	 * @throws IOException
	 */
	private void check() throws IOException {
		Objects.requireNonNull(uploadFileInfo, "缺少配置对象");

		if (!getMethod().equals("POST"))
			throw new IllegalArgumentException("必须 POST 请求");

		if (getContentLength() > uploadFileInfo.maxTotalFileSize) // 是否超大
			throw new IOException("文件大小超过系统限制！");

		if (getContentType().indexOf("multipart/form-data") == -1)// 取得客户端上传的数据类型
			throw new IllegalArgumentException("未设置表单  multipart/form-data");
	}

	/**
	 * 执行上传，主要的调用方法
	 * 
	 * @return 上传结果
	 * @throws IOException
	 */
	public UploadFileInfo upload() throws IOException {
		if (uploadFileInfo.beforeUpload != null && !uploadFileInfo.beforeUpload.apply(uploadFileInfo)) {
			return null;
		}

		check();

		try (ServletInputStream in = getInputStream()) {
			dataBytes = IoHelper.inputStream2Byte(in);
		} catch (IOException e) {
			LOGGER.warning(e);
			throw e;
		}

		uploadFileInfo.contentLength = dataBytes.length;
		dataStr = Encode.byte2String(dataBytes);

		parse(dataStr);

		int offset = getOffset(), length = getLength(offset);

		return save(offset, length);
	}

	/**
	 * 解析 HTTP 报文，得出 文件名 和文件类型
	 * 
	 * @param dataStr HTTP 报文字符串
	 */
	private void parse(String dataStr) {
		uploadFileInfo.name = CommonUtil.regMatch("name=\"(\\w+)\"", dataStr, 1);

		if (uploadFileInfo.name == null)
			throw new IllegalArgumentException("你的表单中没有设置一个 name，不能获取字段");

		uploadFileInfo.oldFilename = CommonUtil.regMatch("filename=\"([^\"]*)\"", dataStr, 1);
		uploadFileInfo.contentType = CommonUtil.regMatch("Content-Type:\\s?([\\w/]+)", dataStr, 1);

		// 文件扩展名判断
		String[] arr = uploadFileInfo.oldFilename.split("\\."); // 获取文件扩展名
		String ext = arr[arr.length - 1];

		uploadFileInfo.extName = ext.toLowerCase();

		if (!CommonUtil.isNull(uploadFileInfo.allowExtFilenames)) {
			boolean isFound = false;

			for (String _ext : uploadFileInfo.allowExtFilenames) {
				if (_ext.equalsIgnoreCase(ext)) {
					isFound = true;
					break;
				}
			}

			if (!isFound)
				throw new IllegalArgumentException(ext + " 上传类型不允许上传");
		}
	}

	/**
	 * 获取上传文件的长度
	 * 
	 * @param start 开始位置
	 * @return 文件长度
	 */
	private int getLength(int start) {
		// 取得数据分割字符串，数据分割线开始位置boundary=---------------------------
		String boundary = CommonUtil.regMatch("boundary=((?:-|\\w)+)$", getContentType(), 1);
		int found = IoHelper.byteIndexOf(dataBytes, boundary.getBytes(), start);

		if (found == -1)
			throw new IllegalArgumentException("找不到 Boundary");

		int endPos = found - 4;
		if (start == endPos)
			throw new IllegalArgumentException("上传表单中没有二进制数据，上传文件为空！");

		return endPos - start;
	}

	/**
	 * 保存文件
	 * 
	 * @param offset 偏移开始位置
	 * @param length 文件长度
	 * @return 上传结果
	 */
	private UploadFileInfo save(int offset, int length) {
		if (uploadFileInfo.saveFileName == null)
			uploadFileInfo.saveFileName = uploadFileInfo.oldFilename; // 如不指定 saveFileName 则默认上传的
		else
			uploadFileInfo.saveFileName += "." + uploadFileInfo.extName;

		uploadFileInfo.fullPath = uploadFileInfo.saveFolder + uploadFileInfo.saveFileName;

		try {
			File file = FileHelper.createFile(uploadFileInfo.fullPath, uploadFileInfo.isFileOverwrite);
			// 写入文件
			FileHelper.save(file, dataBytes, offset, length);
			uploadFileInfo.isOk = true;
		} catch (IOException e) {
			uploadFileInfo.isOk = false;
			uploadFileInfo.errMsg = e.getMessage();
			LOGGER.warning(e);
		}

		if (uploadFileInfo.afterUpload != null)
			uploadFileInfo.afterUpload.accept(uploadFileInfo);

		return uploadFileInfo;
	}

	public UploadFileInfo getUploadFileInfo() {
		return uploadFileInfo;
	}

	public void setUploadFileInfo(UploadFileInfo uploadFileInfo) {
		this.uploadFileInfo = uploadFileInfo;
	}

	// 换行符的字节码
	private final static byte[] b = "\n".getBytes();

	/**
	 * 
	 * @param dataBytes
	 * @return
	 */
	private int getOffset() {
		int skip = 0;

		for (int i = 0; i < dataBytes.length; i++) {
			int temp = i, j = 0;

			while (dataBytes[temp] == b[j]) {
				temp++;
				j++;

				if (j == b.length) {
					skip++;
					if (skip == 3)
						return i + 3;// why plus 3?

					break;
				}
			}
		}

		return 0;
	}
}
package com.ajaxjs.web;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.ajaxjs.util.Encode;
import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.io.FileUtil;
import com.ajaxjs.util.io.StreamUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.upload.MetaData;
import com.ajaxjs.web.upload.UploadResult;

public class UploadFile extends HttpServletRequestWrapper {
	private static final LogHelper LOGGER = LogHelper.getLog(UploadFile.class);

	/**
	 * 创建一个上传请求对象
	 * 
	 * @param request
	 *            请求对象
	 */
	public UploadFile(HttpServletRequest request) {
		super(request);
	}

	/**
	 * 
	 * @param request
	 * @param uploadFileInfo
	 */
	public UploadFile(HttpServletRequest request, UploadFileInfo uploadFileInfo) {
		super(request);
		setUploadFileInfo(uploadFileInfo);
	} 

	private UploadFileInfo uploadFileInfo;

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

		if (getContentLength() > uploadFileInfo.maxTotalFileSize) // 是否超大
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
			LOGGER.warning(e);
		}

		dataBytes = new StreamUtil().setIn(in).inputStream2Byte().close().getData();
		dataStr = Encode.byte2String(dataBytes);

		parseMeta(dataStr);

		int offset = MetaData.get(dataBytes), length = getLength(offset);
		return save(offset, length);
	}

	/**
	 * 解析 HTTP 报文，得出 文件名 和文件类型
	 * 
	 * @param dataStr
	 *            HTTP 报文字符串
	 */
	public void parseMeta(String dataStr) {
		uploadFileInfo.name = StringUtil.regMatch("name=\"(\\w+)\"", dataStr, 1);
		if (uploadFileInfo.name == null)
			throw new IllegalArgumentException("你的表单中没有设置一个 name，不能获取字段");

		uploadFileInfo.filename = StringUtil.regMatch("filename=\"([^\"]*)\"", dataStr, 1);
		uploadFileInfo.contentType = StringUtil.regMatch("Content-Type:\\s?([\\w/]+)", dataStr, 1);
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

		return endPos - start;
	}

	/**
	 * 保存文件
	 * 
	 * @param offset
	 * @param length
	 * @return 上传结果
	 */
	private UploadResult save(int offset, int length) {
		String fullPath = uploadFileInfo.saveFolder + uploadFileInfo.saveFileName;
		File file = null;
		UploadResult result = new UploadResult();

		try {
			file = FileUtil.createFile(fullPath, uploadFileInfo.isFileOverwrite);
			// 写入文件
			new FileUtil().setData(dataBytes).setFile(file).save(offset, length).close();
			result.fullPath = fullPath;
			result.fileName = file.getName();
			result.isOk = true;
		} catch (IOException e) {
			result.errMsg = e.getMessage();
			LOGGER.warning(e);
		}

		return result;
	} 

	public UploadFileInfo getUploadFileInfo() {
		return uploadFileInfo;
	}

	public void setUploadFileInfo(UploadFileInfo uploadFileInfo) {
		this.uploadFileInfo = uploadFileInfo;
	}
}
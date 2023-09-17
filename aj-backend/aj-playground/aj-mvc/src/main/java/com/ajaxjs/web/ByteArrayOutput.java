package com.ajaxjs.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

import com.ajaxjs.util.logger.LogHelper;

/**
 * 自定义响应对象的输出流
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class ByteArrayOutput extends ServletOutputStream {
	private static final LogHelper LOGGER = LogHelper.getLog(ByteArrayOutput.class);

	/**
	 * 输出流
	 */
	private OutputStream out = new ByteArrayOutputStream();

	/**
	 * 创建一个 ByteArrayServletOutputStream 对象
	 */
	public ByteArrayOutput() {
	}

	/**
	 * 
	 * 创建一个 ByteArrayServletOutputStream 对象
	 * 
	 * @param out 输出流
	 */
	public ByteArrayOutput(ByteArrayOutputStream out) {
		this.out = out;
	}

	@Override
	public void write(byte[] data, int offset, int length) {
		try {
			out.write(data, offset, length);
		} catch (IOException e) {
			LOGGER.warning(e);
		}
	}

	@Override
	public void write(int b) throws IOException {
		out.write(b);
	}

	/**
	 * 
	 * @param _out
	 */
	public void writeTo(OutputStream _out) {
		ByteArrayOutputStream bos = (ByteArrayOutputStream) out;

		try {
			bos.writeTo(_out);
		} catch (IOException e) {
			LOGGER.warning(e);
		}
	}

	public OutputStream getOut() {
		return out;
	}

	@Override
	public boolean isReady() {
		return false;
	}

	@Override
	public String toString() {
		return out.toString();
	}

	@Override
	public void setWriteListener(WriteListener writeListener) {
	}
}

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
package com.ajaxjs.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.activation.MimetypesFileTypeMap;

import com.ajaxjs.Constant;

/**
 * 流工具类 
 * http://www.cnblogs.com/skywang12345/archive/2013/02/24/2936044.html
 * http://coach.iteye.com/blog/856429
 * http://www.cnblogs.com/lovebread/archive/2009/11/23/1609122.html
 * 
 * @author frank
 */
public class IO {
	private static final LogHelper LOGGER = LogHelper.getLog(IO.class);

	/**
	 * 将从磁盘读取的文件，放到任意一个字节流里面输出 举例：浏览器下载文件到客户端本地 Stream.download
	 * 
	 * @param filename
	 *            读取的文件名
	 * @param out
	 *            输出的流
	 */
	public static void readFile_save2(String filename, OutputStream out) {
		try {
			// 这时文件是作为输入的，所以使用 FileInputStream
			in2output(new FileInputStream(filename), out);
		} catch (FileNotFoundException e) {
			LOGGER.warning(e);
		}
	}

	/**
	 * 把任意一个流，保存到磁盘 举例：服务端下载远程的资源到服务端本地
	 * 
	 * @param filename
	 *            欲保存的文件名
	 * @param in
	 *            输入流
	 */
	public static void save2file(String filename, InputStream in) {
		// File file = new File(filename); // 根据文件名建立 File 对象
		try {
			// 定义输出类型为 FileOutputStream
			in2output(in, new FileOutputStream(filename));
		} catch (FileNotFoundException e) {
			LOGGER.warning(e);
		}
	}

	public static void save2file(String filename, byte[] data, int off, int length) {
		try (FileOutputStream fos = new FileOutputStream(filename);
				BufferedOutputStream bos = new BufferedOutputStream(fos, 1024);) {
			if (off == 0 && length == 0)
				bos.write(data);
			else
				bos.write(data, off, length);
			bos.flush();
		} catch (IOException e) {
			LOGGER.warning("写入文件" + filename + "失败", e);
		}
	}

	public static void save2file(String filename, byte[] data) {
		save2file(filename, data, 0, 0);
	}

	/**
	 * 流的意思很形象，就是一点一滴的，不是一坨坨大批量的 带缓冲的一入一出 出是字节流，所以要缓冲（字符流自带缓冲，所以不需要额外缓冲）
	 * 
	 * @param in
	 * @param out
	 *            字节流
	 * @deprecated
	 */
	public static void bufferedWrite(InputStream in, OutputStream out) {
		try (OutputStream _out = new BufferedOutputStream(out); // 缓冲区
		) {

			int length = 1024 * 1024;
			byte[] buffer = new byte[length];

			while ((length = in.read(buffer)) > 0)
				_out.write(buffer, 0, length);
		} catch (IOException e) {
			LOGGER.warning(e);
		} finally {
			try {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			} catch (IOException e) {
				LOGGER.warning(e);
			}
		}
	}

	/**
	 * 两端速度不匹配，需要协调 理想环境下，速度一样快，那就没必要搞流，直接一坨给弄过去就可以了 流的意思很形象，就是一点一滴的，不是一坨坨大批量的
	 * 带缓冲的一入一出 出是字节流，所以要缓冲（字符流自带缓冲，所以不需要额外缓冲）
	 * 
	 * @param in
	 * @param out
	 */
	public static void in2output(InputStream in, OutputStream out) {
		// 通过 byte 作为数据中转
		byte[] buffer = new byte[1024];// 1K 的数据块 ,buff 用于存放循环读取的临时数据
		int length;// 读取到的数据长度

		try (OutputStream _out = new BufferedOutputStream(out);) {// 加入缓冲功能
			int i = 0;
			while ((length = in.read(buffer)) != -1) {
				_out.write(buffer, 0, length);
				LOGGER.info((i++ + ":") + length);
			}
		} catch (IOException e) {
			LOGGER.warning(e);
		} finally {
			try {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			} catch (IOException e) {
				LOGGER.warning(e);
			}
		}
	}

	/**
	 * 从输入流中获取数据， 转换到 byte[] 也就是 in 转到内存 虽然大家可能都在内存里面了但还不能直接使用，要转换
	 * 
	 * @param in
	 *            输入流
	 * @return
	 */
	public static byte[] is2byte(InputStream in) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();// 使用内存操作流，读取二进制
		in2output(in, out);
		return out.toByteArray();// InputStream 转换到 byte[]
	}

	/**
	 * 删除文件
	 * 
	 * @param filename
	 */
	public static boolean delete(String filename) {
		return new File(filename).delete();
	}

	/**
	 * 獲取文件名的擴展名
	 * 
	 * @param filename
	 * @return
	 */
	public static String getFileSuffix(String filename) {
		return filename.substring(filename.lastIndexOf(".") + 1);
	}

	/**
	 * 获取文件名的 MIME 类型
	 * 
	 * @param filename
	 * @return
	 */
	public static String getMime(String filename) {
		return new MimetypesFileTypeMap().getContentType(filename);
	}

	/**
	 * 根据日期字符串得到目录名 格式: /2008/10/15/
	 * 
	 * @param datatime
	 * @return
	 */
	public static String getDirnameByDate() {
		String datatime = DateTools.now("yyyy-MM-dd");
		String year = datatime.substring(0, 4);
		String mouth = datatime.substring(5, 7);
		String day = datatime.substring(8, 10);

		return Constant.file_pathSeparator + year + Constant.file_pathSeparator + mouth + Constant.file_pathSeparator
				+ day + Constant.file_pathSeparator;
	}

	/**
	 * 字符流處理
	 * 
	 * @author frank
	 *
	 */
	public static class text {
		private static final com.ajaxjs.util.LogHelper LOGGER = com.ajaxjs.util.LogHelper.getLog(text.class);

		/**
		 * 字节流转换为字符串。注意对于送入的流，执行完毕后会自动关闭。 input=读、output=写
		 * 
		 * @param is
		 * @param encodingSet
		 * @return
		 */
		public static String readStream(InputStream is, String encodingSet) {
			if (is == null)
				return null;
			String line = null;
			StringBuilder result = new StringBuilder();

			// InputStreamReader从一个数据源读取字节，并自动将其转换成Unicode字符
			// OutputStreamWriter将字符的Unicode编码写到字节输出流
			try (
					// 指定编码
					InputStreamReader isReader = new InputStreamReader(is, encodingSet);
					/*
					 * Decorator，装饰模式，又称为 Wrapper，使它具有了缓冲功能
					 * BufferedInputStream、BufferedOutputStream
					 * 只是在这之前动态的为它们加上一些功能（像是缓冲区功能）
					 */
					BufferedReader reader = new BufferedReader(isReader);) {
				while ((line = reader.readLine()) != null) { // 一次读入一行，直到读入null为文件结束
					// 指定编码集的另外一种方法 line = new String(line.getBytes(),
					// encodingSet);
					result.append(line);
					result.append(Constant.newline);
				}
			} catch (IOException e) {
					LOGGER.warning(e);
			} finally {
				try {
					if (is != null)
						is.close();// TODO 考察这个问题，是否执行完毕后会自动关闭？？
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

			return result.toString();
		}

		/**
		 * 读取流，默认 UTF-8
		 * 
		 * @param connection
		 * @return
		 * @throws java.io.IOException
		 */
		public static String readStream(InputStream is) {
			return readStream(is, Constant.encoding_UTF8);
		}

		/**
		 * 读取文件
		 * 
		 * @param filename
		 * @return
		 * @throws IOException
		 */
		public static String readFile(String filename) throws IOException {
			File file = new File(filename);
			if (!file.exists())
				throw new FileNotFoundException(filename + " 不存在！");

			try (FileInputStream is = new FileInputStream(file);) {
				return readStream(is);
			} catch (IOException e) {
				LOGGER.warning("讀取文件流出錯！" + filename, e);
				throw e;
			}
		}

		/**
		 * 写文件不能用 FileWriter，原因是会中文乱码
		 * 
		 * @param filename
		 * @param content
		 * @throws IOException
		 */
		public static void save2file(String filename, String content) throws IOException {
			try (FileOutputStream out = new FileOutputStream(filename);
					// OutputStreramWriter将输出的字符流转化为字节流输出（字符流已带缓冲）
					OutputStreamWriter writer = new OutputStreamWriter(out, Constant.encoding_UTF8);) {
				writer.write(content);
			} catch (IOException e) {
				LOGGER.warning("写入文件" + filename + "失败" , e);
				throw e;
			}
		}
	}
}

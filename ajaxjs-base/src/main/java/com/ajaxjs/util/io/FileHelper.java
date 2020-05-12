/**
 * Copyright sp42 frank@ajaxjs.com
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
package com.ajaxjs.util.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import javax.activation.MimetypesFileTypeMap;

import com.ajaxjs.Version;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 文件操作工具类
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class FileHelper extends IoHelper {
	private static final LogHelper LOGGER = LogHelper.getLog(FileHelper.class);

	/**
	 * 当前系统的分隔符(linux、windows)
	 */
	public static final String SEPARATOR = File.separator;

	/**
	 * 创建目录
	 * 
	 * @param folder 目录字符串
	 */
	public static void mkDir(String folder) {
		mkDir(new File(folder));
	}

	/**
	 * 创建目录
	 * 
	 * @param folder 目录对象
	 */
	public static void mkDir(File folder) {
		if (!folder.exists())// 先检查目录是否存在，若不存在建立
			folder.mkdirs();
	}

	/**
	 * 根据文件名创建目录。 先剥离文件名，剩下的就是目录名。 如果没有输出目录则先创建。
	 * 
	 * @param filePath 完整路径，最后一个元素为文件名
	 */
	public static void mkDirByFileName(String filePath) {
		String arr[] = filePath.split("\\/|\\\\");
		arr[arr.length - 1] = "";// 取消文件名，让最后一个元素为空字符串
		String folder = String.join(File.separator, arr);

		mkDir(folder);
	}

	/**
	 * 检测文件所在的目录是否存在，如果没有则建立。可以跨多个未建的目录
	 * 
	 * @param file 必须是文件，不是目录
	 */
	public static void initFolder(File file) {
		if (file.isDirectory())
			throw new IllegalArgumentException("参数必须是文件，不是目录");

		mkDir(file.getParent());
	}

	/**
	 * 检测文件所在的目录是否存在，如果没有则建立。可以跨多个未建的目录
	 * 
	 * @param file 必须是文件，不是目录
	 */
	public static void initFolder(String file) {
		initFolder(new File(file));
	}

	/**
	 * 新建一个空文件
	 * 
	 * @param folder   如果路径不存在则自动创建
	 * @param fileName 保存的文件名
	 * @return 新建文件的 File 对象
	 */
	public static File createFile(String folder, String fileName) {
		LOGGER.info("正在新建文件 {0}", folder + fileName);

		mkDir(folder);
		return new File(folder + SEPARATOR + fileName);
	}

	/**
	 * 创建文件，注意这是一个空的文件。如果没有指定目录则创建；检测是否可以覆盖文件
	 * 
	 * @param filePath    文件完整路径，最后一个元素是文件名
	 * @param isOverwrite 是否覆盖文件
	 * @return 文件对象
	 * @throws IOException
	 */
	public static File createFile(String filePath, boolean isOverwrite) throws IOException {
		LOGGER.info("正在新建文件 {0}", filePath);

		mkDirByFileName(filePath);

		File file = new File(filePath);
		if (!isOverwrite && file.exists())
			throw new IOException("文件已经存在，禁止覆盖！");

		return file;
	}

	/**
	 * 保存文本文件 写文件不能用 FileWriter，原因是会中文乱码
	 * 
	 * @param file 文件对象
	 * @param text 文本内容
	 */
	public static void saveText(File file, String text) {
		if (Version.isDebug) {
			String _text = text.length() > 200 ? text.substring(0, 200) + "..." : text;
			LOGGER.info("正在保存文件{0}， 保存内容：\n{1}", file.toString(), _text);
		} else
			LOGGER.info("正在保存文件{0}， 保存内容：\n{1}", file.toString());

		save(file, text.getBytes(StandardCharsets.UTF_8), true, false);
	}

	/**
	 * 
	 * 
	 * @param filePath 文件路径
	 * @param text     文本内容
	 */
	public static void saveText(String filePath, String text) {
		saveText(new File(filePath), text);
	}

	/**
	 * 旧方法保存文本内容
	 * 
	 * @param file        文件对象
	 * @param data        文件内容
	 * @param isOverwrite 是否覆盖文件
	 * @param isOldWay    是否已旧的方式打开
	 */
	public static void save(File file, byte[] data, boolean isOverwrite, boolean isOldWay) {
		LOGGER.info("正在保存文件" + file);

		try {
			if (!isOverwrite && file.exists())
				throw new IOException(file + "文件已经存在，禁止覆盖！");

			if (file.isDirectory())
				throw new IOException(file + " 不能是目录，请指定文件");

			if (!file.exists())
				file.createNewFile();

			if (isOldWay)
				save(file, data, 0, data.length);
			else
				Files.write(file.toPath(), data);
		} catch (IOException e) {
			LOGGER.warning(e);
		}
	}

	/**
	 * 
	 * @param file
	 * @param data
	 * @param off
	 * @param len
	 */
	public static void save(File file, byte[] data, int off, int len) {
		try (OutputStream out = new FileOutputStream(file)) {
			out.write(data, off, len);
			out.flush();
		} catch (IOException e) {
			LOGGER.warning(e);
		}
	}

	/**
	 * 保存文本文件 写文件不能用 FileWriter，原因是会中文乱码
	 * 
	 * @param file 文件对象
	 * @param text 文本内容
	 */
	public static void saveTextOld(File file, String text) {
		LOGGER.info("正在保存文件{0}， 保存内容：\n{1}", file.toString(), text);

		// OutputStreramWriter 将输出的字符流转化为字节流输出（字符流已带缓冲）
		try (OutputStream out = new FileOutputStream(file);
				OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);) {
			writer.write(text);
		} catch (IOException e) {
			LOGGER.warning(e);
		}
	}

	/**
	 * 获取文件名的 MIME 类型。检测手段不会真正打开文件进行检查而是单纯文件名的字符串判断。
	 * 
	 * @param filename 文件名
	 * @return MIME 类型
	 */
	public static String getMime(String filename) {
		Path path = Paths.get(filename); // works on java7

		try {
			return Files.probeContentType(path);
		} catch (IOException e) {
			LOGGER.warning(e);
			return null;
		}
	}

	/**
	 * 获取文件名的 MIME 类型
	 * 
	 * @param file 文件对象
	 * @return MIME 类型
	 */
	public static String getMime(File file) {
		String contentType = new MimetypesFileTypeMap().getContentType(file);

		if (file.getName().endsWith(".png"))
			contentType = "image/png"; // TODO needs?

		if (contentType == null)
			contentType = "application/octet-stream";

		return contentType;
	}

	/**
	 * 根据日期字符串得到目录名 格式: /2008/10/15/
	 * 
	 * @return 如 /2008/10/15/ 格式的字符串
	 */
	public static String getDirNameByDate() {
		String datatime = CommonUtil.now("yyyy-MM-dd");
		String year = datatime.substring(0, 4), mouth = datatime.substring(5, 7), day = datatime.substring(8, 10);

		return SEPARATOR + year + SEPARATOR + mouth + SEPARATOR + day + SEPARATOR;
	}

	/**
	 * 输入 /foo/bar/foo.jpg 返回 foo.jpg
	 * 
	 * @param str 输入的字符串
	 * @return 文件名
	 */
	public static String getFileName(String str) {
		String[] arr = str.split("\\/|\\\\");// 取消文件名，让最后一个元素为空字符串

		return arr[arr.length - 1];
	}

	/**
	 * 获取 URL 上的文件名，排除 ? 参数部分
	 * 
	 * @param url URL
	 * @return 文件名
	 */
	public static String getFileNameFromUrl(String url) {
		return getFileName(url).split("\\?")[0];
	}

	/**
	 * 獲取文件名的擴展名
	 * 
	 * @param filename 文件名
	 * @return 擴展名
	 */
	public static String getFileSuffix(String filename) {
		return filename.substring(filename.lastIndexOf(".") + 1);
	}

	/**
	 * 输入文件的完全路径，返回文件输入流 FileInputStream
	 * 
	 * @param filePath 文件的完全路径
	 * @return 文件输入流
	 */
	public static FileInputStream path2FileIn(String filePath) {
		return path2FileIn(new File(filePath));
	}

	/**
	 * 输入文件对象，返回文件输入流 FileInputStream
	 * 
	 * @param file 文件对象
	 * @return 文件输入流
	 */
	public static FileInputStream path2FileIn(File file) {
		try {
			if (!file.exists())
				throw new FileNotFoundException(file.getPath() + " 不存在！");

			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			LOGGER.warning(e);
			return null;
		}
	}

	/**
	 * 打开文件，返回其文本内容
	 * 
	 * @param filePath 文件的完全路径
	 * @return 文件内容
	 */
	public static String openAsText(String filePath) {
		return openAsText(filePath, StandardCharsets.UTF_8, false);
	}

	/**
	 * 打开文件，返回其文本内容，可指定编码
	 * 
	 * @param filePath 文件磁盘路径
	 * @param encode   文件编码，默认是 UTF-8 编码。开发者还应该明确规定文件的字符编码，以避免任异常或解析错误。如果读入的文件的编码是
	 *                 ANSI 编码，那么会报 java.nio.charset.MalformedInputException:Input
	 *                 length = 1 错误
	 * @param isOldWay 是否已旧的方式打开
	 * @return 文件内容
	 */
	public static String openAsText(String filePath, Charset encode, boolean isOldWay) {
		LOGGER.info("读取文件 {0}", filePath);

		if (isOldWay)
			return byteStream2string(path2FileIn(filePath));

		Path path = Paths.get(filePath);

		try {
			if (Files.isDirectory(path))
				throw new IOException("参数 fullpath：" + filePath + " 不能是目录，请指定文件");

			if (!Files.exists(path))
				throw new FileNotFoundException(filePath + "　不存在");
		} catch (IOException e) {
			LOGGER.warning(e);
			return null;
		}

		try {
			// 此方法不适合读取很大的文件，因为可能存在内存空间不足的问题。
			StringBuilder sb = new StringBuilder();
			Files.lines(path, encode).forEach(str -> sb.append(str));

			return sb.toString();
		} catch (IOException e) {
			LOGGER.warning(e);
		}

		return null;

	}

	/**
	 * 获得指定文件的 byte 数组
	 * 
	 * @param file 文件对象
	 * @return 文件字节数组
	 */
	public static byte[] openAsByte(File file) {
		return inputStream2Byte(path2FileIn(file));
	}

	/**
	 * 返回某个文件夹里面的所有文件
	 * 
	 * @return 文件名集合
	 */
	public static String[] getFiles(File file) {
		return file.isDirectory() ? file.list() : null;
	}

	/**
	 * 删除文件或目录
	 * 
	 * @param filePath 文件的完全路径
	 */
	public static void delete(String filePath) {
		try {
			Files.delete(Paths.get(filePath));
		} catch (IOException e) {
			LOGGER.warning(e);
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param filePath 文件的完全路径
	 */
	public static void deleteOldWay(String filePath) {
		delete(new File(filePath));
	}

	/**
	 * 删除文件
	 * 
	 * @param file 文件对象
	 */
	public static void delete(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();

			for (File f : files) {
				delete(f);
			}
		}

		if (!file.delete())
			LOGGER.warning("文件 {0} 删除失败！", file.toString());
	}

	/**
	 * 复制文件
	 * 
	 * @param target 源文件
	 * @param dest   目的文件/目录，如果最后一个为目录，则不改名，如果最后一个为文件名，则改名
	 * @return 是否操作成功
	 * @throws IOException IO 异常
	 */
	public static boolean copy(String target, String dest) {
		return copy(target, dest, false);
	}

	/**
	 * 复制文件
	 * 
	 * @param target             源文件
	 * @param dest               目的文件/目录，如果最后一个为目录，则不改名，如果最后一个为文件名，则改名
	 * @param isREPLACE_EXISTING 是否替换已存在的文件
	 * @return true 表示复制成功
	 */
	public static boolean copy(String target, String dest, boolean isREPLACE_EXISTING) {
		try {
			if (isREPLACE_EXISTING)
				Files.copy(Paths.get(target), Paths.get(dest), StandardCopyOption.REPLACE_EXISTING);
			else
				Files.copy(Paths.get(target), Paths.get(dest));
		} catch (IOException e) {
			LOGGER.warning(e);
			return false;
		}

		return true;
	}

	/**
	 * 移动文件
	 * 
	 * @param target 源文件
	 * @param dest   目的文件/目录，如果最后一个为目录，则不改名，如果最后一个为文件名，则改名
	 * @return 是否操作成功
	 * @throws IOException IO 异常
	 */
	public static boolean move(String target, String dest) {
		try {
			Files.copy(Paths.get(target), Paths.get(dest));
		} catch (IOException e) {
			LOGGER.warning(e);
			return false;
		}

		return true;
	}

	/**
	 * 遍历整个文件目录，递归的
	 * 
	 * @param _dir   指定的目录
	 * @param method 搜索函数
	 * @return 搜索结果
	 * @throws IOException
	 */
	public static List<Path> walkFileTree(String _dir, Predicate<Path> method) throws IOException {
		Path dir = Paths.get(_dir);

		if (!Files.isDirectory(dir))
			throw new IOException("参数 ：" + _dir + " 不是目录，请指定目录");

		List<Path> result = new LinkedList<>();
		Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
				if (method.test(file))
					result.add(file);

				return FileVisitResult.CONTINUE;
			}
		});

		return result;
	}

	/**
	 * 遍历整个目录，非递归的
	 * 
	 * @param _dir   指定的目录
	 * @param method 搜索函数
	 * @return 搜索结果
	 * @throws IOException
	 */
	public static List<Path> walkFile(String _dir, Predicate<Path> method) throws IOException {
		Path dir = Paths.get(_dir);

		if (!Files.isDirectory(dir))
			throw new IOException("参数 ：" + _dir + " 不是目录，请指定目录");

		List<Path> result = new LinkedList<>();
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
			for (Path e : stream) {
				if (method.test(e))
					result.add(e);
			}
		}

		return result;
	}

	/**
	 * 文件格式只有utf8和gbk两种格式，而一般的utf8格式文件一般是带着BOM信息的，这样的文件刚开始的三个字节永远是一样的，
	 * 所以如果能根据这个规律探测文件编码格式，那似乎问题就解决拉
	 * 但是很可惜，我们的文件是不带BOM信息的，所以只好统计整个文件的字节流，看符合utf格式的字节多还是非utf8格式的字节多，
	 * 如果是前者，那这个文件就很可能是utf8格式，根据这个规律，我们发现，探测的准确度还是比较高的
	 * 
	 * @param data
	 * @return
	 */
	public static boolean isUTF8(byte[] data) {
		int countGoodUtf = 0, countBadUtf = 0;
		byte currentByte = 0x00, previousByte = 0x00;

		for (int i = 1; i < data.length; i++) {
			currentByte = data[i];
			previousByte = data[i - 1];

			if ((currentByte & 0xC0) == 0x80) {
				if ((previousByte & 0xC0) == 0xC0)
					countGoodUtf++;
				else if ((previousByte & 0x80) == 0x00)
					countBadUtf++;
			} else if ((previousByte & 0xC0) == 0xC0)
				countBadUtf++;
		}

		return countGoodUtf > countBadUtf;
	}
}

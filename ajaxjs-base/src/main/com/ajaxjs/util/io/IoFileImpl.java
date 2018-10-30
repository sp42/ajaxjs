package com.ajaxjs.util.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class IoFileImpl {
	/**
	 * 打开文件，返回其文本内容
	 * 
	 * @param fullpath 文件磁盘路径
	 * @return 文件内容
	 * @throws IOException IO 异常
	 */
	public static String read(String fullpath) throws IOException {
		return read(fullpath, StandardCharsets.UTF_8);
	}

	/**
	 * 打开文件，返回其文本内容，可指定编码
	 * 
	 * @param fullpath 文件磁盘路径
	 * @param encode 文件编码
	 * @return 文件内容
	 * @throws IOException IO 异常
	 */
	public static String read(String fullpath, Charset encode) throws IOException {
		Path path = Paths.get(fullpath);

		if (Files.isDirectory(path))
			throw new IOException("参数 fullpath：" + fullpath + " 不能是目录，请指定文件");

		if (!Files.exists(path))
			throw new IOException(fullpath + "　不存在");

		return new String(Files.readAllBytes(path), encode);
	}

	/**
	 * 
	 * @param fullpath
	 * @param content
	 * @return 是否操作成功
	 * @throws IOException
	 */
	public static boolean save(String fullpath, String content) throws IOException {
		Path path = Paths.get(fullpath);

		if (Files.isDirectory(path))
			throw new IOException("参数 fullpath：" + fullpath + " 不能是目录，请指定文件");

		if (!Files.exists(path))
			Files.createFile(path);

		Files.write(path, content.getBytes());

		return true;
	}

	/**
	 * 删除文件或目录
	 * 
	 * @param fullpath 源文件
	 * @return 是否操作成功
	 * @throws IOException IO 异常
	 */
	public static boolean delete(String fullpath) throws IOException {
		Files.delete(Paths.get(fullpath));
		return true;
	}

	/**
	 * 复制文件
	 * 
	 * @param target 源文件
	 * @param dest 目的文件/目录，如果最后一个为目录，则不改名，如果最后一个为文件名，则改名
	 * @return 是否操作成功
	 * @throws IOException IO 异常
	 */
	public static boolean copy(String target, String dest) throws IOException {
		Files.copy(Paths.get(target), Paths.get(dest));
		return true;
	}

	/**
	 * 移动文件
	 * 
	 * @param target 源文件
	 * @param dest 目的文件/目录，如果最后一个为目录，则不改名，如果最后一个为文件名，则改名
	 * @return 是否操作成功
	 * @throws IOException IO 异常
	 */
	public static boolean move(String target, String dest) throws IOException {
		Files.copy(Paths.get(target), Paths.get(dest));
		return true;
	}

	/**
	 * 旧方法保存文本内容
	 * 
	 * @param fullpath
	 * @param content
	 * @throws IOException
	 */
	public static void saveClassic(String fullpath, String content) throws IOException {
		File file = new File(fullpath);
		if (file.isDirectory())
			throw new IOException("参数 fullpath：" + fullpath + " 不能是目录，请指定文件");

		try (FileOutputStream fop = new FileOutputStream(file)) {
			if (!file.exists())
				file.createNewFile();

			fop.write(content.getBytes());
			fop.flush();
		}
	}

	// Charset.forName("GBK");

	/**
	 * 遍历整个文件目录，递归的
	 * 
	 * @param _dir 指定的目录
	 * @param method 搜索函数
	 * @return 搜索结果
	 * @throws IOException
	 */
	public static List<Path> walkFileTree(String _dir, Predicate<Path> method) throws IOException {
		List<Path> result = new LinkedList<>();

		Path dir = Paths.get(_dir);
		if (!Files.isDirectory(dir))
			throw new IOException("参数 ：" + _dir + " 不是目录，请指定目录");

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
	 * @param _dir 指定的目录
	 * @param method 搜索函数
	 * @return 搜索结果
	 * @throws IOException
	 */
	public static List<Path> walkFile(String _dir, Predicate<Path> method) throws IOException {
		List<Path> result = new LinkedList<>();

		Path dir = Paths.get(_dir);
		if (!Files.isDirectory(dir))
			throw new IOException("参数 ：" + _dir + " 不是目录，请指定目录");

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
			for (Path e : stream) {
				if (method.test(e))
					result.add(e);
			}
		}

		return result;
	}

	static boolean get(Path p) {
//		System.out.println(p);
		return true;
	}

	public static void main(String[] args) throws IOException {

		walkFileTree("C:\\sp42\\sp42_share\\book\\", IoFileImpl::get);
		FunctionInterfaceTest f = p -> p + "ddf";

//		walk("C:\\sp42\\sp42_share\\book\\");

		System.out.println(f.getInfo("dsadsa"));

		FunctionInterfaceTest f2 = IoFileImpl::foo;
		System.out.println(f.getInfo("dsadsa"));
		System.out.println(f2.getInfo("foooooooo"));
	}

	public static String foo(String input) {
		return input + "sdsad";
	}

	@FunctionalInterface
	public interface FunctionInterfaceTest {
		public String getInfo(String input);
	}
}

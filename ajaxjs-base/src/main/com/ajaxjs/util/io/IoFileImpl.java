package com.ajaxjs.util.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class IoFileImpl {
	public String read(String fullpath) throws IOException {
		return read(fullpath, StandardCharsets.UTF_8);
	}

	public static boolean save(String fullpath, String content) throws IOException {
		Path path = Paths.get(fullpath);

		if (Files.isDirectory(path))
			throw new IOException("参数 fullpath：" + fullpath + " 不能是目录，请指定文件");

		if (!Files.exists(path))
			Files.createFile(path);

		Files.write(path, content.getBytes());

		return true;
	}

	public boolean delete(String target) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean copy(String target, String dest) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean move(String target, String dest) {
		// TODO Auto-generated method stub
		return false;
	}

	public String read(String fullpath, Charset encode) throws IOException {
		Path path = Paths.get(fullpath);

		if (Files.isDirectory(path))
			throw new IOException("参数 fullpath：" + fullpath + " 不能是目录，请指定文件");

		if (!Files.exists(path))
			throw new IOException(fullpath + "　不存在");

		return new String(Files.readAllBytes(path), encode);
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

	public static void main(String[] args) {
		FunctionInterfaceTest f = p -> p + "ddf";
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

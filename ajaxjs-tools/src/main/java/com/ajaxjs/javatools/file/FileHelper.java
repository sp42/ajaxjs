package com.ajaxjs.javatools.file;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class FileHelper {

	/**
	 * 计算一个文件夹以及子文件夹在内的总大小
	 * 
	 * @author http://rookiedong.iteye.com/blog/900100
	 *
	 */
	public static class GetSize {
		double size = 0.0;

		/**
		 * 计算文件或者文件夹的大小 ，单位 MB
		 * 
		 * @param file 要计算的文件或者文件夹 ， 类型：java.io.File
		 * @return 大小，单位：MB
		 */
		public double getSize(File file) {
			// 判断文件是否存在
			if (file.exists()) {
				// 如果是目录则递归计算其内容的总大小，如果是文件则直接返回其大小
				if (!file.isFile()) {
					// 获取文件大小
					File[] fl = file.listFiles();
					double ss = 0;
					for (File f : fl)
						ss += getSize(f);
					return ss;
				} else {
					double ss = (double) file.length() / 1024 / 1024;
					LOGGER.info(file.getName() + " : " + ss + "MB");
					return ss;
				}
			} else {
				LOGGER.info("文件或者文件夹不存在，请检查路径是否正确！");
				return 0.0;
			}
		}
	}

	/**
	 * 删除文件（夹）
	 * 
	 * @param file 文件（夹）
	 */
	public static void delete(File file) {
		if (!file.exists())
			return;
		if (file.isFile())
			file.delete();
		else {
			for (File f : file.listFiles())
				delete(f);

			file.delete();
		}
	}

	/**
	 * 递归删除复杂目录结构下的文件
	 * 
	 * @param file
	 */
	public static void rmDirectory(File file) {
		if (file.isDirectory()) {
			for (String child : file.list())
				rmDirectory(new File(file, child)); // 回调
		}

		if (file.isFile()) {
			file.delete();
			LOGGER.info("成功删除文件:%s" + file.getName());
		}

		if (file.isDirectory()) {
			file.delete();
			LOGGER.info("成功删除文件夹: " + file.getName());
		}
	}

	/**
	 * 复制文件（夹）到一个目标文件夹
	 * 
	 * @param resFile 源文件（夹）
	 * @param objFolderFile 目标文件夹
	 */
	public static void copy(File resFile, File objFolderFile) {
		if (!resFile.exists())
			return;
		if (!objFolderFile.exists())
			objFolderFile.mkdirs();

		if (resFile.isFile()) {
			File objFile = new File(objFolderFile.getPath() + File.separator + resFile.getName());

			// 复制文件到目标地
			try (InputStream ins = new FileInputStream(resFile); FileOutputStream outs = new FileOutputStream(objFile);) {

				byte[] buffer = new byte[1024 * 512];
				int length;
				while ((length = ins.read(buffer)) != -1)
					outs.write(buffer, 0, length);

				outs.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			String objFolder = objFolderFile.getPath() + File.separator + resFile.getName();
			File _objFolderFile = new File(objFolder);
			_objFolderFile.mkdirs();

			for (File sf : resFile.listFiles())
				copy(sf, new File(objFolder));
		}
	}

	/**
	 * 缓存字符流读写复杂目录结构下的各种文件实例。 现在有很多强大的工具包 封装了java IO操作，开发者只需要简单的调用就可以实现文件的读写操作，
	 * 但是对于需要在IO过程中加入特殊业务逻辑的操作还是需要自己去实现IO流的 （比如大数据情况下，用读取的文件名称加日期作为文件夹的名字对文件分类管理），
	 * 上面实现了一个缓冲字节流的基本操作，读者可以在此基础上加入自己的业务逻辑组装自己的IO流。 myFilePath.mkdir(); //建立文件夹
	 * 若要建多层次的文件夹，必须一次一次的建，此方法不支持一次性建完。 ;
	 * 
	 * @param toFile
	 * @param fromFile
	 */
	public static void copyToDirectory(File toFile, File fromFile) {
		if (fromFile.isDirectory()) {
			LOGGER.info("toFile路径: " + toFile.getAbsolutePath());
			if (!toFile.exists())
				toFile.mkdir();

			for (String child : fromFile.list())
				copyToDirectory(new File(toFile, child), new File(fromFile, child));// 如果文件夹有多层会递归调用
		}

		if (toFile.isDirectory())
			return;
		LOGGER.info("toFile名称: " + toFile.getName());

		int BYTE_SIZE = 1, SAVE_SIZE = 1024;
		byte[] buff = new byte[BYTE_SIZE]; // 每次读的缓存
		byte[] save = new byte[SAVE_SIZE]; // 保存前缓存
		int count = 0;

		try (BufferedInputStream bf = new BufferedInputStream(new FileInputStream(fromFile)); BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(toFile));) {
			LOGGER.info("fromFile: " + fromFile.getName());
			LOGGER.info("已经获取资源......");
			LOGGER.info("准备保存到：" + toFile.getPath());
			LOGGER.info("开始读入......");

			int i = 0;
			while (bf.read(buff) != -1) { // 一个字节一个字节读
				save[i] = buff[0];
				if (i == SAVE_SIZE - 1) { // 达到保存长度时开始保存
					bos.write(save, 0, SAVE_SIZE);
					save = new byte[SAVE_SIZE];
					i = 0;
				} else {
					i++;
				}
			}
			// 最后这段如果没达到保存长度，需要把前面的保存下来
			if (i > 0)
				bos.write(save, 0, i - 1);

			LOGGER.info("读取成功！！！");
			count++;

		} catch (IOException e) {
			e.printStackTrace();
		}

		LOGGER.info("读取文件总数: " + count);
	}

	/**
	 * 将文件（夹）移动到令一个文件夹
	 * 
	 * @param resFile 源文件（夹）
	 * @param objFolderFile 目标文件夹
	 * @throws IOException 异常时抛出
	 */
	public static void move(File resFile, File objFolderFile) throws IOException {
		copy(resFile, objFolderFile);
		delete(resFile);
	}

	/**
	 * 将内容追加到文件尾部
	 * 
	 * @param fileName
	 * @param content
	 */
	public static void appendToFile(String fileName, String content) {
		try (RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");) {
			// 文件长度，字节数
			long fileLength = randomFile.length();
			// 将写文件指针移到文件尾。
			randomFile.seek(fileLength);
			randomFile.writeBytes(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * nio 遍历一个文件夹下的所有文件（包括子文件夹）
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static List<File> walkFileTree(Path path) throws IOException {
		final List<File> files = new ArrayList<>();

		SimpleFileVisitor<Path> finder = new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				files.add(file.toFile());
				return super.visitFile(file, attrs);
			}
		};

		java.nio.file.Files.walkFileTree(path, finder);

		return files;
	}

	/**
	 * nio 复制文件 http://mn960mn.blog.163.com/blog/static/114103084201142231759701/
	 * 
	 * @param from
	 * @param to
	 */
	public static void copyFile(String from, String to) {
		try (FileOutputStream fout = new FileOutputStream(from); FileInputStream fin = new FileInputStream(to);) {
			try (FileChannel fcin = fin.getChannel(); FileChannel fcout = fout.getChannel();) {
				ByteBuffer buffer = ByteBuffer.allocate(100);

				while ((fcin.read(buffer)) != -1) {
					buffer.flip();
					fcout.write(buffer);
					buffer.clear();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 监视文件是否有变化。 这里推荐使用 JDK 7 NIO 中新增的 Watch Service API。Watch Service API
	 * 将尽可能使用操作系统的文件 API ，当操作系统不支持时，则使用轮询。简而言之，就是 JNI + 轮询，自己实现轮询倒不难，但要实现跨平台的 JNI
	 * 调用就很啰嗦了。
	 * 
	 * @param dir
	 * @param listener
	 */
	public static void monit(final String dir, final FileChangeListener listener) {
		new Thread() {
			public void run() {
				Path path = new File(dir).toPath();
				WatchService watcher;

				try {
					watcher = FileSystems.getDefault().newWatchService();
					path.register(watcher, ENTRY_CREATE, ENTRY_MODIFY);
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}

				while (true) {
					WatchKey key;
					try {
						key = watcher.take();
					} catch (InterruptedException _) {
						return;
					}

					processKey(path, key, listener);
				}
			};
		}.start();
	}

	/**
	 * 回调接口
	 */
	public static interface FileChangeListener {
		public void fileChanged(File file);
	}

	/**
	 * 
	 * @param path
	 * @param key
	 * @param listener
	 */
	private static void processKey(Path path, WatchKey key, FileChangeListener listener) {
		for (WatchEvent<?> event : key.pollEvents()) {
			if (event.kind() == OVERFLOW)
				continue;

			Path fileName = (Path) event.context();
			File file = path.resolve(fileName).toFile();
			listener.fileChanged(file);
		}

		key.reset();
	}
}

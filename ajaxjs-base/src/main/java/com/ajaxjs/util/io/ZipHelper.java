package com.ajaxjs.util.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.ajaxjs.util.logger.LogHelper;

public class ZipHelper {
	private static final LogHelper LOGGER = LogHelper.getLog(ZipHelper.class);

	/**
	 * 列出ZIP文件中的条目 http://www.importnew.com/12800.html TODO
	 * 
	 * @param zipFile ZIP 包
	 */
	public static void list(String zipFile) {
		try (ZipFile zip = new ZipFile(zipFile)) {

		} catch (IOException e) {
			LOGGER.warning(e);
		}
	}

	/**
	 * 解压缩文件
	 * 
	 * @param zipFile        ZIP 包
	 * @param destinationDir 目的目录
	 * @return 是否操作成功
	 */
	public static boolean unzip(String zipFile, String destinationDir) {
		File destDir = new File(destinationDir);
		byte[] buffer = new byte[1024];

		try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));) {
			ZipEntry zipEntry = zis.getNextEntry();

			while (zipEntry != null) {
				File newFile = newFile(destDir, zipEntry);

				try (OutputStream fos = new FileOutputStream(newFile);) {
					int len;

					while ((len = zis.read(buffer)) > 0)
						fos.write(buffer, 0, len);
				}

				zipEntry = zis.getNextEntry();
			}
		} catch (IOException e) {
			LOGGER.warning(e);
		}

		return true;
	}

	/**
	 * 
	 * @param destinationDir 目的目录
	 * @param zipEntry       ZIP 包里面的条目
	 * @return
	 * @throws IOException IO 异常
	 */
	private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
		File destFile = new File(destinationDir, zipEntry.getName());

		String destDirPath = destinationDir.getCanonicalPath();
		String destFilePath = destFile.getCanonicalPath();

		if (!destFilePath.startsWith(destDirPath + File.separator))
			throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());

		return destFile;
	}

	/**
	 * 压缩文件
	 * 
	 * @param sourceFile 目录或文件
	 * @param zos        压缩流
	 * @param name       压缩后的名称
	 * @throws IOException
	 */
	static void compress(File sourceFile, ZipOutputStream zos, String name) throws IOException {
		if (sourceFile.isFile()) {
			zos.putNextEntry(new ZipEntry(name));

			try (InputStream in = new FileInputStream(sourceFile);) {
				FileUtil.write(in, zos, FileUtil.BUFFER_SIZE);
			}

			zos.closeEntry();
		} else {
			File[] listFiles = sourceFile.listFiles();

			if (listFiles == null || listFiles.length == 0) {
				// 空文件夹的处理 没有文件，不需要文件的copy
				zos.putNextEntry(new ZipEntry(name + "/"));
				zos.closeEntry();
			} else {
				for (File file : listFiles) {
					compress(file, zos, name + "\\" + file.getName());
				}
			}
		}
	}

	/**
	 * 压缩文件
	 * 
	 * @param dir  要压缩的本地目录
	 * @param save 保存的文件名，例如 c:\\temp\\foo.zip
	 * @return 如果压缩成功返回 true
	 */
	public static boolean toZip(String dir, String save) {
		long start = System.currentTimeMillis();
		File sourceFile = new File(dir);

		try (OutputStream saveOut = new FileOutputStream(new File(save));
				ZipOutputStream zos = new ZipOutputStream(saveOut);) {
			compress(sourceFile, zos, sourceFile.getName());

			LOGGER.info("压缩完成，耗时：" + (System.currentTimeMillis() - start) + " ms");
			return true;
		} catch (IOException e) {
			LOGGER.warning(e);
			return false;
		}
	}
}

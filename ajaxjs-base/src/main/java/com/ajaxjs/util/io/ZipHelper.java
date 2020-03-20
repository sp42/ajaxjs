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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.Function;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.ajaxjs.util.logger.LogHelper;

/**
 * ZIP 压缩/解压缩
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class ZipHelper {
	private static final LogHelper LOGGER = LogHelper.getLog(ZipHelper.class);

	/**
	 * 解压文件
	 * 
	 * @param save    解压文件的路径，必须为目录
	 * @param zipFile 输入的解压文件路径，例如C:/temp/foo.zip或 c:\\temp\\bar.zip
	 */
	public static void unzip(String save, String zipFile) {
		if (!new File(save).isDirectory())
			throw new IllegalArgumentException("保存的路径必须为目录路径");

		long start = System.currentTimeMillis();
		File folder = new File(save);
		
		if (!folder.exists())
			folder.mkdirs();

		try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));) {
			ZipEntry ze;
			while ((ze = zis.getNextEntry()) != null) {
				File newFile = new File(save + File.separator + ze.getName());
				System.out.println("file unzip : " + newFile.getAbsoluteFile());

				// 大部分网络上的源码，这里没有判断子目录
				if (ze.isDirectory()) {
					newFile.mkdirs();
				} else {
//					new File(newFile.getParent()).mkdirs();
					FileHelper.initFolder(newFile);
					FileOutputStream fos = new FileOutputStream(newFile);
					IoHelper.write(zis, fos, false);
					fos.close();
				}

//				ze = zis.getNextEntry();
			}
			zis.closeEntry();
		} catch (IOException e) {
			LOGGER.warning(e);
		}

		LOGGER.info("解压缩完成，耗时：{0}ms，保存在{1}", System.currentTimeMillis() - start, save);
	}

	/**
	 * 压缩文件
	 * 
	 * @param toZip   要压缩的目录或文件
	 * @param saveZip 压缩后保存的 zip 文件名
	 */
	public static void zip(String toZip, String saveZip) {
		zip(toZip, saveZip, null);
	}

	/**
	 * 压缩文件
	 * 
	 * @param toZip     要压缩的目录或文件
	 * @param saveZip   压缩后保存的 zip 文件名
	 * @param everyFile 输入 File，可在这 Lambda 里面判断是否加入 ZIP 压缩，返回 true 表示允许，反之不行
	 */
	public static void zip(String toZip, String saveZip, Function<File, Boolean> everyFile) {
		long start = System.currentTimeMillis();
		File fileToZip = new File(toZip);

		FileHelper.initFolder(saveZip);

		try (FileOutputStream fos = new FileOutputStream(saveZip); ZipOutputStream zipOut = new ZipOutputStream(fos);) {
			zip(fileToZip, fileToZip.getName(), zipOut, everyFile);
		} catch (IOException e) {
			LOGGER.warning(e);
		}

		LOGGER.info("压缩完成，耗时：{0}ms，保存在{1}", System.currentTimeMillis() - start, saveZip);
	}

	/**
	 * 内部的压缩方法
	 * 
	 * @param toZip     要压缩的目录或文件
	 * @param fileName  ZIP 内的文件名
	 * @param zipOut    ZIP 流
	 * @param everyFile 输入 File，可在这 Lambda 里面判断是否加入 ZIP 压缩，返回 true 表示允许，反之不行
	 */
	private static void zip(File toZip, String fileName, ZipOutputStream zipOut, Function<File, Boolean> everyFile) {
		if (toZip.isHidden())
			return;

		if (everyFile != null && !everyFile.apply(toZip)) {
			return; // 跳过不要的
		}

		try {
			if (toZip.isDirectory()) {
				zipOut.putNextEntry(new ZipEntry(fileName.endsWith("/") ? fileName : fileName + "/"));
				zipOut.closeEntry();

				File[] children = toZip.listFiles();
				for (File childFile : children) {
					zip(childFile, fileName + "/" + childFile.getName(), zipOut, everyFile);
				}

				return;
			}

			zipOut.putNextEntry(new ZipEntry(fileName));

			try (FileInputStream in = new FileInputStream(toZip);) {
				IoHelper.write(in, zipOut, false);
			}
		} catch (IOException e) {
			LOGGER.warning(e);
		}
	}
}

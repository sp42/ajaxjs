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
 * 
 * @author Administrator
 *
 */
public class ZipHelper {
	private static final LogHelper LOGGER = LogHelper.getLog(ZipHelper.class);

	/**
	 * 解压文件
	 * 
	 * @param zipFile 解压文件路径
	 * @param save    输出解压文件路径
	 */
	public static void unzip(String save, String zipFile) {
		File folder = new File(save);
		if (!folder.exists())
			folder.mkdir();

		try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));) {
			ZipEntry ze;
			while ((ze = zis.getNextEntry()) != null) {
				File newFile = new File(save + File.separator + ze.getName());
				System.out.println("file unzip : " + newFile.getAbsoluteFile());

				// 大部分网络上的源码，这里没有判断子目录
				if (ze.isDirectory()) {
					newFile.mkdirs();
				} else {
					new File(newFile.getParent()).mkdirs();
					FileOutputStream fos = new FileOutputStream(newFile);
					IoHelper.write(zis, fos, false);
					fos.close();
				}

//				ze = zis.getNextEntry();
			}
			zis.closeEntry();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 压缩文件
	 * 
	 * @param sourceFile 要压缩的目录或文件
	 * @param save       压缩后的名称
	 */
	public static void zip(String sourceFile, String save) {
		zip(sourceFile, save, null);
	}

	/**
	 * 压缩文件
	 * 
	 * @param zipFile   要压缩的目录或文件
	 * @param save      压缩后的名称
	 * @param everyFile
	 */
	public static void zip(String zipFile, String save, Function<File, Boolean> everyFile) {
		long start = System.currentTimeMillis();

		try (FileOutputStream fos = new FileOutputStream(save); ZipOutputStream zipOut = new ZipOutputStream(fos);) {

			File fileToZip = new File(zipFile);
			zip(fileToZip, fileToZip.getName(), zipOut, everyFile);
		} catch (IOException e) {
			LOGGER.warning(e);
		}

		LOGGER.info("压缩完成，耗时：" + (System.currentTimeMillis() - start) + " ms");
	}

	private static void zip(File fileToZip, String fileName, ZipOutputStream zipOut,
			Function<File, Boolean> everyFile) {
		if (fileToZip.isHidden())
			return;

		if (everyFile != null && !everyFile.apply(fileToZip)) {
			return; // 跳过不要的
		}

		try {
			if (fileToZip.isDirectory()) {
				zipOut.putNextEntry(new ZipEntry(fileName.endsWith("/") ? fileName : fileName + "/"));
				zipOut.closeEntry();

				File[] children = fileToZip.listFiles();
				for (File childFile : children) {
					zip(childFile, fileName + "/" + childFile.getName(), zipOut, everyFile);
				}

				return;
			}

			zipOut.putNextEntry(new ZipEntry(fileName));

			try (FileInputStream fis = new FileInputStream(fileToZip);) {
				IoHelper.write(fis, zipOut, false);
			}
		} catch (IOException e) {
			LOGGER.warning(e);
		}
	}
}

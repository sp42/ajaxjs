/**
 * Copyright sp42 frank@ajaxjs.com
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.util.io;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.zip.*;

/**
 * ZIP 压缩/解压缩
 *
 * @author sp42 frank@ajaxjs.com
 */
@Slf4j
public class ZipHelper {
    /**
     * 解压文件
     *
     * @param save    解压文件的路径，必须为目录
     * @param zipFile 输入的解压文件路径，例如C:/temp/foo.zip或 c:\\temp\\bar.zip
     */
    public static void unzip(String save, String zipFile) {
        if (!new File(save).isDirectory()) throw new IllegalArgumentException("保存的路径必须为目录路径");

        long start = System.currentTimeMillis();
        File folder = new File(save);

        if (!folder.exists()) folder.mkdirs();

        try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(Paths.get(zipFile)))) {
            ZipEntry ze;

            while ((ze = zis.getNextEntry()) != null) {
                File newFile = new File(save + File.separator + ze.getName());

                if (ze.isDirectory()) // 大部分网络上的源码，这里没有判断子目录
                    newFile.mkdirs();
                else {
//					new File(newFile.getParent()).mkdirs();
                    FileHelper.initFolder(newFile);
                    FileOutputStream fos = new FileOutputStream(newFile);
                    StreamHelper.write(zis, fos, false);
                    fos.close();
                }

//				ze = zis.getNextEntry();
            }
            zis.closeEntry();
        } catch (IOException e) {
            log.warn("ERROR>>", e);
        }

        log.info("解压缩完成，耗时：{}ms，保存在{}", System.currentTimeMillis() - start, save);
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

        try (FileOutputStream fos = new FileOutputStream(saveZip); ZipOutputStream zipOut = new ZipOutputStream(fos)) {
            zip(fileToZip, fileToZip.getName(), zipOut, everyFile);
        } catch (IOException e) {
            log.warn("ERROR>>", e);
        }

        log.info("压缩完成，耗时：{}ms，保存在{}", System.currentTimeMillis() - start, saveZip);
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
        if (toZip.isHidden()) return;

        if (everyFile != null && !everyFile.apply(toZip)) return; // 跳过不要的

        try {
            if (toZip.isDirectory()) {
                zipOut.putNextEntry(new ZipEntry(fileName.endsWith("/") ? fileName : fileName + "/"));
                zipOut.closeEntry();

                File[] children = toZip.listFiles();
                assert children != null;
                for (File childFile : children)
                    zip(childFile, fileName + "/" + childFile.getName(), zipOut, everyFile);

                return;
            }

            zipOut.putNextEntry(new ZipEntry(fileName));

            try (FileInputStream in = new FileInputStream(toZip)) {
                StreamHelper.write(in, zipOut, false);
            }
        } catch (IOException e) {
            log.warn("ERROR>>", e);
        }
    }

    /**
     * Zip压缩大文件从30秒到近乎1秒的优化过程 <a href="https://blog.csdn.net/hj7jay/article/details/102798664">...</a>
     * 这是一个调用本地方法与原生操作系统进行交互，从磁盘中读取数据。
     * 每读取一个字节的数据就调用一次本地方法与操作系统交互，是非常耗时的。例如我们现在有30000个字节的数据，如果使用 FileInputStream
     * 那么就需要调用30000次的本地方法来获取这些数据，而如果使用缓冲区的话（这里假设初始的缓冲区大小足够放下30000字节的数据）那么只需要调用一次就行。因为缓冲区在第一次调用  read() 方法的时候会直接从磁盘中将数据直接读取到内存中。
     * 随后再一个字节一个字节的慢慢返回。
     */
    public static void zipFileBuffer(String toZip, String saveZip) {
        File fileToZip = new File(toZip);

        try (ZipOutputStream zipOut = new ZipOutputStream(Files.newOutputStream(fileToZip.toPath())); BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(zipOut)) {

            for (int i = 1; i < 11; i++) {
                try (BufferedInputStream bufferedInputStream = new BufferedInputStream(Files.newInputStream(Paths.get(saveZip + i + ".jpg")))) {
                    zipOut.putNextEntry(new ZipEntry(saveZip + i + ".jpg"));
                    int temp;

                    while ((temp = bufferedInputStream.read()) != -1) bufferedOutputStream.write(temp);
                }
            }

        } catch (IOException e) {
            log.warn("ERROR>>", e);
        }
    }

    /**
     * Java极快压缩方式 <a href="https://blog.csdn.net/weixin_44044915/article/details/115734457">fileContent</a>
     */
    public static void zipFile(File[] fileContent, String saveZip) {
        try (ZipOutputStream zipOut = new ZipOutputStream(Files.newOutputStream(Paths.get(saveZip))); BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(zipOut)) {

            for (File fc : fileContent) {
                try (BufferedInputStream bufferedInputStream = new BufferedInputStream(Files.newInputStream(fc.toPath()))) {
                    ZipEntry entry = new ZipEntry(fc.getName());
                    // 核心，和复制粘贴效果一样，并没有压缩，但速度很快
                    entry.setMethod(ZipEntry.STORED);
                    entry.setSize(fc.length());
                    entry.setCrc(getFileCRCCode(fc));
                    zipOut.putNextEntry(entry);

                    int len;
                    byte[] data = new byte[8192];

                    while ((len = bufferedInputStream.read(data)) != -1) bufferedOutputStream.write(data, 0, len);

                    bufferedInputStream.close();
                    bufferedOutputStream.flush();
                }
            }
        } catch (IOException e) {
            log.warn("ERROR>>", e);
        }
    }


    /**
     * 获取 CRC32
     * CheckedInputStream一种输入流，它还维护正在读取的数据的校验和。然后可以使用校验和来验证输入数据的完整性。
     */
    public static long getFileCRCCode(File file) {
        CRC32 crc32 = new CRC32();

        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(Files.newInputStream(file.toPath())); CheckedInputStream checkedinputstream = new CheckedInputStream(bufferedInputStream, crc32)) {
            while (checkedinputstream.read() != -1) {
            }
        } catch (IOException e) {
            log.warn("ERROR>>", e);
        }

        return crc32.getValue();
    }

    private static boolean isZipFile(byte[] magicNumber) {
        return magicNumber[0] == 0x50 && magicNumber[1] == 0x4b && magicNumber[2] == 0x03 && magicNumber[3] == 0x04;
    }

//    private static final String ZIP_MAGIC_NUMBER = "504B0304";

    /**
     * 判断给定的文件路径是否为 ZIP 文件。
     *
     * @param filePath 文件路径
     * @return 如果是 ZIP 文件则返回 true，否则返回 false
     */
    public static boolean isZipFile(String filePath) {
        try (InputStream inputStream = Files.newInputStream(Paths.get(filePath))) {
            byte[] magicNumber = new byte[4];

            if (inputStream.read(magicNumber, 0, 4) == 4)  // 读取到了 4 个字节
                return magicNumber[0] == 0x50 && magicNumber[1] == 0x4b && magicNumber[2] == 0x03 && magicNumber[3] == 0x04;
            // ZIP_MAGIC_NUMBER.equalsIgnoreCase(StreamHelper.bytesToHexStr(magicNumber));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

}

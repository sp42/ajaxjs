/**
 * Copyright sp42 frank@ajaxjs.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.util.io;

import com.ajaxjs.util.DateUtil;
import com.ajaxjs.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 文件操作工具类
 * <a href="https://www.cnblogs.com/digdeep/p/4478734.html">...</a>
 *
 * @author sp42 frank@ajaxjs.com
 */
@Slf4j
public class FileHelper extends StreamHelper {
    /**
     * 当前系统的分隔符(linux、windows)
     */
    public static final String SEPARATOR = File.separator;

    /**
     * 复制文件
     *
     * @param target    源文件
     * @param dest      目的文件/目录，如果最后一个为目录，则不改名，如果最后一个为文件名，则改名
     * @param isReplace 是否替换已存在的文件，true = 覆盖
     */
    public static void copy(String target, String dest, boolean isReplace) throws IOException {
        Path source = Paths.get(target);
        Path _dest = Paths.get(dest);

        if (isReplace)
            Files.copy(source, _dest, StandardCopyOption.REPLACE_EXISTING);
        else
            Files.copy(source, _dest);
    }

    /**
     * 移动文件
     *
     * @param target 源文件
     * @param dest   目的文件/目录，如果最后一个为目录，则不改名，如果最后一个为文件名，则改名
     */
    public static void move(String target, String dest) throws IOException {
        Files.copy(Paths.get(target), Paths.get(dest));
    }

    /**
     * 删除文件或目录
     *
     * @param file 文件对象
     */
    public static void delete(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();

            assert files != null;
            for (File f : files)
                delete(f);
        }

        if (!file.delete())
            log.warn("文件 {} 删除失败！", file);
    }

    /**
     * 删除文件或目录
     *
     * @param filePath 文件的完全路径
     */
    public static void delete(String filePath) {
        delete(new File(filePath));
    }

    /**
     * 打开文件，返回其文本内容，可指定编码
     *
     * @param filePath 文件的完全路径
     * @param encode   文件编码
     * @return 文件内容
     */
    public static String openAsText(String filePath, Charset encode) {
        log.info("读取文件[{}]", filePath);

        Path path = Paths.get(filePath);

        try {
            if (Files.isDirectory(path))
                throw new IOException("参数 full path：" + filePath + " 不能是目录，请指定文件");
        } catch (IOException e) {
            log.warn("ERROR>>", e);
            return null;
        }

        StringBuilder sb = new StringBuilder();

        try (Stream<String> lines = Files.lines(path, encode)) { // 要关闭文件，否则文件被锁定
            lines.forEach(sb::append);

            return sb.toString();
        } catch (IOException e) {
            log.warn("ERROR>>", e);
        }

        return null;
    }

    /**
     * 打开文件，返回其文本内容。指定为 UTF-8 编码
     *
     * @param filePath 文件的完全路径
     * @return 文件内容
     */
    public static String openAsText(String filePath) {
        return openAsText(filePath, StandardCharsets.UTF_8);
    }

    /**
     * 获得指定文件的 byte 数组
     *
     * @param file 文件对象
     * @return 文件字节数组
     */
    public static byte[] openAsByte(File file) {
        try {
            return StreamUtils.copyToByteArray(Files.newInputStream(file.toPath()));
        } catch (IOException e) {
            log.warn("ERROR>>", e);

            return null;
        }
    }

    /**
     * 保存文件数据
     *
     * @param file        文件对象
     * @param data        文件数据
     * @param isOverwrite 是否覆盖文件，true = 允许覆盖
     */
    public static void save(File file, byte[] data, boolean isOverwrite) {
        log.info("正在保存文件" + file);

        if (data == null) {
            log.warn("要保存的文件内容为空");
            return;
        }

        try {
            if (!isOverwrite && file.exists())
                throw new IOException(file + "文件已经存在，禁止覆盖！");

            if (file.isDirectory())
                throw new IOException(file + " 不能是目录，请指定文件");

            if (!file.exists())
                if (file.createNewFile())
                    log.info("不会走到这一步");

            Files.write(file.toPath(), data);
        } catch (IOException e) {
            log.warn("ERROR>>", e);
        }
    }

    /**
     * 保存文件数据
     *
     * @param file 文件对象
     * @param data 文件数据
     * @param off  偏移
     * @param len  长度
     */
    public static void save(File file, byte[] data, int off, int len) {
        try (OutputStream out = Files.newOutputStream(file.toPath())) {
            bytes2output(out, data, false, off, len);
        } catch (IOException e) {
            log.warn("ERROR>>", e);
        }
    }

    /**
     * 保存文本内容
     *
     * @param file 文件对象
     * @param text 文本内容
     */
    public static void saveText(File file, String text) {
        String _text = text.length() > 200 ? text.substring(0, 200) + "..." : text;
        log.info("正在保存文件{}， 保存内容：\n{}", file.toString(), _text);

//		try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8);) {
//			writer.write(text);
//			writer.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

        save(file, text.getBytes(StandardCharsets.UTF_8), true);
    }

    /**
     * 保存文本内容
     *
     * @param filePath 文件路径
     * @param text     文本内容
     */
    public static void saveText(String filePath, String text) {
        saveText(new File(filePath), text);
    }

    /**
     * 创建目录
     *
     * @param folder 目录字符串
     */
    public static void mkDir(String folder) {
        File _folder = new File(folder);

        if (!_folder.exists())// 先检查目录是否存在，若不存在建立
            if (!_folder.mkdirs()) // 可以创建多级目录，如果某个父级目录不存在，会一并创建
                log.warn("创建目录 {} 失败", folder);

        if (!_folder.mkdir()) {// 只能创建单级目录，且父目录必须存在
        }
//            LOGGER.warning("创建目录 {0} 失败", folder);
    }

    /**
     * 根据文件名创建目录。 先剥离文件名，剩下的就是目录名。 如果没有输出目录则先创建。
     *
     * @param filePath 完整路径，最后一个元素为文件名
     */
    public static void mkDirByFileName(String filePath) {
        String[] arr = filePath.split("[/\\\\]");
        arr[arr.length - 1] = "";// 取消文件名，让最后一个元素为空字符串
        String folder = String.join(SEPARATOR, arr);

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
        log.info("正在新建文件 {}", folder + SEPARATOR + fileName);

        mkDir(folder);
        return new File(folder + SEPARATOR + fileName);
    }

    /**
     * 创建文件，注意这是一个空的文件。如果没有指定目录则创建；检测是否可以覆盖文件
     *
     * @param filePath    文件完整路径，最后一个元素是文件名
     * @param isOverwrite 是否覆盖文件
     * @return 文件对象
     * @throws IOException 文件已经存在
     */
    public static File createFile(String filePath, boolean isOverwrite) throws IOException {
        log.info("正在新建文件 {}", filePath);

        mkDirByFileName(filePath);

        File file = new File(filePath);
        if (!isOverwrite && file.exists())
            throw new IOException("文件已经存在，禁止覆盖！");

        return file;
    }

    /**
     * 遍历一个文件夹
     *
     * @param folder 文件夹
     */
    public static void loop(String folder) {
        Path dir = Paths.get(folder);

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path e : stream)
                System.out.println(e.getFileName());

        } catch (IOException e) {
            log.warn("ERROR>>", e);
        }
    }

    /**
     * 根据日期字符串得到目录名 格式: /2008/10/15/
     *
     * @return 如 /2008/10/15/ 格式的字符串
     */
    public static String getDirNameByDate() {
        String datetime = DateUtil.now(DateUtil.DATE_FORMAT_SHORTER),
                year = datetime.substring(0, 4), mouth = datetime.substring(5, 7),
                day = datetime.substring(8, 10);

        return SEPARATOR + year + SEPARATOR + mouth + SEPARATOR + day + SEPARATOR;
    }

    /**
     * 输入 /foo/bar/foo.jpg 返回 foo.jpg
     *
     * @param str 输入的字符串
     * @return 文件名
     */
    public static String getFileName(String str) {
        String[] arr = str.split("[/\\\\]");// 取消文件名，让最后一个元素为空字符串

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
     * 列出某个目录下的所有文件，不包括文件夹，不递归子目录
     */
    public static List<File> listFile(String directory) {
        Path path = Paths.get(directory);

        if (!Files.isDirectory(path))
            throw new UnsupportedOperationException("参数非文件夹 " + directory);

        try (Stream<Path> stream = Files.list(path)) {
            return stream.map(Path::toFile).filter(File::isFile).collect(Collectors.toList());
        } catch (IOException e) {
            log.warn("ERROR>>", e);
            return null;
        }
    }

    /**
     * 检测文件的 MIME 类型
     * 该方法利用内部的 FileNameMap 来判断 MIME 类型
     *
     * @param file 件
     * @return MIME 类型
     */
    public static String getContentTypeByExtName(File file) {
        return URLConnection.guessContentTypeFromName(file.getName());
    }

    /**
     * 检测文件的 MIME 类型
     * 根据文件流中前几个字符判断。能够判断真实的文件类型，但是，这种方法的主要缺点是速度非常慢。
     * <a href="https://juejin.cn/post/6950164327404044295">Java获取文件的Mime类型的几种方式总结</a>
     *
     * @param file 文件
     * @return MIME 类型
     */
    public static String getContentType(File file) {
        try {
            return file.toURI().toURL().openConnection().getContentType();
        } catch (IOException e) {
            log.warn("ERROR>>", e);
            return null;
        }
    }

    /**
     * 检测文件的 MIME 类型
     * 根据文件流中前几个字符判断。
     *
     * @param in 文件流
     * @return MIME 类型
     */
    public static String getContentType(InputStream in) {
        try {
            return URLConnection.guessContentTypeFromStream(new BufferedInputStream(in));
        } catch (IOException e) {
            log.warn("ERROR>>", e);
            return null;
        }
    }

    /**
     * 读取大文件
     * 通过文件流式传输，此解决方案将遍历文件中的所有行-允许处理每行-无需将其保留在内存中
     */
    public static void readLargeFileContent(String path, Consumer<String> fn) {
        try (FileInputStream inputStream = new FileInputStream(path);
             Scanner sc = new Scanner(inputStream, StrUtil.UTF8_SYMBOL)) {
            while (sc.hasNextLine())
                fn.accept(sc.nextLine());

            // note that Scanner suppresses exceptions
            if (sc.ioException() != null)
                throw sc.ioException();
        } catch (IOException e) {
            log.warn("ERROR>>", e);
        }
    }
}

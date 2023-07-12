package com.ajaxjs.util;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    public static String md5(String text) {
        try {
            // 创建 MessageDigest 对象，指定算法为 MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 将文本转换为字节数组
            byte[] textBytes = text.getBytes();

            // 计算 MD5 值
            byte[] md5Bytes = md.digest(textBytes);

            // 将字节数组转换为十六进制字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : md5Bytes)
                sb.append(String.format("%02x", b));

            // 返回十六进制字符串
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String base64Encode(String originalString) {
        // 将原始字符串转换为字节数组
        byte[] bytes = originalString.getBytes(StandardCharsets.UTF_8);

        // 进行 Base64 编码
        byte[] encodedBytes = Base64.getEncoder().encode(bytes);

        // 将字节数组转换为字符串
        return new String(encodedBytes, StandardCharsets.UTF_8);
    }

    public static String base64Decode(String encodedString) {
        // 将编码后的字符串转换为字节数组
        byte[] bytes = encodedString.getBytes(StandardCharsets.UTF_8);

        // 进行 Base64 解码
        byte[] decodedBytes = Base64.getDecoder().decode(bytes);

        // 将字节数组转换为字符串
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

    public static boolean hasText(String str) {
        if (str == null || str.isEmpty())
            return false;

        for (int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i)))
                return true;
        }

        return false;
    }

    /**
     * 读输入的字节流转换到字符流，将其转换为文本（多行）的字节流转换为字符串
     *
     * @param in 输入流，无须手动关闭
     * @return 字符串
     */
    public static String byteStream2string(InputStream in) {
        return byteStream2string_Charset(in, StandardCharsets.UTF_8);
    }

    /**
     * 读输入的字节流转换到字符流，将其转换为文本（多行）的字节流转换为字符串。可指定字符编码
     *
     * @param in     输入流，无须手动关闭
     * @param encode 字符编码
     * @return 字符串
     */
    public static String byteStream2string_Charset(InputStream in, Charset encode) {
        StringBuilder result = new StringBuilder();

        // InputStreamReader 从一个数据源读取字节，并自动将其转换成 Unicode 字符
        // 相对地，OutputStreamWriter 将字符的 Unicode 编码写到字节输出流
        try (InputStreamReader inReader = new InputStreamReader(in, encode);
                /*
                 * Decorator，装饰模式，又称为 Wrapper，使它具有了缓冲功能 BufferedInputStream、BufferedOutputStream
                 * 只是在这之前动态的为它们加上一些功能（像是缓冲区功能）
                 */
             BufferedReader reader = new BufferedReader(inReader)) {

            String line;
            while ((line = reader.readLine()) != null) { // 一次读入一行，直到读入 null 为文件结束
                // 指定编码集的另外一种方法 line = new String(line.getBytes(), encodingSet);
                result.append(line);
                result.append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return result.toString();
    }

    /**
     * 1K 的数据块
     */
    public static final int BUFFER_SIZE = 1024;

    /**
     * 两端速度不匹配，需要协调 理想环境下，速度一样快，那就没必要搞流，直接一坨给弄过去就可以了 流的意思很形象，就是一点一滴的，不是一坨坨大批量的
     * 带缓冲的一入一出 出是字节流，所以要缓冲（字符流自带缓冲，所以不需要额外缓冲） 请注意，改方法不会关闭流 close，你需要手动关闭
     *
     * @param in       输入流，无须手动关闭
     * @param out      输出流
     * @param isBuffer 是否加入缓冲功能
     */
    public static void write(InputStream in, OutputStream out, boolean isBuffer) {
        int readSize; // 读取到的数据长度
        byte[] buffer = new byte[BUFFER_SIZE]; // 通过 byte 作为数据中转，用于存放循环读取的临时数据

        try {
            if (isBuffer) {
                try (OutputStream _out = new BufferedOutputStream(out)) {// 加入缓冲功能
                    while ((readSize = in.read(buffer)) != -1) {
                        _out.write(buffer, 0, readSize);
                    }
                }
            } else {
                // 每次读 1KB 数据，将输入流数据写入到输出流中
                // readSize = in.read(buffer, 0, bufferSize);
                while ((readSize = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                    out.write(buffer, 0, readSize);
                    // readSize = in.read(buffer, 0, bufferSize);
                }

                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回 Matcher
     *
     * @param regexp 正则
     * @param str    测试的字符串
     * @return Matcher
     */
    private static Matcher getMatcher(String regexp, String str) {
        return Pattern.compile(regexp).matcher(str);
    }

    /**
     * 使用正则的快捷方式。可指定分组
     *
     * @param regexp     正则
     * @param str        测试的字符串
     * @param groupIndex 分组 id，若为 -1 则取最后一个分组
     * @return 匹配结果
     */
    public static String regMatch(String regexp, String str, int groupIndex) {
        Matcher m = getMatcher(regexp, str);

        if (groupIndex == -1)
            groupIndex = m.groupCount();

        return m.find() ? m.group(groupIndex) : null;
    }

    /**
     * 使用正则的快捷方式
     *
     * @param regexp 正则
     * @param str    测试的字符串
     * @return 匹配结果，只有匹配第一个
     */
    public static String regMatch(String regexp, String str) {
        return regMatch(regexp, str, 0);
    }

    /**
     * 返回所有匹配项
     *
     * @param regexp 正则
     * @param str    测试的字符串
     * @return 匹配结果
     */
    public static String[] regMatchAll(String regexp, String str) {
        Matcher m = getMatcher(regexp, str);
        List<String> list = new ArrayList<>();

        while (m.find())
            list.add(m.group());

        return list.toArray(new String[0]);
    }

    /**
     * 合并两个字节数组
     *
     * @param a 数组a
     * @param b 数组b
     * @return 新合并的数组
     */
    public static byte[] concat(byte[] a, byte[] b) {
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);

        return c;
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
     * URL 编码
     *
     * @param str 输入的字符串
     * @return URL 编码后的字符串
     */
    public static String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 获得指定文件的 byte 数组
     *
     * @param file 文件对象
     * @return 文件字节数组
     */
    public static byte[] openAsByte(File file) {
        try {
            return copyToByteArray(Files.newInputStream(file.toPath()));
        } catch (IOException e) {

            return null;
        }
    }

    public static byte[] copyToByteArray(InputStream in) throws IOException {
        if (in == null)
            return new byte[0];

        ByteArrayOutputStream out = new ByteArrayOutputStream(BUFFER_SIZE);
        copy(in, out);
        return out.toByteArray();
    }

    public static int copy(InputStream in, OutputStream out) throws IOException {
        int byteCount = 0;
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
            byteCount += bytesRead;
        }

        out.flush();

        return byteCount;
    }

    public static final String SEPARATOR = File.separator;

    /**
     * 新建一个空文件
     *
     * @param folder   如果路径不存在则自动创建
     * @param fileName 保存的文件名
     * @return 新建文件的 File 对象
     */
    public static File createFile(String folder, String fileName) {
        mkDir(folder);
        return new File(folder + SEPARATOR + fileName);
    }

    /**
     * 创建目录
     *
     * @param folder 目录字符串
     */
    public static void mkDir(String folder) {
        File _folder = new File(folder);
        if (!_folder.exists())// 先检查目录是否存在，若不存在建立
            _folder.mkdirs();

        _folder.mkdir();
    }

    /**
     * Map 转换为 String
     *
     * @param map Map 结构，Key 必须为 String 类型
     * @param div 分隔符
     * @param fn  对 Value 的处理函数，返回类型 T
     * @return Map 序列化字符串
     */
    public static <T> String join(Map<String, T> map, String div, Function<T, String> fn) {
        String[] pairs = new String[map.size()];

        int i = 0;

        for (String key : map.keySet())
            pairs[i++] = key + "=" + fn.apply(map.get(key));

        return String.join(div, pairs);
    }

    public static <T> String join(Map<String, T> map, Function<T, String> fn) {
        return join(map, "&", fn);
    }

    public static <T> String join(Map<String, T> map, String div) {
        return join(map, div, v -> v == null ? null : v.toString());
    }

    public static <T> String join(Map<String, T> map) {
        return join(map, "&");
    }

}

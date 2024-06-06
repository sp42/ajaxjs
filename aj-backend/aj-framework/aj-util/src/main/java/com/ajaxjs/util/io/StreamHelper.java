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

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * 流操作助手类
 */
@Slf4j
public class StreamHelper {
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

        read(new InputStreamReader(in, encode), line -> {
            result.append(line);
            result.append('\n');
        });

        return result.toString();
    }

    /**
     * 从输入流中读取数据，并对每行数据应用提供的消费函数。
     *
     * @param in 输入流，从中读取数据。
     * @param fn 消费函数，用于处理读取到的每行数据。
     */
    public static void read(InputStream in, Consumer<String> fn) {
        read(new InputStreamReader(in, StandardCharsets.UTF_8), fn);
    }

    /**
     * 从 InputStreamReader 中读取数据，并逐行消费。
     *
     * @param inReader 输入流读取器，用于读取数据。
     * @param fn       消费函数，接收一行数据作为参数，对每行数据进行处理。
     */
    public static void read(InputStreamReader inReader, Consumer<String> fn) {
        try (
                /*
                 * 装饰器模式，又称为包装器，可以在不修改被包装类的情况下动态添加功能（例如缓冲区功能）
                 * 这里使用BufferedReader为输入流添加缓冲功能
                 */
                BufferedReader reader = new BufferedReader(inReader)) {
            String line;

            while ((line = reader.readLine()) != null) { // 一次读入一行，直到读入 null 表示文件结束
                // 指定编码集的另外一种写法 line = new String(line.getBytes(), encodingSet);
                fn.accept(line);
            }
        } catch (IOException e) {
            log.warn("ERROR>>", e);
        }
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
            log.warn("ERROR>>", e);
        }
    }

    /**
     * 使用内存操作流，读取二进制，也就是将流转换为内存的数据。 InputStream 转换到 byte[]. 从输入流中获取数据， 转换到 byte[]
     * 也就是 in 转到内存。虽然大家可能都在内存里面了但还不能直接使用，要转换
     *
     * @param in 输入流
     * @return 返回本实例供链式调用
     */
    public static byte[] inputStream2Byte(InputStream in) {
        // 使用内存操作流，读取二进制
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            write(in, out, true);

            return out.toByteArray();
        } catch (IOException e) {
            log.warn("ERROR>>", e);
            return null;
        }
    }

    /**
     * 送入的 byte[] 转换为输出流。可指定 byte[] 某一部分数据。 注意这函数不会关闭输出流，请记得在适当的时候将其关闭。
     *
     * @param out    输出流
     * @param data   输入的数据
     * @param off    偏移
     * @param length 长度
     */
    public static void bytes2output(OutputStream out, byte[] data, int off, int length) {
        bytes2output(out, data, true, off, length);
    }

    /**
     * 送入的 byte[] 转换为输出流。可指定 byte[] 某一部分数据。 注意这函数不会关闭输出流，请记得在适当的时候将其关闭。
     *
     * @param out        输出流
     * @param data       输入的数据
     * @param isBuffered 是否需要缓冲
     * @param off        偏移
     * @param length     长度
     */
    public static void bytes2output(OutputStream out, byte[] data, boolean isBuffered, int off, int length) {
        try {
            if (isBuffered)
                out = new BufferedOutputStream(out, BUFFER_SIZE);

            if (off == 0 && length == 0)
                out.write(data);
            else
                out.write(data, off, length);

            out.flush();
        } catch (IOException e) {
            log.warn("ERROR>>", e);
        }
    }

    /**
     * 在字节数组中截取指定长度数组
     *
     * @param data   输入的数据
     * @param off    偏移
     * @param length 长度
     * @return 指定 范围的字节数组
     */
    public static byte[] subBytes(byte[] data, int off, int length) {
        byte[] bs = new byte[length];
        System.arraycopy(data, off, bs, 0, length);

        return bs;
    }

    /**
     * 在字节数组里查找某个字节数组，找到返回&lt;=0，未找到返回-1
     *
     * @param data   被搜索的内容
     * @param search 要搜索内容
     * @param start  搜索起始位置
     * @return 目标位置，找不到返回-1
     */
    public static int byteIndexOf(byte[] data, byte[] search, int start) {
        int len = search.length;

        for (int i = start; i < data.length; i++) {
            int temp = i, j = 0;

            while (data[temp] == search[j]) {
                temp++;
                j++;

                if (j == len)
                    return i;
            }
        }

        return -1;
    }

    /**
     * 在字节数组里查找某个字节数组，找到返回 &lt;=0，未找到返回 -1
     *
     * @param data   被搜索的内容
     * @param search 要搜索内容
     * @return 目标位置，找不到返回 -1
     */
    public static int byteIndexOf(byte[] data, byte[] search) {
        return byteIndexOf(data, search, 0);
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

    private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);

    /**
     * byte[] 转化为 16 进制字符串输出
     *
     * @param bytes 字节数组
     * @return 16 进制字符串
     */
    public static String bytesToHexStr(byte[] bytes) {
        byte[] hexChars = new byte[bytes.length * 2];

        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;

            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }

        return new String(hexChars, StandardCharsets.UTF_8);
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr 16进制字符串
     * @return 二进制数组
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) return null;
        byte[] result = new byte[hexStr.length() / 2];

        for (int i = 0; i < hexStr.length() / 2; i++) {
            // 获取高位和低位的16进制数
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            // 计算二进制数
            result[i] = (byte) (high * 16 + low);
        }

        return result;
    }

    /**
     * char 数组转 byte 数组
     * 将char数组转换为byte数组需要考虑编码方式的问题
     * <a href="https://houbb.github.io/2023/06/05/java-perf-02-chars-to-bytes">...</a>
     */
    public static byte[] charToByte(char[] chars) {
        ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(CharBuffer.wrap(chars));
        byte[] bytes = Arrays.copyOfRange(byteBuffer.array(), byteBuffer.position(), byteBuffer.limit());
        Arrays.fill(byteBuffer.array(), (byte) 0);

        return bytes;
    }
}

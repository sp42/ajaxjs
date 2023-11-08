package com.ajaxjs.net.ftp;

import com.ajaxjs.net.ftp.sun.TelnetInputStream;
import com.ajaxjs.net.ftp.sun.TelnetOutputStream;
import com.ajaxjs.net.ftp.sun.ftp.FtpClient;

import java.io.*;
import java.nio.file.Files;

/**
 * FTP 文件上传
 */
public class UploadFtp extends FtpClient {
    public UploadFtp(String server, int port) throws IOException {
        super(server, port);
    }

    /**
     * 用书上传本地文件到 FTP 服务器上
     *
     * @param source 上传文件的本地路径
     * @param target 上传到 FTP 的文件路径
     */
    public void upload(String source, String target) {
        try {
            binary();

            try (TelnetOutputStream ftp = put(target);
                 InputStream file = Files.newInputStream(new File(source).toPath())) {
                BufferedInputStream in = new BufferedInputStream(file);

                new ProgressListener().copy(in, new BufferedOutputStream(ftp), in.available());

                System.out.print("put file suc from " + source + "   to  " + target + "\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从 FTP 上下载所需要的文件
     *
     * @param source 在 FTP 上路径及文件名
     * @param target 要保存的本地的路径
     */
    public void getFile(String source, String target) {
        try {
            binary();

            try (TelnetInputStream ftp = get(source);
                 OutputStream file = Files.newOutputStream(new File(target).toPath())) {

                ProgressListener listener = new ProgressListener();
                listener.setFileName(target);
                listener.copy(new BufferedInputStream(ftp), new BufferedOutputStream(file), getFileSize(source, ftp));

                System.out.print("get file suc from " + source + "   to  " + target + "\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 为了计算下载速度和百分比，读取 FTP 该文件的大小
     */
    private int getFileSize(String source, TelnetInputStream ftp) throws IOException {
        // 这里的组合使用是必须得 sendServer 后到 readServerResponse
        sendServer("SIZE " + source + "\r\n");

        if (readServerResponse() == 213) {
            String msg = getResponseString();

            try {
                return Integer.parseInt(msg.substring(3).trim());
            } catch (Exception e) {
            }
        }

        return 0;
    }
}
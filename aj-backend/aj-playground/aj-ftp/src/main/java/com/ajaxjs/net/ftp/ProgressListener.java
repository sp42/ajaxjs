package com.ajaxjs.net.ftp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 上传进度
 */
public class ProgressListener {
    private String fileName;
    private volatile long bytesRead;
    private volatile long contentLength;

    public void update(long aBytesRead, long aContentLength) {
        bytesRead = aBytesRead / 1024L;
        contentLength = aContentLength / 1024L;
        // long megaBytes = aBytesRead / 1048576L;

        System.out.println("上传或者下载文件：" + fileName + "，文件的大小：" + aBytesRead + "/" + aContentLength);
    }

    public long copy(InputStream in, OutputStream out, long size) {
        byte[] buffer = new byte[8192];
        long total = 0L;
        int res;

        try {
            while (true) {
                res = in.read(buffer);
                if (res == -1) {
                    break;
                }
                if (res > 0) {
                    total += res;
                    if (out != null) {
                        out.write(buffer, 0, res);
                        System.out.println("文件的大小" + size + "读取的大小" + total);
                        update(total, size);
                    }
                }
            }

            return total;
        } catch (IOException e) {
            return 0L;
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public long getBytesRead() {
        return this.bytesRead;
    }

    public long getContentLength() {
        return this.contentLength;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
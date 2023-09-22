package com.ajaxjs.tools.office.export_word;

import org.springframework.util.StringUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 自定义响应对象的输出流
 *
 * @author sp42 frank@ajaxjs.com
 */
public class ByteArrayServletOutputStream extends ServletOutputStream {
    /**
     * 创建一个 ByteArrayServletOutputStream 对象
     */
    public ByteArrayServletOutputStream() {
    }

    /**
     * 输出流
     */
    private OutputStream out = new ByteArrayOutputStream();

    /**
     * 创建一个 ByteArrayServletOutputStream 对象
     *
     * @param out 输出流
     */
    public ByteArrayServletOutputStream(ByteArrayOutputStream out) {
        this.out = out;
    }

    @Override
    public void write(byte[] data, int offset, int length) {
        try {
            out.write(data, offset, length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }

    /**
     * @param _out
     */
    public void writeTo(OutputStream _out) {
        ByteArrayOutputStream bos = (ByteArrayOutputStream) out;

        try {
            bos.writeTo(_out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public OutputStream getOut() {
        return out;
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public String toString() {
        return out.toString();
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
    }

    /**
     * 解析 JSP 模板到服务器磁盘上
     *
     * @param req
     * @param resp
     * @param tplJsp       模板文件名
     * @param isSaveToDisk true= 保存到磁盘文件，saveTo 必须为完整的路径；false = 表示输出流到浏览器端，即让浏览器去下载文件给用户
     * @param saveTo       磁盘完整的文件路径或者只是文件名
     */
    public static void renderer(HttpServletRequest req, HttpServletResponse resp, String tplJsp, boolean isSaveToDisk, String saveTo) {
        RequestDispatcher rd = req.getServletContext().getRequestDispatcher(tplJsp);

        if (isSaveToDisk && !StringUtils.hasText(saveTo))
            throw new IllegalArgumentException("缺少 saveTo 参数");

        if (!isSaveToDisk) {
            if (!StringUtils.hasText(saveTo))
                saveTo = "word.doc";// 下载文件的默认名称


            resp.setHeader("content-Type", "application/msword");
            resp.setHeader("Content-Disposition", "attachment;filename=" + Utils.encodeFileName(saveTo));
        }

        try (ByteArrayServletOutputStream stream = new ByteArrayServletOutputStream();
             PrintWriter pw = new PrintWriter(new OutputStreamWriter(stream.getOut(), StandardCharsets.UTF_8));
             OutputStream out = isSaveToDisk ? Files.newOutputStream(Paths.get(saveTo)) : resp.getOutputStream()
        ) {
            rd.include(req, new HttpServletResponseWrapper(resp) {
                @Override
                public ServletOutputStream getOutputStream() {
                    return stream;
                }

                @Override
                public PrintWriter getWriter() {
                    return pw;
                }
            });

            pw.flush();
            stream.writeTo(out);
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }
    }
}


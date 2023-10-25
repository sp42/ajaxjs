package com.ajaxjs.tools.office_export;

import org.springframework.util.StringUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    /**
     * Java 文件下载中文名不显示&乱码
     * <a href="https://cloud.tencent.com/developer/article/1483095">...</a>
     *
     * @param fileName 文件名，可以包含中文的
     * @return 文件名
     */
    public static String encodeFileName(String fileName) {
        return new String(fileName.getBytes(), StandardCharsets.ISO_8859_1);
    }

    public static String now() {
        return new SimpleDateFormat("yyyy年M月d日").format(new Date());
    }

    public static String getFileName(String filePath) {
        int startIndex = filePath.lastIndexOf("/") + 1;
        int endIndex = filePath.lastIndexOf(".");

        return filePath.substring(startIndex, endIndex);
    }

    static final int BUFFER_SIZE = 4096;// 设置缓冲区大小

    /**
     * 读取服务端磁盘文件，给 Web 下载，形成文件二进制流
     *
     * @param resp        响应对象
     * @param contentType 文件类型
     * @param filePath    服务端磁盘文件
     * @param fileName    下载文件名
     */
    public static void download(HttpServletResponse resp, String contentType, String filePath, String fileName) {
        resp.setContentType(contentType);
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + encodeFileName(fileName) + "\"");

        // 读取文件
        try (InputStream inputStream = Files.newInputStream(new File(filePath).toPath());
             ServletOutputStream outputStream = resp.getOutputStream()) {

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;// 将文件内容写入响应输出流

            while ((bytesRead = inputStream.read(buffer)) != -1)
                outputStream.write(buffer, 0, bytesRead);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析 JSP 模板到服务器磁盘上
     *
     * @param contentType  文件类型
     * @param tplJsp       模板文件名
     * @param isSaveToDisk true= 保存到磁盘文件，saveTo 必须为完整的路径；false = 表示输出流到浏览器端，即让浏览器去下载文件给用户
     * @param saveTo       磁盘完整的文件路径或者只是文件名
     */
    public static void renderer(HttpServletRequest req, HttpServletResponse resp, String contentType, String tplJsp, boolean isSaveToDisk, String saveTo) {
        RequestDispatcher rd = req.getServletContext().getRequestDispatcher(tplJsp);

        if (isSaveToDisk && !StringUtils.hasText(saveTo))
            throw new IllegalArgumentException("缺少 saveTo 参数");

        if (!isSaveToDisk) {
            if (!StringUtils.hasText(saveTo))
                saveTo = "word.doc";// 下载文件的默认名称

            resp.setHeader("content-Type", contentType);
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

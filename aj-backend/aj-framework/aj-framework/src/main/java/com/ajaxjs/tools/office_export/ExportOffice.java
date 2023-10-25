package com.ajaxjs.tools.office_export;

import com.ajaxjs.util.io.Resources;
import com.ajaxjs.util.io.StreamHelper;
import com.ajaxjs.util.logger.LogHelper;

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
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Office 导出
 */
public class ExportOffice {
    private static final LogHelper LOGGER = LogHelper.getLog(ExportOffice.class);

    /**
     * 导出 Excel
     *
     * ExportOffice.rendererXsl(list, req, resp, "/short-trade.jsp", "trade.xls");
     *
     * @param data       数据
     * @param req        请求对象
     * @param resp       响应对象
     * @param tplJsp     模板 XML 文件，必须 / 开头，以及 .jsp 结尾
     * @param outputPath 导出的 docx 位置
     */
    public static void rendererXsl(List<Map<String, Object>> data, HttpServletRequest req, HttpServletResponse resp, String tplJsp, String outputPath) {
        req.setAttribute("list", data); // 内容数据

        if (!tplJsp.startsWith("/"))
            throw new IllegalArgumentException("参数 tplJsp 必须以 / 开头");

        Utils.renderer(req, resp, "application/vnd.ms-excel", tplJsp, false, outputPath);
    }

    /**
     * 导出 Word
     *
     * @param data       数据
     * @param req        请求对象
     * @param resp       响应对象
     * @param tplJsp     模板 XML 文件，必须 / 开头，以及 .jsp 结尾
     * @param filePath   原始 docx/xlsx 文档，其实是个 zip 包，我们取其结构，会替换里面的 document.xml。这个文件应该在资源目录下
     * @param outputPath 导出的 docx/xlsx 位置
     */
    public static void rendererDocx(Map<String, Object> data, HttpServletRequest req, HttpServletResponse resp, String tplJsp, String filePath, String outputPath) {
        renderOffice(data, req, resp, tplJsp, filePath, outputPath, false);
    }

    /**
     * 导出 Excel
     *
     * @param list       数据
     * @param req        请求对象
     * @param resp       响应对象
     * @param tplJsp     模板 XML 文件，必须 / 开头，以及 .jsp 结尾
     * @param filePath   原始 xlsx 文档，其实是个 zip 包，我们取其结构，会替换里面的 xl/worksheets/sheet1.xml。这个文件应该在资源目录下
     * @param outputPath 导出的 xlsx 位置
     */
    public static void rendererXslx(List<Map<String, Object>> list, HttpServletRequest req, HttpServletResponse resp, String tplJsp, String filePath, String outputPath) {
        renderOffice(list, req, resp, tplJsp, filePath, outputPath, true);
    }

    /**
     * 导出 Word
     *
     * @param data       数据
     * @param req        请求对象
     * @param resp       响应对象
     * @param tplJsp     模板 XML 文件，必须 / 开头，以及 .jsp 结尾
     * @param filePath   原始 docx/xlsx 文档，其实是个 zip 包，我们取其结构，会替换里面的 document.xml。这个文件应该在资源目录下
     * @param outputPath 导出的 docx/xlsx 位置
     * @param isXsl      true=Excel 文件
     */
    public static void renderOffice(Object data, HttpServletRequest req, HttpServletResponse resp, String tplJsp, String filePath, String outputPath, boolean isXsl) {
        if (isXsl) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) data;
            req.setAttribute("list", list); // 内容数据
        } else {
            Map<String, Object> map = (Map<String, Object>) data;
            for (String key : map.keySet())
                req.setAttribute(key, map.get(key)); // 内容数据
        }

        if (!tplJsp.startsWith("/"))
            throw new IllegalArgumentException("参数 tplJsp 必须以 / 开头");

        RequestDispatcher rd = req.getServletContext().getRequestDispatcher(tplJsp);

        try (ByteArrayServletOutputStream stream = new ByteArrayServletOutputStream();
             PrintWriter pw = new PrintWriter(new OutputStreamWriter(stream.getOut(), StandardCharsets.UTF_8));
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

            File officeFile = input2file(filePath);
            zip(officeFile, outputPath, stream, false);
            officeFile.delete();
        } catch (IOException | ServletException e) {
            LOGGER.warning(e);
        }
    }

    /**
     * 替换 Zip 包中的 XML
     *
     * @param oldZipFile 原始 docx/xlsx 文档，其实是个 zip 包，我们取其结构，会替换里面的 document.xml。这个文件应该在资源目录下
     * @param outputPath 导出的 docx/xlsx 位置
     * @param stream     文件流
     * @param isXsl      true=Excel 文件
     */
    static void zip(File oldZipFile, String outputPath, ByteArrayServletOutputStream stream, boolean isXsl) {
        int len;
        byte[] buffer = new byte[1024];

        try (OutputStream os = Files.newOutputStream(Paths.get(outputPath));
             ZipOutputStream zipOut = new ZipOutputStream(os); /* 输出的 */
             ZipFile zipFile = new ZipFile(oldZipFile); // 原压缩包
        ) {
            Enumeration<? extends ZipEntry> zipEntry = zipFile.entries();
//            ByteArrayInputStream imgData = img((List<Map<String, Object>>) dataMap.get("picList"), zipOut, dataMap, resXml);

            String targetXml = isXsl ? "xl/worksheets/sheet1.xml" : "word/document.xml";
//
            // 开始覆盖文档------------------
            while (zipEntry.hasMoreElements()) {
                ZipEntry entry = zipEntry.nextElement();

                try (InputStream is = zipFile.getInputStream(entry)) {
                    zipOut.putNextEntry(new ZipEntry(entry.getName()));

                    if (entry.getName().indexOf("document.xml.rels") > 0) { //如果是document.xml.rels由我们输入
//                        if (documentXmlRelsInput != null) {
//                            while ((len = documentXmlRelsInput.read(buffer)) != -1) zipOut.write(buffer, 0, len);
//
//                            documentXmlRelsInput.close();
//                        }
                        while ((len = is.read(buffer)) != -1) zipOut.write(buffer, 0, len);
                    } else if (targetXml.equals(entry.getName())) {//如果是word/document.xml由我们输入
                        stream.writeTo(zipOut);
                    } else {
                        while ((len = is.read(buffer)) != -1) zipOut.write(buffer, 0, len);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.warning(e);
        }
    }

    /**
     * 从资源目录中获取文件对象，兼容 JAR 包的方式
     *
     * @param resourcePath 资源文件
     * @return 文件对象
     */
    public static File input2file(String resourcePath) {
        try {
            File outputFile = File.createTempFile("outputFile", ".docx");// 创建临时文件

            // 创建输出流
            try (InputStream input = Resources.getResource(resourcePath);
                 OutputStream output = Files.newOutputStream(outputFile.toPath())) {
                StreamHelper.write(input, output, false);
            }

            return outputFile;
        } catch (IOException e) {
            LOGGER.warning(e);
        }

        return null;
    }
}

package com.ajaxjs.tools.office_export;

import com.ajaxjs.util.io.Resources;
import com.ajaxjs.util.io.StreamHelper;
import com.ajaxjs.util.logger.LogHelper;
import lombok.Data;

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
@Data
public class Export {
    private static final LogHelper LOGGER = LogHelper.getLog(Export.class);

    /**
     * 模板 XML 文件
     */
    private String tplXml;

    /**
     * 模板 JSP 文件，必须 / 开头，以及 .jsp 结尾
     */
    private String tplJsp;

    /**
     * 原始 docx/xlsx 文档，其实是个 zip 包，我们取其结构，会替换里面的 xml
     */
    private String officeZip;

    /**
     * 是否在资源文件目录
     */
    private Boolean isOfficeZipInRes;

    private File officeZipRes;

    /**
     * 导出的 docx/xlsx 位置
     */
    private String outputPath;

    /**
     * true=Excel 文件
     */
    private Boolean isXsl;

    /**
     * 浏览器下载文件。如果设置该属性，表示浏览器下载文件，否则保存到文件
     */
    private HttpServletResponse respOutput;

    /**
     * 浏览器下载文件。如果设置该属性，表示浏览器下载文件，否则保存到文件
     *
     * @param respOutput 响应对象
     * @param fileName   下载的文件名
     */
    public void setRespOutput(HttpServletResponse respOutput, String fileName) {
        this.respOutput = respOutput;

        respOutput.setContentType("application/vnd.ms-excel");
        respOutput.setHeader("Content-Disposition", "attachment; filename=\"" + Utils.encodeFileName(fileName) + "\"");
    }

    public void renderOffice(Object data, HttpServletRequest req, HttpServletResponse resp) {
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

            officeZipRes = input2file(officeZip);
            zip(stream);
            officeZipRes.delete();
        } catch (IOException | ServletException e) {
            LOGGER.warning(e);
        }
    }

    /**
     * 替换 Zip 包中的 XML
     *
     * @param stream 文件流
     */
    void zip(ByteArrayServletOutputStream stream) {
        zip(stream.getOut());
    }

    /**
     * 替换 Zip 包中的 XML
     *
     * @param stream 文件流
     */
    void zip(ByteArrayOutputStream stream) {
        int len;
        byte[] buffer = new byte[1024];

        try (ZipFile zipFile = isOfficeZipInRes ? new ZipFile(officeZipRes) : new ZipFile(officeZip); // 原压缩包
             ZipOutputStream zipOut = new ZipOutputStream(respOutput == null ? Files.newOutputStream(Paths.get(outputPath)) : respOutput.getOutputStream()) /* 输出的 */
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

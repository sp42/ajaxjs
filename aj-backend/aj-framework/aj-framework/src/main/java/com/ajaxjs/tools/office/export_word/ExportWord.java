package com.ajaxjs.tools.office.export_word;

import com.ajaxjs.util.io.Resources;
import com.ajaxjs.util.io.StreamHelper;

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
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ExportWord {
    public static void renderer(Map<String, Object> data, HttpServletRequest req, HttpServletResponse resp, String tplJsp, String zipDocFile, String outputPath) {
        for (String key : data.keySet())
            req.setAttribute(key, data.get(key)); // 内容数据

        RequestDispatcher rd = req.getServletContext().getRequestDispatcher("/document.jsp");

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

            ExportWord.zip(ExportWord.input2file(zipDocFile), outputPath, stream);
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }
    }

    public static void zip(File oldZipFile, String outputDocx, ByteArrayServletOutputStream stream) {
        int len;
        byte[] buffer = new byte[1024];

        try (OutputStream os = Files.newOutputStream(Paths.get(outputDocx)); ZipOutputStream zipOut = new ZipOutputStream(os); /* 输出的 */
             ZipFile zipFile = new ZipFile(oldZipFile); // 原压缩包
        ) {
            Enumeration<? extends ZipEntry> zipEntry = zipFile.entries();
//            ByteArrayInputStream imgData = img((List<Map<String, Object>>) dataMap.get("picList"), zipOut, dataMap, resXml);
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
                    } else if ("word/document.xml".equals(entry.getName())) {//如果是word/document.xml由我们输入
                        System.out.println("-----------------------------");
                        stream.writeTo(zipOut);
                    } else {
                        while ((len = is.read(buffer)) != -1) zipOut.write(buffer, 0, len);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        oldZipFile.delete();
    }

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
            e.printStackTrace();
        }

        return null;
    }


    public static void main(String[] args) {
//        File docxFile = new File("C:\\code\\car-short-rental\\src\\main\\resources\\short-contract-template.docx");
//        zip(docxFile, "c:\\temp\\output.docx", stream);
    }
}

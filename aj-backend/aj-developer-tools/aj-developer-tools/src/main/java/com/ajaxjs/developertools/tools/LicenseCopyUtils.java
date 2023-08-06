package com.ajaxjs.developertools.tools;

import java.io.*;
import java.nio.file.Files;

/**
 * License复制工具
 *
 * @author jianggujin <a href="https://blog.csdn.net/jianggujin/article/details/81483953">...</a>
 */
public class LicenseCopyUtils implements FileFilter {
    /**
     * 读取 License 文件
     */
    public String readLicenseHeader(InputStream in, String charset) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset))) {
            StringBuilder builder = new StringBuilder("/**\r\n");
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(" * ");
                builder.append(line);
                builder.append("\r\n");
            }

            builder.append(" */");

            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 处理 license 头部信息
     */
    public void processLicenseHeader(File root, InputStream in, String charset) {
        System.out.println("开始读取并格式化license...");
        String headerBody = readLicenseHeader(in, charset);
        System.out.println(headerBody);
        System.out.println("读取并格式化license完成...");

        if (root.isDirectory() || root.getName().endsWith(".java")) {
            System.out.println("开始处理：" + root.getAbsolutePath());
            processLicenseHeader(root, charset, headerBody);
        }
    }

    private void processLicenseHeader(File root, String charset, String headerBody) {
        if (root.isFile()) {
            System.out.println("开始读取并处理：" + root.getAbsolutePath());

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(root.toPath()), charset)); ByteArrayOutputStream stream = new ByteArrayOutputStream(); BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream, charset))) {
                writer.write(headerBody);
                writer.write("\r\n");
                String line;
                boolean body = false;

                while ((line = reader.readLine()) != null) {
                    if (body || line.startsWith("package ") || line.startsWith("import ")) {
                        body = true;
                        writer.write(line);
                        writer.write("\r\n");
                    }
                }

                try (FileOutputStream out = new FileOutputStream(root)) {
                    stream.writeTo(out);
                    out.flush();
                }

                System.out.println("读取并处理[" + root.getAbsolutePath() + "]完成");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            File[] list = root.listFiles(this);

            if (list != null) for (File file : list)
                processLicenseHeader(file, charset, headerBody);
        }
    }

    @Override
    public boolean accept(File file) {
        return file.isDirectory() || file.getName().endsWith(".java");
    }
}
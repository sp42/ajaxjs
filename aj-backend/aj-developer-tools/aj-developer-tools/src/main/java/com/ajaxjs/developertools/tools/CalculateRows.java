package com.ajaxjs.developertools.tools;

import com.ajaxjs.framework.IBaseModel;
import lombok.Data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * 代码行数统计
 *
 * @author <a href="https://blog.csdn.net/u014800380/article/details/73770823">...</a>
 */
@Data
public class CalculateRows implements IBaseModel {
    private long classCount = 0; // Java类的数量
    private long normalLines = 0; // 空行
    private long commentLines = 0; // 注释行
    private long writeLines = 0; // 代码行
    private long allLines = 0; // 代码行

    /**
     * 查找出一个目录下所有的 .java 文件
     *
     * @param f    指定的文件目录
     * @param type 类型
     */
    public void treeFile(File f, String type) {
        File[] children = f.listFiles();

        if (children == null)
            return;

        for (File file : children) {
            if (!file.isDirectory()) {
                if (file.getName().endsWith(type))
                    countFile(file);
            } else
                treeFile(file, type);
        }
    }

    private void countFile(File file) {
        classCount++;
        boolean comment = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                allLines++;
                line = line.trim();

                if (line.matches("^[//s&&[^/n]]*$")) {// 这一行匹配以空格开头，但不是以回车符开头，但以回车符结尾
                    normalLines++;
                } else if (line.startsWith("/*") && !line.endsWith("*/")) {// 匹配以/*......*/括住的多行注释
                    commentLines++;
                    comment = true;
                } else if (comment) {
                    commentLines++;

                    if (line.endsWith("*/"))
                        comment = false;
                    // 匹配以//开头的单行注释，及以/*......*/括住的单行注释
                } else if (line.startsWith("//") || (line.startsWith("/*") && line.endsWith("*/")))
                    commentLines++;
                else // 其他的就是代码行
                    writeLines++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
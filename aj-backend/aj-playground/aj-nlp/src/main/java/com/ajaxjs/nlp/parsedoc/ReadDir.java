/**
 * Author: WuLC
 * Date:   2016-05-19 22:49:42
 * Last modified by:   WuLC
 * Last Modified time: 2016-05-19 22:53:09
 * Email: liangchaowu5@gmail.com
 * ***********************************************************************************
 * Function: read the paths of all the files under a directory,including the sub-directories of  it
 * Input(String): path of the directory,the last character of the path cannot be / due to  sub-directories
 * Output(List<String>): paths of all files under the directory
 */

package com.ajaxjs.nlp.parsedoc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReadDir {
    /**
     * read the paths of all the files under a directory,including the sub-directories of  it
     *
     * @param dirPath(String): path of the directory, remember the last character can't be /
     * @return paths of all files under the directory
     */
    public static List<String> readDirFileNames(String dirPath) {
        if (dirPath.equals("")) {
            System.out.println("The path of the directory can't be empty");
            System.exit(0);
        } else if (dirPath.substring(dirPath.length() - 1).equals("/")) {
            System.out.println("The last character of the path of the directory can't be /");
            System.exit(0);
        }

        File dirFile = new File(dirPath);
        String[] fileNameList;
        String tmp = null;
        List<String> fileList = new ArrayList<>();
        List<String> subFileList = new ArrayList<>();

        fileNameList = dirFile.list();

        assert fileNameList != null;
        for (String s : fileNameList) {
            tmp = dirPath + '/' + s;
            File f1 = new File(tmp);
            if (f1.isFile()) fileList.add(tmp);
            else subFileList = readDirFileNames(tmp);
            fileList.addAll(subFileList);
        }

        return fileList;
    }

}

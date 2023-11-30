/**
 * Copyright Sp42 frank@ajaxjs.com Licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.ajaxjs.util.io;

import com.ajaxjs.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * <a href="https://blog.csdn.net/YangLiehui/article/details/98599253">...</a>
 * <a href="https://www.cnblogs.com/jpfss/p/9447838.html">...</a>
 */
@Slf4j
public class FileIoHelper {
    /**
     * 打开资源内容
     *
     * @param res 资源
     * @return 资源内容的字符串
     */
    public static String openContent(Resource res) {
        EncodedResource encRes = new EncodedResource(res, StrUtil.UTF8_SYMBOL);

        try {
            return FileCopyUtils.copyToString(encRes.getReader());
        } catch (IOException e) {
            log.warn("ERROR>>", e);
            return null;
        }
    }

    /**
     * 打开指定路径的文件内容。
     *
     * @param path 文件路径
     * @return 打开的文件内容
     */
    public static String openContent(String path) {
        return openContent(new FileSystemResource(path));
    }

    /**
     * 读取文件内容并返回字符串
     *
     * @param filePath 文件路径
     * @return 文件内容的字符串形式，若发生异常则返回 null
     */
    public static String readFile(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            log.warn("ERROR>>", e);
            return null;
        }
    }


}

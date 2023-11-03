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
import com.ajaxjs.util.logger.LogHelper;
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
public class FileIoHelper {
    private static final LogHelper LOGGER = LogHelper.getLog(FileIoHelper.class);

    public static String openContent(Resource res) {
        EncodedResource encRes = new EncodedResource(res, StrUtil.UTF8_SYMBOL);

        try {
            return FileCopyUtils.copyToString(encRes.getReader());
        } catch (IOException e) {
            LOGGER.warning(e);
            return null;
        }
    }

    public static String openContent(String path) {
        return openContent(new FileSystemResource(path));
    }

    public static String readFile(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            LOGGER.warning(e);
            return null;
        }
    }

}

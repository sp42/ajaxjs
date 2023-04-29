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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.FileCopyUtils;

import com.ajaxjs.util.logger.LogHelper;

/**
 * <a href="https://blog.csdn.net/YangLiehui/article/details/98599253">...</a>
 * <a href="https://www.cnblogs.com/jpfss/p/9447838.html">...</a>
 */
public class FileIoHelper {
    private static final LogHelper LOGGER = LogHelper.getLog(FileIoHelper.class);

    /**
     * @param res
     * @return
     */
    public static String openContent(Resource res) {
        EncodedResource encRes = new EncodedResource(res, "UTF-8");

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

    /**
     * 获取 CRC32
     * CheckedInputStream一种输入流，它还维护正在读取的数据的校验和。然后可以使用校验和来验证输入数据的完整性。
     *
     * @param file
     * @return
     */
    public static long getFileCRCCode(File file) {
        CRC32 crc32 = new CRC32();

        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(Files.newInputStream(file.toPath()));
             CheckedInputStream checkedinputstream = new CheckedInputStream(bufferedInputStream, crc32)) {
            while (checkedinputstream.read() != -1) {
            }
        } catch (IOException e) {
            LOGGER.warning(e);
        }

        return crc32.getValue();
    }
}

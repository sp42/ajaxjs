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

import com.ajaxjs.util.logger.LogHelper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;

/**
 *
 * https://blog.csdn.net/YangLiehui/article/details/98599253
 * https://www.cnblogs.com/jpfss/p/9447838.html
 */
public class FileIoHelper {
    private static final LogHelper LOGGER = LogHelper.getLog(FileIoHelper.class);

    /**
     *
     * @param res
     * @return
     */
    public static String openContent(Resource res) {
        EncodedResource encRes = new EncodedResource(res, "UTF-8");

        try {
            String content = FileCopyUtils.copyToString(encRes.getReader());
            return content;
        } catch (IOException e) {
            LOGGER.warning(e);
            return null;
        }
    }

    public static String openContent(String path) {
        return openContent(new  FileSystemResource(path));
    }
}

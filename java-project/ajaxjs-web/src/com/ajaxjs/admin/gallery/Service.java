/**
 * Copyright 2015 Frank Cheung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.cms.gallery;

import java.io.File;
 
/**
 * 相册
 * 
 * @author Frank Cheung
 *
 */
public class Service {
	/**
	 * 返回某个文件夹里面的所有文件
	 * 
	 * @param folderName
	 *            文件夹名称
	 * @return
	 */
	public static String[] getImgs(String folderName) {
		File file = new File(folderName);
		return file.isDirectory() ? file.list() : null;
	}
}

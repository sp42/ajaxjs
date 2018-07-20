/**
 * Copyright Sp42 frank@ajaxjs.com
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
package com.ajaxjs.web.upload;

/**
 * 上传结果
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 */
public class UploadResult {
	/**
	 * 上传成功之文件名
	 */
	public String fileName;

	/**
	 * 上传成功之文件完整路径
	 */
	public String fullPath;

	/**
	 * 是否上传成功
	 */
	public boolean isOk;

	/**
	 * 若不成功，是什么异常信息
	 */
	public String errMsg;
}

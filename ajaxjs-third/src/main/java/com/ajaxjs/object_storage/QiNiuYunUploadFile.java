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
package com.ajaxjs.object_storage;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.util.io.StreamHelper;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.web.UploadFile;
import com.ajaxjs.web.UploadFileInfo;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

/**
 * 七牛云对象存储的文件上传
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class QiNiuYunUploadFile extends UploadFile {
	private static final LogHelper LOGGER = LogHelper.getLog(QiNiuYunUploadFile.class);

	/**
	 * 创建一个上传请求对象
	 * 
	 * @param request        请求对象
	 * @param uploadFileInfo 上传的配置信息
	 */
	public QiNiuYunUploadFile(HttpServletRequest request, UploadFileInfo uploadFileInfo) {
		super(request, uploadFileInfo);
	}

	public byte[] uploadBytes;

	/**
	 * 保存文件
	 * 
	 * @param offset 偏移开始位置
	 * @param length 文件长度
	 * @return 上传结果
	 */
	@Override
	public UploadFileInfo save(int offset, int length) {
		UploadFileInfo uploadFileInfo = getUploadFileInfo();
		uploadFileInfo.saveFileName += "." + uploadFileInfo.extName;

		String accessKey = ConfigService.getValueAsString("uploadFile.ObjectStorageService.QiuNiuYun.accessKey");
		String secretKey = ConfigService.getValueAsString("uploadFile.ObjectStorageService.QiuNiuYun.secretKey");
		String bucket = ConfigService.getValueAsString("uploadFile.ObjectStorageService.QiuNiuYun.bucket");

		Auth auth = Auth.create(accessKey, secretKey);
		String upToken = auth.uploadToken(bucket);

		UploadManager uploadManager = new UploadManager(new Configuration(Region.huanan()));
		uploadBytes = StreamHelper.subBytes(getDataBytes(), offset, length); // 内存中的字节数组上传到空间中

		try {
			Response response = uploadManager.put(uploadBytes, uploadFileInfo.saveFileName, upToken);
			// 解析上传成功的结果
//			DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
			Map<String, Object> map = JsonHelper.parseMap(response.bodyString());
			DefaultPutRet putRet = new DefaultPutRet();
			putRet.hash = map.get("hash").toString();
			putRet.key = map.get("key").toString();

			if (putRet.key != null)
				uploadFileInfo.isOk = true;
		} catch (QiniuException ex) {
			Response r = ex.response; // more r.bodyString()

			uploadFileInfo.isOk = false;
			uploadFileInfo.errMsg = r.toString();
			LOGGER.warning(uploadFileInfo.errMsg);
		}

		if (uploadFileInfo.afterUpload != null)
			uploadFileInfo.afterUpload.accept(uploadFileInfo);

		return uploadFileInfo;
	}
}
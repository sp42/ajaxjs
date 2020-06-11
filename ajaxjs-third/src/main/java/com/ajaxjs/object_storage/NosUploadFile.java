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

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.util.io.StreamHelper;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.UploadFile;
import com.ajaxjs.web.UploadFileInfo;
import com.netease.cloud.ClientConfiguration;
import com.netease.cloud.auth.BasicCredentials;
import com.netease.cloud.services.nos.NosClient;
import com.netease.cloud.services.nos.model.ObjectMetadata;

/**
 * 网易云对象存储的文件上传
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class NosUploadFile extends UploadFile {
	private static final LogHelper LOGGER = LogHelper.getLog(NosUploadFile.class);

	/**
	 * 创建一个上传请求对象
	 * 
	 * @param request        请求对象
	 * @param uploadFileInfo 上传的配置信息
	 */
	public NosUploadFile(HttpServletRequest request, UploadFileInfo uploadFileInfo) {
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
		String accessKey = ConfigService.getValueAsString("uploadFile.ObjectStorageService.NOS.accessKey");
		String secretKey = ConfigService.getValueAsString("uploadFile.ObjectStorageService.NOS.secretKey");
		String bucket = ConfigService.getValueAsString("uploadFile.ObjectStorageService.NOS.bucket");
		String folder = ConfigService.get("uploadFile.ObjectStorageService.NOS.folder");

		UploadFileInfo uploadFileInfo = getUploadFileInfo();
		uploadFileInfo.saveFileName += "." + uploadFileInfo.extName;
		uploadFileInfo.saveFileName = folder + "/" + uploadFileInfo.saveFileName;

		ClientConfiguration conf = new ClientConfiguration();
		// 设置 NosClient 使用的最大连接数
		conf.setMaxConnections(200);
		// 设置 socket 超时时间
		conf.setSocketTimeout(10000);
		// 设置失败请求重试次数
		conf.setMaxErrorRetry(2);
		// 如果要用 https 协议，请加上下面语句
//		conf.setProtocol(Protocol.HTTPS);

		NosClient nosClient = new NosClient(new BasicCredentials(accessKey, secretKey), conf);
		nosClient.setEndpoint("nos-eastchina1.126.net");

		uploadBytes = StreamHelper.subBytes(getDataBytes(), offset, length); // 内存中的字节数组上传到空间中

		ObjectMetadata objectMetadata = new ObjectMetadata();
		// 设置流的长度，你还可以设置其他文件元数据信息
		objectMetadata.setContentLength(uploadBytes.length);

		try (InputStream in = new ByteArrayInputStream(uploadBytes);) {
			nosClient.putObject(bucket, uploadFileInfo.saveFileName, in, objectMetadata);
			uploadFileInfo.isOk = true; // 网易云没有返回状态
		} catch (Exception e) {
			LOGGER.warning(e);
			uploadFileInfo.isOk = false;
			uploadFileInfo.errMsg = e.toString();
		}

		if (uploadFileInfo.afterUpload != null)
			uploadFileInfo.afterUpload.accept(uploadFileInfo);

		return uploadFileInfo;
	}
}
/**
 * 版权所有 2017 Sp42 frank@ajaxjs.com 根据 2.0 版本 Apache 许可证("许可证")授权；
 * 根据本许可证，用户可以不使用此文件。 用户可从下列网址获得许可证副本：
 * http://www.apache.org/licenses/LICENSE-2.0
 * 除非因适用法律需要或书面同意，根据许可证分发的软件是基于"按原样"基础提供，
 * 无任何明示的或暗示的保证或条件。详见根据许可证许可下，特定语言的管辖权限和限制。
 */
package com.ajaxjs.framework.thirdparty;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.io.StreamHelper;
import com.ajaxjs.web.UploadFile;
import com.ajaxjs.web.UploadFileInfo;

/**
 * 网易云对象存储的文件上传（HHTP）
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class NsoHttpUploader extends UploadFile {
	/**
	 * 创建一个上传请求对象
	 * 
	 * @param req  请求对象
	 * @param info 上传的配置信息
	 */
	public NsoHttpUploader(HttpServletRequest req, UploadFileInfo info) {
		super(req, info);
	}

	public byte[] uploadBytes;

	@Override
	public UploadFileInfo save(int offset, int length) {
		UploadFileInfo uploadFileInfo = getUploadFileInfo();
		
		if (CommonUtil.isEmptyString(uploadFileInfo.saveFileName))
			uploadFileInfo.saveFileName = uploadFileInfo.oldFilename;
		else
			uploadFileInfo.saveFileName += "." + uploadFileInfo.extName;

		// 设置目录名
		String folder = ConfigService.get("uploadFile.ObjectStorageService.NOS.folder");
		if (!CommonUtil.isEmptyString(folder))
			uploadFileInfo.saveFileName = folder + "/" + uploadFileInfo.saveFileName;

		uploadBytes = StreamHelper.subBytes(getDataBytes(), offset, length); // 内存中的字节数组上传到空间中

		uploadFileInfo.isOk = NsoHttpUpload.uploadFile(uploadBytes, uploadFileInfo.saveFileName, NsoHttpUpload.calcMD5(null, uploadBytes));

		if (uploadFileInfo.isOk && uploadFileInfo.afterUpload != null)
			uploadFileInfo.afterUpload.accept(uploadFileInfo);

		return uploadFileInfo;
	}
}

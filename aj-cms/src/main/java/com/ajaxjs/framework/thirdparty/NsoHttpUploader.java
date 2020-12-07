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

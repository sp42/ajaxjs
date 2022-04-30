package com.ajaxjs.storage.file;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import com.ajaxjs.storage.block.model.SignedRequest;
import com.ajaxjs.storage.block.model.StartMultipartUploadResult;
import com.ajaxjs.storage.block.model.UploadPartResult;
import com.ajaxjs.storage.file.model.AccessControl;
import com.ajaxjs.storage.file.model.FileStatus;
import com.ajaxjs.storage.file.request.AbortMultipartUploadRequest;
import com.ajaxjs.storage.file.request.CompleteMultipartUploadRequest;
import com.ajaxjs.storage.file.request.DownloadSignRequest;
import com.ajaxjs.storage.file.request.UploadCommitRequest;
import com.ajaxjs.storage.file.request.UploadPartSignRequest;
import com.ajaxjs.storage.file.request.UploadSignRequest;
import com.ajaxjs.storage.file.request.UploadSignResult;
import com.ajaxjs.storage.file.request.ZipRequest;

/**
 * 文件服务
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class FileService {
	FileStatus status(long fileId);

	Map<String, String> getResponseHeaders(long fileId);

	Map<String, String> getMetadata(long fileId);

	void update(long fileId, AccessControl accessControl, Map<String, String> responseHeaders, Map<String, String> metadata);

	FileStatus upload(InputStream inStream, String storage, AccessControl accessControl, Map<String, String> responseHeaders, Map<String, String> metadata);

	UploadSignResult generatePresignedUpload(UploadSignRequest request);

	FileStatus uploadCommit(UploadCommitRequest request);

	StartMultipartUploadResult startMultipartUpload(UploadSignRequest request);

	UploadPartResult uploadPart(String storage, String uploadId, int partNumber, InputStream inputStream);

	SignedRequest generatePresignedPartUpload(UploadPartSignRequest request);

	FileStatus completeMultipartUpload(CompleteMultipartUploadRequest request);

	void abortMultipartUpload(AbortMultipartUploadRequest request);

	default InputStream openStream(long fileId) {
		return openStream(fileId, 0, Long.MAX_VALUE);
	}

	InputStream openStream(long fileId, long offset, long limit);

	void zip(ZipRequest request, OutputStream outStream);

	SignedRequest generatePresignedDownload(DownloadSignRequest request);

	void delete(long fileId);
}

package com.ajaxjs.storage.block;


import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import com.ajaxjs.storage.block.model.ContentBlock;
import com.ajaxjs.storage.block.model.SignedRequest;
import com.ajaxjs.storage.block.model.StartMultipartUploadResult;
import com.ajaxjs.storage.block.model.UploadPartResult;

/**
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public interface Storage {
    ContentBlock upload(long blockId, InputStream inStream);

//    default SignedRequest generatePresignedUpload(Application application, long blockId, UploadSignRequest request) {
//        throw new UnsupportedOperationException();
//    }
//
//    default SignedRequest generatePresignedUpload(Strategy.Settings settings,long blockId, UploadSignRequest request ) {
//        throw new UnsupportedOperationException();
//    }

    default StartMultipartUploadResult startMultipartUpload(long blockId) {
        throw new UnsupportedOperationException();
    }

    default SignedRequest generatePresignedPartUpload(String uploadId, int partNumber) {
        throw new UnsupportedOperationException();
    }

    default UploadPartResult uploadPart(String uploadId, int partNumber, InputStream inStream) {
        throw new UnsupportedOperationException();
    }

    default ContentBlock completeMultipartUpload(String uploadId, Collection<UploadPartResult> uploadPartResults) {
        throw new UnsupportedOperationException();
    }

    default void abortMultipartUpload(String uploadId) {
        throw new UnsupportedOperationException();
    }

    ContentBlock status(long blockId);

    default SignedRequest generatePresignedDownload(ContentBlock block, Integer expires, Map<String, String> requestParameters, Map<String, String> responseHeaderOverrides) {
        throw new UnsupportedOperationException();
    }

    default InputStream openStream(ContentBlock block) {
        return openStream(block, 0, Long.MAX_VALUE);
    }

    InputStream openStream(ContentBlock block, long offset, long limit);

    void delete(ContentBlock block);
}

package com.ajaxjs.base.service.file_upload;

/**
 * 云空间的文件上传
 *
 * @author Frank Cheung
 */
public interface IFileUpload {
    /**
     * 上传文件
     *
     * @param filename 上传文件名
     * @param bytes    文件内容
     * @return 是否成功
     */
    boolean upload(String filename, byte[] bytes);
}

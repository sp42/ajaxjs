package com.ajaxjs.oss;

/**
 * 云空间的文件上传
 *
 * @author Frank Cheung
 */
public interface IFileUpload {
    /**
     * 上传文件
     *
     * @param filename 文件名
     * @param bytes    文件内容
     * @return 是否成功
     */
    public boolean upload(String filename, byte[] bytes);
}

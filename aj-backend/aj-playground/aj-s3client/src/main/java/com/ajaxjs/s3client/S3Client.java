package com.ajaxjs.s3client;

import java.util.Map;

/**
 * S3 客户端
 */
public interface S3Client {
    /**
     * 列出存储桶中的所有对象
     *
     * @return XML List
     */
    String listBucket();

    /**
     * 列出存储桶中的所有对象
     *
     * @return XML List Map
     */
    Map<String, String> listBucketXml();

    /**
     * 创建一个存储桶（Bucket）
     *
     * @param bucketName 存储桶的名称，必须全局唯一
     * @return true=操作成功
     */
    boolean createBucket(String bucketName);

    /**
     * 删除一个存储桶（Bucket）
     *
     * @param bucketName 存储桶的名称
     * @return true=操作成功
     */
    boolean deleteBucket(String bucketName);

    /**
     * 将字节数据上传到指定的存储桶中
     *
     * @param bucketName 存储桶的名称
     * @param objectName 对象（文件）在存储桶中的名称
     * @param fileBytes  要上传的文件的字节数据
     * @return true=操作成功
     */
    boolean putObject(String bucketName, String objectName, byte[] fileBytes);

    /**
     * 将字节数据上传到指定的存储桶中
     *
     * @param objectName 对象（文件）在存储桶中的名称
     * @param fileBytes  要上传的文件的字节数据
     * @return true=操作成功
     */
    boolean putObject(String objectName, byte[] fileBytes);

    /**
     * 将字节数据上传到指定的存储桶中
     *
     * @param bucketName 存储桶的名称
     * @param objectName 对象（文件）在存储桶中的名称
     * @return true=操作成功
     */
    boolean getObject(String bucketName, String objectName);

    /**
     * 将字节数据上传到指定的存储桶中
     *
     * @param objectName 对象（文件）在存储桶中的名称
     * @return true=操作成功
     */
    boolean getObject(String objectName);

    /**
     * 删除指定的文件
     *
     * @param bucketName 存储桶的名称
     * @param objectName 要删除的文件名称
     * @return true=操作成功
     */
    boolean deleteObject(String bucketName, String objectName);

    /**
     * 删除指定的文件
     *
     * @param objectName 要删除的文件名称
     * @return true=操作成功
     */
    boolean deleteObject(String objectName);
}

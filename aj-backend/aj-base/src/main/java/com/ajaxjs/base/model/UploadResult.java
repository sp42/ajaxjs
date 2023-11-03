package com.ajaxjs.base.model;

import com.ajaxjs.framework.IBaseModel;
import lombok.Data;

/**
 * 上传结果
 */
@Data
public class UploadResult implements IBaseModel {
    /**
     * 文件名
     */
    private String filename;

    /**
     * 文件 URL
     */
    private String url;
}

package com.ajaxjs.base.business.model;

import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.framework.IBaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 留言反馈
 *
 * @author sp42 frank@ajaxjs.com
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Feedback extends BaseModel implements IBaseModel {
    private String name;

    private Long userId;

    private String phone;

    private String email;

    private String feedback;

    private String contact;

    private String content;
}

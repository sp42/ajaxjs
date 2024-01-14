package com.ajaxjs.framework.spring.response;

import com.ajaxjs.framework.IBaseModel;
import com.ajaxjs.framework.PageResult;
import lombok.Data;

@Data
public class PageDTO implements IBaseModel {
    private PageResult<?> rows;

    private Integer total;
}

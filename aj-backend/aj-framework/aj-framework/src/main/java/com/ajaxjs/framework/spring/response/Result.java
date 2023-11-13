package com.ajaxjs.framework.spring.response;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result<T> {
    /**
     * 实体数据
     */
    private T result;

    /**
     * 是否不用包装外面那一层
     */
    private Boolean isOriginal;
}

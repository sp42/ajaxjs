package com.github.chaijunkun.wechat.common.api;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * API公共返回结果定义
 *
 * @author chaijunkun
 * @since 2016年9月12日
 */
@JsonInclude(Include.NON_NULL)
public abstract class WeChatAPIRet implements Serializable {

    private static final long serialVersionUID = 2422896542684235099L;

    /**
     * 成功返回的代码
     */
    public static final int CODE_OK = 0;

    /**
     * 错误代码
     */
    @JsonProperty(value = "errcode")
    private Integer errcode;

    /**
     * 错误消息
     */
    @JsonProperty(value = "errmsg")
    private String errmsg;

    /**
     * 判断是否是成功返回
     */
    public boolean isSuccess() {
        return null == errcode || errcode == CODE_OK;
    }

}

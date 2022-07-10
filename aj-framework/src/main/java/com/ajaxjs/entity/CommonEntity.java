package com.ajaxjs.entity;

import java.io.Serializable;

import com.ajaxjs.framework.IBaseModel;
import com.ajaxjs.framework.Identity;
import com.ajaxjs.gateway.TenantPortal;

public interface CommonEntity extends IBaseModel, Serializable, Identity<Long>, TenantPortal {

}

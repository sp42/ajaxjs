/**
 * Copyright 2015 Frank Cheung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.framework.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ajaxjs.framework.service.IService;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.bval.constraints.NotEmpty;

/**
 * 基础模型类
 * @author frank
 *
 */
public class BaseModel {
	@FieldDescription(doc="id 序列")  
	@NotEmpty
	@NotNull(message="id不能为空")
	private long id;
	
	@FieldDescription(doc="唯一 uuid")  
	private String uid;
	
	@FieldDescription(doc="实体名称或标题")  
	@NotNull(message="名称不能为空")
	@Size(min = 1, max = 255, message="长度应该介于1和255之间")
	private String name;
	
	@FieldDescription(doc="实体内容")
	@Size(max=60000)
	private String content;
	
	@FieldDescription(doc="创建日期")  
	private Date createDate;
	
	@FieldDescription(doc="修改日期")  
	private Date updateDate;
	
	private IService<? extends BaseModel> service;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public IService<? extends BaseModel> getService() {
		return service;
	}

	public void setService(IService<? extends BaseModel> service) {
		this.service = service;
	}
	
}

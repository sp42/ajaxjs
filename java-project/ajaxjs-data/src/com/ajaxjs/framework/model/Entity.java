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

import javax.validation.constraints.Size;

/**
 * 比 BaseModel 更丰富的实体模型
 * @author frank
 *
 */
public class Entity extends BaseModel {
	@FieldDescription(doc="内容简介") 
	@Size(max=60000)
	private String intro;
	
	@FieldDescription(doc="分类 id") 
	private Integer catalog;
	
	@FieldDescription(doc="封面图路径") 
	private String cover;
	
	@FieldDescription(doc="当前状态") 
	private int status;
	
	@FieldDescription(doc="标签，用逗号（,）分割") 
	private String[] tags;
	
	/**
	 * @return {@link #status}
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status {@link #status}
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public Integer getCatalog() {
		return catalog;
	}

	public void setCatalog(Integer catalog) {
		this.catalog = catalog;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String[] getTags() {
		return tags;
	}

	public void setTags(String tags[]) {
		this.tags = tags;
	}
}

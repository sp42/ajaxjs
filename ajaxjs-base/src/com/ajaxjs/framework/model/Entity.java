/**
 * 版权所有 2017 张鑫
 * 
 * 根据2.0版本Apache许可证("许可证")授权；
 * 根据本许可证，用户可以不使用此文件。
 * 用户可从下列网址获得许可证副本：
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *    
 * 除非因适用法律需要或书面同意，根据许可证分发的软件是基于"按原样"基础提供，
 * 无任何明示的或暗示的保证或条件。详见根据许可证许可下，特定语言的管辖权限和限制。
 */
package com.ajaxjs.framework.model;

import javax.validation.constraints.Size;

/**
 * 比 BaseModel 更丰富的实体模型
 * @author frank
 *
 */
public class Entity extends BaseModel {
	private static final long serialVersionUID = -1497336201756787509L;

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
	@FieldDescription(doc="图片") 
	private String img;
	@FieldDescription(doc="简介描述") 
	private String description;
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

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

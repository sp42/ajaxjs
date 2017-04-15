/**
 * 版权所有 2017 Frank Cheung
 * 
 * 根据 2.0 版本 Apache 许可证("许可证")授权；
 * 根据本许可证，用户可以不使用此文件。
 * 用户可从下列网址获得许可证副本：
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *    
 * 除非因适用法律需要或书面同意，根据许可证分发的软件是基于"按原样"基础提供，
 * 无任何明示的或暗示的保证或条件。详见根据许可证许可下，特定语言的管辖权限和限制。
 */
package com.ajaxjs.framework.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

//import org.apache.bval.constraints.NotEmpty;

/**
 * 基础模型类
 * @author frank
 *
 */
public class BaseModel implements Serializable {
	private static final long serialVersionUID = -5313880199638314543L;

	@FieldDescription(doc="id 序列")  
	@NotNull(message="id不能为空")
	private Long id;
	
	@FieldDescription(doc="唯一 uuid")  
	private String uid;
	
	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

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
	@FieldDescription(doc="图片地址")
	private String cover;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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
}

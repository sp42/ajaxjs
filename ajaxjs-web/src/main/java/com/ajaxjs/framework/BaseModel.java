/**
 * 版权所有 2017 Sp42 frank@ajaxjs.com
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
package com.ajaxjs.framework;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.validation.constraints.Size;

//import org.apache.bval.constraints.NotEmpty;

/**
 * 基础模型类 请注意不要使用 int 而是使用 Integer
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 */
public class BaseModel implements Serializable {
	private static final long serialVersionUID = -5313880199638314543L;

	private Long id;

	private Long uid;

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

//	@NotNull(message="名称不能为空")
	@Size(min = 2, max = 255, message = "长度应该介于3和255之间")
	private String name;

	@Size(max = 60000)
	private String content;

	private Date createDate;

	private Date updateDate;
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

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	private Map<String, Object> data;

	public Map<String, Object> getData() {
		return data;
	}
}
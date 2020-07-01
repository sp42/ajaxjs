/**
 * 版权所有 2017 Sp42 frank@ajaxjs.com 根据 2.0 版本 Apache 许可证("许可证")授权；
 * 根据本许可证，用户可以不使用此文件。 用户可从下列网址获得许可证副本：
 * http://www.apache.org/licenses/LICENSE-2.0
 * 除非因适用法律需要或书面同意，根据许可证分发的软件是基于"按原样"基础提供，
 * 无任何明示的或暗示的保证或条件。详见根据许可证许可下，特定语言的管辖权限和限制。
 */
package com.ajaxjs.framework;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// import org.apache.bval.constraints.NotEmpty;

/**
 * 基础模型类 请注意不要使用 int 而是使用 Integer
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class BaseModel implements Serializable {
	private static final long serialVersionUID = -5313880199638314543L;

	/**
	 * 扩展字段
	 */
	public Map<String, Object> extractData;

	private Long id;

	/**
	 * 唯一 id
	 */
	private Long uid;

	/**
	 * 数据字典：状态
	 */
	private Integer stat;

	/**
	 * 设置数据字典：状态
	 * 
	 * @param stat 状态
	 */
	public void setStat(Integer stat) {
		this.stat = stat;
	}

	/**
	 * 获取数据字典：状态
	 * 
	 * @return 数据字典：状态
	 */
	public Integer getStat() {
		return stat;
	}

	// @NotBlank(message="名称不能为空")
	// @Size(min = 2, max = 255, message = "长度应该介于3和255之间")
	private String name;

	// @Size(max = 60000)
	private String content;

	/**
	 * 创建日期
	 */
	private Date createDate;

	/**
	 * 修改日期
	 */
	private Date updateDate;

	/**
	 * 封面图片
	 */
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

	public Map<String, Object> getExtractData() {
		return extractData;
	}

	public void setExtractData(HashMap<String, Object> extractData /* 若为 Map 不能进行反射，即使强类型也不行 */) {
		this.extractData = extractData;
	}

	public int getExtractInt(String key) {
		Object obj = getExtractData().get(key);
		return obj != null ? (int) obj : 0;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}
}
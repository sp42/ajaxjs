package com.ajaxjs.base.business.model;

import com.ajaxjs.base.business.common.BaseEntityConstant.AttachmentType;
import com.ajaxjs.framework.IBaseModel;
import lombok.Data;

import java.util.Date;

/**
 * 附件
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
@Data
public class Attachment implements IBaseModel {

	/**
	 * 主键 id，自增
	 */
	private Long id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 简介、描述
	 */
	private String desc;

	/**
	 * 分类：null/0/1=普通图片、2=头像/封面图片、3=相册图片
	 */
	private AttachmentType type;

	/**
	 * 租户 id。0 = 不设租户
	 */
	private Long tenantId;

	/**
	 * 路径
	 */
	private String path;

	/**
	 * 文件大小（单位：字节）
	 */
	private Long fileSize;

	/**
	 * 该图片属于哪个实体？这里给出实体的 uid
	 */
	private Long ownerId;

	/**
	 * 数据字典：状态
	 */
	private Integer stat;

	/**
	 * 唯一 id，通过 uuid 生成不重复 id
	 */
	private Long uid;

	/**
	 * 创建者 id
	 */
	private Long createByUser;

	/**
	 * 创建时间
	 */
	private Date createDate;

	/**
	 * 修改时间
	 */
	private Date updateDate;
}

package com.ajaxjs.export.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 单元格注释
 */
@Getter
@Setter
public class Comment {
	private String author;

	private Data data;

	private Font font;

}
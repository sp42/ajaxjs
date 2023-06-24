package com.ajaxjs.export.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cell {

	private String styleID;

	private Integer mergeAcross;

	private Integer MergeDown;

	private Data data;

	private Integer index;

	private Comment comment;

}

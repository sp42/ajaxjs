package com.ajaxjs.view;


import java.util.List;

/**
 * 分页信息 bean
 * @author frank
 *
 * @param <T>
 */
public class PageResultView<T> {
	private List<T> rows;	// 结果集
	private int totalCount; // 总记录数
	private int start; // 从第几笔记录开始
	private int pageSize; // 每页大小
	private int totalPage; // 总页数
	private int currentPage; // 当前第几页

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * 每页大小
	 */
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
}
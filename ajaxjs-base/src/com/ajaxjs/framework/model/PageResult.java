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

import java.util.List;
import java.util.Arrays;

/**
 * 分页信息 bean
 * @author frank
 *
 * @param <T> Bean 对象，也可以是 map
 */
public class PageResult<T> {
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
	
	/**
	 * 数组转换为 List
	 * @param rows
	 */
	public void setRows(T[] rows) {
		this.rows = Arrays.asList(rows);
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

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

	/**
	 * 分页的逻辑运算
	 */
	public void page() {
		int totalPage = getTotalCount() / getPageSize(), yushu = getTotalCount() % getPageSize();

		totalPage = (yushu == 0 ? totalPage : totalPage + 1);
		setTotalPage(totalPage);

		int currentPage = (getStart() / getPageSize()) + 1;
		setCurrentPage(currentPage);
	}
}
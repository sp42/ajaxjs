package com.ajaxjs.fast_doc;

import java.util.List;

/**
 * 实体一个
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class FooBean {
	/**
	 * 名称
	 */
	private String name;

	/**
	 * 数量
	 */
	private List<Integer> numbers;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Integer> getNumbers() {
		return numbers;
	}

	public void setNumbers(List<Integer> numbers) {
		this.numbers = numbers;
	}

}

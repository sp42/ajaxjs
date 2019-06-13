/**
 * Copyright sp42 frank@ajaxjs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.framework;

import java.math.BigDecimal;
import java.util.Date;

import com.ajaxjs.framework.BaseModel;

/**
 * 一个 Model 的例子
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
final class TestUser extends BaseModel {
	private static final long serialVersionUID = 1L;

	private long id;

	private int age;

	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String[] getChildren() {
		return children;
	}

	public void setChildren(String[] children) {
		this.children = children;
	}

	public int[] getLuckyNumbers() {
		return luckyNumbers;
	}

	public void setLuckyNumbers(int[] luckyNumbers) {
		this.luckyNumbers = luckyNumbers;
	}

	public boolean isSex() {
		return sex;
	}

	public void setSex(boolean sex) {
		this.sex = sex;
	}

	public boolean isGood() {
		return good;
	}

	public void setGood(boolean good) {
		this.good = good;
	}

	private Date birthday;

	private String[] children;

	private int[] luckyNumbers;

	private boolean sex;

	private boolean good;

	private BigDecimal price;

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}

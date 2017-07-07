package test.com.ajaxjs.mock;

import java.util.Date;

import com.ajaxjs.framework.model.Entity;

public class User extends Entity{
	/**
	 * 
	 */
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
}

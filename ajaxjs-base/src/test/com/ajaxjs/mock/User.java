package test.com.ajaxjs.mock;

import java.util.Date;

public class User {
	private long id;
	private String name;
	private int age;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

package com.ajaxjs.util;

/**
 * 用户的测试用例
 *
 * @author sp42 frank@ajaxjs.com
 */
public class TestCaseUserBean {
    public String directField = "directField";

    private long id;
    private String name;
    private int age;
    private boolean sex;
    private String[] children;
    private int[] luckyNumbers;

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

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }
}

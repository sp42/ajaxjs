package com.ajaxjs.cms.app.user.service.idcard;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 类说明:提取身份证相关信息
 * 
 * @author http://www.cnblogs.com/tenking/archive/2009/12/07/2075290.html
 *
 */
public class IdcardInfoExtractor extends GetInfo {
	// 省份
	private String province;
	// 城市
	private String city;
	// 区县
	private String region;
	// 年份
	private int year;
	// 月份
	private int month;
	// 日期
	private int day;
	// 性别
	private String gender;
	// 出生日期
	private Date birthday;

	private static Map<String, String> cityCodeMap = new HashMap<String, String>() {
		private static final long serialVersionUID = 4593620948714292923L;
		{
			this.put("11", "北京");
			this.put("12", "天津");
			this.put("13", "河北");
			this.put("14", "山西");
			this.put("15", "内蒙古");
			this.put("21", "辽宁");
			this.put("22", "吉林");
			this.put("23", "黑龙江");
			this.put("31", "上海");
			this.put("32", "江苏");
			this.put("33", "浙江");
			this.put("34", "安徽");
			this.put("35", "福建");
			this.put("36", "江西");
			this.put("37", "山东");
			this.put("41", "河南");
			this.put("42", "湖北");
			this.put("43", "湖南");
			this.put("44", "广东");
			this.put("45", "广西");
			this.put("46", "海南");
			this.put("50", "重庆");
			this.put("51", "四川");
			this.put("52", "贵州");
			this.put("53", "云南");
			this.put("54", "西藏");
			this.put("61", "陕西");
			this.put("62", "甘肃");
			this.put("63", "青海");
			this.put("64", "宁夏");
			this.put("65", "新疆");
			this.put("71", "台湾");
			this.put("81", "香港");
			this.put("82", "澳门");
			this.put("91", "国外");
		}
	};

	/**
	 * 通过构造方法初始化各个成员属性
	 */
	public IdcardInfoExtractor(String idcard) {
		if (isValidatedAllIdcard(idcard)) {
			if (idcard.length() == 15)
				idcard = convertIdcarBy15bit(idcard);

			// 获取省份
			String provinceId = idcard.substring(0, 2);
			Set<String> key = cityCodeMap.keySet();

			for (String id : key) {
				if (id.equals(provinceId)) {
					this.province = cityCodeMap.get(id);
					break;
				}
			}

			// 获取性别
			String id17 = idcard.substring(16, 17);
			gender = Integer.parseInt(id17) % 2 != 0 ? "男" : "女";

			// 获取出生日期
			String birthday = idcard.substring(6, 14);

			try {
				this.birthday = new SimpleDateFormat("yyyyMMdd").parse(birthday);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			GregorianCalendar currentDay = new GregorianCalendar();
			currentDay.setTime(this.birthday);

			year = currentDay.get(Calendar.YEAR);
			month = currentDay.get(Calendar.MONTH) + 1;
			day = currentDay.get(Calendar.DAY_OF_MONTH);
		}
	}

	/**
	 * @return the province
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @return the month
	 */
	public int getMonth() {
		return month;
	}

	/**
	 * @return the day
	 */
	public int getDay() {
		return day;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @return the birthday
	 */
	public Date getBirthday() {
		return birthday;
	}

	@Override
	public String toString() {
		return "省份：" + province + ", 性别：" + gender + ", 出生日期：" + birthday;
	}

	public static void main(String[] args) {
		String idcard = "440105198309060315";
		IdcardInfoExtractor ie = new IdcardInfoExtractor(idcard);
		System.out.println(ie.toString());
	}
}
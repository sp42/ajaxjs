package com.github.chaijunkun.wechat.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DateTimeAdapter extends XmlAdapter<String, Date> {
	
	private static final SimpleDateFormat FORMAT= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public String marshal(Date date) throws Exception {
		if (date == null) {
			return null;
		}
		return FORMAT.format(date);
	}

	@Override
	public Date unmarshal(String string) throws Exception {
		if (string == null) {
			return null;
		}
		return FORMAT.parse(string);
	}

}

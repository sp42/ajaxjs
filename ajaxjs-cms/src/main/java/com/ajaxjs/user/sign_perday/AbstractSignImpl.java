package com.ajaxjs.user.sign_perday;

import java.util.Date;
import java.util.LinkedHashMap;

public abstract class AbstractSignImpl implements Sign {
	SignDao dao;

	public boolean isSignToday() {
		int signInfo = dao.getSignInfoByWeekIndex(SignUtil.getWEEK_OF_YEAR());
		if (signInfo == 0) // 找不到数据=今周从未签到，也就是今天没签到
			return false;
		return false;
	}

	@Override
	public LinkedHashMap<Date, Boolean> getSignInfo() {
		return getSignInfo(7);
	}
}

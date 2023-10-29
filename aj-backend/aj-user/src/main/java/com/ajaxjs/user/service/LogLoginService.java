package com.ajaxjs.user.service;

import com.ajaxjs.framework.PageResult;
import com.ajaxjs.net.http.Get;
import com.ajaxjs.user.common.UserConstants;
import com.ajaxjs.user.controller.LogLoginController;
import com.ajaxjs.user.model.User;
import com.ajaxjs.user.model.po.LogLogin;
import com.ajaxjs.web.WebHelper;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Service
public class LogLoginService implements LogLoginController, UserConstants {
    /**
     * 用户登录日志
     */
    public void saveLoginLog(User user, HttpServletRequest req) {
        LogLogin userLoginLog = new LogLogin();
        userLoginLog.setUserId(user.getId());
        userLoginLog.setLoginType(LoginType.PASSWORD);
        saveIp(userLoginLog, req);

//		if (LogLoginDAO.create(userLoginLog) == null)
//			LOGGER.warning("更新会员登录日志出错");
    }

    void saveIp(LogLogin bean, HttpServletRequest req) {
        if (req == null)
            return;

        String ip = WebHelper.getIp(req);

        if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "localhost";
            bean.setIpLocation("本机");
        } else {
            try {
                // ip 查询
                Map<String, Object> map = Get.api("http://ip-api.com/json/" + ip + "?lang=zh-CN");

                if (!map.containsKey("errMsg")) {
                    String location = map.get("country") + " " + map.get("regionName");
                    bean.setIpLocation(location);
                } else
                    throw new Exception("接口返回不成功 " + map.get("errMsg"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        bean.setIp(ip);
        bean.setUserAgent(req.getHeader("user-agent"));
    }

    @Override
    public PageResult<LogLogin> page() {
        return null;
    }

    @Override
    public List<LogLogin> findListByUserId(Long userId) {
        return null;
    }

    @Override
    public LogLogin getLastUserLoginInfo(long userId) {
        return null;
    }
}

package com.ajaxjs.iam.user.service;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.iam.user.common.UserConstants;
import com.ajaxjs.iam.user.controller.LogLoginController;
import com.ajaxjs.iam.user.model.LogLogin;
import com.ajaxjs.iam.user.model.User;
import com.ajaxjs.net.http.Get;

import com.ajaxjs.util.WebHelper;
import com.ajaxjs.util.logger.LogHelper;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

@Service
public class LogLoginService implements LogLoginController, UserConstants {
    private static final LogHelper LOGGER = LogHelper.getLog(LogLoginService.class);

    /**
     * 用户登录日志
     */
    public void saveLoginLog(User user, HttpServletRequest req) {
        LogLogin userLoginLog = new LogLogin();
        userLoginLog.setUserId(user.getId());
        userLoginLog.setLoginType(LoginType.PASSWORD);
        userLoginLog.setUserName(user.getLoginId());
        saveIp(userLoginLog, req);

        Long id = CRUD.create(userLoginLog);

        if (CRUD.create(userLoginLog) == null)
            LOGGER.warning("更新会员登录日志出错");
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
                LOGGER.warning(e);
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

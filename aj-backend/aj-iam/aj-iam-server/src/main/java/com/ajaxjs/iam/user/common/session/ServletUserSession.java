package com.ajaxjs.iam.user.common.session;

import com.ajaxjs.framework.spring.DiContextUtil;
import com.ajaxjs.iam.user.model.User;


import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.Objects;

/**
 * 基于 Servlet Session 最简单的用户会话，适合单机，不支持集群
 */
public class ServletUserSession implements UserSession {

    private HttpSession getSession() {
        return Objects.requireNonNull(DiContextUtil.getRequest()).getSession();
    }

    @Override
    public void put(String key, User user) {
        getSession().setAttribute(key, user);
    }

    @Override
    public void delete(String key) {
        getSession().removeAttribute(key);
    }

    @Override
    public void setExpires(int minutes) {
        getSession().setMaxInactiveInterval(minutes * 60);
    }

    @Override
    public void removeAllUser() {
        // 遍历 HttpSession 中的属性
        Enumeration<String> attributeNames = getSession().getAttributeNames();

        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();

            if (attributeName.startsWith(SESSION_KEY))
                getSession().removeAttribute(attributeName);
            // 处理属性值
//            System.out.println("Attribute: " + attributeName + ", Value: " + attributeValue);
        }
    }
}

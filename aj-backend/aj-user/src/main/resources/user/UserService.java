package com.ajaxjs.user.controller;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.framework.entity.BaseEntityConstants;
import com.ajaxjs.framework.spring.DiContextUtil;
import com.ajaxjs.user.common.util.CheckStrength;
import com.ajaxjs.user.controller.UserController;
import com.ajaxjs.user.model.User;
import com.ajaxjs.user.model.po.UserAuth;
import com.ajaxjs.web.WebHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public class UserService implements UserController, UserConstants {
    @Autowired
    @Qualifier("passwordEncode")
    Function<String, String> passwordEncode;

    @Override
    public Boolean register(Map<String, Object> params) {
        // 所有字符串 trim 一下
        for (String key : params.keySet()) {
            Object obj = params.get(key);

            if (obj instanceof String)
                params.put(key, obj.toString().trim());
        }

        // 校验
        if (isNull(params, "tenantId"))
            throw new IllegalArgumentException("租户 id 不能为空");

        if (isNull(params, "password"))
            throw new IllegalArgumentException("注册密码不能为空");

        boolean hasNoUsername = isNull(params, "username"), hasNoEmail = isNull(params, "email"), hasNoPhone = isNull(params, "phone");
        if (hasNoUsername && hasNoEmail && hasNoPhone)
            throw new IllegalArgumentException("没有用户标识， username/email/phone 至少填一种");

        int tenantId = Integer.parseInt(params.get("tenantId").toString());

        // 是否重复
        if (!hasNoUsername && isRepeat("username", params.get("username").toString(), tenantId))
            throw new IllegalArgumentException("用户名 username: " + params.get("username").toString() + " 重复");

        if (!hasNoEmail && isRepeat("email", params.get("email").toString(), tenantId))
            throw new IllegalArgumentException("邮箱: " + params.get("email").toString() + " 重复");

        if (!hasNoPhone && isRepeat("phone", params.get("phone").toString(), tenantId))
            throw new IllegalArgumentException("手机: " + params.get("phone").toString() + " 重复");

        // 有些字段不要
        String psw = params.get("password").toString();
        params.remove("password");

        // 检测密码强度
        CheckStrength.LEVEL passwordLevel = CheckStrength.getPasswordLevel(psw);

        if (passwordLevel == CheckStrength.LEVEL.EASY)
            throw new UnsupportedOperationException("密码强度太低");

        long userId = CRUD.create(params); // 写入数据库
        UserAuth auth = new UserAuth();
        auth.setUserId(userId);
        auth.setCredential(passwordEncode.apply(psw));
        auth.setRegisterType(LoginType.PASSWORD);
        auth.setRegisterIp(WebHelper.getIp(Objects.requireNonNull(DiContextUtil.getRequest())));

        return true;
    }

    private static boolean isNull(Map<String, Object> params, String key) {
        return !params.containsKey(key) || !StringUtils.hasText(params.get(key).toString());
    }

    @Override
    public Boolean checkRepeat(String field, String value) {
        return isRepeat(field, value, TenantService.getTenantId());
    }

    /**
     * 检查某个值是否已经存在一样的值
     *
     * @param field 数据库里面的字段名称
     * @param value 欲检查的值
     * @return true=值重复
     */
    public static boolean isRepeat(String field, String value, int tenantId) {
        String sql = "SELECT id FROM user WHERE stat != 1 AND %s = ? AND tenantId = ? LIMIT 1";
        sql = String.format(sql, field.trim());

        return CRUD.queryOne(Long.class, sql, value.trim(), tenantId) != null; // 有这个数据表示重复
    }

    @Override
    public User info(Long id) {
        String sql = "SELECT * FROM user WHERE stat != 1 AND id = ?";
        sql = TenantService.addTenantIdQuery(sql);

        return CRUD.info(User.class, sql, id);
    }

    @Override
    public Boolean update(User user) {
        return CRUD.update(user);
    }

    @Override
    public Boolean delete(Long id) {
        User user = new User();
        user.setId(id);
        user.setStat(BaseEntityConstants.STATUS_DELETED);  // 逻辑删除

        return update(user);
    }
}

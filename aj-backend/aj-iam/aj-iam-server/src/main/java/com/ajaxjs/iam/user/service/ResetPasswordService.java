package com.ajaxjs.iam.user.service;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.iam.user.common.UserUtils;
import com.ajaxjs.iam.user.common.util.CheckStrength;
import com.ajaxjs.iam.user.common.util.SendEmail;
import com.ajaxjs.iam.user.controller.ResetPasswordController;
import com.ajaxjs.iam.user.model.User;
import com.ajaxjs.iam.user.model.UserAuth;
import com.ajaxjs.util.Digest;
import com.ajaxjs.util.EncryptUtil;
import com.ajaxjs.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class ResetPasswordService implements ResetPasswordController {
    @Autowired
    private SendEmail sendEmail;

    @Autowired
    @Qualifier("passwordEncode")
    Function<String, String> passwordEncode;

    @Value("${User.restPassword.encryptKey}")
    private String encryptKey;

    @Value("${Website.basePath}")
    private String websiteBasePath;

    /**
     *
     */
    private final static int TOKEN_TIMEOUT = 20;

    /**
     *
     */
    private final static String FIND_BY_EMAIL = "/user/reset_password/findByEmail/";

    User findUserBy(String type, Object value, Integer tenantId) {
        String sql = "SELECT * FROM user WHERE %s = ? AND stat != 1";
        sql = String.format(sql, type);

        if (tenantId != null && tenantId != 0)
            sql += " AND tenant_id = " + tenantId;

        User user = CRUD.info(User.class, sql, value);

        if (user == null)
            throw new IllegalAccessError("该 " + type + ": " + value + " 的用户不存在！");

        return user;
    }

    @Override
    public boolean sendRestEmail(String email) {
        if (!StringUtils.hasText(email) || !UserUtils.isValidEmail(email))
            throw new IllegalArgumentException("请提交有效的邮件地址");

        Integer tenantId = TenantService.getTenantId();
        User user = findUserBy("email", email, tenantId);
        String token = makeEmailToken(email, tenantId);
        String url = websiteBasePath + FIND_BY_EMAIL + String.format("?email=%s&token=%s", StrUtil.urlEncode(email), StrUtil.urlEncode(token));

        String title = "重置密码";
        Map<String, String> map = new HashMap<>();
        map.put("username", user.getUsername());
        map.put("link", url);
        map.put("desc", title);
        map.put("timeout", String.valueOf(TOKEN_TIMEOUT));
        String content = sendEmail.getEmailContent(map);

        return sendEmail.send(email, title, content);
    }

    @Override
    public boolean sendRestPhone(String phone) {
        if (!StringUtils.hasText(phone) || !UserUtils.isValidPhone(phone))
            throw new IllegalArgumentException("请提交有效的手机");

        User user = findUserBy("phone", phone, TenantService.getTenantId());
//        String code = sendSMS.getRandomCodeAndSave(phone, String.valueOf(user.getId()), user.getUsername());
//
//        return sendSMS.send(phone, code);
        return true;
    }

    @Override
    public Boolean verifySmsUpdatePsw(String code, String newPsw, String phone) {
        User user = findUserBy("phone", phone, TenantService.getTenantId());
//        sendSMS.checkSmsCode(phone, code); // 没有异常就表示通过

//        return updatePwd(user, newPsw);
        return false;
    }

    @Override
    public boolean verifyTokenUpdatePsw(String token, String newPsw, String email) {
//        UserDAO.setWhereQuery(" u.email = '" + email + "' AND u.tenantId = 1 ");
//        Map<String, Object> user = UserDAO.findOneWithAuth();
//
//        if (user == null)
//            throw new IllegalAccessError("该 email：" + email + " 的用户不存在！");
//
        if (!checkEmailToken(token, email))
            throw new IllegalAccessError("校验 Token　失败");
//
//        return updatePwd(user, newPsw);
        return false;
    }

    /**
     * 生成重置密码的 Token（ for 邮件） 这 Token 在有效期内一直有效 TODO，令其无效。 该签名方法不能公开
     * <a href="https://www.cnblogs.com/shenliang123/p/3266770.html">...</a>
     * <a href="https://blog.wamdy.com/archives/1708.html">...</a>
     *
     * @param email    邮件地址
     * @param tenantId 租户 id
     * @return Token 签名
     */
    public String makeEmailToken(String email, Integer tenantId) {
        String expireHex = Long.toHexString(System.currentTimeMillis());
        String emailToken = Digest.getSHA1(encryptKey + email), timeToken = EncryptUtil.getInstance().AES_encode(expireHex, encryptKey);

        return emailToken + timeToken;
    }

    /**
     * 验证重置密码的 token 是否有效
     *
     * @param token 令牌
     * @param email 用户提交用于对比的 email
     * @return true = 通过
     */
    public boolean checkEmailToken(String token, String email) {
        String emailToken = token.substring(0, 40), timeToken = token.substring(40);

        if (!Digest.getSHA1(encryptKey + email).equals(emailToken))
            throw new SecurityException("非法 email 账号！ " + email);

        String expireHex = EncryptUtil.getInstance().AES_decode(timeToken, encryptKey);
        long cha = new Date().getTime() - Long.parseLong(expireHex, 16);
        double result = cha * 1.0 / (1000 * 60 * 60);

        if (result <= TOKEN_TIMEOUT)
            return true;// 合法
        else
            throw new IllegalAccessError("该请求已经过期，请重新发起");
    }

    /**
     * 更新用户密码
     *
     * @param user        用户信息
     * @param newPassword 用户输入的新密码
     * @return 是否修改成功
     */
    public boolean updatePwd(Map<String, Object> user, String newPassword) {
        CheckStrength.LEVEL passwordLevel = CheckStrength.getPasswordLevel(newPassword); // 检测密码强度

        if (passwordLevel == CheckStrength.LEVEL.EASY)
            throw new UnsupportedOperationException("密码强度太低");

        newPassword = passwordEncode.apply(newPassword);

        if (newPassword.equalsIgnoreCase(user.get("password").toString()))
            throw new UnsupportedOperationException("新密码与旧密码一致，没有修改");

        UserAuth updateAuth = new UserAuth();
        updateAuth.setId(Long.parseLong(String.valueOf(user.get("authId"))));
        updateAuth.setCredential(newPassword);

        return CRUD.update(updateAuth);
    }
}

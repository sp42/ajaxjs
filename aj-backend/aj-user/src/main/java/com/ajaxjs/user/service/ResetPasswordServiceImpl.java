//package com.ajaxjs.user.service;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//import com.ajaxjs.user.common.util.CheckStrength;
//import com.ajaxjs.user.common.util.SendEmail;
//import com.ajaxjs.user.controller.ResetPasswordController;
//import com.ajaxjs.user.model.User;
//import com.ajaxjs.user.model.po.UserAuth;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//
//
//import com.ajaxjs.util.StrUtil;
//
//@Service
//public class ResetPasswordServiceImpl implements ResetPasswordController {
//    @Value("${ResetPassword.encryptKey}")
//    private String encryptKey;
//
//    @Autowired
//    SendEmail sendEmail;
//
//    @Value("${Website.basePath}")
//    String websiteBasePath;
//
//    /**
//     *
//     */
//    private final static String FIND_BY_EMAIL = "/user/reset_password/findByEmail/";
//
//    public boolean sendRestEmail(String email) {
//        int tenantId = SsoUtil.getTenantId();
//
//        if (!StringUtils.hasText(email) || !UserUtils.EMAIL_REG.matcher(email).find())
//            throw new IllegalArgumentException("请提交有效的邮件地址");
//
//        User user = findByEmail(email, tenantId);
//        if (user == null)
//            throw new IllegalAccessError("该 email：" + email + " 的用户不存在！");
//
//        String token = makeEmailToken(email, tenantId);
//        String url = websiteBasePath + FIND_BY_EMAIL;
//        url += String.format("?email=%s&token=%s", StrUtil.urlEncode(email), StrUtil.urlEncode(token));
//
//        String title = "重置密码";
//        Map<String, String> map = new HashMap<>(4);
//        map.put("username", user.getUsername());
//        map.put("link", url);
//        map.put("desc", title);
//        map.put("timeout", String.valueOf(tokenTimeout));
//
//        String content = sendEmail.getEmailContent(map);
//
//        return sendEmail.send(email, title, content);
//    }
//
//    @Autowired(required = false)
//    ISendSMS sendSMS;
//
//    public boolean sendRestPhone(String phone) {
//        int tenantId = SsoUtil.getTenantId();
//
//        if (!StringUtils.hasText(phone) || !UserUtils.PHONE_REG.matcher(phone).find())
//            throw new IllegalArgumentException("请提交有效的手机");
//
//        User user = UserDao.findUserBy("phone", phone, tenantId);
//
//        if (user == null)
//            throw new IllegalAccessError("该手机： " + phone + " 的用户不存在！");
//
//        String code = sendSMS.getRandomCodeAndSave(phone, String.valueOf(user.getId()), user.getUsername());
//
//        return sendSMS.send(phone, code);
//    }
//
//    @Override
//    public Boolean verifySmsUpdatePsw(String code, String newPsw, String phone) {
//        UserDAO.setWhereQuery(" u.phone = '" + phone + "' AND u.tenantId = 1 ");
//        Map<String, Object> user = UserDAO.findOneWithAuth();
//
//        if (user == null)
//            throw new IllegalAccessError(String.format("不存在手机号码[%s]的用户", phone));
//
//        sendSMS.checkSmsCode(phone, code); // 没有异常就表示通过
//
//        return updatePwd(user, newPsw);
//    }
//
//    @Override
//    public boolean verifyTokenUpdatePsw(String token, String newPsw, String email) {
//        UserDAO.setWhereQuery(" u.email = '" + email + "' AND u.tenantId = 1 ");
//        Map<String, Object> user = UserDAO.findOneWithAuth();
//
//        if (user == null)
//            throw new IllegalAccessError("该 email：" + email + " 的用户不存在！");
//
//        if (!checkEmailToken(token, email))
//            throw new IllegalAccessError("校验 Token　失败");
//
//        return updatePwd(user, newPsw);
//    }
//
//    /**
//     * 生成重置密码的 Token（ for 邮件） 这 Token 在有效期内一直有效 TODO，令其无效。 该签名方法不能公开
//     * <a href="https://www.cnblogs.com/shenliang123/p/3266770.html">...</a>
//     * <a href="https://blog.wamdy.com/archives/1708.html">...</a>
//     *
//     * @param email    邮件地址
//     * @param tenantId 租户 id
//     * @return Token 签名
//     */
//    public String makeEmailToken(String email, int tenantId) {
//        String expireHex = Long.toHexString(System.currentTimeMillis());
//        String emailToken = Digest.getSHA1(encryptKey + email), timeToken = SymmetriCipher.AES_Encrypt(expireHex, encryptKey);
//
//        return emailToken + timeToken;
//    }
//
//    @Value("${ResetPassword.tokenTimeout}")
//    int tokenTimeout;
//
//    /**
//     * 验证重置密码的 token 是否有效
//     *
//     * @param token
//     * @param email 用户提交用于对比的 email
//     * @return true = 通过
//     */
//    public boolean checkEmailToken(String token, String email) {
//        String emailToken = token.substring(0, 40), timeToken = token.substring(40, token.length());
//
//        if (!Digest.getSHA1(encryptKey + email).equals(emailToken))
//            throw new SecurityException("非法 email 账号！ " + email);
//
//        String expireHex = SymmetriCipher.AES_Decrypt(timeToken, encryptKey);
//        long cha = new Date().getTime() - Long.parseLong(expireHex, 16);
//        double result = cha * 1.0 / (1000 * 60 * 60);
//
//        if (result <= tokenTimeout)
//            // 合法
//            return true;
//        else
//            throw new IllegalAccessError("该请求已经过期，请重新发起！ ");
//    }
//
//    @Autowired
//    LoginServiceImpl loginService;
//
//    /**
//     * 更新用户密码
//     *
//     * @param user        用户信息
//     * @param newPassword 用户输入的新密码
//     * @return 是否修改成功
//     */
//    public boolean updatePwd(Map<String, Object> user, String newPassword) {
//        // 检测密码强度
//        CheckStrength.LEVEL passwordLevel = CheckStrength.getPasswordLevel(newPassword);
//
//        if (passwordLevel == CheckStrength.LEVEL.EASY)
//            throw new UnsupportedOperationException("密码强度太低");
//
//        newPassword = loginService.encodePassword(newPassword);
//
//        if (newPassword.equalsIgnoreCase(user.get("password").toString()))
//            throw new UnsupportedOperationException("新密码与旧密码一致，没有修改");
//
//        UserAuth updateAuth = new UserAuth();
//        updateAuth.setId(Long.parseLong(String.valueOf(user.get("authId"))));
//        updateAuth.setCredential(newPassword);
//
////		if (com.ajaxjs.user.common.service.UserDAO.UserAuthDAO.update(updateAuth)) // 密码修改成功
////			return true;
//
//        return false; // 密码修改失败
//    }
//
//}

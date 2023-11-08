package com.ajaxjs.iam.user.controller;

import org.springframework.web.bind.annotation.*;

/**
 * 重置密码
 *
 * @author Frank Cheung
 */
@RestController
@RequestMapping("/reset_psw")
public interface ResetPasswordController {
    /**
     * 根据 email 重置密码
     *
     * @param email    用户邮件
     * @param tenantId 租户 id
     * @return true 表示发送成功
     */
    @PostMapping("/send_reset_email/{email}")
    boolean sendRestEmail(@PathVariable String email);

    /**
     * 校验 Token 并更新密码。
     * <p>
     * 根据邮件查询用户，验证 token，若通过更新密码
     *
     * @param token  用户令牌
     * @param newPsw 用户输入的新密码
     * @param email  邮件地址
     * @return true 表示更新成功
     */
    @PostMapping("/verify_token_update_psw/{email}/{token}")
    boolean verifyTokenUpdatePsw(@PathVariable String token, @RequestParam String newPsw, @PathVariable String email);

    /**
     * 根据 手机 重置密码
     *
     * @param phone 手机号码
     * @return true 表示发送成功
     */
    @PostMapping("/send_reset_phone/{phone}")
    boolean sendRestPhone(@PathVariable String phone);

    /**
     * 校验 Sms code 并更新密码。
     * <p>
     * 根据邮件查询用户，验证 token，若通过更新密码
     *
     * @param token  用户令牌
     * @param newPsw 用户输入的新密码
     * @param email  邮件地址
     * @return true 表示更新成功
     */
    @PostMapping("/verify_sms_update_psw/{phone}/{code}")
    Boolean verifySmsUpdatePsw(@PathVariable String code, @RequestParam String newPsw, @PathVariable String phone);
}

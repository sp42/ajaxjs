package com.ajaxjs.cms.user.controller;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.cms.user.User;
import com.ajaxjs.cms.user.service.UserService;
import com.ajaxjs.cms.utils.sms.Message;
import com.ajaxjs.cms.utils.sms.SMS;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.SimpleSMSFilter;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 基础的用户控制器，通用的會員操作，放在這裏
 * 
 * @author frank
 *
 */
public abstract class BaseUserController extends BaseController<User> {
	private static final LogHelper LOGGER = LogHelper.getLog(BaseUserController.class);
	
	@Override
	public abstract UserService getService();

	/**
	 * 提示已登錄
	 */
	public static String loginedMsg = "redirect::/showMsg?msg=已经登录用户无法執行該操作";

	/**
	 * 验证手机号码是否正确
	 */
	public static final String phoneRegex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";

	/**
	 * 会员是否已经登录
	 * 
	 * @param request 请求对象
	 * @return true=會員已經登錄；fasle=未登录
	 */
	public static boolean isLogined(HttpServletRequest request) {
		return request != null && request.getSession() != null && request.getSession().getAttribute("userId") != null;
	}

	/**
	 * 会员是否已经登录。该方法不用传入请求对象，而是从 ThreadLocal 获取，使用更简单。
	 * 
	 * @return true=會員已經登錄；fasle=未登录
	 */
	public static boolean isLogined() {
		HttpServletRequest request = MvcRequest.getHttpServletRequest();
		if (request == null)
			throw new NullPointerException("ThreadLocal 未定义 HttpServletRequest。请先保存 HttpServletRequest");

		return isLogined(request);
	}

	/**
	 * 获取用户 id，在 session 中获取，也可以在 userId 请求参数中获取（前者优先）
	 * 
	 * @param request 请求对象
	 * @return Long 類型的參數
	 */
	public static Long getUserId(HttpServletRequest request) {
		if (isLogined(request)) {
			Object obj = request.getSession().getAttribute("userId");
			if (obj instanceof Integer)
				return ((Integer) obj).longValue();
			else if (obj instanceof Long)
				return (Long) obj;
			else if (obj instanceof String) {
				return Long.parseLong((String) obj);
			} else {
				throw new UnsupportedOperationException("Unknow type! " + obj);
			}
		} else if (request.getParameter("userId") != null) {
			return Long.parseLong(request.getParameter("userId"));
		} else
			throw new UnsupportedOperationException("Fail to access user id");
	}

	public static Long getUserUid() {
		Long uid = null;
		HttpServletRequest request = MvcRequest.getHttpServletRequest();

		if (isLogined(request)) {
			Object obj = request.getSession().getAttribute("userUid");

			if (obj == null)
				throw new UnsupportedOperationException("Fail to access user id");
			else {
				return (Long) obj;
			}
		}

		return uid;
	}

	/**
	 * 
	 * @return
	 */
	public static Long getUserId() {
		return getUserId(MvcRequest.getHttpServletRequest());
	}

	/**
	 * 返回单个会员的详情，这会访问数据库
	 * 
	 * @return 单个会员
	 */
	public User getUserDetail() {
		try {
			return getService().findById(getUserId());
		} catch (Throwable e) {
			LOGGER.warning(e);
			return null;
		}
	}

	/**
	 * 返回用户对象，这通过 session 返回信息，不会太多信息但是速度比较快（不用访问数据库）
	 * 
	 * @return 用户对象
	 */
	public User getUserBySession() {
		User user = new User();
		user.setId(getUserId());
		HttpServletRequest request = MvcRequest.getHttpServletRequest();
		if (request.getSession().getAttribute("userName") != null)
			user.setName(request.getSession().getAttribute("userName").toString());
		if (request.getSession().getAttribute("userPhone") != null)
			user.setPhone(request.getSession().getAttribute("userPhone").toString());

		return user;
	}

	/**
	 * 发送短信
	 * 
	 * @param phoneNo
	 * @param request
	 * @return
	 */
	public String sendSMScode(String phoneNo) {
		LOGGER.info("发送短信到::" + phoneNo);

		if (phoneNo.length() != 11) {
			return jsonNoOk(phoneNo + " 中国大陆手机号应为11位数");
		} else {
			if (CommonUtil.regMatch(phoneRegex, phoneNo) == null) {
				return jsonNoOk(phoneNo + " 为非法手机号码");
			} else {
				int randomCode = (int) ((Math.random() * 9 + 1) * 100000);

				Message message = new Message();
				message.setPhoneNo(phoneNo);
				message.setSignName("我是zyjf");
				message.setTemplateCode("SMS_128495105");
				message.setTemplateParam(String.format("{\"code\":\"%s\"}", randomCode));

				if (sms.send(message)) {
					LOGGER.info("发送手机 " + phoneNo + " 验证码成功");
					MvcRequest.getHttpServletRequest().getSession().setAttribute(SimpleSMSFilter.SMS_KEY_NAME, randomCode + "");
					return jsonOk("发送手机 " + phoneNo + " 验证码成功");
				} else {
					return jsonNoOk("发送手机 " + phoneNo + " 验证码失敗");
				}
			}
		}
	}

	private SMS sms;

	public SMS getSms() {
		return sms;
	}

	public void setSms(SMS sms) {
		this.sms = sms;
	}
}

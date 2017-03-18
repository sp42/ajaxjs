/**
 * Copyright 2015 Frank Cheung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.web;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import com.ajaxjs.mvc.controller.MvcOutput;
import com.ajaxjs.util.io.ImageUtil;

import java.awt.Font;

/**
 * 验证码的简单实现
 * JSP 调用方式：<%com.ajaxjs.bigfoot.tools.VcodeImg.init(pageContext);%>
 * 
 * @author 网上收集 
 */
public class Captcha extends ImageUtil {
	/**
	 * 验证码
	 */
	private String code;

	/**
	 * SESSION 的键值
	 */
	public static final String SESSION_KEY = "rand";
	
	/**
	 * 生成验证码图片
	 * 
	 * @return 图片对象
	 */
	public Captcha get() {
		BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);// 在内存中创建图像
		Graphics g = image.getGraphics(); // 获取图形上下文 
		g.setColor(getRandColor(200, 250)); // 设定背景 
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setFont(new Font("Times New Roman", Font.PLAIN, 18)); // 设定字体 
		g.setColor(getRandColor(160, 200)); 

		Random random = new Random();// 随机产生干扰线
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(getWidth()), y = random.nextInt(getHeight());
			int xl = random.nextInt(12), yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}

		String sRand = ""; // 随机产生4位验证码
		for (int i = 0; i < 4; i++) {
			String rand = String.valueOf(random.nextInt(10));
			sRand += rand;
			g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110))); // 将认证码显示到图象中  
			g.drawString(rand, 13 * i + 6, 16);// 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成 
		}

		g.dispose();// 图象生效     

		setbImg(image);
		setCode(sRand);
		
		return this;
	}
	
	/**
	 * 生成随机颜色
	 * 
	 * @param fc
	 * @param bc
	 * @return
	 */
	private static Color getRandColor(int fc, int bc) {
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;

		Random random = new Random();
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);

		return new Color(r, g, b);
	}

	/**
	 * 显示验证码图片并将认证码存入 Session
	 * 
	 * @param response
	 *            响应对象
	 * @param session
	 *            会话对象
	 */
	public static void init(HttpServletResponse response, HttpSession session) {
		Captcha captcha = new Captcha();
		captcha.setWidth(60).setHeight(20);
		captcha.get();
		
		new MvcOutput(response).noCache().setContent_Type("image/jpeg").go(captcha.getbImg());

		session.setAttribute(SESSION_KEY, captcha.getCode()); // 将验证码存入 SESSION 
		
		System.out.println("生成验证码:" + captcha.getCode());
	}

	/**
	 * 显示验证码图片并将认证码存入 Session（For JSP）
	 * 
	 * @param pageContext
	 *            页面上下文对象
	 */
	public static void init(PageContext pageContext) {
		init((HttpServletResponse) pageContext.getResponse(), pageContext.getSession());
	}

	/**
	 * 判断用户输入的验证码是否通过
	 * 
	 * @param request
	 * @param CaptchaCode
	 * @return true 表示通过
	 * @throws Throwable
	 */
	public static boolean isPass(HttpServletRequest request, String CaptchaCode) throws Throwable {
		boolean isCaptchaPass = false;

		String rand = (String) request.getSession().getAttribute(SESSION_KEY);

		System.out.println("rand:" + rand);
		System.out.println("CaptchaCode:" + CaptchaCode);

		if (rand == null)
			throw new UnsupportedOperationException("请刷新验证码。");
		else if (CaptchaCode == null || CaptchaCode.equals("")) {
			throw new IllegalArgumentException("没提供验证码参数");
		} else {
			isCaptchaPass = rand.equals(CaptchaCode);
			if (!isCaptchaPass)
				throw new IllegalAccessError("验证码不正确");
		}

		if (isCaptchaPass) {
			request.getSession().removeAttribute(SESSION_KEY);// 通过之后记得要 清除验证码
		}

		return isCaptchaPass;
	}
	
	/**
	 * 获取验证码
	 * 
	 * @return
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 设置验证码
	 * 
	 * @param code
	 *            验证码
	 */
	public void setCode(String code) {
		this.code = code;
	}

}
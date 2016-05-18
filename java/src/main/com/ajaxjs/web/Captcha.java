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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import java.awt.Font;

import com.ajaxjs.util.LogHelper;
import com.ajaxjs.util.stream.Web;

/**
 * 验证码的简单实现 http://a455360448201209214217.iteye.com/blog/1953785 调用方式： <%@ page
 * language="java" contentType="image/JPEG; charset=UTF-8" pageEncoding="UTF-8"
 * trimDirectiveWhitespaces="true"%> <%@page contentType=
 * "image/JPEG; charset=UTF-8" trimDirectiveWhitespaces="true"%>
 * <%com.ajaxjs.bigfoot.tools.VcodeImg.init(pageContext);%>
 * 
 * @author 网上收集
 */
public class Captcha {
	private static final LogHelper LOGGER = LogHelper.getLog(Captcha.class);

	/**
	 * 生成随机颜色
	 * 
	 * @param fc
	 * @param bc
	 * @return
	 */
	private Color getRandColor(int fc, int bc) {
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
	 * 默认宽度 60
	 */
	private int width = 60;

	/**
	 * 获取宽度
	 * 
	 * @return
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * 设置宽度
	 * 
	 * @param width
	 *            宽度
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * 默认高度 20
	 */
	private int height = 20;

	/**
	 * 获取高度
	 * 
	 * @return
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * 设置高度
	 * 
	 * @param height
	 *            高度
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	private String code;

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

	/**
	 * 生成验证码图片
	 * 
	 * @return
	 */
	public BufferedImage get() {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Graphics g;
		g = image.getGraphics();
		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);
		g.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		g.setColor(getRandColor(160, 200));

		Random random = new Random();
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(width), y = random.nextInt(height);
			int xl = random.nextInt(12), yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}

		String sRand = "";
		for (int i = 0; i < 4; i++) {
			String rand = String.valueOf(random.nextInt(10));
			sRand += rand;
			g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
			g.drawString(rand, 13 * i + 6, 16);
		}

		// 将认证码存入SESSION
		// session.setAttribute("rand", sRand);
		setCode(sRand);
		g.dispose();

		return image;
	}

	public static final String SESSION_KEY = "rand";

	/**
	 * 显示验证码图片并将认证码存入 Session
	 * 
	 * @param response
	 * @param session
	 */
	public static void init(HttpServletResponse response, HttpSession session) {
		Captcha img = new Captcha();
		Responser rh = new Responser(response);

		rh.noCache(); // 不用缓存
		Web.loadImage(img.get(), response);

		session.setAttribute(SESSION_KEY, img.getCode());
		LOGGER.info("生成验证码:" + img.getCode());
	}

	/**
	 * For JSP
	 * 
	 * @param pageContext
	 */
	public static void init(PageContext pageContext) {
		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
		init(response, pageContext.getSession());
	}

	/**
	 * 判断用户输入的验证码是否通过
	 * 
	 * @return
	 * @throws Throwable
	 */
	public boolean isPass(PageContext pageContext) throws Throwable {
		boolean isCaptchaPass = false;

		String rand = (String) pageContext.getSession().getAttribute(SESSION_KEY),
				CaptchaCode = pageContext.getRequest().getParameter("CaptchaCode");

		LOGGER.info("rand:" + rand);
		LOGGER.info("CaptchaCode:" + CaptchaCode);

		if (rand == null)
			throw new UnsupportedOperationException("请刷新验证码。");
		else if (CaptchaCode == null || CaptchaCode.equals("")) {
			throw new IllegalArgumentException("没提供验证码参数");
		} else {
			isCaptchaPass = rand.equals(CaptchaCode);
			if (!isCaptchaPass)
				throw new IllegalAccessError("验证码不正确");
		}

		return isCaptchaPass;
	}
}
/**
 * Copyright 2015 Sp42 frank@ajaxjs.com
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
package com.ajaxjs.web.captcha;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 验证码的简单实现
 * 
 */
public class Captcha {
	/**
	 * 验证码
	 */
	private String code;

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
	 * @return 随机颜色
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
	 * 获取验证码
	 * 
	 * @return 验证码字符串
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 设置验证码
	 * 
	 * @param code 验证码
	 */
	public void setCode(String code) {
		this.code = code;
	}

}
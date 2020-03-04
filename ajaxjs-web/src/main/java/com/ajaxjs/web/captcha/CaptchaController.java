package com.ajaxjs.web.captcha;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.Random;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import com.ajaxjs.mvc.controller.MvcOutput;

/**
 * 验证码的简单实现
 */
@WebServlet("/Captcha")
public class CaptchaController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * 表单中指定的字段名称 或是 SESSION 的键值
	 */
	public static final String CAPTCHA_CODE = "CAPTCHA_CODE";

	/**
	 * GET 請求獲取驗證碼圖片
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		init(response, request.getSession());
	}

	/**
	 * 生成验证码图片
	 * 
	 * @param width     图片宽度
	 * @param height    图片高度
	 * @param randomStr 随机字符串
	 * @return 图片对象
	 */
	public static RenderedImage getCaptcha(int width, int height, String randomStr) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);// 在内存中创建图像
		Graphics g = image.getGraphics(); // 获取图形上下文
		g.setColor(getRandColor(200, 250)); // 设定背景
		g.fillRect(0, 0, width, height);
		g.setFont(new Font("Times New Roman", Font.PLAIN, 18)); // 设定字体
		g.setColor(getRandColor(160, 200));

		Random random = new Random();// 随机产生干扰线
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(width), y = random.nextInt(height);
			int xl = random.nextInt(12), yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}

		String[] arr = randomStr.split("");
		for (int i = 0; i < 4; i++) {
			g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110))); // 将认证码显示到图象中
			g.drawString(arr[i], 13 * i + 6, 16);// 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
		}

		g.dispose();// 图象生效

		return image;
	}

	/**
	 * 随机产生四位验证码
	 * 
	 * @return 四位验证码
	 */
	private static String getRandom() {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();

		for (int i = 0; i < 4; i++)
			sb.append(String.valueOf(random.nextInt(10)));

		return sb.toString();
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
		int r = fc + random.nextInt(bc - fc), g = fc + random.nextInt(bc - fc), b = fc + random.nextInt(bc - fc);

		return new Color(r, g, b);
	}

	/**
	 * 显示验证码图片并将认证码存入 Session
	 * 
	 * @param response 响应对象
	 * @param session  会话对象
	 */
	public static void init(HttpServletResponse response, HttpSession session) {
		String code = getRandom();
		MvcOutput re = response instanceof MvcOutput ? (MvcOutput) response : new MvcOutput(response);
		re.noCache().setContent_Type("image/jpeg").go(getCaptcha(60, 20, code));
		session.setAttribute(CAPTCHA_CODE, code);
	}

	/**
	 * 显示验证码图片并将认证码存入 Session JSP 调用方式：
	 * <%com.ajaxjs.web.captcha.CaptchaController.init(pageContext);%>
	 * 
	 * @param cxt 页面上下文对象
	 */
	public static void init(PageContext cxt) {
		init((HttpServletResponse) cxt.getResponse(), cxt.getSession());
	}
}

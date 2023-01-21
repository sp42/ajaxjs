package com.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;

import sun.font.FontDesignMetrics;
 
/**
 * 可以旋转文字的验证码
 * https://blog.csdn.net/collonn/article/details/41724099
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
public class SecurityCodeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
	private int width = 173;
	private int height = 24;
	private int fontSize = height;
	private int securityCodeLength = 4;
    private int interferingLineCount = 20;
    private Font font = new Font("Times New Romans", Font.BOLD, fontSize);
	private String charStr = "A0KLBMNC2PD3QRE4STF5UVG6WXH7YZ8J9秋花惨淡秋草黄耿耿秋灯秋夜长已觉秋窗秋不尽那堪风雨助凄凉";
	private String[] chars;
	private int charWidth;
	private int charHeight;
 
	@Override
	public void init() throws ServletException {
		try{
			Properties config = new Properties();
			config.load(SecurityCodeServlet.class.getResourceAsStream("/config.properties"));
 
			String withStr = config.getProperty("security.code.width");
			if(StringUtils.hasText(withStr)){
				this.width = Integer.parseInt(withStr);
			}
 
			String securityCodeLengthStr = config.getProperty("security.code.length");
			if(StringUtils.hasText(securityCodeLengthStr)){
				this.securityCodeLength = Integer.parseInt(securityCodeLengthStr);
			}
 
			String interferingLineCountStr = config.getProperty("security.code.interfering.line.count");
			if(StringUtils.hasText(interferingLineCountStr)){
				this.interferingLineCount = Integer.parseInt(interferingLineCountStr);
			}
 
			String fontStr = config.getProperty("security.code.fontStyle");
			if(StringUtils.hasText(fontStr)){
				if(!StringUtils.contains(fontStr, ".ttf"))
					this.font = new Font(fontStr, Font.BOLD, fontSize);
				else
					this.font = registerFont(fontStr);
			}
 
			String charsStr = config.getProperty("security.code.text");
			if(StringUtils.hasText(charsStr))
				this.charStr = charsStr;
			
			this.chars = new String[charsStr.length()];
			for(int i = 0; i < charsStr.length(); i++)
				this.chars[i] = String.valueOf(charsStr.charAt(i));
			
 
			FontMetrics fontMetrics = FontDesignMetrics.getMetrics(this.font);
			this.charWidth = fontMetrics.stringWidth("M");
			this.charHeight = fontMetrics.getHeight();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
 
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}
 
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Random random = new Random();
			BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = buffImg.createGraphics();
			g.setFont(this.font);
 
			//画背景
			g.setColor(this.getRandomColor(random, 200, 55));
			g.fillRect(0, 0, width, height);
			//画边框
			g.setColor(this.getRandomColor(random, 100, 155));
			g.drawRect(0, 0, width-1, height-1);
 
			//画干扰线
			g.setColor(this.getRandomColor(random, 0, 255));
			g.setStroke(new BasicStroke(1f));
			
			for (int i = 0; i < this.interferingLineCount; i++) {
				int x = random.nextInt(this.width);
				int y = random.nextInt(this.height);
				int xl = random.nextInt(this.width);
				int yl = random.nextInt(this.height);
				g.drawLine(x, y, x + xl, y + yl);
			}
 
			//画旋转文字
			int charX = 0;
			List<String> chartList = this.getRandomString(random);
			int charsRealWidth = this.charWidth * this.securityCodeLength;
			if(this.width > charsRealWidth)
				charX = (this.width - charsRealWidth)/2;
			
			double radianPercent = 0D;
			int chartY = this.height - 5;
			
			for(String chart : chartList){
				g.setColor(this.getRandomColor(random, 80, 120));
				radianPercent =  Math.PI * (random.nextInt(60)/180D);
				if(random.nextBoolean()) radianPercent = -radianPercent;
				g.rotate(radianPercent, charX + 9, chartY);
				g.drawString(chart, charX, chartY);
				g.rotate(-radianPercent, charX + 9, chartY);
				charX += this.charWidth;
			}
 
			//释放此图形的上下文以及它使用的所有系统资源
			g.dispose();
 
			//设置response类型
			response.setContentType("image/jpeg");
			//取消缓存
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
 
			//输出图像
			ServletOutputStream os = response.getOutputStream();
			ImageIO.write(buffImg, "jpeg", os);
 
			os.close();
 
			//设置Session，将字符串转换成小写
			request.getSession().setAttribute(AppConst.SECURITY_CODE_SESSION_KEY, StringUtils.join(chartList, "").toLowerCase());
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
 
	private Font registerFont(String fontStr) throws Exception {
		InputStream fontInputStream = SecurityCodeServlet.class.getClassLoader().getResourceAsStream(fontStr);
		Font fontNew = Font.createFont(Font.TRUETYPE_FONT, fontInputStream);
		Font fontNewPt = fontNew.deriveFont(Font.BOLD, this.fontSize);
		fontInputStream.close();
 
		//注意这里，如果不注册文字的话，什么都画不出来
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		ge.registerFont(fontNew);
		
		return fontNewPt;
	}
 
	private List<String> getRandomString(Random random){
		List<String> chartList = new LinkedList<>();
		
		for(int i = 0; i < this.securityCodeLength; i++){
			String character = this.chars[random.nextInt(this.chars.length)];
			character = (random.nextBoolean() == true ? character.toUpperCase() : character.toLowerCase());
			chartList.add(character);
		}
		
		return chartList;
	}
	
	private Color getRandomColor(Random random, int start, int max){
		int r = start + random.nextInt(max);
		int g = start + random.nextInt(max);
		int b = start + random.nextInt(max);
		
		return new Color(r, g, b);
	}
}
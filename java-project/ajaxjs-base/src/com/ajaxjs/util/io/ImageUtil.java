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
package com.ajaxjs.util.io;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 * 图片处理工具
 * 
 */
public class ImageUtil extends FileUtil {
	/**
	 * 图片宽度
	 */
	private int width;
	
	/**
	 * 图片高度
	 */
	private int height;

	/**
	 * 图片对象
	 */
	private Image img;

	/**
	 * BufferedImage是Image的一个子类，BufferedImage生成的图片在内存里有一个图像缓冲区，
	 * 利用这个缓冲区我们可以很方便的操作这个图片，通常用来做图片修改操作如大小变换、图片变灰、设置图片透明或不透明等
	 */
	private BufferedImage bImg;

	/**
	 * 图片类型，默认 jpg
	 */
	private String format = "jpg";

	/**
	 * 保存图片 TODO 其实可以考虑使用 bufferedWrite(InputStream is, OutputStream out) 保存的
	 * 《使用ImageIO.write存储png格式图片性能较差问题》http://zhang-xzhi-xjtu.iteye.com/blog/
	 * 1328084
	 */
	public ImageUtil save() {
		try {
			ImageIO.write(bImg, format, getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return this;
	}

	/**
	 * BufferedImage 转换为 byte[]。
	 * 在传输中，图片是不能直接传的，因此需要把图片变为字节数组，然后传输比较方便。
	 */
	@Override
	public ImageUtil output2byte() {
		ByteArrayOutputStream out = new ByteArrayOutputStream(1024);// 1024 指定缓冲大小
		setOut(out);

		try {
			ImageIO.write(bImg, format, out);
		} catch (IOException e) {
			e.printStackTrace();
		}

		setData(out.toByteArray());

		return this;
	}

	/**
	 * 完成设置图片大小
	 * 
	 * @param newHeight
	 *            高
	 * @param newWidth
	 *            宽
	 * @return 缓冲的图片对象
	 */
	public ImageUtil resize() {
		bImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		bImg.getGraphics().drawImage(img, 0, 0, width, height, null);

		return this;
	}

	/**
	 * 缩放比例
	 * 
	 * @param height
	 *            高
	 * @param width
	 *            宽
	 * @return 缩放比例之后的高宽
	 */
	public int[] resize(int height, int width) {
		int oHeight = img.getHeight(null), oWidth = img.getWidth(null);
		double ratio = (new Integer(oHeight)).doubleValue() / (new Integer(oWidth)).doubleValue();

		if (width != 0) {
			height = (int) (ratio * width);
		} else {
			width = (int) (height / ratio);
		}

		return new int[] { height, width };
	}

	/**
	 * 图片裁切
	 * 
	 * @param x
	 *            选择区域左上角的x坐标
	 * @param y
	 *            选择区域左上角的y坐标
	 * @param width
	 *            选择区域的宽度
	 * @param height
	 *            选择区域的高度
	 */
	public ImageUtil cut(int x, int y, int width, int height) {
		try {
			setIn(new FileInputStream(getFile()));
			
			try(ImageInputStream in = ImageIO.createImageInputStream(getIn());) {
				
				String fileSuffix = FileUtil.getFileSuffix(getFilePath());
				Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(fileSuffix);
				ImageReader reader = it.next();
				reader.setInput(in, true);
				
				ImageReadParam param = reader.getDefaultReadParam();
				param.setSourceRegion(new Rectangle(x, y, width, height));
				
				bImg = reader.read(0, param);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return this;
	}

	/**
	 * 为图片添加水印文字
	 * 
	 * @param watermark
	 *            水印文字
	 */
	public ImageUtil mark(String watermark) {
		// 读取原图片信息
		int width = img.getWidth(null), height = img.getHeight(null);

		// 加水印
		bImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bImg.createGraphics();
		g.drawImage(img, 0, 0, width, height, null);
		g.setColor(Color.white); // 根据图片的背景设置水印颜色

		Font font = new Font("楷书", Font.PLAIN, 15);
		g.setFont(font);

		// int x = (srcImgWidth - getWatermarkLength(watermarkStr, g)) / 1;
		// int y = srcImgHeight / 1;

		FontMetrics fm = g.getFontMetrics(font);
		// 设置换行操作
		int fontHeight = fm.getHeight(); // 字符的高度
		int offsetLeft = 30, rowIndex = 12;

		for (int i = 0; i < watermark.length(); i++) {
			char c = watermark.charAt(i);
			int charWidth = fm.charWidth(c); // 字符的宽度

			// 另起一行
			if (Character.isISOControl(c) || offsetLeft >= (width - charWidth)) {
				rowIndex++;
				offsetLeft = 16;
			}

			g.drawString(String.valueOf(c), offsetLeft, rowIndex * fontHeight); // 把一个个写到图片上
			offsetLeft += charWidth; // 设置下字符的间距
		}
		// g.drawString(watermarkStr, x+10, y-5);

		g.dispose();

		return this;
	}

	/**
	 * 为图片添加水印图片
	 * 
	 * @param watermark
	 *            水印文件
	 */
	public ImageUtil mark(File watermark) {
		Image watermarkImg;

		try {
			watermarkImg = ImageIO.read(watermark);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		int width = img.getWidth(null), height = img.getHeight(null), w_width = watermarkImg.getWidth(null),
				w_height = watermarkImg.getHeight(null);

		bImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = bImg.createGraphics();
		g.drawImage(img, 0, 0, width, height, null);
		g.drawImage(watermarkImg, width - w_width, height - w_height, w_width, w_height, null);// 水印文件在源文件的右下角
		g.dispose();

		return this;
	}

	// 获取水印文字总长度
	// private static int getWatermarkLength(String str, Graphics2D g) {
	// return g.getFontMetrics(g.getFont()).charsWidth(str.toCharArray(), 0,
	// str.length());
	// }

	/**
	 * 获取宽度
	 * @return 宽度
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            宽度
	 */
	public ImageUtil setWidth(int width) {
		this.width = width;
		return this;
	}

	/**
	 * 获取高度
	 * @return 高度
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height
	 *            高度
	 */
	public ImageUtil setHeight(int height) {
		this.height = height;
		return this;
	}

	/**
	 * 读取图片流
	 * 
	 * @return 图片流
	 */
	public Image getImg() {
		return img;
	}

	/**
	 * 设置图片流
	 * 
	 * @param img
	 *            图片流
	 */
	public ImageUtil setImg(Image img) {
		this.img = img;
		return this;
	}

	/**
	 * @return the bImg
	 */
	public BufferedImage getbImg() {
		return bImg;
	}

	/**
	 * @param bImg
	 *            the bImg to set
	 */
	public ImageUtil setbImg(BufferedImage bImg) {
		this.bImg = bImg;

		return this;
	}

	@Override
	public ImageUtil setFilePath(String filePath) {
		super.setFilePath(filePath);
		setFile(getFile()); // 同时设 File 对象，但有点多余
		return this;
	}

	@Override
	public ImageUtil setFile(File file) {
		super.setFile(file);

		try {
			img = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return this;
	}

	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * @param format
	 *            the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}
	
    /**
	 * 打开URL对应的网页并保存为图片
	 * 程序运行时用户不能有其它操作，否则可能保存错误截屏。 这里假设加载一个网页时间最长为8秒.
	 * @param svaefile
	 */
	public static void webscreenCut(String svaefile) {
		if (!Desktop.isDesktopSupported()) {
			System.err.println("Desktop is not supported (fatal)");
			return;
		}

		Desktop desktop = Desktop.getDesktop();
		if (!desktop.isSupported(Desktop.Action.BROWSE)) {
			System.err.println("Desktop doesn't support the browse action (fatal)");
			return;
		}

		try {
			desktop.browse(URI.create("http://www.csdn.net"));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		try {
			Thread.sleep(8000); // 8 seconds is enough to load the any page.
		} catch (InterruptedException e) {
			e.printStackTrace();
			return;
		} 
		
		// Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize() );
		BufferedImage image = null;
		
		try {
			image = new Robot().createScreenCapture(new Rectangle(300, 90, 1000, 720));
		} catch (AWTException e) {
			e.printStackTrace();
			return;
		}
		
//		if(image != null) Image.saveImgfile(svaefile, image, "jpg");
	}
}

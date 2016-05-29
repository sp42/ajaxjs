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
package com.ajaxjs.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 * 图片处理工具
 * 
 */
public class Img extends IO {
	/**
	 * 读取图片流
	 * 
	 * @param imgPath
	 *            图片路径
	 * @return
	 */
	public static Image readImg(File imgPath) {
		try {
			return ImageIO.read(imgPath);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 读取图片流（重载版本）
	 * 
	 * @param imgPath
	 *            图片路径
	 * @return
	 */
	public static Image readImg(String imgPath) {
		return readImg(new File(imgPath));
	}

	/**
	 * 读取图片流（重载版本）
	 * 
	 * @param in
	 * @return
	 */
	public static Image readImg(InputStream in) {
		try {
			return ImageIO.read(in);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * 完成设置图片大小
	 * 
	 * @param img
	 *            图片对象
	 * @param newHeight
	 *            高
	 * @param newWidth
	 *            宽
	 * @return 缓冲的图片对象
	 */
	public static BufferedImage setResize(Image img, int newHeight, int newWidth) {
		BufferedImage newImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
		newImg.getGraphics().drawImage(img, 0, 0, newWidth, newHeight, null);
		
		return newImg;
	}
	
	public static boolean setResize(String src, String filePathname, int newHeight, int newWidth) {
		setResize(readImg(src), newHeight, newWidth); 
		saveImgfile(filePathname, setResize(readImg(src), newHeight, newWidth), "jpg");
		return true;
	}

	/**
	 * 缩放比例
	 * 
	 * @param img
	 *            图片对象
	 * @param height
	 *            高
	 * @param width
	 *            宽
	 * @return
	 */
	public static int[] resize(Image img, int height, int width) {
		int oHeight = img.getHeight(null), oWidth = img.getWidth(null);
		double ratio = (new Integer(oHeight)).doubleValue() / (new Integer(oWidth)).doubleValue();
		
		if(width != 0) {
			height =  (int) (ratio * width); 
		}else {
			width = (int) ( height / ratio);
		} 
		
		return new int[]{height, width};
	}
	
	/**
	 * 保存图片 TODO 其实可以考虑使用 bufferedWrite(InputStream is, OutputStream out) 保存的
	 * 《使用ImageIO.write存储png格式图片性能较差问题》http://zhang-xzhi-xjtu.iteye.com/blog/
	 * 1328084
	 * 
	 * @param filePathName
	 *            图片路径
	 * @param img
	 *            图片对象
	 * @param imgType
	 *            图片类型
	 */
	public static boolean saveImgfile(String filePathName, BufferedImage img, String imgType) {
		try (FileOutputStream outImgStream = new FileOutputStream(filePathName);) {
			return ImageIO.write(img, imgType, outImgStream);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 重载版本
	 * 
	 * @param imgPath
	 *            图片路径
	 * @param bufImg
	 * @param imgType
	 */
	public static boolean saveImgfile(File imgPath, BufferedImage bufImg, String imgType) {
		try {
			return ImageIO.write(bufImg, imgType, imgPath);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * BufferedImage 转换为 byte[]
	 * 
	 * @param image
	 *            图片文件名
	 * @param format
	 * @return
	 */
	public static byte[] bufferedImage2bytes(BufferedImage image, String format) {
		try (ByteArrayOutputStream bout = new ByteArrayOutputStream(1024)) { // 指定缓冲大小
			ImageIO.write(image, format, bout);
			return bout.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 图片裁切
	 * 
	 * @param x1
	 *            选择区域左上角的x坐标
	 * @param y1
	 *            选择区域左上角的y坐标
	 * @param width
	 *            选择区域的宽度
	 * @param height
	 *            选择区域的高度
	 * @param src
	 *            源图片路径
	 * @param out
	 *            裁切后图片的保存路径
	 */
	public static void cut(String src, String out, int x1, int y1, int width, int height) {
		String fileSuffix = getFileSuffix(src);
		BufferedImage bufImg = null;

		try (FileInputStream is = new FileInputStream(src);
			ImageInputStream iis = ImageIO.createImageInputStream(is);) {
			Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(fileSuffix);
			ImageReader reader = it.next();
			reader.setInput(iis, true);

			ImageReadParam param = reader.getDefaultReadParam();
			param.setSourceRegion(new Rectangle(x1, y1, width, height));

			bufImg = reader.read(0, param);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (bufImg != null)
			saveImgfile(new File(out == null ? src : out), bufImg, fileSuffix);
	}

	/**
	 * 为图片添加水印文字
	 * 
	 * @param src
	 *            原图位置
	 * @param out
	 *            输出图片位置。如果为 null 覆盖原文件
	 * @param watermark
	 *            水印文字
	 */
	public static void mark(String src, String out, String watermark) {
		// 读取原图片信息
		Image srcImg = readImg(src);
		int width = srcImg.getWidth(null), height = srcImg.getHeight(null);

		// 加水印
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = img.createGraphics();
		g.drawImage(srcImg, 0, 0, width, height, null);
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

		// 输出图片
		saveImgfile(out == null ? src : out, img, "jpg");
	}

	/**
	 * 为图片添加水印图片
	 * 
	 * @param src
	 *            要添加的图片
	 * @param out
	 *            如果为 null 覆盖原文件
	 * @param watermark
	 *            水印文件
	 */
	public static void mark(String src, String out, File watermark) {
		Image srcImg = readImg(src), watermarkImg = readImg(watermark);

		int width = srcImg.getWidth(null), height = srcImg.getHeight(null),
			widthWaterMark = watermarkImg.getWidth(null), heightWaterMark = watermarkImg.getHeight(null);

		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = img.createGraphics();
		
		g.drawImage(srcImg, 0, 0, width, height, null);
		// 水印文件在源文件的右下角
		g.drawImage(watermarkImg, width - widthWaterMark, height - heightWaterMark, widthWaterMark, heightWaterMark, null);
		g.dispose();

		// 输出图片
		saveImgfile(out == null ? src : out, img, "jpg");
	}


	// 获取水印文字总长度
	// private static int getWatermarkLength(String str, Graphics2D g) {
	// return g.getFontMetrics(g.getFont()).charsWidth(str.toCharArray(), 0,
	// str.length());
	// }
}

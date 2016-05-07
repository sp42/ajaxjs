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
package com.ajaxjs.util.stream;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
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

import com.ajaxjs.util.IO;

/**
 * 图片处理工具
 * 
 */
public class Image extends IO {
	/**
	 * 读取图片流
	 * 
	 * @param imgPath
	 *            图片路径
	 * @return
	 */
	public static java.awt.Image readImg(File imgPath) {
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
	public static java.awt.Image readImg(String imgPath) {
		return readImg(new File(imgPath));
	}

	/**
	 * 读取图片流（重载版本）
	 * 
	 * @param in
	 * @return
	 */
	public static java.awt.Image readImg(InputStream in) {
		try {
			return ImageIO.read(in);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 保存图片 TODO 其实可以考虑使用 bufferedWrite(InputStream is, OutputStream out) 保存的
	 * 《使用ImageIO.write存储png格式图片性能较差问题》http://zhang-xzhi-xjtu.iteye.com/blog/1328084
	 * 
	 * @param imgPath 图片路径
	 * @param bufImg
	 * @param imgType
	 */
	public static boolean saveImgfile(String imgPath, BufferedImage bufImg, String imgType) {
		try (FileOutputStream outImgStream = new FileOutputStream(imgPath);) {
			return ImageIO.write(bufImg, imgType, outImgStream);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 重载版本
	 * 
	 * @param imgPath 图片路径
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
	 * 图片高度宽度
	 * 
	 * @param filename
	 * @return
	 */
	public static int[] getImgSize(String filename) {
		java.awt.Image img = readImg(filename);

		return new int[] { img.getHeight(null), img.getHeight(null) };
	}

	/**
	 * BufferedImage 转换为 byte[]
	 * 
	 * @param image
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
	 * 修改图片大小
	 * 
	 * @param srcImgPath
	 * @param outImgPath
	 *            如果为 null 覆盖原文件
	 * @param new_w
	 * @param new_h
	 */
	public static void modiflySize(String srcImgPath, String outImgPath, int new_w, int new_h) {
		BufferedImage bufImg = new BufferedImage(new_w, new_h, BufferedImage.TYPE_INT_RGB);
		bufImg.getGraphics().drawImage(readImg(srcImgPath), 0, 0, new_w, new_h, null);

		// 输出图片
		saveImgfile(outImgPath == null ? srcImgPath : outImgPath, bufImg, "jpg");
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
	 * @param srcImgPath
	 *            源图片路径
	 * @param outImgPath
	 *            裁切后图片的保存路径
	 */
	public static void cut(String srcImgPath, String outImgPath, int x1, int y1, int width, int height) {
		String fileSuffix = getFileSuffix(srcImgPath);
		BufferedImage bufImg = null;

		try (FileInputStream is = new FileInputStream(srcImgPath);
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
			saveImgfile(new File(outImgPath == null ? srcImgPath : outImgPath), bufImg, fileSuffix);
	}

	/**
	 * 为图片添加水印文字
	 * 
	 * @param srcImgPath
	 *            原图位置
	 * @param outImgPath
	 *            输出图片位置。如果为 null 覆盖原文件
	 * @param watermarkStr
	 *            水印文字
	 */
	public static void mark(String srcImgPath, String outImgPath, String watermarkStr) {
		// 读取原图片信息
		java.awt.Image srcImg = readImg(srcImgPath);
		int srcImgWidth = srcImg.getWidth(null);
		int srcImgHeight = srcImg.getHeight(null);

		// 加水印
		BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bufImg.createGraphics();
		g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
		g.setColor(Color.white); // 根据图片的背景设置水印颜色

		// Font font = new Font("微软雅黑", Font.PLAIN, 18);
		Font font = new Font("楷书", Font.PLAIN, 15);
		g.setFont(font);

		// int x = (srcImgWidth - getWatermarkLength(watermarkStr, g)) / 1;
		// int y = srcImgHeight / 1;

		// ------------------------------------------------------------------
		FontMetrics fm = g.getFontMetrics(font);
		// 设置换行操作
		int fontHeight = fm.getHeight(); // 字符的高度
		int offsetLeft = 30;
		int rowIndex = 12;
		for (int i = 0; i < watermarkStr.length(); i++) {
			char c = watermarkStr.charAt(i);
			int charWidth = fm.charWidth(c); // 字符的宽度

			// 另起一行
			if (Character.isISOControl(c) || offsetLeft >= (srcImgWidth - charWidth)) {
				rowIndex++;
				offsetLeft = 16;
			}

			g.drawString(String.valueOf(c), offsetLeft, rowIndex * fontHeight); // 把一个个写到图片上
			offsetLeft += charWidth; // 设置下字符的间距
		}
		// ------------------------------------------------------------------
		// g.drawString(watermarkStr, x+10, y-5);

		g.dispose();

		// 输出图片
		saveImgfile(outImgPath == null ? srcImgPath : outImgPath, bufImg, "jpg");
	}

	/**
	 * 为图片添加水印图片
	 * 
	 * @param srcImgPath
	 * @param outImgPath
	 *            如果为 null 覆盖原文件
	 * @param watermarkImg
	 *            水印文件
	 */
	public static void mark(String srcImgPath, String outImgPath, File watermarkImg) {
		java.awt.Image imageOriginal = readImg(srcImgPath), imageWaterMark = readImg(watermarkImg);

		int widthOriginal = imageOriginal.getWidth(null), heightOriginal = imageOriginal
				.getHeight(null), widthWaterMark = imageWaterMark
				.getWidth(null), heightWaterMark = imageWaterMark
				.getHeight(null);

		BufferedImage bufImg = new BufferedImage(widthOriginal, heightOriginal, BufferedImage.TYPE_INT_RGB);
		Graphics g = bufImg.createGraphics();
		g.drawImage(imageOriginal, 0, 0, widthOriginal, heightOriginal, null);
		// 水印文件在源文件的右下角
		g.drawImage(imageWaterMark, widthOriginal - widthWaterMark,
				heightOriginal - heightWaterMark, widthWaterMark,
				heightWaterMark, null);
		g.dispose();

		// 输出图片
		saveImgfile(outImgPath == null ? srcImgPath : outImgPath, bufImg, "jpg");
	}

	// 获取水印文字总长度
	// private static int getWatermarkLength(String str, Graphics2D g) {
	// return g.getFontMetrics(g.getFont()).charsWidth(str.toCharArray(), 0,
	// str.length());
	// }
}

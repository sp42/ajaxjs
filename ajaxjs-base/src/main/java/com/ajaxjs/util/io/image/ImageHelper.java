package com.ajaxjs.util.io.image;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.ajaxjs.util.io.FileHelper;

/**
 * 图片助手类
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class ImageHelper {

	/**
	 * 根据文件名获取图片对象
	 * 
	 * @param filePath
	 *            磁盘上文件名
	 * @return 图片对象
	 */
	public static BufferedImage getImg(String filePath) {
		try {
			return ImageIO.read(new File(filePath));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 按照一定范围控制图片的高宽
	 * 
	 * @param originWidth
	 *            图片原始尺寸
	 * @param originHeight
	 * @param maxWidth
	 *            最大尺寸限制
	 * @param maxHeight
	 * @return 0=宽、1=高
	 */
	public static int[] resize(Integer originWidth, Integer originHeight, Integer maxWidth, Integer maxHeight) {
		// 目标尺寸
		int targetWidth = originWidth, targetHeight = originHeight;

		// 图片尺寸超过400x400的限制
		if (originWidth > maxWidth || originHeight > maxHeight) {
			if (originWidth / originHeight > maxWidth / maxHeight) {
				// 更宽，按照宽度限定尺寸
				targetWidth = maxWidth;
				targetHeight = Math.round(maxWidth * (originHeight.floatValue() / originWidth.floatValue()));
			} else {
				targetHeight = maxHeight;
				targetWidth = Math.round(maxHeight * (originWidth.floatValue() / originHeight.floatValue()));
			}
		}

		return new int[] { targetWidth, targetHeight };
	}

	/**
	 * 拉伸图片到指定尺寸
	 * 
	 * @param img
	 *            图片对象
	 * @param width
	 *            宽
	 * @param height
	 *            高
	 * @return
	 */
	public static BufferedImage resize(BufferedImage img, int width, int height) {
		BufferedImage sImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		sImg.getGraphics().drawImage(img, 0, 0, width, height, null);

		return sImg;
	}

	/**
	 * 修正 jpg 输出红色一层的问题
	 * 
	 * @param img
	 *            图片对象
	 * @return 图片对象
	 */
	public static BufferedImage fixICC(BufferedImage img) {
		int w = img.getWidth();
		int h = img.getHeight();

		BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		int[] rgb = img.getRGB(0, 0, w, h, null, 0, w);
		newImage.setRGB(0, 0, w, h, rgb, 0, w);

		return newImage;
	}

	/**
	 * 加入边框
	 * 
	 * @param image
	 * @param borderWidth
	 *            边框宽度
	 * @param borderColor
	 *            边框颜色
	 * @return
	 */
	public static BufferedImage addBorder(BufferedImage image, int borderWidth, Color borderColor) {
		int h = image.getHeight(), w = image.getWidth();

		Graphics2D g = image.createGraphics();
		BufferedImage imageNew = g.getDeviceConfiguration().createCompatibleImage(w + borderWidth * 2, h + borderWidth * 2);// 最终高宽
		g.dispose();
		g = imageNew.createGraphics();
		g.setBackground(borderColor);
		g.drawImage(image, borderWidth, borderWidth, w, h, null);

		// 添加边框
		g.setStroke(new BasicStroke(borderWidth));
		g.setColor(Color.white);
		g.drawRect(borderWidth / 2, borderWidth / 2, w + borderWidth, h + borderWidth);
		g.dispose();

		return imageNew;
	}

	/**
	 * 保存图片文件
	 * 
	 * @param img
	 *            图片对象
	 * @param fileName
	 *            保持的文件路径
	 */
	public static void saveFile(BufferedImage img, String fileName) {
		saveFile(img, new File(fileName), false);
	}

	/**
	 * 保存图片文件
	 * 
	 * @param img
	 *            图片对象
	 * @param file
	 *            保存的文件对象
	 */
	public static void saveFile(BufferedImage img, File file) {
		saveFile(img, file, false);
	}

	/**
	 * 保存图片文件
	 * 
	 * @param img
	 *            图片对象
	 * @param fileName
	 *            保持的文件路径
	 * @param isFixICC
	 *            是否修复 jpg 输出红色一层的问题
	 */
	public static void saveFile(BufferedImage img, String fileName, boolean isFixICC) {
		saveFile(img, new File(fileName), isFixICC);
	}

	/**
	 * 保存图片文件
	 * 
	 * @param img
	 *            图片对象
	 * @param file
	 *            保存的文件对象
	 * @param isFixICC
	 *            是否修复 jpg 输出红色一层的问题
	 */
	public static void saveFile(BufferedImage img, File file, boolean isFixICC) {
		String fileName = file.getName();
		String ext = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		FileHelper.mkDir(file.getPath()); // 如果目录不存在，先创建

		try {
			ImageIO.write(isFixICC ? fixICC(img) : img, ext, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 旋转图片
	 * 
	 * @param img
	 *            图片对象
	 * @param angel
	 *            旋转角度
	 * @return 图片对象
	 */
	public static BufferedImage rotate(Image img, int angel) {
		int src_width = img.getWidth(null);
		int src_height = img.getHeight(null);

		// calculate the new image size  
		Rectangle rect_des = calcRotatedSize(new Rectangle(new Dimension(src_width, src_height)), angel);
		BufferedImage res = new BufferedImage(rect_des.width, rect_des.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = res.createGraphics();
		g2.translate((rect_des.width - src_width) / 2, (rect_des.height - src_height) / 2);// transform  
		g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);
		g2.drawImage(img, null, null);

		return res;
	}

	/**
	 * 
	 * @param src
	 * @param angel
	 *            旋转角度
	 * @return
	 */
	public static Rectangle calcRotatedSize(Rectangle src, int angel) {
		// if angel is greater than 90 degree, we need to do some conversion  
		if (angel >= 90) {
			if (angel / 90 % 2 == 1) {
				int temp = src.height;
				src.height = src.width;
				src.width = temp;
			}

			angel = angel % 90;
		}

		double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
		double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
		double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
		double angel_dalta_width = Math.atan((double) src.height / src.width);
		double angel_dalta_height = Math.atan((double) src.width / src.height);

		int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_width));
		int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_height));
		int des_width = src.width + len_dalta_width * 2;
		int des_height = src.height + len_dalta_height * 2;

		return new Rectangle(new Dimension(des_width, des_height));
	}
}

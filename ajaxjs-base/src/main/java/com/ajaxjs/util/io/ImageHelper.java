package com.ajaxjs.util.io;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.ajaxjs.util.logger.LogHelper;

/**
 * 图片助手类
 * 
 * BufferedImage是Image的一个子类，BufferedImage生成的图片在内存里有一个图像缓冲区，利用这个缓冲区我们可以很方便的操作这个图片，通常用来做图片修改操作如大小变换、图片变灰、设置图片透明或不透明等
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class ImageHelper {
	private static final LogHelper LOGGER = LogHelper.getLog(ImageHelper.class);

	public File file;

	public int width;

	public int height;

	/**
	 * 提供一些方便的属性
	 * 
	 * @param filePath 图片文件完整路径
	 */
	public ImageHelper(String filePath) {
		file = new File(filePath);
		Integer[] arr = getBufferedImgSize.apply(getImg(file));
		width = arr[0];
		height = arr[1];
	}
	
	public ImageHelper(byte[] data) {
		Integer[] arr = getBufferedImgSize.apply(getImg(data));
		width = arr[0];
		height = arr[1];
	}

	/**
	 * 根据文件名获取图片对象
	 * 
	 * @param filePath 图片文件完整路径
	 * @return 图片对象
	 */
	public static BufferedImage getImg(String filePath) {
		return getImg(new File(filePath));
	}

	/**
	 * 根据 文件对象获取图片对象
	 * 
	 * @param file 文件对象
	 * @return 图片对象
	 */
	public static BufferedImage getImg(File file) {
		try {
			return ImageIO.read(file);
		} catch (IOException e) {
			LOGGER.warning(e);
			return null;
		}
	}

	/**
	 * 根据字节获取图片对象
	 * 
	 * @param file 文件对象
	 * @return 图片对象
	 */
	public static BufferedImage getImg(byte[] data) {
		try (ByteArrayInputStream in = new ByteArrayInputStream(data);) {
			return ImageIO.read(in);
		} catch (IOException e) {
			LOGGER.warning(e);
			return null;
		}
	}

	/**
	 * BufferedImage 转换为 byte[]。 在传输中，图片是不能直接传的，因此需要把图片变为字节数组，然后传输比较方便。
	 * 
	 * @param bImg   缓冲图片对象
	 * @param format 图片格式，例如 "jpg"
	 * @return 图片字节码
	 */
	public static byte[] img2byte(BufferedImage bImg, String format) {
		ByteArrayOutputStream out = new ByteArrayOutputStream(IoHelper.BUFFER_SIZE);// 1024 指定缓冲大小

		try {
			ImageIO.write(bImg, format, out);
		} catch (IOException e) {
			LOGGER.warning(e);
			return null;
		}

		return out.toByteArray();
	}

	/**
	 * 缓冲图片对象工厂函数
	 */
	public static final BiFunction<Integer, Integer, BufferedImage> getBufferedImg = (width,
			height) -> new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

	/**
	 * 获取图片对象分辨率大小。 数组中 0 = width 1 = height
	 */
	public static final Function<Image, Integer[]> getImgSize = img -> new Integer[] { img.getWidth(null),
			img.getHeight(null) };

	/**
	 * 获取缓冲图片对象分辨率大小。 数组中 0 = width 1 = height
	 */
	public static final Function<BufferedImage, Integer[]> getBufferedImgSize = img -> new Integer[] { img.getWidth(),
			img.getHeight() };

	/**
	 * 按照一定范围控制图片的高宽
	 * 
	 * @param originWidth  图片原始尺寸
	 * @param originHeight
	 * @param maxWidth     最大尺寸限制
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
	 * 缩放比例
	 * 
	 * @param img    图片对象
	 * @param height
	 * @param width
	 * @return 缩放比例之后的高宽
	 */
	public int[] resize(Image img, int height, int width) {
		Integer[] arr = getImgSize.apply(img);
		int oWidth = arr[0], oHeight = arr[1];
		double ratio = (new Integer(oHeight)).doubleValue() / (new Integer(oWidth)).doubleValue();

		if (width != 0) {
			height = (int) (ratio * width);
		} else {
			width = (int) (height / ratio);
		}

		return new int[] { height, width };
	}

	/**
	 * 拉伸图片到指定尺寸
	 * 
	 * @param img    缓冲图片对象
	 * @param width  宽
	 * @param height 高
	 * @return 指定尺寸的缓冲图片对象
	 */
	public static BufferedImage resizeImg(Image img, int width, int height) {
		BufferedImage sImg = getBufferedImg.apply(width, height);
		sImg.getGraphics().drawImage(img, 0, 0, width, height, null);

		return sImg;
	}

	/**
	 * 修正 jpg 输出红色一层的问题
	 * 
	 * @param bImg 缓冲图片对象
	 * @return 缓冲图片对象
	 */
	public static BufferedImage fixICC(BufferedImage bImg) {
		Integer[] arr = getBufferedImgSize.apply(bImg);
		int w = arr[0], h = arr[1];

		int[] rgb = bImg.getRGB(0, 0, w, h, null, 0, w);
		BufferedImage _bImg = getBufferedImg.apply(w, h);
		_bImg.setRGB(0, 0, w, h, rgb, 0, w);

		return _bImg;
	}

	/**
	 * 加入边框
	 * 
	 * @param bImg        缓冲图片对象
	 * @param borderWidth 边框宽度
	 * @param borderColor 边框颜色
	 * @return 加入边框的图片
	 */
	public static BufferedImage addBorder(BufferedImage bImg, int borderWidth, Color borderColor) {
		Integer[] arr = getBufferedImgSize.apply(bImg);
		int w = arr[0], h = arr[1];

		Graphics2D g = bImg.createGraphics();
		BufferedImage imageNew = g.getDeviceConfiguration().createCompatibleImage(w + borderWidth * 2,
				h + borderWidth * 2);// 最终高宽
		g.dispose();
		g = imageNew.createGraphics();
		g.setBackground(borderColor);
		g.drawImage(bImg, borderWidth, borderWidth, w, h, null);

		// 添加边框
		g.setStroke(new BasicStroke(borderWidth));
		g.setColor(Color.white);
		g.drawRect(borderWidth / 2, borderWidth / 2, w + borderWidth, h + borderWidth);
		g.dispose();

		return imageNew;
	}

	/**
	 * 保存图片文件。 TODO 其实可以考虑使用 bufferedWrite(InputStream is, OutputStream out) 保存的
	 * 《使用ImageIO.write存储png格式图片性能较差问题》http://zhang-xzhi-xjtu.iteye.com/blog/1328084
	 * 
	 * @param bImg   缓冲图片对象
	 * @param format 图片格式，例如 "jpg"
	 * @param file   图片文件对象
	 * @return 是否操作成功
	 */
	public static boolean save(BufferedImage bImg, String format, File file) {
		try {
			return ImageIO.write(bImg, format, file);
		} catch (IOException e) {
			LOGGER.warning(e);
			return false;
		}
	}

	/**
	 * 保存图片文件
	 * 
	 * @param bImg     缓冲图片对象
	 * @param file     保存的文件对象
	 * @param isFixICC 是否修复 jpg 输出红色一层的问题
	 */
	public static void save(BufferedImage bImg, File file, boolean isFixICC) {
		String fileName = file.getName();
		String ext = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		FileHelper.mkDirByFileName(file.getPath()); // 如果目录不存在，先创建

		save(isFixICC ? fixICC(bImg) : bImg, ext, file);
	}

	/**
	 * 保存图片文件
	 * 
	 * @param bImg     缓冲图片对象
	 * @param filePath 保存的文件路径
	 */
	public static void save(BufferedImage bImg, String filePath) {
		save(bImg, new File(filePath), false);
	}

	/**
	 * 保存图片文件
	 * 
	 * @param bImg 缓冲图片对象
	 * @param file 保存的文件对象
	 */
	public static void save(BufferedImage bImg, File file) {
		save(bImg, file, false);
	}

	/**
	 * 保存图片文件
	 * 
	 * @param bImg     缓冲图片对象
	 * @param filePath 保存的文件路径
	 * @param isFixICC 是否修复 jpg 输出红色一层的问题
	 */
	public static void save(BufferedImage bImg, String filePath, boolean isFixICC) {
		save(bImg, new File(filePath), isFixICC);
	}

	/**
	 * 旋转图片
	 * 
	 * @param img   图片对象
	 * @param angel 旋转角度
	 * @return 缓冲图片对象
	 */
	public static BufferedImage rotate(Image img, int angel) {
		Integer[] arr = getImgSize.apply(img);
		int width = arr[0], height = arr[1];

		// calculate the new image size
		Rectangle rect_des = calcRotatedSize(new Rectangle(new Dimension(width, height)), angel);
		BufferedImage bImg = getBufferedImg.apply(rect_des.width, rect_des.height);

		Graphics2D g = bImg.createGraphics();
		g.translate((rect_des.width - width) / 2, (rect_des.height - height) / 2);// transform
		g.rotate(Math.toRadians(angel), width / 2, height / 2);
		g.drawImage(img, null, null);

		return bImg;
	}

	/**
	 * 
	 * @param src
	 * @param angel 旋转角度
	 * @return 矩形
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

	/**
	 * 为图片添加水印图片
	 * 
	 * @param img       图片对象
	 * @param watermark 水印文件
	 * @return 缓冲图片对象
	 */
	public static BufferedImage mark(Image img, File watermark) {
		Image watermarkImg = getImg(watermark);

		Integer[] arr = getImgSize.apply(img), wArr = getImgSize.apply(watermarkImg);
		int width = arr[0], height = arr[1], w_width = wArr[0], w_height = wArr[1];

		BufferedImage bImg = getBufferedImg.apply(width, height);
		Graphics g = bImg.createGraphics();
		g.drawImage(img, 0, 0, width, height, null);
		g.drawImage(watermarkImg, width - w_width, height - w_height, w_width, w_height, null);// 水印文件在源文件的右下角
		g.dispose();

		return bImg;
	}

	/**
	 * 图片裁切
	 * 
	 * @param x      选择区域左上角的x坐标
	 * @param y      选择区域左上角的y坐标
	 * @param width  选择区域的宽度
	 * @param height 选择区域的高度
	 * @return 缓冲图片对象
	 */
	public static BufferedImage cut(String filePath, int x, int y, int width, int height) {
		Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(FileHelper.getFileSuffix(filePath));
		ImageReader reader = it.next();

		try (ImageInputStream in = ImageIO.createImageInputStream(new FileInputStream(filePath));) {
			reader.setInput(in, true);
			ImageReadParam param = reader.getDefaultReadParam();
			param.setSourceRegion(new Rectangle(x, y, width, height));

			return reader.read(0, param);
		} catch (IOException e) {
			LOGGER.warning(e);
			return null;
		}
	}

	/**
	 * 为图片添加水印文字
	 * 
	 * @param img       图片对象
	 * @param watermark 水印文字
	 * @return 缓冲图片对象
	 */
	public static BufferedImage mark(Image img, String watermark) {
		// 读取原图片信息
		Integer[] arr = getImgSize.apply(img);
		int width = arr[0], height = arr[1];

		// 加水印
		BufferedImage bImg = getBufferedImg.apply(width, height);
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

		return bImg;
	}
}

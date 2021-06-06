package com.ajaxjs.util.io;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

public class ImageUtil {
	/**
	 * 判断文件是否为图片
	 * 在上传图片文件的时候除了需要限制文件的大小，通常还需要对文件类型进行判断。因为用户可能会上传任何东西上来，如果被有心人上传木马到你服务器那就麻烦了。
	 * 
	 * 该方法适用的图片格式为：bmp/gif/jpg/png
	 * 
	 * @param file
	 * @return
	 */
	public static boolean checkIfIsImage(File file) {
		try {
			return ImageIO.read(file) != null;
		} catch (IOException ex) {
			return false;
		}
	}

	/**
	 * 对图片进行旋转（无损）
	 *
	 * @param src   被旋转图片
	 * @param angel 旋转角度
	 * @return 旋转后的图片
	 */
	public static BufferedImage Rotate(Image src, int angel) {
		int src_width = src.getWidth(null);
		int src_height = src.getHeight(null);

		// 计算旋转后图片的尺寸
		Rectangle rect_des = calcRotatedSize(new Rectangle(new Dimension(src_width, src_height)), angel);
		BufferedImage res = null;

		res = new BufferedImage(rect_des.width, rect_des.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = res.createGraphics();
		// 进行转换
		g2.translate((rect_des.width - src_width) / 2, (rect_des.height - src_height) / 2);
		g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);
		g2.drawImage(src, null, null);

		return res;
	}

	/**
	 * 计算旋转后的图片
	 *
	 * @param src   被旋转的图片
	 * @param angel 旋转角度
	 * @return 旋转后的图片
	 */
	public static Rectangle calcRotatedSize(Rectangle src, int angel) {
		// 如果旋转的角度大于90度做相应的转换
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
	 * 压缩图片
	 * 
	 * @param srcFilePath
	 * @param descFilePath
	 * @param quality
	 * @throws IOException
	 */
	public static void compressPic(String srcFilePath, String descFilePath, Float quality) throws IOException {
		File input = new File(srcFilePath);
		BufferedImage image = ImageIO.read(input);
		ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();// 指定写图片的方式为 jpg

		// 先指定 Output，才能调用 writer.write 方法
		File output = new File(descFilePath);
		OutputStream out = new FileOutputStream(output);
		ImageOutputStream ios = ImageIO.createImageOutputStream(out);
		writer.setOutput(ios);

		ImageWriteParam param = writer.getDefaultWriteParam();

		if (param.canWriteCompressed()) {
			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);// 指定压缩方式为 MODE_EXPLICIT
			param.setCompressionQuality(quality);// 压缩程度，参数qality是取值0~1范围内
		}

		writer.write(null, new IIOImage(image, null, null), param);// 调用write方法，向输入流写图片

		out.close();
		ios.close();

		writer.dispose();
	}

	/**
	 * 编码 JPG。 将数据放入内存输出流便于转化
	 * 
	 * @param img
	 * @return
	 */
	public static byte[] encodeJPEG(BufferedImage img) {
		//
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				// 创建图片输出数据流
				ImageOutputStream ious = ImageIO.createImageOutputStream(out);) {
			ImageIO.write(img, "JPEG", ious);// 将图片输出为 jpeg

			return out.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 解析
	 * 
	 * @param b
	 * @return
	 */
	public static BufferedImage decodeJPEG(byte[] b) {
		try (ByteArrayInputStream in = new ByteArrayInputStream(b); ImageInputStream is = ImageIO.createImageInputStream(in);) {
			return ImageIO.read(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}

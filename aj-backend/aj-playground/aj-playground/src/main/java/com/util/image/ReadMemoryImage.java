package com.util.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;

import org.junit.Test;

/**
 * 构建ImageInputStream利用ImageReader对内存字节流进行图像解码
 * 
 * https://blog.csdn.net/10km/article/details/52119508
 * 
 * java提供了一个非常方便的图像工具类javax.imageio.ImageIO，用它的javax.imageio.ImageIO.read方法可以很方便的将一个图像文件进行解码。
 * javax.imageio.ImageIO.read方法有多个重载方法，支持File,InputStream,URL等参数，但这些方法有可能会在解码过程中使用文件系统做cache，具体原因这里不展开讲了，好长，你要研究java源码。
 * 有了磁盘IO势必会影响解码效率，这在性能敏感的应用环境是不能容忍的，
 * 如果要实现完全基于内存的图像解码，就不能简单使用javax.imageio.ImageIO.read方法。需要利用javax.imageio.stream.MemoryCacheImageInputStream来实现内存cache。来实现完全的内存解码,以下是完整的代码,
 * ———————————————— 版权声明：本文为CSDN博主「10km」的原创文章，遵循CC 4.0
 * BY-SA版权协议，转载请附上原文出处链接及本声明。
 * 原文链接：https://blog.csdn.net/10km/article/details/52119508
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class ReadMemoryImage {
	@Test
	public final void testreadMemoryImage() throws IllegalArgumentException, IOException {
		// 将图像文件加读取到内存成为字节数组
		byte[] imgBytes = readBytes(ReadMemoryImage.class.getResourceAsStream("/images/he049.jpg"));
		BufferedImage bufImg = readMemoryImage(imgBytes);
		System.out.printf("decode success,width=%d,heigh=%d\n", bufImg.getWidth(), bufImg.getHeight());
	}

	@Test
	public final void testreadMemoryImage1() throws IllegalArgumentException, IOException {
		// 将图像文件加读取到内存成为字节数组
		byte[] imgBytes = readBytes(ReadMemoryImage.class.getResourceAsStream("/images/he049.jpg"));
		BufferedImage bufImg = readMemoryImage1(imgBytes);
		System.out.printf("decode success,width=%d,heigh=%d\n", bufImg.getWidth(), bufImg.getHeight());
	}

	/**
	 * 从内存字节数组中读取图像
	 * 
	 * @param imgBytes 未解码的图像数据
	 * @return 返回 {@link BufferedImage}
	 * @throws IOException 当读写错误或不识别的格式时抛出
	 */
	public static final BufferedImage readMemoryImage(byte[] imgBytes) throws IOException {
		if (null == imgBytes || 0 == imgBytes.length)
			throw new NullPointerException("the argument 'imgBytes' must not be null or empty");

		// 将字节数组转为InputStream，再转为MemoryCacheImageInputStream
		ImageInputStream imageInputstream = new MemoryCacheImageInputStream(new ByteArrayInputStream(imgBytes));
		// 获取所有能识别数据流格式的ImageReader对象
		Iterator<ImageReader> it = ImageIO.getImageReaders(imageInputstream);

		// 迭代器遍历尝试用ImageReader对象进行解码
		while (it.hasNext()) {
			ImageReader imageReader = it.next();
			// 设置解码器的输入流
			imageReader.setInput(imageInputstream, true, true);
			// 图像文件格式后缀
			String suffix = imageReader.getFormatName().trim().toLowerCase();
			// 图像宽度
			int width = imageReader.getWidth(0);
			// 图像高度
			int height = imageReader.getHeight(0);
			System.out.printf("format %s,%dx%d\n", suffix, width, height);

			try {
				// 解码成功返回BufferedImage对象
				// 0即为对第0张图像解码(gif格式会有多张图像),前面获取宽度高度的方法中的参数0也是同样的意思
				return imageReader.read(0, imageReader.getDefaultReadParam());
			} catch (Exception e) {
				imageReader.dispose();
				// 如果解码失败尝试用下一个ImageReader解码
			}
		}

		imageInputstream.close();
		// 没有能识别此数据的图像ImageReader对象，抛出异常
		throw new IOException("unsupported image format");
	}

	public static final BufferedImage readMemoryImage1(byte[] imgBytes) throws IOException {
		if (null == imgBytes || 0 == imgBytes.length)
			throw new NullPointerException("the argument 'imgBytes' must not be null or empty");

		// 将字节数组转为InputStream，再转为MemoryCacheImageInputStream
		ImageInputStream imageInputstream = new MemoryCacheImageInputStream(new ByteArrayInputStream(imgBytes));
		// 直接调用ImageIO.read方法解码
		BufferedImage bufImg = ImageIO.read(imageInputstream);

		if (null == bufImg)
			// 没有能识别此数据的图像ImageReader对象，抛出异常
			throw new IOException("unsupported image format");

		return bufImg;
	}

	/**
	 * 从{@link InputStream}读取字节数组<br>
	 * 结束时会关闭{@link InputStream}<br>
	 * {@code in}为{@code null}时抛出{@link NullPointerException}
	 * 
	 * @param in
	 * @return 字节数组
	 * @throws IOException
	 */
	public static final byte[] readBytes(InputStream in) throws IOException {
		if (null == in)
			throw new NullPointerException("the argument 'in' must not be null");

		try {
			int buffSize = Math.max(in.available(), 1024 * 8);
			byte[] temp = new byte[buffSize];
			ByteArrayOutputStream out = new ByteArrayOutputStream(buffSize);
			int size = 0;

			while ((size = in.read(temp)) != -1)
				out.write(temp, 0, size);

			return out.toByteArray();
		} finally {
			in.close();
		}
	}
}
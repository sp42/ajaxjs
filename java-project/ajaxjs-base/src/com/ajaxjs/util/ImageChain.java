package com.ajaxjs.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageChain extends FileChain {

	private int width;
	private int height;

	/**
	 * 图片对象
	 */
	private Image img;

	/**
	 * 
	 */
	private BufferedImage bImg;

	/**
	 * 图片类型
	 */
	private String format;

	/**
	 * 保存图片 TODO 其实可以考虑使用 bufferedWrite(InputStream is, OutputStream out) 保存的
	 * 《使用ImageIO.write存储png格式图片性能较差问题》http://zhang-xzhi-xjtu.iteye.com/blog/
	 * 1328084
	 */
	public ImageChain save() {
		try {
			ImageIO.write(bImg, format, getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return this;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public ImageChain setWidth(int width) {
		this.width = width;
		return this;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public ImageChain setHeight(int height) {
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
	public ImageChain setImg(Image img) {
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
	public ImageChain setbImg(BufferedImage bImg) {
		this.bImg = bImg;

		return this;
	}

	@Override
	public ImageChain setFilePath(String filePath) {
		super.setFilePath(filePath);
		setFile(getFile()); // 同时设 File 对象，但有点多余
		return this;
	}

	@Override
	public ImageChain setFile(File file) {
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
}

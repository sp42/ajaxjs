package com.ajaxjs.watermark.model;

import javax.imageio.ImageIO;

import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.watermark.type.StyleTypeEnum;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class PictureStyle {
	private static final LogHelper LOGGER = LogHelper.getLog(PictureStyle.class);

	/**
	 * 图片的文件流
	 */
	private String url;

	/**
	 * 显示位置的 X轴 坐标
	 */
	private Integer x;

	/**
	 * 显示位置的 Y轴 坐标
	 */
	private Integer y;

	/**
	 * 显示图片的宽度
	 */
	private Integer width;

	/**
	 * 显示图片的长度
	 */
	private Integer height;

	/**
	 * 图片的透明度
	 */
	private Float transparency = .5f;

	/**
	 * 旋转角度，-180 至 180， 超过部分将会进行转化到此范围
	 */
	private Integer rotate = 0;

	/**
	 * 可以指定图片的布局，当启用图片的布局的时候以上的大小、坐标不可用
	 */
	private StyleTypeEnum styleTypeEnum = null;

	/**
	 * 图片宽度的缩放
	 */
	private Float widthZoom = null;

	/**
	 * 图片高度的缩放
	 */
	private Float heightZoom = null;

	public PictureStyle() {
	}

	/**
	 * 构造器
	 * 
	 * @param url          图片的输入流
	 * @param x            显示的 x 轴坐标
	 * @param y            显示的 y 轴坐标
	 * @param width        显示的长度
	 * @param height       显示的宽度
	 * @param transparency 显示的透明度
	 * @param rotate       显示的旋转角度
	 */
	public PictureStyle(String url, Integer x, Integer y, Integer width, Integer height, Float transparency, Integer rotate, StyleTypeEnum styleTypeEnum, Float widthZoom,
			Float heightZoom) {
		this.url = url;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.transparency = transparency;
		this.rotate = rotate;
		this.styleTypeEnum = styleTypeEnum;
		this.widthZoom = widthZoom;
		this.heightZoom = heightZoom;
	}

	/**
	 * 根据 office 文件类型获取 文字水印 template
	 * 
	 * @return 返回对应格式的 template
	 * @param width  添加水印文件的宽
	 * @param height 添加水印文件的高
	 * @return 返回对应格式的 template
	 */
	public static List<PictureStyle> turnBasicPictureStyle(List<PictureStyle> pictureStyles, int width, int height) {
		List<PictureStyle> retStyle = new ArrayList<>();

		for (PictureStyle style : pictureStyles) {
			StyleTypeEnum styleTypeEnum = style.getStyleTypeEnum();

			// 非特殊情况的水印，直接指定大小位置等样式
			if (styleTypeEnum == null)
				retStyle.add(style);

			else { // 根据布局获取样式
				FileInputStream fileInputStream = null;

				Double picWidth = 200.0;
				Double picHeight = 200.0;
				Double rate = 1.0;

				try {
					fileInputStream = new FileInputStream(style.getUrl());
					BufferedImage read = ImageIO.read(fileInputStream);
					picWidth = 1.0 * read.getWidth();
					picHeight = 1.0 * read.getHeight();
					rate = picWidth / picHeight;
				} catch (Exception e) {
					LOGGER.warning("Failed to obtain the real width and height of image which going to draw in a big image.", e);
				} finally {
					try {
						assert fileInputStream != null;
						fileInputStream.close();
					} catch (IOException e) {
						LOGGER.warning("Failed to close file.", e);
					}
				}
				// 如果用户设置缩放比例，则使用缩放比例，缩放比例优先度最高
				if (style.getWidthZoom() != null && style.getHeightZoom() != null) {
					picWidth = picWidth * style.getWidthZoom();
					picHeight = picHeight * style.getHeightZoom();
					// 如果用户设置了 width、height 则使用、第二优先度，最后才是系统自定义的
				} else if (style.getWidth() != null && style.getHeight() != null) {
					picWidth = 1.0d * style.getWidth();
					picHeight = 1.0d * style.getHeight();
				}
//                暂时需求是使用图片原先的大小，所以暂不改动，有需求时可以使用自动缩放的方式
//                else {
//                    if (picWidth > 0.25d * width && picHeight < height){
//                        picWidth = .25d * width;
//                        picHeight = picWidth / rate;
//                    } else {
//                        picHeight = .25d * height;
//                        picWidth = rate * picHeight;
//                    }
//                }

				switch (styleTypeEnum) {
				case CENTER:
					if ((style.getWidthZoom() == null) || (style.getHeight() == null) || (style.getWidth() == null)) {
						if ((picWidth > (0.71f * width)) && (picHeight <= height)) {
							picWidth = 0.71 * width;
							picHeight = picWidth / rate;
						} else if ((picHeight > (0.71f * height)) && (picWidth <= width)) {
							picHeight = 0.71 * height;
							picWidth = picHeight * rate;
						}
					}

					retStyle.add(new PictureStyle(style.getUrl(), width / 2, height / 2, picWidth.intValue(), picHeight.intValue(), style.getTransparency(), style.getRotate(),
							null, null, null));
					break;
				case TOP_LEFT:
					retStyle.add(new PictureStyle(style.getUrl(), (int) (picWidth / 2), (int) (picHeight / 2), picWidth.intValue(), picHeight.intValue(), style.getTransparency(),
							style.getRotate(), null, null, null));
					break;
				case TOP_RIGHT:
					retStyle.add(new PictureStyle(style.getUrl(), width - (int) (picWidth / 2), (int) (picHeight / 2), picWidth.intValue(), picHeight.intValue(),
							style.getTransparency(), style.getRotate(), null, null, null));
					break;
				case BOTTOM_LEFT:
					retStyle.add(new PictureStyle(style.getUrl(), (int) (picWidth / 2), height - (int) (picHeight / 2), picWidth.intValue(), picHeight.intValue(),
							style.getTransparency(), style.getRotate(), null, null, null));
					break;
				case BOTTOM_RIGHT:
					retStyle.add(new PictureStyle(style.getUrl(), width - (int) (picWidth / 2), height - (int) (picHeight / 2), picWidth.intValue(), picHeight.intValue(),
							style.getTransparency(), style.getRotate(), null, null, null));
					break;
				case TILE:
					for (int x = (int) (picWidth / 2); x <= width + picWidth.intValue(); x += picWidth.intValue()) {
						for (int y = (int) (picHeight / 2); y <= height + picHeight.intValue(); y += picHeight.intValue()) {
							retStyle.add(new PictureStyle(style.getUrl(), x, y, picWidth.intValue(), picHeight.intValue(), style.getTransparency(), style.getRotate(), null, null,
									null));
						}
					}

					break;
				default:
				}
			}
		}
		return retStyle;
	}

	public String getUrl() {
		return url;
	}

	public PictureStyle setUrl(String url) {
		this.url = url;
		return this;
	}

	public Integer getX() {
		return x;
	}

	public PictureStyle setX(Integer x) {
		this.x = x;
		return this;
	}

	public Integer getY() {
		return y;
	}

	public PictureStyle setY(Integer y) {
		this.y = y;
		return this;
	}

	public Integer getWidth() {
		return width;
	}

	public PictureStyle setWidth(Integer width) {
		this.width = width;
		return this;
	}

	public Integer getHeight() {
		return height;
	}

	public PictureStyle setHeight(Integer height) {
		this.height = height;
		return this;
	}

	public Float getTransparency() {
		return transparency;
	}

	public PictureStyle setTransparency(Float transparency) {
		this.transparency = transparency;
		return this;
	}

	public Integer getRotate() {
		return rotate;
	}

	public PictureStyle setRotate(Integer rotate) {
		this.rotate = rotate;
		return this;
	}

	public StyleTypeEnum getStyleTypeEnum() {
		return styleTypeEnum;
	}

	public PictureStyle setStyleTypeEnum(StyleTypeEnum styleTypeEnum) {
		this.styleTypeEnum = styleTypeEnum;
		return this;
	}

	public Float getWidthZoom() {
		return widthZoom;
	}

	public PictureStyle setWidthZoom(Float widthZoom) {
		this.widthZoom = widthZoom;
		return this;
	}

	public Float getHeightZoom() {
		return heightZoom;
	}

	public PictureStyle setHeightZoom(Float heightZoom) {
		this.heightZoom = heightZoom;
		return this;
	}
}

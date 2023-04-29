package com.ajaxjs.watermark.model;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

import java.util.ArrayList;
import java.util.List;

import com.ajaxjs.watermark.type.StyleTypeEnum;
import com.ajaxjs.watermark.utils.ImageUtils;

/**
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class TextStyle {
	/* 文字内容 */
	private String text;

	/* 字号、大小 */
	private Integer fontSize;

	/* 字体 */
	private String font = "微软雅黑";

	/* 旋转角度，-180 至 180， 超过部分将会进行转化到此范围 */
	private Integer rotate = 0;

	/* 显示位置的 X轴 坐标 */
	private Integer x;

	/* 显示位置的 Y轴 坐标 */
	private Integer y;

	/* 透明度，范围为 0.0 到 1.0，从大到小显示越来越透明， 即0为完全透明，1为完全不透明 */
	private float transparency = .5f;

	/* 字体颜色 */
	private String fontColor = "#777777";

	/* 当且仅当 styleTypeEnum 部位空的时候使用布局，且使用布局不可自定义配置图片位置 */
	private StyleTypeEnum styleTypeEnum = null;

	public TextStyle() {
	}

	/**
	 * 构造器
	 * 
	 * @param text         文字内容
	 * @param fontSize     字号
	 * @param font         字体
	 * @param rotate       旋转角度
	 * @param x            显示 x 轴坐标
	 * @param y            显示 y 轴坐标
	 * @param transparency 透明度
	 * @param fontColor    字体颜色
	 */
	public TextStyle(String text, Integer fontSize, String font, Integer rotate, Integer x, Integer y, float transparency, String fontColor, StyleTypeEnum styleTypeEnum) {
		this.text = text;
		this.fontSize = fontSize;
		this.font = font;
		this.rotate = rotate;
		this.x = x;
		this.y = y;
		this.transparency = transparency;
		this.fontColor = fontColor;
		this.styleTypeEnum = styleTypeEnum;
	}

	public static List<TextStyle> turnBasicTextStyle(List<TextStyle> textStyles, int width, int height) {
		List<TextStyle> retStyle = new ArrayList<>();

		for (TextStyle textStyle : textStyles) {
			StyleTypeEnum styleTypeEnum = textStyle.getStyleTypeEnum();

			// 非特殊情况的水印，直接指定大小位置等样式
			if (styleTypeEnum == null)
				retStyle.add(textStyle);
			else { // 根据布局获取样式
				int wordWidth = (int) (ImageUtils.getWordWidth(textStyle.getText()) + 0.5d);
				int wordHeight = (int) (ImageUtils.getWordHeight(textStyle.getText()) + 0.5d);
				// 提前设置建议值，如已设置好则不修改为建议值
				Integer suggestionFontSize = textStyle.getFontSize() != null ? textStyle.getFontSize() : (int) (0.25 * (min(width, height)) / wordWidth);
				int drawHeight = wordHeight * suggestionFontSize;
				int drawWidth = wordWidth * suggestionFontSize;
				int diagonal = (int) Math.sqrt(drawHeight * drawHeight + drawWidth * drawWidth) / 2;
				int angle = (int) (180 * Math.atan(1.0d * wordHeight / wordWidth) / Math.PI);
				int defaultY = max((int) Math.abs(diagonal * Math.sin(Math.toRadians(angle + textStyle.getRotate()))),
						(int) Math.abs(diagonal * Math.sin(Math.toRadians(180 - angle + textStyle.getRotate())))) + 5;
				int defaultX = max((int) Math.abs(diagonal * Math.cos(Math.toRadians(angle + textStyle.getRotate()))),
						(int) Math.abs(diagonal * Math.cos(Math.toRadians(180 - angle + textStyle.getRotate())))) + 5;

				// 平铺方案
				switch (styleTypeEnum) {
				case TILE:
					// 如果未指定 字号，则默认字号为24号字体
					suggestionFontSize = textStyle.getFontSize() != null ? textStyle.getFontSize() : 24;
					drawHeight = (int) (wordHeight * suggestionFontSize * 2.45);
					drawWidth = (int) (wordWidth * suggestionFontSize * 1.2);
					for (int x = drawWidth / 2; x < width + drawWidth; x += drawWidth) {
						for (int y = drawHeight / 4; y < height + drawHeight / 2; y += drawHeight) {
							retStyle.add(new TextStyle(textStyle.getText(), suggestionFontSize, textStyle.getFont(), textStyle.getRotate(), x, y, textStyle.getTransparency(),
									textStyle.getFontColor(), null));
						}
					}
					break;
				case CENTER:
					// 如果未指定字号， 则把字体的大小控制在文字霸占 .85 宽度
					suggestionFontSize = textStyle.getFontSize() != null ? textStyle.getFontSize() : (int) ((min(width, height) * 0.85) / max(wordWidth, wordHeight));
					retStyle.add(new TextStyle(textStyle.getText(), suggestionFontSize, textStyle.getFont(), textStyle.getRotate(), width / 2, height / 2,
							textStyle.getTransparency(), textStyle.getFontColor(), null));
					break;
				case TOP_LEFT:
					retStyle.add(new TextStyle(textStyle.getText(), suggestionFontSize, textStyle.getFont(), textStyle.getRotate(), defaultX, defaultY, textStyle.getTransparency(),
							textStyle.getFontColor(), null));
					break;
				case TOP_RIGHT:
					retStyle.add(new TextStyle(textStyle.getText(), suggestionFontSize, textStyle.getFont(), textStyle.getRotate(), width - defaultX, defaultY,
							textStyle.getTransparency(), textStyle.getFontColor(), null));
					break;
				case BOTTOM_LEFT:
					retStyle.add(new TextStyle(textStyle.getText(), suggestionFontSize, textStyle.getFont(), textStyle.getRotate(), defaultX, height - defaultY,
							textStyle.getTransparency(), textStyle.getFontColor(), null));
					break;
				case BOTTOM_RIGHT:
					retStyle.add(new TextStyle(textStyle.getText(), suggestionFontSize, textStyle.getFont(), textStyle.getRotate(), width - defaultX, height - defaultY,
							textStyle.getTransparency(), textStyle.getFontColor(), null));
					break;
				default:
//					if (styleTypeEnum == null)
//						textStyles.add(textStyle);
					break;
				}
			}
		}

		return retStyle;
	}

	public String getText() {
		return text;
	}

	public TextStyle setText(String text) {
		this.text = text;
		return this;
	}

	public Integer getFontSize() {
		return fontSize;
	}

	public TextStyle setFontSize(Integer fontSize) {
		this.fontSize = fontSize;
		return this;
	}

	public String getFont() {
		return font;
	}

	public TextStyle setFont(String font) {
		this.font = font;
		return this;
	}

	public Integer getRotate() {
		return rotate;
	}

	public TextStyle setRotate(Integer rotate) {
		this.rotate = rotate;
		return this;
	}

	public Integer getX() {
		return x;
	}

	public TextStyle setX(Integer x) {
		this.x = x;
		return this;
	}

	public Integer getY() {
		return y;
	}

	public TextStyle setY(Integer y) {
		this.y = y;
		return this;
	}

	public float getTransparency() {
		return transparency;
	}

	public TextStyle setTransparency(float transparency) {
		this.transparency = transparency;
		return this;
	}

	public String getFontColor() {
		return fontColor;
	}

	public TextStyle setFontColor(String fontColor) {
		this.fontColor = fontColor;
		return this;
	}

	public StyleTypeEnum getStyleTypeEnum() {
		return styleTypeEnum;
	}

	public TextStyle setStyleTypeEnum(StyleTypeEnum styleTypeEnum) {
		this.styleTypeEnum = styleTypeEnum;
		return this;
	}
}

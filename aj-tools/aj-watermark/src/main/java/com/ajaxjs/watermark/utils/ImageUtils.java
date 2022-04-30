package com.ajaxjs.watermark.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.watermark.model.PictureStyle;
import com.ajaxjs.watermark.model.TextStyle;
import com.ajaxjs.watermark.type.OfficeTypeEnum;

public class ImageUtils {
	private static final LogHelper LOGGER = LogHelper.getLog(ImageUtils.class);

	public static boolean watermarkPictureProducer(List<TextStyle> textStyles, List<PictureStyle> picStyles, String target, OfficeTypeEnum officeType) {
		return watermarkPictureProducer(textStyles == null ? null : textStyles, picStyles == null ? null : picStyles, target, officeType.getWidth(), officeType.getHeight());
	}

	/**
	 * 在给定的大小的空白 png 图上画 《文字》、《图片》，产生之后根据给定的 target 生成 所有的样式是以图片的中心进行写入，旋转也是如此，
	 * 以模板图的左上角为坐标原点，向左为正 X 方向，向下为正 Y 方向
	 * 
	 * @param textStyles 文字内容以及其样式
	 * @param picStyles  图片内容及其样式
	 * @param target     目标位置
	 * @param height     模板图片的高度
	 * @param width      模板图片的宽度
	 * @return true 代表成功、false 代表失败
	 */
	public static boolean watermarkPictureProducer(List<TextStyle> textStyles, List<PictureStyle> picStyles, String target, int width, int height) {
		// 设置底图
		BufferedImage bufferedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// 设置底图空白
		Graphics2D graphics = bufferedImg.createGraphics();
		bufferedImg = graphics.getDeviceConfiguration().createCompatibleImage(bufferedImg.getWidth(), bufferedImg.getHeight(), Transparency.TRANSLUCENT);
		graphics.dispose();
		bufferedImg = addWatermarkCore(bufferedImg, textStyles, picStyles);

		try {
			ImageIO.write(bufferedImg, "png", new File(target));
		} catch (IOException e) {
			LOGGER.warning("IOException was throw when write image stream to \"" + target + "\".", e);
			return false;
		}

		return true;
	}

	/**
	 * 核心代码，将图片和文字画上去的关键，使用流的方式
	 * 
	 * @param in            写入水印的图片文件流
	 * @param textStyles    文字内容以及其样式
	 * @param pictureStyles 图片内容及其样式
	 * @return 正常返回 bufferImage 的时候表示操作成功
	 * @throws IOException 当操作失败的时候就会抛出异常
	 */
	public static BufferedImage addWatermarkCore(InputStream in, List<TextStyle> textStyles, List<PictureStyle> pictureStyles) {
		BufferedImage bufferedImage;

		try {
			bufferedImage = ImageIO.read(in);
		} catch (IOException e) {
			LOGGER.warning(e);
			return null;
		}

		int width = bufferedImage.getWidth(), height = bufferedImage.getHeight();

		if (textStyles != null)
			textStyles = TextStyle.turnBasicTextStyle(textStyles, width, height);

		if (pictureStyles != null)
			pictureStyles = PictureStyle.turnBasicPictureStyle(pictureStyles, width, height);

		return addWatermarkCore(bufferedImage, textStyles, pictureStyles);
	}

	/**
	 * 核心代码，将图片和文字画上去的关键
	 * 
	 * @param bufferedImage 写入水印的图片文件
	 * @param textStyles    文字内容以及其样式
	 * @param pictureStyles 图片内容及其样式
	 * @return 正常返回 bufferImage 的时候表示操作成功
	 * @throws IOException 当操作失败的时候就会抛出异常
	 */
	public static BufferedImage addWatermarkCore(BufferedImage bufferedImage, List<TextStyle> textStyles, List<PictureStyle> pictureStyles) {
		Graphics2D graphics = bufferedImage.createGraphics();

		// 往模板图片写入图片
		if (pictureStyles != null) {
			for (PictureStyle style : pictureStyles) {
				if (style == null)
					continue;

				graphics = bufferedImage.createGraphics();

				try (FileInputStream in = new FileInputStream(style.getUrl())) {
					BufferedImage image = ImageIO.read(in);
					graphics.rotate(Math.toRadians((style.getRotate() + 360) % 360 - 360), style.getX(), style.getY());
					graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, style.getTransparency()));
					graphics.drawImage(image.getScaledInstance(image.getWidth(), image.getHeight(), Image.SCALE_DEFAULT), style.getX() - (style.getWidth() / 2),
							style.getY() - (style.getHeight() / 2), style.getWidth(), style.getHeight(), null);
					graphics.dispose();
				} catch (IOException e) {
					LOGGER.warning("IOException was throw when create image to image, maybe you can checkout image path or checkout your image type.", e);
					return null;
				}
			}
		}

		// 往模板图片写入文字
		if (textStyles != null) {
			for (TextStyle style : textStyles) {
				if (style == null)
					continue;

				graphics = bufferedImage.createGraphics();
				// 旋转
				double textLength = getWordWidth(style.getText());
				int textline = (int) getWordHeight(style.getText());
//				double wordHeight = textline * style.getFontSize();
				double wordWidth = textLength * style.getFontSize();
//              double offsetX = Math.sin(Math.toRadians(-style.getRotate())) * style.getFontSize();
//              double offsetY = Math.cos(Math.toRadians(-style.getRotate())) * style.getFontSize();
				double offsetX = 0, offsetY = style.getFontSize();

				// 文字 属性
				graphics.setColor(Color.decode(style.getFontColor()));
				graphics.setFont(new Font(style.getFont(), Font.PLAIN, style.getFontSize()));
				graphics.rotate(Math.toRadians((style.getRotate() + 360) % 360 - 360), style.getX(), style.getY());
				String[] splits = style.getText().split("\n");
				float x = (float) (style.getX()), y = (float) (style.getY() - 0.5f * offsetY * (textline - 1));

				for (String split : splits) {
//                    log.info(x + " --X-- " + offsetX + " <----> " + y + " --Y-- " + offsetY);
					graphics.drawString(split, (float) (x - wordWidth / 2 + (textLength - split.length()) * 0.5 * style.getFontSize()), y + 1.0f * style.getFontSize() / 4);
					x += offsetX;
					y += offsetY;
				}

				graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, style.getTransparency()));
				graphics.dispose();
			}
		}

		return bufferedImage;
	}

	/**
	 * 获取文字水印的长度
	 * 
	 * @param text
	 * @return
	 */
	public static double getWordWidth(String text) {
		int length = text.length();
		double res = 0.0, max = 0.0;

		for (int i = 0; i < length; i++) {
			if (text.charAt(i) == '\n') {
				max = res;
				res = 0.0;
			} else if (text.charAt(i) < 256)
				res += 0.5d;
			else
				res += 1.0d;
		}

		return res > max ? res : max;
	}

	/**
	 * 获取文字水印的高度
	 * 
	 * @param text
	 * @return
	 */
	public static double getWordHeight(String text) {
		int length = text.length();
		double res = 1.0;

		for (int i = 0; i < length; i++) {
			if (text.charAt(i) == '\n')
				res += 1;
		}

		return res;
	}
}

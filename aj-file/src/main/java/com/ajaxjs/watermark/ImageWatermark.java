package com.ajaxjs.watermark;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.ajaxjs.watermark.model.PictureStyle;
import com.ajaxjs.watermark.model.TextStyle;
import com.ajaxjs.watermark.utils.ImageUtils;

/**
 * 图片添加水印
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class ImageWatermark {
	/**
	 * 给已有的图片写入比较简单的水印，例如平铺、居中、左上角，中上角之类的简单样式，需要在使用的时候设置好样式，当然也可以
	 * 
	 * @param inputStream
	 * @param textStyle
	 * @param pictureStyle
	 * @param outputStream
	 * @throws IOException
	 */
	public void watermark(InputStream inputStream, TextStyle textStyle, PictureStyle pictureStyle, OutputStream outputStream) throws IOException {
		List<TextStyle> textStyles = new ArrayList<>();

		if (textStyle != null)
			textStyles.add(textStyle);

		List<PictureStyle> pictureStyles = new ArrayList<>();

		if (pictureStyle != null)
			pictureStyles.add(pictureStyle);

		BufferedImage bufferedImage = ImageUtils.addWatermarkCore(inputStream, textStyles, pictureStyles);
		ImageIO.write(bufferedImage, "png", outputStream);
	}
}

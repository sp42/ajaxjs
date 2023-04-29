package com.ajaxjs.watermark;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.ajaxjs.util.io.FileHelper;
import com.ajaxjs.watermark.model.PictureStyle;
import com.ajaxjs.watermark.model.TextStyle;
import com.ajaxjs.watermark.type.OfficeTypeEnum;
import com.ajaxjs.watermark.utils.ImageUtils;
import com.ajaxjs.watermark.utils.OfficeUtils;

/**
 * Office 文件添加水印
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class OfficeWatermark {
	public final static String Path = "/tmp/watermark/office/";

	/**
	 * 通过获取模板生成水印、可使用文字和图片两种方式的水印模板
	 * 
	 * @param officeType office 类型
	 * @param officePath office 路径
	 * @param picStyles  图片使用的模板
	 * @param textStyles 文字使用的模板
	 * @param desPath    目标地址
	 * @return true 水印添加操作成功 false 水印添加操作失败
	 */
	public boolean watermark(OfficeTypeEnum officeType, String officePath, List<PictureStyle> picStyles, List<TextStyle> textStyles, String desPath) {
		String optPath = Path + (int) (Math.random() * 199999999) + "/";
		String picPath = optPath + "bingo.png";

		if (picStyles != null && picStyles.size() > 0)
			picStyles = PictureStyle.turnBasicPictureStyle(picStyles, officeType.getWidth(), officeType.getHeight());

		if (textStyles != null && textStyles.size() > 0)
			textStyles = TextStyle.turnBasicTextStyle(textStyles, officeType.getWidth(), officeType.getHeight());

		String officeOptPath = optPath + "office/";
		FileHelper.mkDir(optPath);
		FileHelper.mkDir(officeOptPath);
		ImageUtils.watermarkPictureProducer(textStyles, picStyles, picPath, officeType);
		boolean success = new OfficeUtils().officeWatermark(officePath, picPath, desPath, officeOptPath, officeType);
		FileHelper.delete(optPath);

		return success;
	}

	/**
	 * 简单的功能，将图片和文字按照 详细的配置 或者 样式中的布局去生成水印 office 输出流
	 * 
	 * @param file         office 文件
	 * @param textStyle    水印 w 文字的样式相关信息
	 * @param picStyle     水印图片文件的样式相关信息
	 * @param outputStream 输出流，文件读写成功之后将会直接写入到 outputStream
	 */
	public void watermark(File file, PictureStyle picStyle, TextStyle textStyle, OutputStream outputStream) throws IOException {
		// 获取文档类型
		String fileName = file.getName();
		OfficeTypeEnum officeType = OfficeTypeEnum.valueOf(fileName.substring(fileName.lastIndexOf('.') + 1).toUpperCase());

		// 更新样式详情
		List<TextStyle> textStyles = new ArrayList<>();
		List<PictureStyle> pictureStyles = new ArrayList<>();

		if (textStyle != null) {
			textStyles.add(textStyle);
			textStyles = TextStyle.turnBasicTextStyle(textStyles, officeType.getWidth(), officeType.getHeight());
		}

		if (picStyle != null) {
			pictureStyles.add(picStyle);
			pictureStyles = PictureStyle.turnBasicPictureStyle(pictureStyles, officeType.getWidth(), officeType.getHeight());
		}

		String optPath = Path + (int) (Math.random() * 199999999) + "/", picPath = optPath + "bingo.png";
		String officeOptPath = optPath + "office/";

		FileHelper.mkDir(optPath);
		FileHelper.mkDir(officeOptPath);
		ImageUtils.watermarkPictureProducer(textStyles, pictureStyles, picPath, officeType);
		new OfficeUtils().officeWatermark(new FileInputStream(file), officeType, picPath, optPath, outputStream);
		FileHelper.delete(optPath);
	}

}
